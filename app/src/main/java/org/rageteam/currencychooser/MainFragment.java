package org.rageteam.currencychooser;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.rageteam.currencychooser.model.Valute;
import org.rageteam.currencychooser.mvp.MainActivityMVP;
import org.rageteam.currencychooser.mvp.MainActivityPresenter;

public class MainFragment extends Fragment implements MainActivityMVP.View {
    private MainActivityMVP.Presenter presenter;
    private MainFragmentHolder holder;
    private ArrayAdapter<Valute> adapter;
    private boolean convertForward;

    @Override
    public void loadCurrenciesCompleted(Valute[] valutes) {
        if (adapter != null) {
            return;
        }
        adapter = createAdapter(valutes);
        getActivity().runOnUiThread(this::setAdapter);
    }

    private void setAdapter() {
        holder.currencyFromSp.setAdapter(adapter);
        holder.currencyToSp.setAdapter(adapter);
        presenter.adapterUpdated(true);
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

        convertForward = true;

        presenter = new MainActivityPresenter(this, ((App) getActivity().getApplication()).model());
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
        holder.convertBtn.setOnClickListener(v -> presenter.convert(convertForward));
        if (adapter != null) {
            setAdapter();
        } else {
            presenter.adapterUpdated(false);
        }
        holder.currencyValET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                presenter.convert(convertForward);
            }
        });
        holder.direction.setImageResource(convertForward ? R.drawable.ic_arrow_forward_black_24dp
                : R.drawable.ic_arrow_back_black_24dp);
        holder.direction.setOnClickListener(v -> {
            flipDirection();
        });
        holder.currencyFromSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                presenter.updateNameFrom((Valute) parent.getItemAtPosition(position));
                presenter.convert(convertForward);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //do nothing
            }
        });

        holder.currencyToSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                presenter.updateNameTo((Valute) parent.getItemAtPosition(position));
                presenter.convert(convertForward);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //do nothing
            }
        });
        return view;
    }

    private void flipDirection() {
        convertForward = !convertForward;
        if (convertForward) {
            holder.direction.setImageResource(R.drawable.ic_arrow_forward_black_24dp);
        } else {
            holder.direction.setImageResource(R.drawable.ic_arrow_back_black_24dp);
        }
        presenter.convert(convertForward);
    }

    @Override
    public void enableCurrencies(boolean enable) {
        holder.currencyFromSp.setEnabled(enable);
        holder.currencyToSp.setEnabled(enable);
        holder.convertBtn.setEnabled(enable);
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

    @Override
    public void setNameFrom(String from) {
        holder.nameFromTV.setText(from);
    }

    @Override
    public void setNameTo(String to) {
        holder.nameToTV.setText(to);
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
        TextView nameFromTV;
        TextView nameToTV;
        ImageView direction;

        MainFragmentHolder(View view) {
            currencyValET = view.findViewById(R.id.currency_value);
            currencyFromSp = view.findViewById(R.id.currency_from);
            currencyToSp = view.findViewById(R.id.currency_to);
            convertBtn = view.findViewById(R.id.button_convert);
            convertedTV = view.findViewById(R.id.converted_value);
            nameFromTV = view.findViewById(R.id.name_from);
            nameToTV = view.findViewById(R.id.name_to);
            direction = view.findViewById(R.id.direction);
        }
    }
}
