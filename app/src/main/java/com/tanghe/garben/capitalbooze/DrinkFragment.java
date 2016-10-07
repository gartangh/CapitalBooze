package com.tanghe.garben.capitalbooze;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DrinkFragment.OnDrinkFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class DrinkFragment extends Fragment {

    private OnDrinkFragmentInteractionListener mListener;

    private String name = "";
    private double price = 0.00;
    private double min = 0.00;
    private double max = 0.00;

    public DrinkFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_drink, container, false);

        final TextView mName = (TextView) view.findViewById(R.id.mName);
        mName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mName.setText("");
            }
        });
        mName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (mName.getText().toString().length() > 1) {
                    String s = mName.getText().toString();
                    s = s.substring(0,1).toUpperCase() + s.substring(1).toLowerCase();
                    boolean geldig = true;
                    for (DrinkUI uidrink : DrinkUI.uidrinks) {
                        if (uidrink.getName().equals(s)) {
                            geldig = false;
                        }
                    }
                    if (geldig) {
                        name = s;
                        mName.setText(name);
                        mName.setHint(getString(R.string.name));
                    }
                    else {
                        mName.setText("");
                        mName.setHint(getString(R.string.already_used));
                    }
                }
                else {
                    mName.setText("");
                    mName.setHint(getString(R.string.invalid_length));
                }
                return false;
            }
        });
        final EditText mPrice = (EditText) view.findViewById(R.id.mPrice);
        mPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPrice.setText("");
            }
        });
        mPrice.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                try {
                    double d = MainActivity.round(Double.parseDouble(mPrice.getText().toString()));
                    if (d > 60.00) {
                        mPrice.setText("");
                        mPrice.setHint(getString(R.string.invalid_max_price));
                    }
                    else if (d < 1.00) {
                        mPrice.setText("");
                        mPrice.setHint(getString(R.string.invalid_min_price));
                    }
                    else {
                        price = d;
                        mPrice.setText(String.format(Locale.getDefault(), "%.2f",d));
                        mPrice.setHint(getString(R.string.price));
                    }
                }
                catch (NumberFormatException e) {
                    mPrice.setText("");
                    mPrice.setHint(getString(R.string.invalid_min_price));
                }

                return false;
            }
        });
        final EditText mMin = (EditText) view.findViewById(R.id.mMin);
        mMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMin.setText("");
            }
        });
        mMin.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                try {
                    double d = MainActivity.round(Double.parseDouble(mMin.getText().toString()));
                    if (d < 1.00) {
                        mMin.setText("");
                        mMin.setHint(getString(R.string.invalid_min_price));
                    }
                    else if (d > price) {
                        mMin.setText("");
                        mMin.setHint(getString(R.string.invalid_min_price));
                    }
                    else {
                        min = d;
                        mMin.setText(String.format(Locale.getDefault(), "%.2f",d));
                        mMin.setHint(getString(R.string.min));
                    }
                }
                catch (NumberFormatException e) {
                    mMin.setText("");
                    mMin.setHint(getString(R.string.invalid_min_price));
                }
                return false;

            }
        });
        final EditText mMax = (EditText) view.findViewById(R.id.mMax);
        mMax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMax.setText("");
            }
        });
        mMax.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                try {
                    double d = MainActivity.round(Double.parseDouble(mMax.getText().toString()));
                    if (d > 60.00) {
                        mMax.setText("");
                        mMax.setHint(getString(R.string.invalid_max));
                    }
                    else if (d < price) {
                        mMax.setText("");
                        mMax.setHint(getString(R.string.invalid_max_price));
                    }
                    else {
                        max = d;
                        mMax.setText(String.format(Locale.getDefault(), "%.2f",d));
                        mMax.setHint(getString(R.string.max));
                    }
                }
                catch (NumberFormatException e) {
                    mMax.setText("");
                    mMax.setHint(getString(R.string.invalid_min_price));
                }
                return false;
            }
        });
        final Button add = (Button) view.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!name.equals("") && price >= 1.00 && min >= 1.00 && max >= price && min <= price && !AdminOnlyFragment.partyStarted) {
                    new DrinkUI(name, price, min, max);
                }
                else {
                    Toast.makeText(getContext(), getString(R.string.could_not_add_drink), Toast.LENGTH_LONG).show();
                }
                name = "";
                price = 0.00;
                min = 0.00;
                max = 0.00;
                mName.setText("");
                mPrice.setText("");
                mMin.setText("");
                mMax.setText("");
            }
        });

        final Button back = (Button) view.findViewById(R.id.drink_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onDrinkBackPressed();
            }
        });
        final Button next = (Button) view.findViewById(R.id.drink_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onDrinkNextPressed();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDrinkFragmentInteractionListener) {
            mListener = (OnDrinkFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnDrinkFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnDrinkFragmentInteractionListener {
        void onDrinkBackPressed();
        void onDrinkNextPressed();
    }
}
