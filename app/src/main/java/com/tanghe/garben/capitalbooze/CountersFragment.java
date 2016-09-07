package com.tanghe.garben.capitalbooze;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class CountersFragment extends Fragment {

    private OnCountersFragmentInteractionListener mListener;
    protected static Context context;

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

        final LinearLayout verticalLayoutCounters = (LinearLayout) view.findViewById(R.id.verticalLayoutCounters);
        for (DrinkUI i : DrinkUI.uidrinks) {
            verticalLayoutCounters.addView(i.horizontalLayoutCounters);
        }

        final Button back = (Button) view.findViewById(R.id.counters_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onCountersBackPressed();
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

    public interface OnCountersFragmentInteractionListener {
        void onCountersBackPressed();
        void onCountersNextPressed();
    }

    public static void setArgument(Context context) {
        CountersFragment.context = context;
    }
}