package home.journal.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import home.journal.model.Tweet;
import home.journal.util.TweetExtractor;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.Properties;

public class KafkaTweetStream
{
    static final private Logger LOGGER = LoggerFactory.getLogger(KafkaTweetStream.class);

    public static void main(String... args)
    {
        final KafkaTweetStream tweetStream = new KafkaTweetStream();

        tweetStream.stream();
    }

    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").setPrettyPrinting().create();

    public boolean stream()
    {
        LOGGER.info("Open tweet stream");

        final TwitterStream twitterStream = new TwitterStreamFactory(
            new ConfigurationBuilder().setJSONStoreEnabled(true).build()
        ).getInstance();

        twitterStream.addListener(new TweetListener());
        twitterStream.sample();

        return true;
    }

    class TweetListener implements StatusListener
    {
        final Logger LOGGER = LoggerFactory.getLogger(TweetListener.class);

        private Producer<String, String> producer;

        @Override
        public void onStatus(Status status)
        {
            if (status.getPlace() != null && status.getPlace().getCountryCode().equalsIgnoreCase("us")) {
                final Tweet tweet = TweetExtractor.from(status)
                                                  .extractDate()
                                                  .extractState()
                                                  .extractLatLon()
                                                  .extractDevice()
                                                  .extractLanguage()
                                                  .extractRetweetCount()
                                                  .to(new Tweet());
                final String tweetStr = gson.toJson(tweet);

                LOGGER.info(tweetStr);

                getKafkaProducer().send(new KeyedMessage<>("tweet", tweetStr));
            }
        }

        private Producer<String, String> getKafkaProducer()
        {
            if (producer == null) {
                final Properties props = new Properties();
                props.put("serializer.class", "kafka.serializer.StringEncoder");
                props.put("metadata.broker.list", "localhost:9092");

                producer = new Producer<>(new ProducerConfig(props));
            }

            return producer;
        }

        @Override
        public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}

        @Override
        public void onTrackLimitationNotice(int i) {}

        @Override
        public void onScrubGeo(long l, long l1) {}

        @Override
        public void onStallWarning(StallWarning stallWarning) {}

        @Override
        public void onException(Exception e) {}
    }
}
