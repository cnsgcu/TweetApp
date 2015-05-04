package home.journal.model;

import java.util.Date;
import java.util.List;

public class LanguageRankResponse
{
    private Date timestamp;
    private List<LanguageCount> result;

    public Date getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(Date timestamp)
    {
        this.timestamp = timestamp;
    }

    public List<LanguageCount> getResult()
    {
        return result;
    }

    public void setResult(List<LanguageCount> result)
    {
        this.result = result;
    }
}
