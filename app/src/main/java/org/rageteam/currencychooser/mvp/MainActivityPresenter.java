package org.rageteam.currencychooser.mvp;

import org.rageteam.currencychooser.model.Valute;

public class MainActivityPresenter implements MainActivityMVP.Presenter {
    private MainActivityMVP.View view;
    private MainActivityMVP.Model model;
    private boolean visible;

    public MainActivityPresenter(MainActivityMVP.View view, MainActivityMVP.Model model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void convert() {
        model.updateConverted(view.getCurrencyVal(), view.getValuteFrom(), view.getValuteTo());
        if (visible) {
            showConverted();
        }
    }

    @Override
    public void updateAll() {
        if (visible) {
            showConverted();
            loadCompleted();
        }
    }

    @Override
    public void updateCurrencies() {
        model.loadValutes(new MainActivityMVP.Callback<Valute[]>() {
            @Override
            public void onSuccess(Valute[] result) {
                if (visible) {
                    loadCompleted();
                }
            }

            @Override
            public void onFailure(String message, Throwable t) {
                if (visible) {
                    view.showError(message);
                }
            }
        }, false);
    }

    @Override
    public void onStart() {
        visible = true;
    }

    @Override
    public void onStop() {
        visible = false;
    }

    @Override
    public void updateNameFrom(Valute from) {
        if (visible) {
            view.setNameFrom(from.getName());
        }
    }

    @Override
    public void updateNameTo(Valute to) {
        if (visible) {
            view.setNameTo(to.getName());
        }
    }

    private void loadCompleted() {
        if (model.getValutes() != null) {
            view.loadCurrenciesCompleted(model.getValutes());
        }
    }

    private void showConverted() {
        if (visible) {
            view.showConverted(model.getConverted());
        }
    }
}
