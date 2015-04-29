package home.journal.model;

import java.util.Date;

public class TweetCountResponse
{
    private Date timestamp;
    private TweetCountResult result;


    public TweetCountResult getResult()
    {
        return result;
    }

    public void setResult(TweetCountResult result)
    {
        this.result = result;
    }

    public Date getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(Date timestamp)
    {
        this.timestamp = timestamp;
    }
}
