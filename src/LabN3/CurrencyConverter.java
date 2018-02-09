package LabN3;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;

public class CurrencyConverter
{
    private HashMap<String, Float> currencies;

    public CurrencyConverter(String fileName)
    {
        this.currencies = new HashMap<>();
        this.currencies.put("NOK", 1.0f);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(fileName))))
        {
            // Skips the two top rows
            reader.readLine();
            reader.readLine();

            String line;
            while((line = reader.readLine()) != null)
            {
                String[] elements = line.split(",");

                String currency = elements[2].trim();
                float amount = Float.parseFloat(elements[1].trim());
                float amountInNOK = Float.parseFloat(elements[9].trim());

                if(amount != 1.0f)
                    amountInNOK /= amount;

                currencies.put(currency, amountInNOK);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public float getRate(String from, String to)
    {
        float fromCurrency = currencies.get(from);
        float toCurrency = currencies.get(to);
        return fromCurrency / toCurrency;
    }

    public boolean isSupported(String currency)
    {
        return currencies.get(currency) != null;
    }

    public String getAllCurrencies()
    {
        return Arrays.toString(currencies.keySet().toArray());
    }
}