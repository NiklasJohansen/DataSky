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
        String[] elements = query.toUpperCase().split(",");

        if(elements.length != 3)
            return false;

        try
        {
            this.fromAmount = Float.parseFloat(elements[0].trim());
            this.fromCurrency = elements[1].trim();
            this.toCurrency = elements[2].trim();
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
