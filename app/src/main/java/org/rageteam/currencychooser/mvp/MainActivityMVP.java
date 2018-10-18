package org.rageteam.currencychooser.mvp;

import org.rageteam.currencychooser.model.ValCurs;

public class MainActivityMVP {
    public interface View {

        void showConverted(String converted);

        void loadCurrenciesCompleted(ValCurs currencies);
    }

    public interface Presenter {
        void onStart();
        void onStop();

        void convert(String currencyVal, String from, String to);

        void updateCurrencies();
    }
}
