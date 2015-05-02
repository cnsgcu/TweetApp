package home.journal.model;

import java.util.Date;

public class CountResponse
{
    private Date timestamp;
    private CountResult result;


    public CountResult getResult()
    {
        return result;
    }

    public void setResult(CountResult result)
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
