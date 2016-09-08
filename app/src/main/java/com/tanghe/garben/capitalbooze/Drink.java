package com.tanghe.garben.capitalbooze;

import android.util.Log;
import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Drink {

    // B = Big, S = Small, I = Increase, D = Decrease
    protected final static double BI = 1.3;
    protected final static double BD = 0.8;
    protected final static double SI = 1.15;
    protected final static double SD = 0.9;

    // party
    protected static long countTotalCurrent = 0;
    protected static long countTotalLast;
    protected static long countTotalSecondLast;
    protected static long countTotalDifference;
    protected static long partyCountTotal = 0;
    protected static double partyRevenueTotal = 0.00;

    // drinks
    protected String name;
    protected double price;
    protected double min;
    protected double max;
    protected double priceLast;
    protected double priceDifference;
    protected int countCurrent = 0;
    protected int countLast;
    protected int countSecondLast;
    protected int countDifference;
    protected int partyCount = 0;
    protected double partyRevenue = 0.0;

    public Drink() {
        // Default constructor required for calls to DataSnapshot.getValue(Drink.class)
    }

    public Drink(String name, double price, double min, double max) {
        this.name = name;
        this.price = price;
        this.min = min;
        this.max = max;

        MainActivity.ref2.child("Drinks").child(name).setValue(this);
        Log.d("Drink", "Wrote " + name + " to database");
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getPriceLast() {
        return priceLast;
    }

    public double getPriceDifference() {
        return priceDifference;
    }

    public int getCountCurrent() {
        return countCurrent;
    }

    public int getCountLast() {
        return countLast;
    }

    public int getCountSecondLast() {
        return countSecondLast;
    }

    public int getCountDifference() {
        return countDifference;
    }

    public int getPartyCount() {
        return partyCount;
    }

    public double getPartyRevenue() {
        return partyRevenue;
    }

}