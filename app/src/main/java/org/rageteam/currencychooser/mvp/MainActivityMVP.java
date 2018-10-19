package org.rageteam.currencychooser.mvp;

import org.rageteam.currencychooser.model.ValCurs;
import org.rageteam.currencychooser.model.Valute;

public interface MainActivityMVP {
    interface View {
        void showConverted(String converted);

        void loadCurrenciesCompleted(Valute[] valutes);

        void showError(String message);

        String getCurrencyVal();
        Valute getValuteFrom();
        Valute getValuteTo();
    }

    interface Presenter {
        void onStart();

        void onStop();

        void convert();

        void updateCurrencies();

        void updateAll();
    }

    interface Model {
        void loadValutes(Callback<Valute[]> callback, boolean noCache);

        Valute[] getValutes();

        void updateConverted(String currencyVal, Valute from, Valute to);

        String getConverted();
    }

    interface Repository {
        void loadValCurs(Callback<ValCurs> callback);
    }

    interface Callback<T> {
        void onSuccess(T result);

        void onFailure(String message, Throwable t);
    }
}
