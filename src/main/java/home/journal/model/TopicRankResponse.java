package home.journal.model;

import java.util.Date;
import java.util.List;

public class TopicRankResponse
{
    private Date timestamp;
    private List<TopicCount> result;

    public Date getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(Date timestamp)
    {
        this.timestamp = timestamp;
    }

    public List<TopicCount> getResult()
    {
        return result;
    }

    public void setResult(List<TopicCount> result)
    {
        this.result = result;
    }
}
