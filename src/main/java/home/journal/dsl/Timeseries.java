package home.journal.dsl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Timeseries
{
    private final String queryType = "timeseries";

    private String datasource;
    private GranularityType granularity;
    private List<Aggregation> aggregations;

    private Timeseries(String datasource)
    {
        this.datasource = datasource;
    }

    public GranularityType getGranularity()
    {
        return granularity;
    }

    private void setGranularity(GranularityType granularity)
    {
        this.granularity = granularity;
    }

    public static Timeseries from(String datasource)
    {
        return new Timeseries(datasource);
    }

    final public Timeseries granularity(GranularityType granularityType)
    {
        this.setGranularity(granularityType);

        return this;
    }

    final public Timeseries aggregations(Aggregation aggregation, Aggregation... aggs)
    {
        aggregations = new ArrayList<>(aggs.length + 1);
        aggregations.add(aggregation);
        Collections.addAll(aggregations, aggs);

        return this;
    }

    final public Timeseries filters(List<Filter> filters)
    {
        return this;
    }

    final public Timeseries intervals(List<String> intervals)
    {
        return this;
    }
}
