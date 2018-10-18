package org.rageteam.currencychooser.mvp;

import org.rageteam.currencychooser.model.ValCurs;
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
                if (visible) {
                    loadCompleted();
                }
            } catch (Exception e) {
                //TODO show load error
            }
        });
        t.start();
    }

    private void loadCompleted() {
        if (currencies != null) {
            view.loadCurrenciesCompleted(this.currencies);
        }
    }

    @Override
    public void onStart() {
        visible = true;
        showConverted();
        loadCompleted();
    }

    @Override
    public void onStop() {
        visible = false;
    }

    @Override
    public void convert(String currencyVal, String from, String to) {
        converted = currencyVal;
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
