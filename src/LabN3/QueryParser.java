package LabN3;

public class QueryParser
{
    private String fromCurrency;
    private String toCurrency;
    private float fromAmount;

    public QueryParser()
    {

    }

    public boolean parse(String query)
    {
        // Bryter opp strengen, formaterer elementene og legger dataen inn i klasse variablene
        // returnerer true/false avhengig av om det er en godkjent query
        return true;
    }

    public String getFromCurrency()
    {
        return fromCurrency;
    }

    public String getToCurrency()
    {
        return toCurrency;
    }

    public float getFromAmount()
    {
        return fromAmount;
    }
}
