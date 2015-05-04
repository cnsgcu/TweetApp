package home.journal.model;

import java.util.Date;
import java.util.List;

public class DeviceRankResponse
{
    private Date timestamp;
    private List<DeviceCount> result;

    public Date getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(Date timestamp)
    {
        this.timestamp = timestamp;
    }

    public List<DeviceCount> getResult()
    {
        return result;
    }

    public void setResult(List<DeviceCount> result)
    {
        this.result = result;
    }
}
