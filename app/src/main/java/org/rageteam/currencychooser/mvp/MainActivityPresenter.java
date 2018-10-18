package org.rageteam.currencychooser.mvp;

import org.rageteam.currencychooser.model.ValCurs;
import org.rageteam.currencychooser.model.Valute;
import org.rageteam.currencychooser.util.NetworkUtils;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

public class MainActivityPresenter implements MainActivityMVP.Presenter {
    private static final String CURRENCY_FORMAT = "###,###.00";

    private boolean visible;
    private MainActivityMVP.View view;
    private String converted = "";

    private ValCurs currencies;

    public MainActivityPresenter(MainActivityMVP.View view) {
        this.view = view;
    }

    @Override
    public void updateCurrencies() {
        Thread t = new Thread(() -> {
            try {
                this.currencies = NetworkUtils.loadCurrencies();
                loadCompleted();
            } catch (Exception e) {
                loadError(e.getMessage());
            }
        });
        t.start();
    }

    private void loadCompleted() {
        if (currencies != null) {
            view.loadCurrenciesCompleted(this.currencies);
        }
    }

    private void loadError(String message) {
        view.loadError(message);
    }

    @Override
    public void onStart() {
        visible = true;
        showConverted();
    }

    @Override
    public void onStop() {
        visible = false;
    }

    @Override
    public void convert(String currencyVal, Valute from, Valute to) {
        if (currencyVal == null || currencyVal.length() == 0) {
            return;
        }
        double result = from.getValue() * Double.valueOf(currencyVal) / from.getNominal() / to.getValue() * to.getNominal();
        converted = String.valueOf(result);
        if (visible) {
            showConverted();
        }
    }

    private void showConverted() {
        if (converted == null || converted.length() == 0) {
            return;
        }
        DecimalFormat formatter = new DecimalFormat(CURRENCY_FORMAT);
        view.showConverted(formatter.format(Double.valueOf(converted)));
    }
}
