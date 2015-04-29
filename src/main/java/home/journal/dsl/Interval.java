package home.journal.dsl;

import java.time.LocalDate;

public class Interval
{
    private LocalDate end;
    private LocalDate start;

    public LocalDate getEnd()
    {
        return end;
    }

    public void setEnd(LocalDate end)
    {
        this.end = end;
    }

    public LocalDate getStart()
    {
        return start;
    }

    public void setStart(LocalDate start)
    {
        this.start = start;
    }
}
