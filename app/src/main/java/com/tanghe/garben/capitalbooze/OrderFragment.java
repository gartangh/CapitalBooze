package com.tanghe.garben.capitalbooze;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class OrderFragment extends Fragment {

    private final static String TAG = "Order";

    protected static double totalPrice = 0.00;
    protected static double totalPriceLast = 0.00;
    protected static int totalSquares = 0;
    protected static int totalSquaresLast = 0;
    protected static int totalCount = 0;
    protected static int totalCountLast = 0;
    protected static long maxOrder = 0L;
    protected static long allTimeWolf = 0L;

    static LinearLayout verticalLayoutOrders;
    static TextView mTotalPrice;
    static TextView mTotalPriceLast;
    static TextView mTotalSquares;
    static TextView mTotalSquaresLast;
    static TextView mTotalCount;
    static TextView mTotalCountLast;

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

        verticalLayoutOrders = view.findViewById(R.id.verticalLayoutOrders);
        for (DrinkUI i : DrinkUI.uidrinks) {
            try {
                verticalLayoutOrders.addView(i.horizontalLayoutOrders);
            } catch (IllegalStateException e) {
                Log.d(TAG, "DrinkUI " + i.name + " already in verticalLayoutOrders");
            }
        }

        mTotalPrice = view.findViewById(R.id.mTotalPrice);
        mTotalPriceLast = view.findViewById(R.id.mTotalPriceLast);
        mTotalSquares = view.findViewById(R.id.mTotalSquares);
        mTotalSquaresLast = view.findViewById(R.id.mTotalSquaresLast);
        mTotalCount = view.findViewById(R.id.mTotalCount);
        mTotalCountLast = view.findViewById(R.id.mTotalCountLast);

        setTotals();
        setTotalsLast();

        final Button order = view.findViewById(R.id.order);
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (totalCount > 0 && AdminOnlyFragment.partyStarted) {
                        for (DrinkUI i : DrinkUI.uidrinks) {
                        i.countCurrent += i.orderCount;
                        MainActivity.myRef.child("Drinks").child(i.name).child("countCurrent").setValue(i.countCurrent);

                        i.partyCount += i.orderCount;
                        MainActivity.myRef.child("Drinks").child(i.name).child("partyCount").setValue(i.partyCount);

                        i.partyRevenue += i.orderCount * i.price;
                        MainActivity.myRef.child("Drinks").child(i.name).child("partyRevenue").setValue(i.partyRevenue);

                        Drink.partyRevenueTotal += i.orderCount * i.price;
                        MainActivity.myRef.child("partyRevenueTotal").setValue(Drink.partyRevenueTotal);

                        i.orderCount = 0;
                        i.mDrinkCountOrders.setText(String.format(Locale.getDefault(), "%1d", i.orderCount));
                    }

                    Drink.countTotalCurrent += totalCount;
                    MainActivity.myRef.child("countTotalCurrent").setValue(Drink.countTotalCurrent);
                    Drink.partyCountTotal += totalCount;
                    MainActivity.myRef.child("partyCountTotal").setValue(Drink.partyCountTotal);

                    totalPriceLast = totalPrice;
                    totalPrice = 0.00;
                    totalSquaresLast = totalSquares;
                    totalSquares = 0;
                    totalCountLast = totalCount;
                    if (totalCount > maxOrder) {
                        MainActivity.myRef.child("maxOrder").setValue(totalCount);
                        if (totalCount > allTimeWolf) {
                            MainActivity.myRef.child("allTimeWolf").setValue(totalCount);
                            Toast.makeText(getActivity(), getString(R.string.new_all_time_wolf), Toast.LENGTH_LONG).show();
                            Log.d(TAG, getString(R.string.new_all_time_wolf));
                        } else {
                            Toast.makeText(getActivity(), getString(R.string.new_wolf), Toast.LENGTH_LONG).show();
                            Log.d(TAG, getString(R.string.new_wolf));
                        }
                    }
                    totalCount = 0;

                    setTotals();
                    setTotalsLast();
                } else if (totalCount == 0){
                    Toast.makeText(getActivity(), getString(R.string.nothing_to_order), Toast.LENGTH_LONG).show();
                    Log.d(TAG, getString(R.string.nothing_to_order));
                } else if (!AdminOnlyFragment.partyStarted) {
                    Toast.makeText(getActivity(), getString(R.string.party_not_started), Toast.LENGTH_LONG).show();
                    Log.d(TAG, getString(R.string.party_not_started));
                } else {
                    // Should never come here
                    Toast.makeText(getActivity(), getString(R.string.nothing_to_order_or_party_not_started), Toast.LENGTH_LONG).show();
                    Log.d(TAG, getString(R.string.nothing_to_order_or_party_not_started));
                }
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public static void setTotals() {
        if (totalPrice > 0.00) {
            mTotalPrice.setText(String.format(Locale.getDefault(), "€%.2f", totalPrice));
        } else {
            totalPrice = 0.00;
            mTotalPrice.setText(String.format(Locale.getDefault(), "€%.2f", totalPrice));
        }
        mTotalSquares.setText(String.format(Locale.getDefault(), "#%1d", totalSquares));
        mTotalCount.setText(String.format(Locale.getDefault(), "%1d", totalCount));
    }

    public static void setTotalsLast() {
        mTotalPriceLast.setText(String.format(Locale.getDefault(), "(€%.2f)", totalPriceLast));
        mTotalSquaresLast.setText(String.format(Locale.getDefault(), "(#%1d)", totalSquaresLast));
        mTotalCountLast.setText(String.format(Locale.getDefault(), "(%1d)", totalCountLast));
    }
}