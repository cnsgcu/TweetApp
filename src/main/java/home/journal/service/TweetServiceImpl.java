package home.journal.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import home.journal.model.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

@Component
public class TweetServiceImpl implements TweetService
{
    static final private Logger LOGGER = LoggerFactory.getLogger(TweetServiceImpl.class);

    static final public String TWEET_COUNT_QUERY_TEMPLATE          = loadResource("query_templates/tweetCount.json");
    static final public String RETWEET_COUNT_QUERY_TEMPLATE        = loadResource("query_templates/retweetCount.json");
    static final public String DEVICE_RANK_QUERY_TEMPLATE          = loadResource("query_templates/deviceRanking.json");
    static final public String TOPIC_RANK_QUERY_TEMPLATE           = loadResource("query_templates/topicRanking.json");
    static final public String LANGUAGE_RANK_QUERY_TEMPLATE        = loadResource("query_templates/languageRanking.json");
    static final public String TWEET_COUNT_BY_STATE_QUERY_TEMPLATE = loadResource("query_templates/tweetCountByState.json");

    static final private String URL = "http://localhost:8082/druid/v2/";
    static final private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();

    final public HttpClient httpClient = HttpClientBuilder.create().build();

    @Override
    public long getTweetCount(LocalDateTime from, LocalDateTime to)
    {
        try {
            final HttpPost request = new HttpPost(URL);
            final String interval = from.toString() + "/" + to.toString();

            LOGGER.info("Tweet count interval " + interval);

            final StringEntity entity = new StringEntity(String.format(TWEET_COUNT_QUERY_TEMPLATE, interval));
            request.addHeader("Content-Type", "application/json");
            request.setEntity(entity);

            final HttpResponse response = httpClient.execute(request);
            final String resStr = EntityUtils.toString(response.getEntity());

            LOGGER.info("Get tweet count response: " + resStr);

            final Type listType = new TypeToken<ArrayList<CountResponse>>() {}.getType();
            final List<CountResponse> countResponseList = (ArrayList<CountResponse>) gson.fromJson(resStr, listType);

            return countResponseList.get(0).getResult().getCount();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1;
    }

    @Override
    public long getRetweetCount(LocalDateTime from, LocalDateTime to)
    {
        try {
            final HttpPost request = new HttpPost(URL);
            final String interval = from.toString() + "/" + to.toString();

            LOGGER.info("Retweet count interval " + interval);

            final StringEntity entity = new StringEntity(String.format(RETWEET_COUNT_QUERY_TEMPLATE, interval));
            request.addHeader("Content-Type", "application/json");
            request.setEntity(entity);

            final HttpResponse response = httpClient.execute(request);
            final String resStr = EntityUtils.toString(response.getEntity());

            LOGGER.info("Get retweet count response: " + resStr);

            final Type listType = new TypeToken<ArrayList<CountResponse>>() {}.getType();
            final List<CountResponse> countResponseList = (ArrayList<CountResponse>) gson.fromJson(resStr, listType);

            return countResponseList.get(0).getEvent().getCount();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1;
    }

    @Override
    public long getTweetCountByState(LocalDateTime from, LocalDateTime to, String state)
    {
        try {
            final HttpPost request = new HttpPost(URL);
            final String interval = from.toString() + "/" + to.toString();

            LOGGER.info("Tweet count by state interval " + interval);

            final StringEntity entity = new StringEntity(String.format(TWEET_COUNT_BY_STATE_QUERY_TEMPLATE, state, interval));
            request.addHeader("Content-Type", "application/json");
            request.setEntity(entity);

            final HttpResponse response = httpClient.execute(request);
            final String resStr = EntityUtils.toString(response.getEntity());

            LOGGER.info("Get tweet count by state response: " + resStr);

            final Type listType = new TypeToken<ArrayList<CountResponse>>() {}.getType();
            final List<CountResponse> countResponseList = (ArrayList<CountResponse>) gson.fromJson(resStr, listType);

            return countResponseList.get(0).getResult().getCount();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1;
    }

    @Override
    public List<TopicCount> getTopNTopics(LocalDateTime from, LocalDateTime to)
    {
        try {
            final HttpPost request = new HttpPost(URL);
            final String interval = from.toString() + "/" + to.toString();

            LOGGER.info("Get top N topics interval " + interval);

            final StringEntity entity = new StringEntity(String.format(TOPIC_RANK_QUERY_TEMPLATE, interval));
            request.addHeader("Content-Type", "application/json");
            request.setEntity(entity);

            final HttpResponse response = httpClient.execute(request);
            final String resStr = EntityUtils.toString(response.getEntity());

            LOGGER.info("Get top N topics response: " + resStr);

            final Type listType = new TypeToken<ArrayList<TopicRankResponse>>() {}.getType();
            final List<TopicRankResponse> countResponseList = (ArrayList<TopicRankResponse>) gson.fromJson(resStr, listType);

            return countResponseList.get(0).getResult();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    @Override
    public List<DeviceCount> getTopNDevices(LocalDateTime from, LocalDateTime to)
    {
        try {
            final HttpPost request = new HttpPost(URL);
            final String interval = from.toString() + "/" + to.toString();

            LOGGER.info("Get top N devices interval " + interval);

            final StringEntity entity = new StringEntity(String.format(DEVICE_RANK_QUERY_TEMPLATE, interval));
            request.addHeader("Content-Type", "application/json");
            request.setEntity(entity);

            final HttpResponse response = httpClient.execute(request);
            final String resStr = EntityUtils.toString(response.getEntity());

            LOGGER.info("Get top N devices response: " + resStr);

            final Type listType = new TypeToken<ArrayList<DeviceRankResponse>>() {}.getType();
            final List<DeviceRankResponse> countResponseList = (ArrayList<DeviceRankResponse>) gson.fromJson(resStr, listType);

            return countResponseList.get(0).getResult();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    @Override
    public List<LanguageCount> getTopNLanguages(LocalDateTime from, LocalDateTime to)
    {
        try {
            final HttpPost request = new HttpPost(URL);
            final String interval = from.toString() + "/" + to.toString();

            LOGGER.info("Get top N languages interval " + interval);

            final StringEntity entity = new StringEntity(String.format(LANGUAGE_RANK_QUERY_TEMPLATE, interval));
            request.addHeader("Content-Type", "application/json");
            request.setEntity(entity);

            final HttpResponse response = httpClient.execute(request);
            final String resStr = EntityUtils.toString(response.getEntity());

            LOGGER.info("Get top N languages response: " + resStr);

            final Type listType = new TypeToken<ArrayList<LanguageRankResponse>>() {}.getType();
            final List<LanguageRankResponse> countResponseList = (ArrayList<LanguageRankResponse>) gson.fromJson(resStr, listType);

            return countResponseList.get(0).getResult();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    private static String loadResource(String path)
    {
        LOGGER.info("Load " + path);

        return new Scanner(
            Thread.currentThread().getContextClassLoader().getResourceAsStream(path)
        ).useDelimiter("\\A").next();
    }
}
