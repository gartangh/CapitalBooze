package com.tanghe.garben.capitalbooze;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

class Drink {

    final static String TAG = "Drink";

    // B = Big, S = Small, I = Increase, D = Decrease
    final static double BI = 1.3;
    final static double BD = 0.8;
    final static double SI = 1.15;
    final static double SD = 0.9;

    // party
    static long countTotalCurrent;
    static long countTotalLast;
    static long countTotalSecondLast;
    static long countTotalDifference;
    static long partyCountTotal;
    static double partyRevenueTotal;

    // drinks
    String name;
    double startPrice;
    ArrayList<Double> prices = new ArrayList<>();
    double price;
    double min;
    double max;
    double priceLast;
    double priceDifference;
    long countCurrent = 0;
    long countLast;
    long countSecondLast;
    long countDifference;
    long partyCount = 0;
    double partyRevenue = 0.00;

    public Drink() {
        // Default constructor required for calls to DataSnapshot.getValue(Drink.class)
    }

    Drink(final String name, double price, double min, double max) {
        this.name = name;
        this.startPrice = price;
        this.price = price;
        this.min = min;
        this.max = max;

        MainActivity.myRef.child("Drinks").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(name).getValue() == null) {
                    MainActivity.myRef.child("Drinks").child(name).setValue(Drink.this);
                    Log.d("Drink", "Wrote " + name + " to database");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public String getName() {
        return name;
    }

    // Not used, but necessary for Firebase
    public double getStartPrice() {
        return startPrice;
    }

    /*
    public ArrayList<Double> getPrices() {
        return prices;
    }
    */

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

    public long getCountCurrent() {
        return countCurrent;
    }

    public long getCountLast() {
        return countLast;
    }

    public long getCountSecondLast() {
        return countSecondLast;
    }

    public long getCountDifference() {
        return countDifference;
    }

    public long getPartyCount() {
        return partyCount;
    }

    public double getPartyRevenue() {
        return partyRevenue;
    }
}