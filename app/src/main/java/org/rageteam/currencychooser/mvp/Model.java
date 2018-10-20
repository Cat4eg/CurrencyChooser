package org.rageteam.currencychooser.mvp;

import org.rageteam.currencychooser.model.ValCurs;
import org.rageteam.currencychooser.model.Valute;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;
import java.util.stream.DoubleStream;

public class Model implements MainActivityMVP.Model {
    private static final String CURRENCY_FORMAT = "###,###.00";
    private static final String CURRENCY_FORMAT_BELOW_1 = "0.0000";
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
                    callback.onSuccess(sort(valCurs.getValutes()).toArray(new Valute[]{}));
                }

                @Override
                public void onFailure(String message, Throwable t) {
                    callback.onFailure(message, t);
                }
            });
        } else {
            callback.onSuccess(sort(valCurs.getValutes()).toArray(new Valute[]{}));
        }
    }


    @Override
    public Valute[] getValutes() {
        return valCurs == null ? null : sort(valCurs.getValutes()).toArray(new Valute[]{});
    }

    private List<Valute> sort(List<Valute> valutes) {
        Collections.sort(valutes);
        return valutes;
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
        Double value = Double.valueOf(converted);
        DecimalFormat formatter = value < 1 ? new DecimalFormat(CURRENCY_FORMAT_BELOW_1)
                : new DecimalFormat(CURRENCY_FORMAT);
        return formatter.format(value);
    }
}
