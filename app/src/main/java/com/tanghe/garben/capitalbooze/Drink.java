package com.tanghe.garben.capitalbooze;

import android.content.Context;
import android.util.Log;
import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Drink {

    protected final static DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    protected final static Firebase ref2 = new Firebase("https://capital-booze.firebaseio.com/");

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

    protected String name;
    protected double price;
    protected double min;
    protected double max;
    protected double priceLast;
    protected double priceDifference;
    protected int count = 0;
    protected int countLast;
    protected int countSecondLast;
    protected int countDifference;
    protected int partyCount = 0;
    protected double partyRevenue = 0.0;

    public Drink() {
        // Default constructor required for calls to DataSnapshot.getValue(Drink.class)
    }

    public Drink(String name2, double price, double min, double max) {
        this.name = name2;
        this.price = price;
        this.min = min;
        this.max = max;

        ref2.child("Drinks").child(name).setValue(this);
        Log.d("Drink", "Wrote " + this.name + " to database");
    }

    public String getName() {
        return name;
    }

    public static double round1decimal(double d) {
        return Math.round(d*10)/10.00;
    }

    public static void setArgument(Context context) {
        Drink.context = context;
    }
}