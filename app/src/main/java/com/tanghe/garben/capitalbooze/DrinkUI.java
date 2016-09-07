package com.tanghe.garben.capitalbooze;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class DrinkUI extends Drink {

    private static Context context;
    private static String TAG = "DinkUI";

    protected final static ArrayList<DrinkUI> uidrinks = new ArrayList<>();

    protected LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    // Orders
    protected LinearLayout horizontalLayoutOrders;
    protected TextView mNameOrders;
    protected Button mRed;
    protected TextView mDrinkCountOrders;
    protected Button mGreen;

    // Counters
    protected LinearLayout horizontalLayoutCounters;
    protected TextView mNameCounters;
    protected TextView mCountCurrent;
    protected TextView mCountLast;
    protected TextView mPartyCount;
    protected TextView mPartyRevenue;

    // Prices
    protected LinearLayout horizontalLayoutPrices;
    protected TextView mNamePrices;
    protected TextView mPrice;
    protected TextView mPriceDifference;

    public DrinkUI(String name, double price, double min, double max) {
        super(name,price,min,max);
        makeUIElements();
    }

    private void makeUIElements() {
        // Orders
        horizontalLayoutOrders = new LinearLayout(context);
        horizontalLayoutOrders.setOrientation(LinearLayout.HORIZONTAL);
        horizontalLayoutOrders.setLayoutParams(params);
        horizontalLayoutOrders.setGravity(Gravity.END);

        mNameOrders = new TextView(context);
        mNameOrders.setText(name);
        mNameOrders.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                for (DrinkUI i :
                        DrinkUI.uidrinks) {
                    if (i.name.equals(mNameOrders.getText().toString()) && MainActivity.accountType == 2)  {
                        DrinkUI.uidrinks.remove(i);
                        Log.d(TAG, "Drink " + i.name + " removed");
                    }
                }
                return false;
            }
        });

        mRed = new Button(context);
        mRed.setGravity(Gravity.END);
        mRed.setText("-");
        mRed.setBackgroundResource(R.drawable.round_red);
        mRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (countCurrent > 0) {
                    mDrinkCountOrders.setText(--countCurrent);
                    /*
                    ref.runTransaction(new Transaction.Handler() {
                        @Override
                        public Transaction.Result doTransaction(MutableData mutableData) {
                            Drink drink = mutableData.getValue(Drink.class);
                            if (drink == null) {
                                return Transaction.success(mutableData);
                            }

                            // Set value and report transaction success
                            mutableData.setValue(drink);
                            return Transaction.success(mutableData);
                        }

                        @Override
                        public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                            // Transaction completed
                            Log.d("DrinkUI", "postTransaction:onComplete:" + databaseError);
                        }
                    });
                    */
                }
            }
        });

        mDrinkCountOrders = new TextView(context);
        mDrinkCountOrders.setGravity(Gravity.END);
        mDrinkCountOrders.setText("0");

        mGreen = new Button(context);
        mGreen.setGravity(Gravity.END);
        mGreen.setText("+");
        mGreen.setBackgroundResource(R.drawable.round_green);
        mGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrinkCountOrders.setText(++countCurrent);
                /*
                ref.runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        Drink drink = mutableData.getValue(Drink.class);
                        if (drink == null) {
                            return Transaction.success(mutableData);
                        }

                        // Set value and report transaction success
                        mutableData.setValue(drink);
                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                        // Transaction completed
                        Log.d("Drink", "postTransaction:onComplete:" + databaseError);
                    }
                });
                */
            }
        });

        horizontalLayoutOrders.addView(mNameOrders);
        horizontalLayoutOrders.addView(mRed);
        horizontalLayoutOrders.addView(mDrinkCountOrders);
        horizontalLayoutOrders.addView(mGreen);
        // Orders end

        // Counters
        horizontalLayoutCounters = new LinearLayout(context);
        horizontalLayoutCounters.setOrientation(LinearLayout.HORIZONTAL);
        horizontalLayoutCounters.setLayoutParams(params);
        horizontalLayoutCounters.setGravity(Gravity.END);

        mNameCounters = new TextView(context);
        mNameCounters.setText(name);

        mCountCurrent = new TextView(context);
        mCountCurrent.setText(String.format(Locale.getDefault(), "%1d", countCurrent));

        mCountLast = new TextView(context);
        mCountLast.setText(String.format(Locale.getDefault(), "(%1d)", countLast));

        mPartyCount = new TextView(context);
        mPartyCount.setText(String.format(Locale.getDefault(), "%1d", partyCount));

        mPartyRevenue = new TextView(context);
        mPartyRevenue.setText(String.format(Locale.getDefault(), "€ %.2f", partyRevenue));

        horizontalLayoutCounters.addView(mNameCounters);
        horizontalLayoutCounters.addView(mCountCurrent);
        horizontalLayoutCounters.addView(mCountLast);
        horizontalLayoutCounters.addView(mPartyCount);
        horizontalLayoutCounters.addView(mPartyRevenue);
        // Counters end

        // Prices
        horizontalLayoutPrices = new LinearLayout(context);
        horizontalLayoutPrices.setOrientation(LinearLayout.HORIZONTAL);
        horizontalLayoutPrices.setLayoutParams(params);
        horizontalLayoutPrices.setGravity(Gravity.END);

        mNamePrices = new TextView(context);
        mNamePrices.setText(name);

        mPrice = new TextView(context);
        mPrice.setText(String.format(Locale.getDefault(), "€ %.2f", price));

        mPriceDifference = new TextView(context);
        mPriceDifference.setTextColor(context.getResources().getColor(R.color.grey));
        mPriceDifference.setText(String.format(Locale.getDefault(), "%.2f", priceDifference));

        horizontalLayoutPrices.addView(mNamePrices);
        horizontalLayoutPrices.addView(mPrice);
        horizontalLayoutPrices.addView(mPriceDifference);
        // Prices end

        uidrinks.add(this);
    }

    public static void task() {
        for (DrinkUI i : uidrinks) {
            i.partyCount += i.countCurrent;
            partyCountTotal += i.countCurrent;
            i.partyRevenue += i.countCurrent *i.price;
            partyRevenueTotal += i.countCurrent *i.price;

            i.countSecondLast = i.countLast;
            i.countLast = i.countCurrent;
            i.countCurrent = 0;
            i.priceLast = i.price;
            i.countDifference = i.countLast - i.countSecondLast;
        }
        countTotalSecondLast = countTotalLast;
        countTotalLast = countTotalCurrent;
        countTotalCurrent = 0;
        countTotalDifference = countTotalLast - countTotalSecondLast;

        calcPrises();

        Log.d(TAG, "Task executed");
    }

    protected static void calcPrises() throws IllegalArgumentException {
        for (DrinkUI i : uidrinks) {
            try {
                double rate = i.countLast*1.0/countTotalLast - i.countSecondLast*1.0/countTotalSecondLast;

                if (countTotalDifference >= 0) {
                    if (rate >= 0) {
                        i.testPrice(BI *i.priceLast);
                    } else {
                        i.testPrice(SD *i.priceLast);
                    }
                }
                else {
                    if (rate > 0) {
                        i.testPrice(SI *i.priceLast);
                    } else {
                        i.testPrice(BD *i.priceLast);
                    }
                }
            }
            catch (IllegalArgumentException e) {
                if (i.priceDifference > 0) {
                    i.testPrice(i.priceLast);
                } else {
                    i.testPrice(i.priceLast - 0.1);
                }
            }
        }
    }

    protected void testPrice(double testPrice) {
        if (testPrice > max) {
            price = max;
        }
        else if (testPrice < min){
            price = min;
        }
        else {
            price = MainActivity.round(testPrice);
        }
    }

    public static void crash() {

        Log.d(TAG, "Crash executed");
    }

    public static void setArgument(Context context) {
        DrinkUI.context = context;
    }
}