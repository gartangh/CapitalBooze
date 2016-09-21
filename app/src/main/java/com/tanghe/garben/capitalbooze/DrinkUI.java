package com.tanghe.garben.capitalbooze;

import android.content.Context;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import java.util.ArrayList;
import java.util.Locale;

class DrinkUI extends Drink {

    private static Context context;
    private static String TAG = "DinkUI";

    static long timeCrashLast = 0L;

    int orderCount = 0;

    final static ArrayList<DrinkUI> uidrinks = new ArrayList<>();

    private LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    private LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);

    // Orders
    LinearLayout horizontalLayoutOrders;
    TextView mDrinkCountOrders;

    // Counters
    LinearLayout horizontalLayoutCounters;
    TextView mCountCurrent;
    TextView mCountLast;
    TextView mPartyCount;
    TextView mPartyRevenue;

    // Prices
    LinearLayout horizontalLayoutPrices;
    TextView mPrice;
    TextView mPriceDifference;

    DrinkUI(final String name, double price, double min, double max) {
        super(name, price, min, max);

        MainActivity.ref2.child("Drinks").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                countCurrent = (long) dataSnapshot.child(name).child("countCurrent").getValue();
                partyCount = (long) dataSnapshot.child(name).child("partyCount").getValue();
                countLast = (long) dataSnapshot.child(name).child("countLast").getValue();
                partyRevenue = (double) dataSnapshot.child(name).child("partyRevenue").getValue();

                makeUIElements();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void makeUIElements() {
        // Orders
        horizontalLayoutOrders = new LinearLayout(context);
        horizontalLayoutOrders.setOrientation(LinearLayout.HORIZONTAL);
        horizontalLayoutOrders.setLayoutParams(params);
        horizontalLayoutOrders.setGravity(Gravity.END);

        final TextView mNameOrders = new TextView(context);
        mNameOrders.setText(name);
        mNameOrders.setTextSize(24);
        mNameOrders.setLayoutParams(params2);
        mNameOrders.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                for (DrinkUI i : uidrinks) {
                    if (i.name.equals(mNameOrders.getText().toString()) && MainActivity.accountType == 2) {
                        DrinkUI.uidrinks.remove(i);
                        MainActivity.ref2.child("Drinks").child(i.name).removeValue();
                        Log.d(TAG, "Drink " + i.name + " removed");
                        break;
                    }
                }
                return false;
            }
        });

        Button mRed = new Button(context);
        mRed.setText("-");
        mRed.setTextSize(24);
        mRed.setBackgroundResource(R.drawable.round_red);
        mRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (orderCount > 0) {
                    mDrinkCountOrders.setText(String.format(Locale.getDefault(), "%1d", --orderCount));
                    OrderFragment.totalPrice -= price;
                    OrderFragment.totalSquares -= (int) (10*price);
                    --OrderFragment.totalCount;
                    OrderFragment.setTotals();
                }
            }
        });

        mDrinkCountOrders = new TextView(context);
        mDrinkCountOrders.setText(String.format(Locale.getDefault(), "%1d", 0));
        mDrinkCountOrders.setTextSize(24);

        Button mGreen = new Button(context);
        mGreen.setText("+");
        mGreen.setTextSize(24);
        mGreen.setBackgroundResource(R.drawable.round_green);
        mGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrinkCountOrders.setText(String.format(Locale.getDefault(), "%1d", ++orderCount));
                OrderFragment.totalPrice += price;
                OrderFragment.totalSquares += (int) (10*price);
                ++OrderFragment.totalCount;
                OrderFragment.setTotals();
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

        TextView mNameCounters = new TextView(context);
        mNameCounters.setText(name);
        mNameCounters.setTextSize(24);
        mNameCounters.setLayoutParams(params2);

        mCountCurrent = new TextView(context);
        mCountCurrent.setText(String.format(Locale.getDefault(), "%1d", countCurrent));
        Log.d(TAG, "" + countCurrent);
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

        TextView mNamePrices = new TextView(context);
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

    static void task() {
        for (DrinkUI i : uidrinks) {
            i.countSecondLast = i.countLast;
            MainActivity.ref2.child("Drinks").child(i.name).child("countSecondLast").setValue(i.countSecondLast);

            i.countLast = i.countCurrent;
            MainActivity.ref2.child("Drinks").child(i.name).child("countLast").setValue(i.countLast);

            i.countCurrent = 0;
            MainActivity.ref2.child("Drinks").child(i.name).child("countCurrent").setValue(i.countCurrent);

            i.countDifference = i.countLast - i.countSecondLast;
            MainActivity.ref2.child("Drinks").child(i.name).child("countDifference").setValue(i.countDifference);
        }

        countTotalSecondLast = countTotalLast;
        MainActivity.ref2.child("countTotalSecondLast").setValue(countTotalSecondLast);

        countTotalLast = countTotalCurrent;
        MainActivity.ref2.child("countTotalLast").setValue(countTotalLast);

        countTotalCurrent = 0;
        MainActivity.ref2.child("countTotalCurrent").setValue(countTotalCurrent);

        countTotalDifference = countTotalLast - countTotalSecondLast;
        MainActivity.ref2.child("countTotalDifference").setValue(countTotalDifference);

        calcPrises();

        Log.d(TAG, "Task executed");
    }

    private static void calcPrises() throws IllegalArgumentException {
        for (DrinkUI i : uidrinks) {
            try {
                double rate = i.countLast*1.0/countTotalLast - i.countSecondLast*1.0/countTotalSecondLast;
                if (i.countLast == 0) {
                    i.testPrice(i.price - 0.20);
                }
                else {
                    if (countTotalDifference >= 0) {
                        if (rate >= 0) {
                            i.testPrice(BI *i.price);
                        } else {
                            i.testPrice(SD *i.price);
                        }
                    }
                    else {
                        if (rate > 0) {
                            i.testPrice(SI *i.price);
                        } else {
                            i.testPrice(BD *i.price);
                        }
                    }
                }
            }
            catch (IllegalArgumentException e) {
                if (countTotalLast == 0) {
                    i.testPrice(i.price - 0.10);
                } else {
                    i.testPrice(i.price);
                }
            }
        }
    }

    private void testPrice(double testPrice) {
        // price
        priceLast = price;
        MainActivity.ref2.child("Drinks").child(name).child("priceLast").setValue(priceLast);

        if (testPrice > max) {
            price = max;
        }
        else if (testPrice < min) {
            price = min;
        }
        else {
            price = MainActivity.round(testPrice);
        }
        MainActivity.ref2.child("Drinks").child(name).child("price").setValue(price);

        // priceDifference
        priceDifference = price - priceLast;
        MainActivity.ref2.child("Drinks").child(name).child("priceDifference").setValue(priceDifference);
    }

    static void crash() {
        final Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        if (System.currentTimeMillis() - timeCrashLast >= 60*60*1000L) {
            for (DrinkUI i : uidrinks) {
                i.testPrice(0.75 * i.price);
            }
            MainActivity.ref2.child("timeCrashLast").setValue(System.currentTimeMillis());

            v.vibrate(500L);

            Log.d(TAG, "Crash executed");
        }
        else {
            Log.d(TAG, "Crash NOT executed, wait " + (60*60*1000L - (System.currentTimeMillis() - timeCrashLast))/(60*1000L) + " more minutes");
        }
    }

    static void setArgument(Context context) {
        DrinkUI.context = context;
    }
}