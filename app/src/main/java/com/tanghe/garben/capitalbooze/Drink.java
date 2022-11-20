package com.tanghe.garben.capitalbooze;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


class Drink {

    final static String TAG = "Drink";

    // B = Big, S = Small, I = Increase, D = Decrease
    final static double BI = 1.3;
    final static double BD = 0.8;
    final static double SI = 1.15;
    final static double SD = 0.9;

    // Party
    static long countTotalCurrent;
    static long countTotalLast;
    static long countTotalSecondLast;
    static long countTotalDifference;
    static long partyCountTotal;
    static double partyRevenueTotal;

    // drinks
    String name = "";
    double startPrice = 0.0;
    //ArrayList<Double> prices = new ArrayList<>();
    double price = 0.00;
    double crashPrice = 0.00;
    double min = 0.00;
    double max = 0.00;
    double priceLast = 0.00;
    double priceDifference = 0.00;
    long countCurrent = 0L;
    long countLast = 0L;
    long countSecondLast = 0L;
    long countDifference = 0L;
    long partyCount = 0L;
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
        // TODO: change this property according to input
        this.crashPrice = 2;

        // TODO: make setValue() work
        MainActivity.myRef.child("Drinks").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(name).getValue() == null) {
                    // Not yet in database
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

    // Not used, but necessary for Firebase, only argument with a public getter will be written to firebase
    public double getStartPrice() {
        return startPrice;
    }

    public double getPrice() {
        return price;
    }

    public double getCrashPrice() {
        return crashPrice;
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