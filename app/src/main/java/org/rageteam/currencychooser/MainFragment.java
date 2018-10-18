package org.rageteam.currencychooser;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.rageteam.currencychooser.model.ValCurs;
import org.rageteam.currencychooser.mvp.MainActivityMVP;
import org.rageteam.currencychooser.mvp.MainActivityPresenter;

public class MainFragment extends Fragment implements MainActivityMVP.View {
    private MainActivityMVP.Presenter presenter;
    private MainFragmentHolder holder;

    @Override
    public void loadCurrenciesCompleted(ValCurs currencies) {
        holder.currencyFromET.post(() -> {
           holder.currencyFromET.setText(currencies.getValutes().get(0).getName());
           holder.currencyToET.setText(currencies.getValutes().get(1).getName());
        });
    }

    @Override
    public void showConverted(String converted) {
        holder.convertedTV.setText(converted);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override
    public void onStop() {
        presenter.onStop();
        super.onStop();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        presenter = new MainActivityPresenter(this);
        presenter.updateCurrencies();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        holder = new MainFragmentHolder(view);
        holder.convertBtn.setOnClickListener(v -> presenter.convert(holder.currencyValET.getText().toString(),
                holder.currencyFromET.getText().toString(), holder.currencyToET.getText().toString()));

        return view;
    }

    private static class MainFragmentHolder {
        EditText currencyValET;
        EditText currencyFromET;
        EditText currencyToET;
        Button convertBtn;
        TextView convertedTV;
        MainFragmentHolder(View view) {
            currencyValET = view.findViewById(R.id.currency_value);
            currencyFromET = view.findViewById(R.id.currency_from);
            currencyToET = view.findViewById(R.id.currency_to);
            convertBtn = view.findViewById(R.id.button_convert);
            convertedTV = view.findViewById(R.id.converted_value);
        }
    }
}
