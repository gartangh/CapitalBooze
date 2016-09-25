package com.tanghe.garben.capitalbooze;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PricesFragment.OnPricesFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class PricesFragment extends Fragment {

    private OnPricesFragmentInteractionListener mListener;
    private final static String TAG = "Prices";

    static Date updated;

    private LinearLayout verticalLayoutPrices;
    static TextView mUpdated;

    public PricesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_prices, container, false);

        verticalLayoutPrices = (LinearLayout) view.findViewById(R.id.verticalLayoutPrices);
        for (DrinkUI i : DrinkUI.uidrinks) {
            try {
                verticalLayoutPrices.addView(i.horizontalLayoutPrices);
            }
            catch (IllegalStateException e) {
                Log.d(TAG, "DrinkUI " + i.name + " already in verticalLayoutPrices");
            }
        }

        mUpdated = (TextView) view.findViewById(R.id.mUpdated);

        if (updated == null) {
            mUpdated.setText(getContext().getResources().getString(R.string.no_data_yet));
        }
        else {
            mUpdated.setText(getContext().getResources().getString(R.string.updated, MainActivity.sdf.format(updated)));
        }

        final Button back = (Button) view.findViewById(R.id.prices_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onPricesBackPressed();
            }
        });

        final Button next = (Button) view.findViewById(R.id.prices_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPricesNextPressed();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPricesFragmentInteractionListener) {
            mListener = (OnPricesFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnPricesFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        verticalLayoutPrices.removeAllViews();
        super.onDestroyView();
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
    public interface OnPricesFragmentInteractionListener {
        void onPricesBackPressed();
        void onPricesNextPressed();
    }

    public static void setUpdated(Date date) {
        updated = date;
    }
}