package LabN3;

public class QueryParser
{
    private float fromAmount;
    private String fromCurrency;
    private String toCurrency;

    // Defined format: AMOUNT,FROM,TO
    // Example: 500,USD,NOK
    public boolean parse(String query)
    {
        query = query.toUpperCase();
        String[] elements = query.split(",");

        if(elements.length != 3)
            return false;

        try
        {
            this.fromAmount = Float.parseFloat(elements[0]);
            this.fromCurrency = elements[1];
            this.toCurrency = elements[2];
            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
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
