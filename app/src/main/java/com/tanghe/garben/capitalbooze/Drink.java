package com.tanghe.garben.capitalbooze;

import android.util.Log;
import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Drink {

    protected final static DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

    // B = Big, S = Small, I = Increase, D = Decrease
    protected final static double BI = 1.3;
    protected final static double BD = 0.7;
    protected final static double SI = 1.15;
    protected final static double SD = 0.85;

    // party
    protected static int countTotalCurrent = 0;
    protected static int countTotalLast;
    protected static int countTotalSecondLast;
    protected static int countTotalDifference;
    protected static int partyCountTotal;
    protected static double partyRevenueTotal;

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

        MainActivity.ref2.child("Drinks").child(this.name).setValue(Drink.this);
        Log.d("Drink", "Wrote " + name + " to database");
    }

    public String getName() {
        return name;
    }
}