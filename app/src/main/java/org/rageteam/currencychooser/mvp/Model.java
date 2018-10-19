package org.rageteam.currencychooser.mvp;

import org.rageteam.currencychooser.model.ValCurs;
import org.rageteam.currencychooser.model.Valute;

import java.text.DecimalFormat;

public class Model implements MainActivityMVP.Model {
    private static final String CURRENCY_FORMAT = "###,###.00";
    private static final String DEFAULT_VALUE = "";

    private ValCurs valCurs;
    private String converted;

    private MainActivityMVP.Repository repository;

    public Model(MainActivityMVP.Repository repository) {
        this.repository = repository;
    }

    @Override
    public void loadValutes(MainActivityMVP.Callback<Valute[]> callback, boolean noCache) {
        if (valCurs == null || noCache) {
            repository.loadValCurs(new MainActivityMVP.Callback<ValCurs>() {
                @Override
                public void onSuccess(ValCurs result) {
                    valCurs = result;
                    callback.onSuccess(valCurs.getValutes().toArray(new Valute[]{}));
                }

                @Override
                public void onFailure(String message, Throwable t) {
                    callback.onFailure(message, t);
                }
            });
        } else {
            callback.onSuccess(valCurs.getValutes().toArray(new Valute[]{}));
        }
    }


    @Override
    public Valute[] getValutes() {
        return valCurs == null ? null : valCurs.getValutes().toArray(new Valute[]{});
    }

    @Override
    public void updateConverted(String currencyVal, Valute from, Valute to) {
        if (currencyVal == null || currencyVal.trim().length() == 0) {
            return;
        }
        double result = from.getValue() * Double.valueOf(currencyVal) / from.getNominal() / to.getValue() * to.getNominal();
        converted = String.valueOf(result);
    }

    @Override
    public String getConverted() {
        if (converted == null || converted.trim().length() == 0) {
            return DEFAULT_VALUE;
        }
        DecimalFormat formatter = new DecimalFormat(CURRENCY_FORMAT);
        return formatter.format(Double.valueOf(converted));
    }
}
