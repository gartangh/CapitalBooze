package com.tanghe.garben.capitalbooze;

import android.os.Bundle;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by Gebruiker on 10/08/2016.
 */
public class Drink {
    protected static ArrayList<Drink> drinks = new ArrayList<>();
    protected final static double GS = 1.3;
    protected final static double GD = 0.7;
    protected final static double KS = 1.15;
    protected final static double KD = 0.85;

    protected static int countTotal = 0;
    protected static int countTotalLast;
    protected static int countTotalSecondLast;
    protected static int countTotalDifference;

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


    public Drink(String name,double price, double min, double max) {
        this.name = name;
        this.price = price;
        this.MIN = min;
        this.MAX = max;
        drinks.add(this);
    }


    public String[] green() {
        return new String[]{Integer.toString(++count),Integer.toString(++countTotal)};
    }

    public String[] red() {
        if (count > 0) {
            countTotal--;
            count--;
        }
        return new String[] {Integer.toString(count),Integer.toString(countTotal)};
    }

    public static int getCountTotal() {
        return countTotal;
    }

    public static int getCountTotalLast() {
        return countTotalLast;
    }

    public static int getCountTotalSecondLast() {
        return countTotalSecondLast;
    }

    public static int getCountTotalDifference() {
        return countTotalDifference;
    }

    public double getPrice() {
        return price;
    }

    public double getPriceLast() {
        return priceLast;
    }

    public double getPriceDifference() {
        return priceDifference;
    }

    public int getCount() {
        return count;
    }

    public int getCountLast() {

        return countLast;
    }

    public int getCountSecondLast() {
        return countSecondLast;
    }

    public static double getGS() {
        return GS;
    }

    public static double getGD() {
        return GD;
    }

    public static double getKS() {
        return KS;
    }

    public static double getKD() {
        return KD;
    }

    public double getMIN() {
        return MIN;
    }

    public String getName() {
        return name;
    }

    public double getMAX() {
        return MAX;
    }


    public static void task() {
        for (Drink i :
                drinks) {
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
    }

    protected static void calcPrises() throws IllegalArgumentException {
        for (Drink i :
                drinks) {
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
}