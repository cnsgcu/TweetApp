package home.journal.model;

import java.util.Date;
import java.util.List;

public class StateRankResponse
{
    private Date timestamp;
    private List<StateCount> result;

    public Date getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(Date timestamp)
    {
        this.timestamp = timestamp;
    }

    public List<StateCount> getResult()
    {
        return result;
    }

    public void setResult(List<StateCount> result)
    {
        this.result = result;
    }
}
