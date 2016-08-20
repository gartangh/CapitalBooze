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

/**
 * Created by Gebruiker on 10/08/2016.
 */
public class Drink {
    protected final static ArrayList<Drink> drinks = new ArrayList<>();
    protected final static double GS = 1.3;
    protected final static double GD = 0.7;
    protected final static double KS = 1.15;
    protected final static double KD = 0.85;

    protected static Context context;
    protected static int countTotal = 0;
    protected static int countTotalLast;
    protected static int countTotalSecondLast;
    protected static int countTotalDifference;
    protected static int partyCountTotal;
    protected static double partyRevenueTotal;

    protected final double MIN;
    protected final double MAX;

    protected String name;
    protected double price;
    protected double priceLast;
    protected double priceDifference;
    protected int count = 0;
    protected int countLast;
    protected int countSecondLast;
    protected int countDifference;
    protected int partyCount = 0;
    protected double partyRevenue = 0.0;

    protected LinearLayout horizontalLayout;
    protected LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    protected TextView drinkName;
    protected TextView drinkCountLast;
    protected Button green;
    protected TextView drinkCount;
    protected Button red;

    public Drink(String name,double price, double min, double max) {
        this.name = name;
        this.price = price;
        this.MIN = min;
        this.MAX = max;

        horizontalLayout = new LinearLayout(context);
        horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
        horizontalLayout.setLayoutParams(params);

        drinkName = new TextView(context);
        drinkName.setText(this.name);

        drinkCountLast = new TextView(context);
        drinkCountLast.setGravity(Gravity.RIGHT);
        drinkCountLast.setText("(0)");

        red = new Button(context);
        red.setGravity(Gravity.RIGHT);
        red.setText("-");

        drinkCount = new TextView(context);
        drinkCount.setGravity(Gravity.RIGHT);
        drinkCount.setText("0");

        green = new Button(context);
        green.setGravity(Gravity.RIGHT);
        green.setText("+");

        horizontalLayout.addView(drinkName);
        horizontalLayout.addView(drinkCountLast);
        horizontalLayout.addView(red);
        horizontalLayout.addView(drinkCount);
        horizontalLayout.addView(green);
        drinks.add(this);
    }

    public String[] green() {
        return new String[]{Integer.toString(++this.count),Integer.toString(++this.countTotal)};
    }

    public String[] red() {
        if (count > 0) {
            countTotal--;
            count--;
        }
        return new String[] {Integer.toString(count),Integer.toString(countTotal)};
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public static void setCountTotal(int countTotal) {
        Drink.countTotal = countTotal;
    }

    public static void task() {
        for (Drink i : drinks) {
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
        for (Drink i : drinks) {
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
        if (testPrice > MAX) {
            price = MAX;
        }
        else if (testPrice < MIN){
            price = MIN;
        }
        else {
            price = testPrice;
        }
    }

    public static double round2decimals(double d) {
        return Math.round(d*100)/100.0;
    }

    public static void setArgument(Context context) {
        Drink.context = context;
    }
}