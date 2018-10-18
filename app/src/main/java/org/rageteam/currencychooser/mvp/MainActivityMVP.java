package org.rageteam.currencychooser.mvp;

import org.rageteam.currencychooser.model.ValCurs;
import org.rageteam.currencychooser.model.Valute;

public class MainActivityMVP {
    public interface View {
        void showConverted(String converted);
        void loadCurrenciesCompleted(ValCurs currencies);
        void disableSelectors();
        void loadError(String message);
    }

    public interface Presenter {
        void onStart();
        void onStop();

        void convert(String currencyVal, Valute from, Valute to);
        void updateCurrencies();
    }
}
