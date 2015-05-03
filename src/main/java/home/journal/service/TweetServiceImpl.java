package home.journal.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import home.journal.model.CountResponse;
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
import java.util.List;
import java.util.Scanner;

@Component
public class TweetServiceImpl implements TweetService
{
    static final private Logger LOGGER = LoggerFactory.getLogger(TweetServiceImpl.class);

    static final public String TWEET_COUNT_QUERY_TEMPLATE   = loadResource("query_templates/tweetCount.json");
    static final public String RETWEET_COUNT_QUERY_TEMPLATE = loadResource("query_templates/retweetCount.json");
    static final public String DEVICE_RANK_QUERY_TEMPLATE   = loadResource("query_templates/deviceRanking.json");
    static final public String TOPIC_RANK_QUERY_TEMPLATE    = loadResource("query_templates/topicRanking.json");
    static final public String LANGUAGE_RANK_QUERY_TEMPLATE = loadResource("query_templates/languageRanking.json");

    static final private String URL = "http://localhost:8082/druid/v2/";
    static final private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();

    final public HttpClient httpClient = HttpClientBuilder.create().build();

    @Override
    public long getTweetCount(LocalDateTime start, LocalDateTime end)
    {
        try {
            final HttpPost request = new HttpPost(URL);
            final StringEntity entity = new StringEntity(String.format(TWEET_COUNT_QUERY_TEMPLATE, "2012-10-01T00:00/2020-01-01T00"));
            request.addHeader("Content-Type", "application/json");
            request.setEntity(entity);

            final HttpResponse response = httpClient.execute(request);
            final String resStr = EntityUtils.toString(response.getEntity());
            LOGGER.info(resStr);

            final Type listType = new TypeToken<ArrayList<CountResponse>>() {}.getType();
            final List<CountResponse> countResponseList = (ArrayList<CountResponse>) gson.fromJson(resStr, listType);

            return countResponseList.get(0).getResult().getCount();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1;
    }

    @Override
    public long getRetweetCount(LocalDateTime start, LocalDateTime end)
    {
        try {
            final HttpPost request = new HttpPost(URL);
            final StringEntity entity = new StringEntity(String.format(RETWEET_COUNT_QUERY_TEMPLATE, "2012-10-01T00:00/2020-01-01T00"));
            request.addHeader("Content-Type", "application/json");
            request.setEntity(entity);

            final HttpResponse response = httpClient.execute(request);
            final String resStr = EntityUtils.toString(response.getEntity());
            LOGGER.info(resStr);

            final Type listType = new TypeToken<ArrayList<CountResponse>>() {}.getType();
            final List<CountResponse> countResponseList = (ArrayList<CountResponse>) gson.fromJson(resStr, listType);

            return countResponseList.get(0).getResult().getCount();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1;    }

    @Override
    public long getTweetCountInState(LocalDateTime start, LocalDateTime end, String state)
    {
        try {
            final HttpPost request = new HttpPost(URL);

            // TODO use state name
            final StringEntity entity = new StringEntity(String.format(TWEET_COUNT_QUERY_TEMPLATE, "2012-10-01T00:00/2020-01-01T00"));
            request.addHeader("Content-Type", "application/json");
            request.setEntity(entity);

            final HttpResponse response = httpClient.execute(request);
            final String resStr = EntityUtils.toString(response.getEntity());
            LOGGER.info(resStr);

            final Type listType = new TypeToken<ArrayList<CountResponse>>() {}.getType();
            final List<CountResponse> countResponseList = (ArrayList<CountResponse>) gson.fromJson(resStr, listType);

            return countResponseList.get(0).getResult().getCount();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1;
    }

    @Override
    public void getTopNTopics(LocalDateTime start, LocalDateTime end)
    {

    }

    @Override
    public void getTopNDevices(LocalDateTime start, LocalDateTime end)
    {

    }

    @Override
    public void getTopNLanguages(LocalDateTime start, LocalDateTime end)
    {

    }

    private static String loadResource(String path)
    {
        LOGGER.info("Load " + path);

        return new Scanner(
            Thread.currentThread().getContextClassLoader()
                  .getResourceAsStream(path)
        ).useDelimiter("\\A").next();
    }
}
