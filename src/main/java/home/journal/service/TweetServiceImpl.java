package home.journal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Scanner;

@Component
public class TweetServiceImpl implements TweetService
{
    final static private Logger LOGGER = LoggerFactory.getLogger(TweetServiceImpl.class);

    static final public String tweetCountQueryTemplate = readResource("query_templates/tweetCount.json");
    static final public String retweetCountQueryTemplate = readResource("query_templates/retweetCount.json");
    static final public String deviceRankingQueryTemplate = readResource("query_templates/deviceRanking.json");
    static final public String topicRankingQueryTemplate = readResource("query_templates/topicRanking.json");
    static final public String languageRankingQueryTemplate = readResource("query_templates/languageRanking.json");

    @Override
    public long getTweetCount(LocalDateTime start, LocalDateTime end)
    {
        LOGGER.info(tweetCountQueryTemplate);
        LOGGER.info(retweetCountQueryTemplate);
        LOGGER.info(deviceRankingQueryTemplate);
        LOGGER.info(topicRankingQueryTemplate);
        LOGGER.info(languageRankingQueryTemplate);

        return 0;
    }

    private static String readResource(String path)
    {
        return new Scanner(
            Thread.currentThread().getContextClassLoader()
                  .getResourceAsStream(path)
        ).useDelimiter("\\A").next();
    }
}
