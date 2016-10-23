package com.tanghe.garben.capitalbooze;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.Date;
import java.util.Locale;

public class CountersFragment extends Fragment {

    private OnCountersFragmentInteractionListener mListener;
    private final static String TAG = "Counters";

    static boolean seen = false;
    static Date updated;

    private static LinearLayout verticalLayoutCounters;
    static TextView mCountTotalCurrent;
    static TextView mCountTotalLast;
    static TextView mPartyCountTotal;
    static TextView mPartyRevenueTotal;
    static TextView mUpdated;

    public CountersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_counters, container, false);

        mCountTotalCurrent = (TextView) view.findViewById(R.id.mCountTotalCurrent);
        CountersFragment.mCountTotalCurrent.setText(String.format(Locale.getDefault(), "%1d", Drink.countTotalCurrent));
        mCountTotalLast = (TextView) view.findViewById(R.id.mCountTotalLast);
        CountersFragment.mCountTotalLast.setText(String.format(Locale.getDefault(), "(%1d)", Drink.countTotalLast));
        mPartyCountTotal = (TextView) view.findViewById(R.id.mPartyCountTotal);
        CountersFragment.mPartyCountTotal.setText(String.format(Locale.getDefault(), "%1d", Drink.partyCountTotal));
        mPartyRevenueTotal = (TextView) view.findViewById(R.id.mPartyRevenueTotal);
        CountersFragment.mPartyRevenueTotal.setText(String.format(Locale.getDefault(), "€%.2f", Drink.partyRevenueTotal));

        verticalLayoutCounters = (LinearLayout) view.findViewById(R.id.verticalLayoutCounters);
        for (DrinkUI i : DrinkUI.uidrinks) {
            try {
                verticalLayoutCounters.addView(i.horizontalLayoutCounters);
            }
            catch (IllegalStateException e) {
                Log.d(TAG, "DrinkUI " + i.name + " already in verticalLayoutCounters");
            }
        }

        mUpdated = (TextView) view.findViewById(R.id.mCountersUpdated);
        if (updated == null) {
            mUpdated.setText(getContext().getString(R.string.no_data_yet));
        }
        else {
            mUpdated.setText(getContext().getString(R.string.updated, MainActivity.sdf.format(updated)));
        }
        if (seen) {
            mUpdated.setTextColor(ContextCompat.getColor(getContext(), R.color.grey));
        }
        else {
            mUpdated.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        }
        mUpdated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seen = true;
                mUpdated.setTextColor(ContextCompat.getColor(getContext(), R.color.grey));
            }
        });

        final Button next = (Button) view.findViewById(R.id.counters_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onCountersNextPressed();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCountersFragmentInteractionListener) {
            mListener = (OnCountersFragmentInteractionListener) context;
        }
        else {
            throw new RuntimeException(context.toString() + " must implement OnCountersFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        verticalLayoutCounters.removeAllViews();
        super.onDestroyView();
    }

    public interface OnCountersFragmentInteractionListener {
        void onCountersNextPressed();
    }
}