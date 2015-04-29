package home.journal.dsl;

public class Aggregation
{
    private AggregationType type;
    private String fieldName;
    private String name;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getFieldName()
    {
        return fieldName;
    }

    public void setFieldName(String fieldName)
    {
        this.fieldName = fieldName;
    }

    public AggregationType getType()
    {
        return type;
    }

    public void setType(AggregationType type)
    {
        this.type = type;
    }
}
