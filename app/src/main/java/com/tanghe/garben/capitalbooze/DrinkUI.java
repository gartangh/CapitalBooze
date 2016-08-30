package com.tanghe.garben.capitalbooze;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.ArrayList;

public class DrinkUI extends Drink {

    protected final static ArrayList<DrinkUI> uidrinks = new ArrayList<>();

    protected static TextView drinkCountTotal;
    protected LinearLayout horizontalLayout;
    protected LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    protected TextView drinkName;
    protected TextView drinkCountLast;
    protected Button green;
    protected TextView drinkCount;
    protected Button red;

    public DrinkUI(String name, double price, double min, double max) {
        super(name,price,min,max);
        makeUIElements(name, price, min, max);
    }

    private void makeUIElements(String name,double price, double min, double max) {
        drinkCountTotal = new TextView(context);

        horizontalLayout = new LinearLayout(context);
        horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
        horizontalLayout.setLayoutParams(params);

        drinkName = new TextView(context);
        drinkName.setText(this.name);

        drinkCountLast = new TextView(context);
        drinkCountLast.setGravity(Gravity.END);
        drinkCountLast.setText("(0)");

        drinkCount = new TextView(context);
        drinkCount.setGravity(Gravity.END);
        drinkCount.setText("0");

        red = new Button(context);
        red.setGravity(Gravity.END);
        red.setText("-");
        red.setBackgroundColor(context.getResources().getColor(R.color.red));
        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count > 0) {
                    drinkCount.setText(--count);
                    drinkCountTotal.setText(--countTotal);
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
                }
            }
        });

        green = new Button(context);
        green.setGravity(Gravity.END);
        green.setText("+");
        green.setBackgroundColor(context.getResources().getColor(R.color.green));
        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drinkCount.setText(++count);
                drinkCountTotal.setText(++countTotal);
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

            }
        });

        horizontalLayout.addView(drinkName);
        horizontalLayout.addView(drinkCountLast);
        horizontalLayout.addView(red);
        horizontalLayout.addView(drinkCount);
        horizontalLayout.addView(green);
        uidrinks.add(this);
    }

    public static void task() {
        for (DrinkUI i : uidrinks) {
            i.partyCount += i.count;
            partyCountTotal += i.count;
            i.partyRevenue += i.count*i.price;
            partyRevenueTotal += i.count*i.price;

            i.countSecondLast = i.countLast;
            i.countLast = i.count;
            i.count = 0;
            i.priceLast = i.price;
            i.countDifference = i.countLast - i.countSecondLast;
        }
        countTotalSecondLast = countTotalLast;
        countTotalLast = countTotal;
        countTotal = 0;
        countTotalDifference = countTotalLast - countTotalSecondLast;

        calcPrises();



        Log.d("debug", "Task executed");
    }

    protected static void calcPrises() throws IllegalArgumentException {
        for (DrinkUI i : uidrinks) {
            try {
                double rate = i.countLast*1.0/countTotalLast - i.countSecondLast*1.0/countTotalSecondLast;

                if (countTotalDifference >= 0) {
                    if (rate >= 0) {
                        i.testPrice(GS*i.priceLast);
                    } else {
                        i.testPrice(KD*i.priceLast);
                    }
                }
                else {
                    if (rate > 0) {
                        i.testPrice(KS*i.priceLast);
                    } else {
                        i.testPrice(GD*i.priceLast);
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
            price = Drink.round1decimal(testPrice);
        }
    }
}