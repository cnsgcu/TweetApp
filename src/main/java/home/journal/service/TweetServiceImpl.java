package home.journal.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import home.journal.model.TweetCountResponse;
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
    final static private Logger LOGGER = LoggerFactory.getLogger(TweetServiceImpl.class);

    static final public String tweetCountQueryTemplate = loadResource("query_templates/tweetCount.json");
    static final public String retweetCountQueryTemplate = loadResource("query_templates/retweetCount.json");
    static final public String deviceRankingQueryTemplate = loadResource("query_templates/deviceRanking.json");
    static final public String topicRankingQueryTemplate = loadResource("query_templates/topicRanking.json");
    static final public String languageRankingQueryTemplate = loadResource("query_templates/languageRanking.json");

    static final private String URL = "http://localhost:8082/druid/v2/";
    static final private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();

    final public HttpClient httpClient = HttpClientBuilder.create().build();

    @Override
    public long getTweetCount(LocalDateTime start, LocalDateTime end)
    {
        try {
            final HttpPost request = new HttpPost(URL);
            final StringEntity entity = new StringEntity(String.format(tweetCountQueryTemplate, "2012-10-01T00:00/2020-01-01T00"));
            request.addHeader("Content-Type", "application/json");
            request.setEntity(entity);

            final HttpResponse response = httpClient.execute(request);
            final String resStr = EntityUtils.toString(response.getEntity());
            LOGGER.info(resStr);

            final Type listType = new TypeToken<ArrayList<TweetCountResponse>>() {}.getType();
            final List<TweetCountResponse> tweetCountResponseList = (ArrayList<TweetCountResponse>) gson.fromJson(resStr, listType);

            return tweetCountResponseList.get(0).getResult().getTweet_count();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1;
    }

    private static String loadResource(String path)
    {
        LOGGER.info("");
        return new Scanner(
            Thread.currentThread().getContextClassLoader()
                  .getResourceAsStream(path)
        ).useDelimiter("\\A").next();
    }
}
