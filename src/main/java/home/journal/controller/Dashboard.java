package home.journal.controller;

import com.google.gson.Gson;
import home.journal.model.DeviceCount;
import home.journal.model.LanguageCount;
import home.journal.model.StateCount;
import home.journal.model.TopicCount;
import home.journal.service.TweetService;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Controller
public class Dashboard
{
    final static private Logger LOGGER = LoggerFactory.getLogger(Dashboard.class);

    @Autowired
    private Gson gson;

    @Autowired
    private TweetService tweetService;

    @Autowired
    private ScheduledExecutorService scheduler;

    @Resource
    private Map<String, String> stateCodes;

    @RequestMapping("/")
    public String get()
    {
        LOGGER.info("Get dashboard page!");

        return "dashboard/page";
    }

    @RequestMapping("/statistic")
    public void statistic(HttpServletResponse response)
    {
        response.setContentType("text/event-stream");
        final List<ScheduledFuture<?>> futures = new ArrayList<>(5);

        try {
            final PrintWriter writer = response.getWriter();

            futures.add(scheduler.scheduleAtFixedRate(() -> this.collectTopicRank(writer), 0, 1, TimeUnit.SECONDS));
            futures.add(scheduler.scheduleAtFixedRate(() -> this.collectDeviceRank(writer), 0, 2, TimeUnit.SECONDS));
            futures.add(scheduler.scheduleAtFixedRate(() -> this.collectLanguageRank(writer), 0, 3, TimeUnit.SECONDS));
            futures.add(scheduler.scheduleAtFixedRate(() -> this.collectTweetFrequency(writer), 0, 4, TimeUnit.SECONDS));
            futures.add(scheduler.scheduleAtFixedRate(() -> this.collectStateTweetCount(writer), 0, 5, TimeUnit.SECONDS));
            futures.add(scheduler.scheduleAtFixedRate(() -> this.collectRetweetFrequency(writer), 0, 6, TimeUnit.SECONDS));

            futures.get(0).get();
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();

            for (ScheduledFuture<?> future : futures) {
                future.cancel(true);
            }
        }
    }

    @RequestMapping("/tweet_point")
    public void map(HttpServletResponse response)
    {
        response.setContentType("text/event-stream");

        try {
            final PrintWriter writer = response.getWriter();

            final Properties properties = new Properties();
            properties.put("zookeeper.connect","localhost:2181");
            properties.put("group.id","test-group");

            final ConsumerConfig consumerConfig = new ConsumerConfig(properties);
            final ConsumerConnector consumerConnector = Consumer.createJavaConsumerConnector(consumerConfig);

            final Map<String, Integer> topicCountMap = new HashMap<>();
            final String kafkaTopic = "tweet_point";
            topicCountMap.put(kafkaTopic, 1);

            final Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumerConnector.createMessageStreams(topicCountMap);
            final KafkaStream<byte[], byte[]> stream =  consumerMap.get(kafkaTopic).get(0);

            for (MessageAndMetadata<byte[], byte[]> aStream : stream) {
                writer.write("data: " + new String(aStream.message()) + "\n\n");
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void collectTweetFrequency(PrintWriter writer)
    {
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime _1minuteAgo = now.minusMinutes(1);

        final long tweetCount = tweetService.getTweetCount(_1minuteAgo, now);

        synchronized (this) {
            writer.write("event: tweet_frq\n");
            writer.write("data: " + tweetCount + "\n\n");
            writer.flush();
        }
    }

    private void collectRetweetFrequency(PrintWriter writer)
    {
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime _1minuteAgo = now.minusMinutes(1);

        final long retweetCount = tweetService.getRetweetCount(_1minuteAgo, now);

        synchronized (this) {
            writer.write("event: retweet_frq\n");
            writer.write("data: " + retweetCount + "\n\n");
            writer.flush();
        }
    }

    private void collectDeviceRank(PrintWriter writer)
    {
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime _30MinutesAgo = now.minusMinutes(30);

        final List<DeviceCount> deviceCounts = tweetService.getTopNDevices(_30MinutesAgo, now);

        synchronized (this) {
            writer.write("event: device_rank\n");
            writer.write("data: " + gson.toJson(deviceCounts) + "\n\n");
            writer.flush();
        }
    }

    private void collectTopicRank(PrintWriter writer)
    {
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime _30MinutesAgo = now.minusMinutes(30);

        final List<TopicCount> topicCounts = tweetService.getTopNTopics(_30MinutesAgo, now);

        synchronized (this) {
            writer.write("event: topic_rank\n");
            writer.write("data: " + gson.toJson(topicCounts) + "\n\n");
            writer.flush();
        }
    }

    private void collectLanguageRank(PrintWriter writer)
    {
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime _30MinutesAgo = now.minusMinutes(30);

        final List<LanguageCount> languageCounts = tweetService.getTopNLanguages(_30MinutesAgo, now);

        synchronized (this) {
            writer.write("event: language_rank\n");
            writer.write("data: " + gson.toJson(languageCounts) + "\n\n");
            writer.flush();
        }
    }

    private void collectStateTweetCount(PrintWriter writer)
    {
        for (String state : stateCodes.values()) {
            final LocalDateTime now = LocalDateTime.now();
            final LocalDateTime _1minuteAgo = now.minusMinutes(1);

            final long count = tweetService.getTweetCountByState(_1minuteAgo, now, state);
            final StateCount stateCount = new StateCount();
            stateCount.setState(state);
            stateCount.setCount(count);

            synchronized (this) {
                writer.write("event: state_tweet_count\n");
                writer.write("data: " + gson.toJson(stateCount) + "\n\n");
                writer.flush();
            }
        }
    }
}
