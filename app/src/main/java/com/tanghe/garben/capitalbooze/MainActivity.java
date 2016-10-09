package com.tanghe.garben.capitalbooze;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements
        AboutFragment.OnAboutFragmentInteractionListener,
        LogInFragment.OnLogInFragmentInteractionListener,
        DrinkFragment.OnDrinkFragmentInteractionListener,
        OrderFragment.OnOrderFragmentInteractionListener,
        CountersFragment.OnCountersFragmentInteractionListener,
        AdminOnlyFragment.OnAdminOnlyFragmentInteractionListener,
        PricesFragment.OnPricesFragmentInteractionListener {

    private final static String TAG = "MainActivity";
    private final static String TAG2 = "Firebase";

    static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle drawerToggle;
    FragmentManager fragmentManager = getSupportFragmentManager();
    private static ProgressDialog mProgressDialog;

    static Long accountType = 0L;
    static Firebase ref2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DrinkUI.setArgument(MainActivity.this);
        AdminOnlyFragment.setArgument(MainActivity.this);

        // Set a Toolbar to replace the ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Find drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        // Find our drawer view
        NavigationView nvDrawer = (NavigationView) findViewById(R.id.nvView);
        // Setup drawer view
        setupDrawerContent(nvDrawer);
        drawerToggle = new ActionBarDrawerToggle(this, mDrawer, R.string.drawer_open,  R.string.drawer_close);
        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);

        // Default fragment
        fragmentManager.beginTransaction().replace(R.id.container, new AboutFragment()).commit();
        setTitle(getString(R.string.nav_about));

        Firebase.setAndroidContext(this);
        ref2 = new Firebase(getString(R.string.url));
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        // if internet, set valueEvenListeners
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (user != null) {
                ref2.child("Users").child(user.getUid()).child("accountType").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        accountType = (Long) snapshot.getValue();
                        Log.d("FireBase", "Data changed: accountType = " + accountType);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        Log.d("FireBase", "The read failed: " + firebaseError.getMessage());
                    }
                });
            }

            setValueEventListeners();
        }
        else {
            notConnectedError();
        }
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                selectDrawerItem(menuItem);
                return true;
            }
        });
    }

    private void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.nav_drink_fragment:
                fragmentClass = DrinkFragment.class;
                break;
            case R.id.nav_order_fragment:
                fragmentClass = OrderFragment.class;
                break;
            case R.id.nav_counters_fragment:
                fragmentClass = CountersFragment.class;
                break;
            case R.id.nav_admin_only_fragment:
                fragmentClass  = AdminOnlyFragment.class;
                break;
            case R.id.nav_prices_fragment:
                fragmentClass = PricesFragment.class;
                break;
            case R.id.nav_about_fragment:
                fragmentClass = AboutFragment.class;
                break;
            case R.id.nav_log_in_fragment:
                fragmentClass = LogInFragment.class;
                break;
            case R.id.nav_exit:
                finish();
                System.exit(0);
            default:
                fragmentClass = AboutFragment.class;
        }

        try {
            if (accountType == 0 && (fragmentClass == DrinkFragment.class || fragmentClass == OrderFragment.class || fragmentClass == CountersFragment.class || fragmentClass == AdminOnlyFragment.class)) {
                fragmentClass = AboutFragment.class;
            }
            if (accountType == 1 && (fragmentClass == AdminOnlyFragment.class || fragmentClass == DrinkFragment.class || fragmentClass == CountersFragment.class)) {
                fragmentClass = AboutFragment.class;
            }
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }

    // `onPostCreate` called when activity start-up is complete after `onStart()`
    // NOTE! Make sure to override the method with only a single `Bundle` argument
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    public static double round(double d) {
        return Math.round(d*10)/10.0;
    }

    @Override
    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    void notConnectedError() {
        Toast.makeText(getApplicationContext(), getString(R.string.not_connected_error), Toast.LENGTH_LONG).show();
    }

    void setValueEventListeners() {

        // Drinks
        ref2.child("Drinks").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot dataSnapshotDrink, String s) {
                boolean valid = true;
                for (DrinkUI i : DrinkUI.uidrinks) {
                    if (dataSnapshotDrink.child("name").getValue().equals(i.name)) {
                        valid = false;
                    }
                }
                if (valid) {
                    new DrinkUI((String) dataSnapshotDrink.child("name").getValue(), (double) dataSnapshotDrink.child("price").getValue(), (double) dataSnapshotDrink.child("min").getValue(), (double) dataSnapshotDrink.child("max").getValue());
                    Log.d("FireBase", "new Drink: " + dataSnapshotDrink.child("name").getValue() + " added");
                } else {
                    Log.d("FireBase","new Drink: " + dataSnapshotDrink.child("name").getValue() + " already in DrinkUI.uidrinks");
                }

                // Public
                ref2.child("Drinks").child(dataSnapshotDrink.child("name").getValue().toString()).child("price").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DrinkUI i : DrinkUI.uidrinks) {
                            if (dataSnapshotDrink.child("name").getValue().toString().equals(i.getName())) {
                                i.price = round((double) dataSnapshot.getValue());
                                i.mPrice.setText(String.format(Locale.getDefault(), "€%.2f", i.price));
                                i.prices.add(i.price);
                                Log.d(TAG, i.name + ": " + i.prices.toString());
                                updatePrices();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
                ref2.child("Drinks").child(dataSnapshotDrink.child("name").getValue().toString()).child("priceDifference").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DrinkUI i : DrinkUI.uidrinks) {
                            if (dataSnapshotDrink.child("name").getValue().toString().equals(i.getName())) {
                                i.priceDifference = round((double) dataSnapshot.getValue());
                                if (i.priceDifference >= 0) {
                                    i.mPriceDifference.setText(String.format(Locale.getDefault(), "+%.2f", i.priceDifference));
                                }
                                else {
                                    i.mPriceDifference.setText(String.format(Locale.getDefault(), "%.2f", i.priceDifference));
                                }

                                if (i.priceDifference < 0) {
                                    i.mPriceDifference.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.green));
                                }
                                else if (i.priceDifference > 0) {
                                    i.mPriceDifference.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.red));
                                }
                                else {
                                    i.mPriceDifference.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.grey));
                                }
                                updatePrices();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

                // TODO: reduce internet use
                // Admin only
                //if (accountType == 2) {
                    ref2.child("Drinks").child(dataSnapshotDrink.child("name").getValue().toString()).child("startPrice").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DrinkUI i : DrinkUI.uidrinks) {
                                if (dataSnapshotDrink.child("name").getValue().toString().equals(i.getName())) {
                                    try {
                                        i.startPrice = (double) dataSnapshot.getValue();
                                    }
                                    catch (NullPointerException e) {
                                        Log.d(TAG, "" + e);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                    ref2.child("Drinks").child(dataSnapshotDrink.child("name").getValue().toString()).child("countCurrent").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DrinkUI i : DrinkUI.uidrinks) {
                                if (dataSnapshotDrink.child("name").getValue().toString().equals(i.getName())) {
                                    try {
                                        i.countCurrent = (long) dataSnapshot.getValue();
                                        i.mCountCurrent.setText(String.format(Locale.getDefault(), "%1d", i.countCurrent));
                                    }
                                    catch (NullPointerException e) {
                                        Log.d(TAG, e.toString());
                                    }
                                    updateCounters();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                    ref2.child("Drinks").child(dataSnapshotDrink.child("name").getValue().toString()).child("countDifference").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DrinkUI i : DrinkUI.uidrinks) {
                                if (dataSnapshotDrink.child("name").getValue().toString().equals(i.getName())) {
                                    i.countDifference = (long) dataSnapshot.getValue();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                    ref2.child("Drinks").child(dataSnapshotDrink.child("name").getValue().toString()).child("countLast").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DrinkUI i : DrinkUI.uidrinks) {
                                if (dataSnapshotDrink.child("name").getValue().toString().equals(i.getName())) {
                                    i.countLast = (long) dataSnapshot.getValue();
                                    i.mCountLast.setText(String.format(Locale.getDefault(), "(%1d)", i.countLast));
                                }
                                updateCounters();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                    ref2.child("Drinks").child(dataSnapshotDrink.child("name").getValue().toString()).child("countSecondLast").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DrinkUI i : DrinkUI.uidrinks) {
                                if (dataSnapshotDrink.child("name").getValue().toString().equals(i.getName())) {
                                    i.countSecondLast = (long) dataSnapshot.getValue();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                    ref2.child("Drinks").child(dataSnapshotDrink.child("name").getValue().toString()).child("min").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DrinkUI i : DrinkUI.uidrinks) {
                                if (dataSnapshotDrink.child("name").getValue().toString().equals(i.getName())) {
                                    i.min = round((double) dataSnapshot.getValue());
                                }
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                    ref2.child("Drinks").child(dataSnapshotDrink.child("name").getValue().toString()).child("max").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DrinkUI i : DrinkUI.uidrinks) {
                                if (dataSnapshotDrink.child("name").getValue().toString().equals(i.getName())) {
                                    i.max = round((double) dataSnapshot.getValue());
                                }
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                    // Can't change the name
                    ref2.child("Drinks").child(dataSnapshotDrink.child("name").getValue().toString()).child("partyCount").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DrinkUI i : DrinkUI.uidrinks) {
                                if (dataSnapshotDrink.child("name").getValue().toString().equals(i.getName())) {
                                    i.partyCount = (long) dataSnapshot.getValue();
                                    i.mPartyCount.setText(String.format(Locale.getDefault(), "%1d", i.partyCount));
                                }
                                updateCounters();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                    ref2.child("Drinks").child(dataSnapshotDrink.child("name").getValue().toString()).child("partyRevenue").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DrinkUI i : DrinkUI.uidrinks) {
                                if (dataSnapshotDrink.child("name").getValue().toString().equals(i.getName())) {
                                    i.partyRevenue = round((double) dataSnapshot.getValue());
                                    i.mPartyRevenue.setText(String.format(Locale.getDefault(), "€%.2f", i.partyRevenue));
                                }
                                updateCounters();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                    ref2.child("Drinks").child(dataSnapshotDrink.child("name").getValue().toString()).child("priceLast").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DrinkUI i : DrinkUI.uidrinks) {
                                if (dataSnapshotDrink.child("name").getValue().toString().equals(i.getName())) {
                                    i.priceLast = round((double) dataSnapshot.getValue());
                                }
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                //}
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                // Adjust time in mCountersUpdated
                CountersFragment.updated = new Date();
                CountersFragment.seen = false;
                if (CountersFragment.mUpdated != null) {
                    CountersFragment.mUpdated.setText(getString(R.string.updated, MainActivity.sdf.format(CountersFragment.updated)));
                    CountersFragment.mUpdated.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                for (DrinkUI i : DrinkUI.uidrinks) {
                    if (dataSnapshot.child("name").getValue().equals(i.name)) {
                        // TODO: make this work
                        //DrinkUI.uidrinks.remove(i);

                        // Adjust time in mPricesUpdated
                        PricesFragment.updated = new Date();
                        PricesFragment.seen = false;
                        if (PricesFragment.mUpdated != null) {
                            PricesFragment.mUpdated.setText(getString(R.string.updated, MainActivity.sdf.format(PricesFragment.updated)));
                            PricesFragment.mUpdated.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
                        }

                        // Adjust time in mCountersUpdated
                        CountersFragment.updated = new Date();
                        CountersFragment.seen = false;
                        if (CountersFragment.mUpdated != null) {
                            CountersFragment.mUpdated.setText(getString(R.string.updated, MainActivity.sdf.format(CountersFragment.updated)));
                            CountersFragment.mUpdated.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
                        }
                    }
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d("FireBase", "The read failed: " + firebaseError.getMessage());
            }
        });

        // TODO: reduce internet use
        // Admin only
        //if (accountType == 2) {
            ref2.child("countTotalCurrent").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Drink.countTotalCurrent = (long) dataSnapshot.getValue();
                    if (CountersFragment.mCountTotalCurrent != null) {
                        CountersFragment.mCountTotalCurrent.setText(String.format(Locale.getDefault(), "%1d", Drink.countTotalCurrent));
                    }
                    updateCounters();
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
            ref2.child("countTotalDifference").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Drink.countTotalDifference = (long) dataSnapshot.getValue();
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
            ref2.child("countTotalLast").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Drink.countTotalLast = (long) dataSnapshot.getValue();
                    if (CountersFragment.mCountTotalLast != null) {
                        CountersFragment.mCountTotalLast.setText(String.format(Locale.getDefault(), "(%1d)", Drink.countTotalLast));
                    }
                    updateCounters();
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
            ref2.child("countTotalSecondLast").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Drink.countTotalSecondLast = (long) dataSnapshot.getValue();
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
            ref2.child("partyCountTotal").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Drink.partyCountTotal = (long) dataSnapshot.getValue();
                    if (CountersFragment.mPartyCountTotal != null) {
                        CountersFragment.mPartyCountTotal.setText(String.format(Locale.getDefault(), "%1d", Drink.partyCountTotal));
                    }
                    updateCounters();
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
            ref2.child("partyRevenueTotal").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Drink.partyRevenueTotal = round((double) dataSnapshot.getValue());
                    if (CountersFragment.mPartyRevenueTotal != null) {
                        CountersFragment.mPartyRevenueTotal.setText(String.format(Locale.getDefault(), "€%.2f", Drink.partyRevenueTotal));
                    }
                    updateCounters();
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
            ref2.child("partyStarted").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    AdminOnlyFragment.partyStarted = (boolean) dataSnapshot.getValue();
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
            ref2.child("timeCrashLast").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    DrinkUI.timeCrashLast = (long) dataSnapshot.getValue();
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
            ref2.child("maxOrder").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    OrderFragment.maxOrder = (long) dataSnapshot.getValue();
                    if (PricesFragment.maxOrderName.equals("")) {
                        if (PricesFragment.mWolf != null) {
                            PricesFragment.mWolf.setText(String.format(Locale.getDefault(), getString(R.string.wolf), getString(R.string.no_wolf), OrderFragment.maxOrder));
                        }
                    }
                    else {
                        if (PricesFragment.mWolf != null) {
                            PricesFragment.mWolf.setText(String.format(Locale.getDefault(), getString(R.string.wolf), PricesFragment.maxOrderName, OrderFragment.maxOrder));
                        }
                    }

                    updatePrices();
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
            ref2.child("maxOrderName").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() == null) {
                        PricesFragment.maxOrderName = "";
                    }
                    else {
                        PricesFragment.maxOrderName = (String) dataSnapshot.getValue();
                    }
                    if (PricesFragment.maxOrderName.equals("")) {
                        if (PricesFragment.mWolf != null) {
                            PricesFragment.mWolf.setText(String.format(Locale.getDefault(), getString(R.string.wolf), getString(R.string.no_wolf), OrderFragment.maxOrder));
                        }
                    }
                    else {
                        if (PricesFragment.mWolf != null) {
                            PricesFragment.mWolf.setText(String.format(Locale.getDefault(), getString(R.string.wolf), PricesFragment.maxOrderName, OrderFragment.maxOrder));
                        }
                    }

                    updatePrices();
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
            ref2.child("allTimeWolf").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    OrderFragment.allTimeWolf = (long) dataSnapshot.getValue();
                    updatePrices();
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
            ref2.child("allTimeWolfName").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() == null) {
                        PricesFragment.allTimeWolfName = "";
                    }
                    else {
                        PricesFragment.allTimeWolfName = (String) dataSnapshot.getValue();
                    }
                    updatePrices();
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        //}
    }

    void updatePrices() {
        // Adjust time in mPricesUpdated
        PricesFragment.updated = new Date();
        PricesFragment.seen = false;
        if (PricesFragment.mUpdated != null) {
            PricesFragment.mUpdated.setText(getString(R.string.updated, MainActivity.sdf.format(PricesFragment.updated)));
            PricesFragment.mUpdated.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
        }
    }

    void updateCounters() {
        // Adjust time in mCountersUpdated
        CountersFragment.updated = new Date();
        CountersFragment.seen = false;
        if (CountersFragment.mUpdated != null) {
            CountersFragment.mUpdated.setText(getString(R.string.updated, MainActivity.sdf.format(CountersFragment.updated)));
            CountersFragment.mUpdated.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
        }
    }

    // Navigation
    @Override
    public void onAboutNextPressed() {
        fragmentManager.beginTransaction().replace(R.id.container, new PricesFragment()).commit();
        setTitle(getString(R.string.nav_prices));
    }

    @Override
    public void onPricesBackPressed() {
        fragmentManager.beginTransaction().replace(R.id.container, new AboutFragment()).commit();
        setTitle(getString(R.string.nav_about));
    }

    @Override
    public void onPricesNextPressed() {
        fragmentManager.beginTransaction().replace(R.id.container, new LogInFragment()).commit();
        setTitle(getString(R.string.nav_log_in));
    }

    @Override
    public void onLogInBackPressed() {
        fragmentManager.beginTransaction().replace(R.id.container, new PricesFragment()).commit();
        setTitle(getString(R.string.nav_prices));
    }

    @Override
    public void onLogInNextPressed() {
        if (accountType != 0) {
            fragmentManager.beginTransaction().replace(R.id.container, new OrderFragment()).commit();
            setTitle(getString(R.string.nav_order));
        } else {
            fragmentManager.beginTransaction().replace(R.id.container, new AboutFragment()).commit();
            setTitle(getString(R.string.nav_about));
        }
    }

    @Override
    public void onOrderBackPressed() {
        fragmentManager.beginTransaction().replace(R.id.container, new LogInFragment()).commit();
        setTitle(getString(R.string.nav_log_in));
    }

    @Override
    public void onOrderNextPressed() {
        if (accountType == 2) {
            fragmentManager.beginTransaction().replace(R.id.container, new DrinkFragment()).commit();
            setTitle(getString(R.string.nav_drink));
        }
        else {
            fragmentManager.beginTransaction().replace(R.id.container, new AboutFragment()).commit();
            setTitle(getString(R.string.nav_about));
        }
    }

    @Override
    public void onDrinkBackPressed() {
        fragmentManager.beginTransaction().replace(R.id.container, new OrderFragment()).commit();
        setTitle(getString(R.string.nav_order));
    }

    @Override
    public void onDrinkNextPressed() {
        fragmentManager.beginTransaction().replace(R.id.container, new CountersFragment()).commit();
        setTitle(getString(R.string.nav_counters));
    }

    @Override
    public void onCountersBackPressed() {
        fragmentManager.beginTransaction().replace(R.id.container, new DrinkFragment()).commit();
        setTitle(getString(R.string.nav_drink));
    }

    @Override
    public void onCountersNextPressed() {
        fragmentManager.beginTransaction().replace(R.id.container, new AdminOnlyFragment()).commit();
        setTitle(getString(R.string.nav_admin_only));
    }

    @Override
    public void onAdminOnlyBackPressed() {
        fragmentManager.beginTransaction().replace(R.id.container, new CountersFragment()).commit();
        setTitle(getString(R.string.nav_counters));
    }

    @Override
    public void onAdminOnlyNextPressed() {
        fragmentManager.beginTransaction().replace(R.id.container, new AboutFragment()).commit();
        setTitle(getString(R.string.nav_about));
    }
}