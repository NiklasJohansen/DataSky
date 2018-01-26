package LabN3;

public class CurrencyConverter
{
    // Data fra fil lagres i en hashmap/liste/array

    public CurrencyConverter(String fileName)
    {
        // Laster inn all data fra fil og legger det i klasse variablene
    }

    public float getRate(String from, String to)
    {
        // Konvereterer valutaen, eks. 1 USD = ? EUR
        // Kan evetuelt ta inn mengden from også og returnere den ferdige verdien
        return 0;
    }

    public boolean isSupported(String currency)
    {
        // Søker etter currencyen i listen og returner true hvis den finnes
        return true;
    }
}
