package com.tanghe.garben.capitalbooze;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;
import java.util.Locale;

import static android.support.v4.content.ContextCompat.getColor;

public class PricesFragment extends Fragment {

    private final static String TAG = "Prices";

    static boolean seen = false;
    static Date updated;
    static String maxOrderName = "";
    static String allTimeWolfName = "";

    static TextView mWolf;
    static LinearLayout verticalLayoutPrices;
    static TextView mUpdated;
    static String wolf = "Wolf";

    public PricesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_prices, container, false);

        mWolf = (TextView) view.findViewById(R.id.mWolf);
        setWolf();

        mWolf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wolf.equals("Wolf")) {
                    setWolf();
                } else {
                    setAllTimeWolf();
                }
            }
        });

        verticalLayoutPrices = (LinearLayout) view.findViewById(R.id.verticalLayoutPrices);
        for (DrinkUI i : DrinkUI.uidrinks) {
            try {
                verticalLayoutPrices.addView(i.horizontalLayoutPrices);
            } catch (IllegalStateException e) {
                Log.d(TAG, "DrinkUI " + i.name + " already in verticalLayoutPrices");
            }
        }

        mUpdated = (TextView) view.findViewById(R.id.mPricesUpdated);
        if (updated == null) {
            mUpdated.setText(getString(R.string.no_data_yet));
        } else {
            mUpdated.setText(getString(R.string.updated, MainActivity.sdf.format(updated)));
        }
        if (seen) {
            mUpdated.setTextColor(getColor(getActivity(), R.color.grey));
        } else {
            mUpdated.setTextColor(getColor(getActivity(), R.color.colorPrimary));
        }
        mUpdated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seen = true;
                mUpdated.setTextColor(getColor(getActivity(), R.color.grey));
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void setWolf() {
        if (maxOrderName.equals("")) {
            mWolf.setText(String.format(Locale.getDefault(), getString(R.string.wolf), "", OrderFragment.maxOrder));
        } else {
            mWolf.setText(String.format(Locale.getDefault(), getString(R.string.wolf), maxOrderName, OrderFragment.maxOrder));
        }
        wolf = "";
    }

    public void setAllTimeWolf() {
        if (allTimeWolfName.equals("")) {
            mWolf.setText(String.format(Locale.getDefault(), getString(R.string.all_time_wolf), "", OrderFragment.allTimeWolf));
        } else {
            mWolf.setText(String.format(Locale.getDefault(), getString(R.string.all_time_wolf), allTimeWolfName, OrderFragment.allTimeWolf));
        }
        wolf = "Wolf";
    }
}