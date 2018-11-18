package org.rageteam.currencychooser.mvp;

import org.rageteam.currencychooser.model.ValCurs;
import org.rageteam.currencychooser.model.Valute;
import org.rageteam.currencychooser.util.NetworkUtils;

public class NetworkRepository implements MainActivityMVP.Repository {
    @Override
    public void loadValCurs(MainActivityMVP.Callback<ValCurs> callback) {
        synchronized (this) {
            Thread t = new Thread(() -> {
                try {
                    ValCurs result = NetworkUtils.loadCurrencies();
                    callback.onSuccess(result);
                } catch (Exception e) {
                    callback.onFailure("Error in loading or parsing", e);
                }
            });
            t.start();
        }
    }
}
