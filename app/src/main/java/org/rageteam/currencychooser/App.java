package org.rageteam.currencychooser;

import android.app.Application;

import org.rageteam.currencychooser.mvp.MainActivityMVP;
import org.rageteam.currencychooser.mvp.Model;
import org.rageteam.currencychooser.mvp.NetworkRepository;

public class App extends Application {
    private MainActivityMVP.Model model;

    @Override
    public void onCreate() {
        super.onCreate();

        //тут могла быть ваша реклама (или даггер)
        model = new Model(new NetworkRepository());
    }

    public MainActivityMVP.Model model() {
        return model;
    }
}
