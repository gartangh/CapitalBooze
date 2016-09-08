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

import org.w3c.dom.Text;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OrderFragment.OnOrderFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class OrderFragment extends Fragment {

    private OnOrderFragmentInteractionListener mListener;
    private final static String TAG = "Order";

    private static LinearLayout verticalLayoutOrders;
    private static TextView mTotalPrice;
    private static TextView mTotalPriceLast;
    private static TextView mTotalSquares;
    private static TextView mTotalSquaresLast;
    private static TextView mTotalCount;
    private static TextView mTotalCountLast;

    public OrderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        verticalLayoutOrders = (LinearLayout) view.findViewById(R.id.verticalLayoutOrders);
        for (DrinkUI i : DrinkUI.uidrinks) {
            try {
                verticalLayoutOrders.addView(i.horizontalLayoutOrders);
            }
            catch (IllegalStateException e) {
                Log.d(TAG, "DrinkUI " + i.name + " already in verticalLayoutOrders");
            }
        }

        mTotalPrice = (TextView) view.findViewById(R.id.mTotalPrice);
        mTotalPriceLast = (TextView) view.findViewById(R.id.mTotalPriceLast);
        mTotalSquares = (TextView) view.findViewById(R.id.mTotalSquares);
        mTotalSquaresLast = (TextView) view.findViewById(R.id.mTotalSquaresLast);
        mTotalCount = (TextView) view.findViewById(R.id.mTotalCount);
        mTotalCountLast = (TextView) view.findViewById(R.id.mTotalCountLast);

        final Button order = (Button) view.findViewById(R.id.order);
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(mTotalCount.getText().toString()) > 0) {
                    mTotalPriceLast.setText("(" + mTotalPrice.getText() + ")");
                    mTotalSquaresLast.setText("(" + mTotalSquares.getText() + ")");
                    mTotalCountLast.setText("(" + mTotalCount.getText() + ")");

                    DrinkUI.orderSend();

                    Log.d(TAG, "Order send");
                }
                else {
                    Log.d(TAG, "Nothing in order");
                }

            }
        });

        final Button back = (Button) view.findViewById(R.id.order_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onOrderBackPressed();
            }
        });
        final Button next = (Button) view.findViewById(R.id.order_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onOrderNextPressed();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnOrderFragmentInteractionListener) {
            mListener = (OnOrderFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        verticalLayoutOrders.removeAllViews();
        super.onDestroyView();
    }

    public static void setTotals(double p, int s, int c) {
        mTotalPrice.setText(String.format(Locale.getDefault(), "€ %.2f", p));
        mTotalSquares.setText(String.format(Locale.getDefault(), "#%1d", s));
        mTotalCount.setText(String.format(Locale.getDefault(), "%1d", c));
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
    public interface OnOrderFragmentInteractionListener {
        void onOrderBackPressed();
        void onOrderNextPressed();
    }
}