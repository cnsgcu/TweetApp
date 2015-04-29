package home.journal.dsl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;

public class TimeseriesTest
{
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Test
    public void timeseriesQueryTest()
    {
        final Aggregation aggregation = new Aggregation();
        aggregation.setFieldName("tweets");
        aggregation.setName("tweet_count");
        aggregation.setType(AggregationType.longSum);

        final Timeseries query = Timeseries.from("twitter")
                                           .granularity(GranularityType.all)
                                           .aggregations(aggregation);

        System.out.println(gson.toJson(query));
    }
}
