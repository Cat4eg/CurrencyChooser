package org.rageteam.currencychooser;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.rageteam.currencychooser.model.Valute;
import org.rageteam.currencychooser.mvp.MainActivityMVP;
import org.rageteam.currencychooser.mvp.MainActivityPresenter;
import org.rageteam.currencychooser.mvp.Model;
import org.rageteam.currencychooser.mvp.NetworkRepository;

public class MainFragment extends Fragment implements MainActivityMVP.View {
    private MainActivityMVP.Presenter presenter;
    private MainFragmentHolder holder;
    private ArrayAdapter<Valute> adapter;

    @Override
    public void loadCurrenciesCompleted(Valute[] valutes) {
        if (adapter != null) {
            return;
        }
        adapter = createAdapter(valutes);
        getActivity().runOnUiThread(() -> {
            holder.currencyFromSp.setAdapter(adapter);
            holder.currencyToSp.setAdapter(adapter);
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
        presenter.updateAll();
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

        presenter = new MainActivityPresenter(this, new Model(new NetworkRepository()));
        presenter.updateCurrencies();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        holder = new MainFragmentHolder(view);
        holder.convertBtn.setOnClickListener(v -> presenter.convert());
        if (adapter != null) {
            holder.currencyFromSp.setAdapter(adapter);
            holder.currencyToSp.setAdapter(adapter);
        }
        return view;
    }

    @Override
    public Valute getValuteTo() {
        return (Valute) holder.currencyToSp.getSelectedItem();
    }

    @Override
    public Valute getValuteFrom() {
        return (Valute) holder.currencyFromSp.getSelectedItem();
    }

    @Override
    public String getCurrencyVal() {
        return holder.currencyValET.getText().toString();
    }

    private ArrayAdapter<Valute> createAdapter(Valute[] valutes) {
        if (adapter != null) {
            return adapter;
        }
        ArrayAdapter<Valute> result = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, valutes);
        result.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        return result;
    }

    private static class MainFragmentHolder {
        EditText currencyValET;
        Spinner currencyFromSp;
        Spinner currencyToSp;
        Button convertBtn;
        TextView convertedTV;

        MainFragmentHolder(View view) {
            currencyValET = view.findViewById(R.id.currency_value);
            currencyFromSp = view.findViewById(R.id.currency_from);
            currencyToSp = view.findViewById(R.id.currency_to);
            convertBtn = view.findViewById(R.id.button_convert);
            convertedTV = view.findViewById(R.id.converted_value);
        }
    }
}
