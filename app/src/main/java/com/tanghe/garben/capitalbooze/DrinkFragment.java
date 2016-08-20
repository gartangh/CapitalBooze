package com.tanghe.garben.capitalbooze;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DrinkFragment.OnDrinkFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class DrinkFragment extends Fragment {
    private OnDrinkFragmentInteractionListener mListener;

    private String name = "";
    private double price = 0.0;
    private double min = 0.0;
    private double max = 0.0;

    public DrinkFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_drink, container, false);

        final TextView nametext = (TextView) view.findViewById(R.id.name);
        nametext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nametext.setText("");
            }
        });
        nametext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (!nametext.getText().toString().equals("")) {
                    name = nametext.getText().toString();
                }
                return false;
            }
        });
        final EditText pricenr = (EditText) view.findViewById(R.id.price);
        pricenr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pricenr.setText("");
            }
        });
        pricenr.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                price = Drink.round2decimals(Double.parseDouble(pricenr.getText().toString()));
                if (price > 5.0 || price < 1 ) {
                    price = 0.0;
                }
                return false;
            }
        });
        // Not completely without mistakes!
        final EditText minnr = (EditText) view.findViewById(R.id.min);
        minnr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minnr.setText("");
            }
        });
        minnr.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                min = Drink.round2decimals(Double.parseDouble(minnr.getText().toString()));
                if (min > price || min < 1.0) {
                    min = 0.0;
                }
                return false;

            }
        });
        final EditText maxnr = (EditText) view.findViewById(R.id.max);
        maxnr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                maxnr.setText("");
            }
        });
        maxnr.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                max = Drink.round2decimals(Double.parseDouble(maxnr.getText().toString()));
                if (max < price || max > 60.0) {
                    max = 0.0;
                }
                return false;
            }
        });
        final Button add = (Button) view.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!name.equals("") && price > 0.0 && min > 0.0 && max > 0.0) {
                    new Drink(name, price, min, max);
                    name = "";
                    price = 0.0;
                    min = 0.0;
                    max = 0.0;
                    nametext.setText("");
                    pricenr.setText("");
                    minnr.setText("");
                    maxnr.setText("");
                }
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
