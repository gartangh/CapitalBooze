package com.tanghe.garben.capitalbooze;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PricesFragment.OnPricesFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class PricesFragment extends Fragment {
    protected static Context context;

    private OnPricesFragmentInteractionListener mListener;

    public PricesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_prices, container, false);


        final LinearLayout linearLayoutPrices = (LinearLayout) view.findViewById(R.id.LinearLayoutPrices);
        for (DrinkUI i : DrinkUI.uidrinks) {
            linearLayoutPrices.addView(i.horizontalLayout);
        }

        final Button back = (Button) view.findViewById(R.id.prices_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onPricesBackPressed();
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
    }

    public static void setArgument(Context context) {
        PricesFragment.context = context;
    }
}