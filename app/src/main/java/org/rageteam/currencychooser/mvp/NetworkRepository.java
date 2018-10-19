package org.rageteam.currencychooser.mvp;

import org.rageteam.currencychooser.model.ValCurs;
import org.rageteam.currencychooser.util.NetworkUtils;

public class NetworkRepository implements MainActivityMVP.Repository {
    @Override
    public void loadValCurs(MainActivityMVP.Callback<ValCurs> callback) {
        synchronized (this) {
            Thread t = new Thread(() -> {
                try {
                    callback.onSuccess(NetworkUtils.loadCurrencies());
                } catch (Exception e) {
                    callback.onFailure("Error in loading or parsing", e);
                }
            });
            t.start();
        }
    }
}
