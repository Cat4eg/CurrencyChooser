package org.rageteam.currencychooser.util;

import org.rageteam.currencychooser.model.ValCurs;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtils {
    private static final String CURRENCY_URL = "http://www.cbr.ru/scripts/XML_daily.asp";

    public static ValCurs loadCurrencies() throws Exception {
        HttpURLConnection con = null;
        try {
            URL url = new URL(CURRENCY_URL);
            con = (HttpURLConnection) url.openConnection();
            InputStream is = new BufferedInputStream(con.getInputStream());
            Serializer serializer = new Persister();

            return serializer.read(ValCurs.class, is);
        } finally {
            con.disconnect();
        }
    }
}
