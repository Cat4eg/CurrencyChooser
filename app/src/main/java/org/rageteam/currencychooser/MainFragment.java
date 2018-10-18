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

import org.rageteam.currencychooser.model.ValCurs;
import org.rageteam.currencychooser.model.Valute;
import org.rageteam.currencychooser.mvp.MainActivityMVP;
import org.rageteam.currencychooser.mvp.MainActivityPresenter;

public class MainFragment extends Fragment implements MainActivityMVP.View {
    private MainActivityMVP.Presenter presenter;
    private MainFragmentHolder holder;
    private ArrayAdapter<Valute> adapter;
    private String message;

    @Override
    public void loadCurrenciesCompleted(ValCurs currencies) {
        Valute[] valutes = currencies.getValutes().toArray(new Valute[]{});
        adapter = createAdapter(valutes);
        getActivity().runOnUiThread(() -> {
            if (holder != null) {
                holder.currencyFromSp.setAdapter(adapter);
                holder.currencyToSp.setAdapter(adapter);
                enableSelectors();
            }
        });
    }

    @Override
    public void disableSelectors() {
        showErrorMessage();
        holder.currencyFromSp.setEnabled(false);
        holder.currencyToSp.setEnabled(false);
    }

    private void showErrorMessage() {
        if (message != null && message.length() > 0) {
            holder.errorTV.setVisibility(View.VISIBLE);
            holder.errorTV.setText(message);
        } else {
            holder.errorTV.setVisibility(View.GONE);
        }
    }

    public void enableSelectors() {
        this.message = null;
        showErrorMessage();
        holder.currencyFromSp.setEnabled(true);
        holder.currencyToSp.setEnabled(true);
    }

    @Override
    public void loadError(String message) {
        this.message = message;
        getActivity().runOnUiThread(() -> {
            if (holder != null) {
                showErrorMessage();
            }
        });
    }

    private ArrayAdapter<Valute> createAdapter(Valute[] valutes) {
        ArrayAdapter<Valute> result = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, valutes);
        result.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        return result;
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
        if (adapter != null) {
            holder.currencyFromSp.setAdapter(adapter);
            holder.currencyToSp.setAdapter(adapter);
            enableSelectors();
        } else {
            disableSelectors();
        }
        holder.convertBtn.setOnClickListener(v -> presenter.convert(
                holder.currencyValET.getText().toString(),
                (Valute)holder.currencyFromSp.getSelectedItem(),
                (Valute) holder.currencyToSp.getSelectedItem())
        );
        return view;
    }

    private static class MainFragmentHolder {
        EditText currencyValET;
        Spinner currencyFromSp;
        Spinner currencyToSp;
        Button convertBtn;
        TextView convertedTV;
        TextView errorTV;

        MainFragmentHolder(View view) {
            currencyValET = view.findViewById(R.id.currency_value);
            currencyFromSp = view.findViewById(R.id.currency_from);
            currencyToSp = view.findViewById(R.id.currency_to);
            convertBtn = view.findViewById(R.id.button_convert);
            convertedTV = view.findViewById(R.id.converted_value);
            errorTV = view.findViewById(R.id.error);
        }
    }
}
