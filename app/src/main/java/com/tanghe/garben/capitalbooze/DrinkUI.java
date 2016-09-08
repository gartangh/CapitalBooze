package com.tanghe.garben.capitalbooze;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.Locale;

public class DrinkUI extends Drink {

    protected final static DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    private static Context context;
    private static String TAG = "DinkUI";

    protected static double p = 0.00;
    protected static int s = 0;
    protected static int c = 0;

    protected int orderCount = 0;

    protected final static ArrayList<DrinkUI> uidrinks = new ArrayList<>();

    private LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    private LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);

    // Orders
    protected LinearLayout horizontalLayoutOrders;
    private TextView mNameOrders;
    private Button mRed;
    private TextView mDrinkCountOrders;
    private Button mGreen;

    // Counters
    protected LinearLayout horizontalLayoutCounters;
    private TextView mNameCounters;
    private TextView mCountCurrent;
    private TextView mCountLast;
    private TextView mPartyCount;
    private TextView mPartyRevenue;

    // Prices
    protected LinearLayout horizontalLayoutPrices;
    private TextView mNamePrices;
    private TextView mPrice;
    private TextView mPriceDifference;

    public DrinkUI(String name, double price, double min, double max) {
        super(name, price, min, max);
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
        mNameOrders.setTextSize(24);
        mNameOrders.setLayoutParams(params2);
        /*
        TODO: make this code work
        mNameOrders.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                for (DrinkUI i : DrinkUI.uidrinks) {
                    if (i.name.equals(mNameOrders.getText().toString()) && MainActivity.accountType == 2)  {
                        DrinkUI.uidrinks.remove(i);
                        // TODO: remove drink from FireBase
                        Log.d(TAG, "Drink " + i.name + " removed");
                        continue;
                    }
                }
                return false;
            }
        });
        */

        mRed = new Button(context);
        mRed.setText("-");
        mRed.setTextSize(24);
        mRed.setBackgroundResource(R.drawable.round_red);
        mRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (orderCount > 0) {
                    mDrinkCountOrders.setText(String.format(Locale.getDefault(), "%1d", --orderCount));
                    p -= price;
                    s -= (int) (10*price);
                    --c;
                    OrderFragment.setTotals(p, s, c);
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
        mDrinkCountOrders.setText("0");
        mDrinkCountOrders.setTextSize(24);

        mGreen = new Button(context);
        mGreen.setText("+");
        mGreen.setTextSize(24);
        mGreen.setBackgroundResource(R.drawable.round_green);
        mGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrinkCountOrders.setText(String.format(Locale.getDefault(), "%1d", ++orderCount));
                p += price;
                s += (int) (10*price);
                ++c;
                OrderFragment.setTotals(p, s, c);
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
        mNameCounters.setTextSize(24);
        mNameCounters.setLayoutParams(params2);

        mCountCurrent = new TextView(context);
        mCountCurrent.setText(String.format(Locale.getDefault(), "%1d", countCurrent));
        mCountCurrent.setTextSize(24);

        mCountLast = new TextView(context);
        mCountLast.setText(String.format(Locale.getDefault(), "(%1d)", countLast));
        mCountLast.setTextSize(24);

        mPartyCount = new TextView(context);
        mPartyCount.setText(String.format(Locale.getDefault(), "%1d", partyCount));
        mPartyCount.setTextSize(24);

        mPartyRevenue = new TextView(context);
        mPartyRevenue.setText(String.format(Locale.getDefault(), "€ %.2f", partyRevenue));
        mPartyRevenue.setTextSize(24);

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
        mNamePrices.setTextSize(24);
        mNamePrices.setLayoutParams(params2);

        mPrice = new TextView(context);
        mPrice.setText(String.format(Locale.getDefault(), "€ %.2f", price));
        mPrice.setTextSize(24);

        mPriceDifference = new TextView(context);
        mPriceDifference.setTextColor(context.getResources().getColor(R.color.grey));
        if (priceDifference >= 0) {
            mPriceDifference.setText(String.format(Locale.getDefault(), "+%.2f", priceDifference));
        }
        else {
            mPriceDifference.setText(String.format(Locale.getDefault(), "%.2f", priceDifference));
        }

        mPriceDifference.setTextSize(24);

        horizontalLayoutPrices.addView(mNamePrices);
        horizontalLayoutPrices.addView(mPrice);
        horizontalLayoutPrices.addView(mPriceDifference);
        // Prices end

        uidrinks.add(this);
    }

    public static void task() {
        for (DrinkUI i : uidrinks) {
            i.countSecondLast = i.countLast;
            i.countLast = i.countCurrent;
            i.mCountLast.setText(String.format(Locale.getDefault(), "(%1d)", i.countLast));
            i.countCurrent = 0;
            i.mCountCurrent.setText(String.format(Locale.getDefault(), "%1d", 0));
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
                if (i.countDifference < 0) {
                    i.testPrice(i.price - 0.10);
                }
            }
        }
    }

    private void testPrice(double testPrice) {
        if (testPrice > max) {
            price = max;
        }
        else if (testPrice < min) {
            price = min;
        }
        else {
            price = MainActivity.round(testPrice);
        }
        priceDifference = price - priceLast;

        mPrice.setText(String.format(Locale.getDefault(), "€ %.2f", price));
        mPriceDifference.setText(String.format(Locale.getDefault(), "%.2f", priceDifference));
        if (priceDifference < 0) {
            mPriceDifference.setTextColor(context.getResources().getColor(R.color.green));
        }
        else if (priceDifference > 0) {
            mPriceDifference.setTextColor(context.getResources().getColor(R.color.red));
        }
        else {
            mPriceDifference.setTextColor(context.getResources().getColor(R.color.grey));
        }
    }

    public static void crash() {
        // TODO: insert crash here
        Log.d(TAG, "Crash executed");
    }

    public static void orderSend() {
        for (DrinkUI i : DrinkUI.uidrinks) {
            i.countCurrent += i.orderCount;
            i.mCountCurrent.setText(String.format(Locale.getDefault(), "%1d", i.countCurrent));
            i.partyCount += i.orderCount;
            i.mPartyCount.setText(String.format(Locale.getDefault(), "%1d", i.partyCount));
            i.partyRevenue += i.orderCount * i.price;
            i.mPartyRevenue.setText(String.format(Locale.getDefault(), "€ %.2f", i.partyRevenue));
            partyRevenueTotal += i.orderCount * i.price;
            i.orderCount = 0;

            i.mDrinkCountOrders.setText(String.format(Locale.getDefault(), "%1d", i.orderCount));



        }
        countTotalCurrent += c;
        partyCountTotal += c;
        p = 0.00;
        s = 0;
        c = 0;
        OrderFragment.setTotals(p, s, c);
    }

    public static void setArgument(Context context) {
        DrinkUI.context = context;
    }
}