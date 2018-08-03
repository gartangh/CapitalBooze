package com.tanghe.garben.capitalbooze;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements
        AboutFragment.OnAboutFragmentInteractionListener,
        ExplanationFragment.OnExplanationFragmentInteractionListener,
        LogInFragment.OnLogInFragmentInteractionListener,
        DrinkFragment.OnDrinkFragmentInteractionListener,
        CountersFragment.OnCountersFragmentInteractionListener,
        AdminOnlyFragment.OnAdminOnlyFragmentInteractionListener {

    private final static String TAG = "MainActivity";

    static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private FragmentManager fragmentManager = getFragmentManager();
    private static ProgressDialog mProgressDialog;

    static Long accountType = 0L;

    static FirebaseDatabase database = FirebaseDatabase.getInstance();
    static DatabaseReference myRef = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hide navigation buttons
        // When swiping inwards, show them temporary
        final View decorView = getWindow().getDecorView();
        final int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int i) {
                decorView.setSystemUiVisibility(uiOptions);
            }
        });

        DrinkUI.setArgument(MainActivity.this);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        // if internet, set valueEvenListeners
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;
        if (cm != null)
            activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (user != null) {
                myRef.child("Users").child(user.getUid()).child("accountType").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        accountType = snapshot.getValue(Long.class);
                        Log.d(TAG, "Data changed: accountType = " + accountType);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });
            }

            setValueEventListeners();
        } else {
            Toast.makeText(this, getString(R.string.not_connected_error), Toast.LENGTH_LONG).show();
            Log.d(TAG, getString(R.string.not_connected_error));
        }

        // Set a Toolbar to replace the ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Find drawer view
        mDrawer = findViewById(R.id.drawer_layout);
        // Find our drawer view
        NavigationView nvDrawer = findViewById(R.id.nvView);
        // Setup drawer view
        setupDrawerContent(nvDrawer);
        drawerToggle = new ActionBarDrawerToggle(this, mDrawer, R.string.drawer_open, R.string.drawer_close);
        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);

        // Default fragment
        fragmentManager.beginTransaction().replace(R.id.container, new AboutFragment()).commit();
        setTitle(getString(R.string.nav_about));

        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
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
        Fragment fragment;
        Class fragmentClass;

        if (PricesFragment.verticalLayoutPrices != null) {
            PricesFragment.verticalLayoutPrices.removeAllViews();
        }
        if (OrderFragment.verticalLayoutOrders != null) {
            OrderFragment.verticalLayoutOrders.removeAllViews();
        }
        if (CountersFragment.verticalLayoutCounters != null) {
            CountersFragment.verticalLayoutCounters.removeAllViews();
        }

        switch (menuItem.getItemId()) {
            case R.id.nav_prices_fragment:
                fragment = new PricesFragment();
                fragmentClass = PricesFragment.class;
                break;
            case R.id.nav_explanation_fragment:
                fragment = new ExplanationFragment();
                fragmentClass = ExplanationFragment.class;
                break;
            case R.id.nav_log_in_fragment:
                fragment = new LogInFragment();
                fragmentClass = LogInFragment.class;
                break;
            case R.id.nav_exit:
                finish();
                System.exit(0);
            case R.id.nav_order_fragment:
                fragment = new OrderFragment();
                fragmentClass = OrderFragment.class;
                break;
            case R.id.nav_counters_fragment:
                fragment = new CountersFragment();
                fragmentClass = CountersFragment.class;
                break;
            case R.id.nav_admin_only_fragment:
                fragment = new AdminOnlyFragment();
                fragmentClass = AdminOnlyFragment.class;
                break;
            case R.id.nav_drink_fragment:
                fragment = new DrinkFragment();
                fragmentClass = DrinkFragment.class;
                break;
            default:
                fragment = new AboutFragment();
                fragmentClass = AboutFragment.class;
        }

        try {
            if (accountType == 0 && (fragmentClass == OrderFragment.class || fragmentClass == CountersFragment.class || fragmentClass == AdminOnlyFragment.class || fragmentClass == DrinkFragment.class)) {
                fragment = new AboutFragment();
            }
            else if (accountType == 1 && (fragmentClass == CountersFragment.class || fragmentClass == AdminOnlyFragment.class || fragmentClass == DrinkFragment.class)) {
                fragment = new AboutFragment();
            }

            // Replace the old fragment by the new one.
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();

            // Highlight the selected item has been done by NavigationView
            menuItem.setChecked(true);
            // Set action bar title
            setTitle(menuItem.getTitle());
            // Close the navigation drawer
            mDrawer.closeDrawers();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        return Math.round(d * 10) / 10.0;
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

    void setValueEventListeners() {
        // Drinks
        myRef.child("Drinks").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot dataSnapshotDrink, String s) {
                boolean valid = true;
                for (DrinkUI i : DrinkUI.uidrinks) {
                    if (dataSnapshotDrink.child("name").getValue(String.class).equals(i.name)) {
                        valid = false;
                    }
                }
                if (valid) {
                    new DrinkUI(dataSnapshotDrink.child("name").getValue(String.class), round(dataSnapshotDrink.child("price").getValue(Double.class)), round(dataSnapshotDrink.child("min").getValue(Double.class)), round(dataSnapshotDrink.child("max").getValue(Double.class)));
                    Log.d(TAG, "new Drink: " + dataSnapshotDrink.child("name").getValue(String.class) + " added");
                } else {
                    Log.d(TAG, "new Drink: " + dataSnapshotDrink.child("name").getValue(String.class) + " already in DrinkUI.uidrinks");
                }

                // Public
                myRef.child("Drinks").child(dataSnapshotDrink.child("name").getValue(String.class)).child("priceDifference").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DrinkUI i : DrinkUI.uidrinks) {
                            if (dataSnapshotDrink.child("name").getValue(String.class).equals(i.getName())) {
                                i.priceDifference = round(dataSnapshot.getValue(Double.class));
                                if (i.priceDifference < 0) {
                                    i.mPriceDifference.setText(String.format(Locale.getDefault(), "%.2f", i.priceDifference));
                                    i.mPriceDifference.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
                                } else if (i.priceDifference > 0) {
                                    i.mPriceDifference.setText(String.format(Locale.getDefault(), "+%.2f", i.priceDifference));
                                    i.mPriceDifference.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
                                } else {
                                    i.mPriceDifference.setText(String.format(Locale.getDefault(), "+%.2f", i.priceDifference));
                                    i.mPriceDifference.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));
                                }
                                updatePrices();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });
                myRef.child("Drinks").child(dataSnapshotDrink.child("name").getValue(String.class)).child("price").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DrinkUI i : DrinkUI.uidrinks) {
                            if (dataSnapshotDrink.child("name").getValue(String.class).equals(i.getName())) {
                                i.price = round(dataSnapshot.getValue(Double.class));
                                if (i.price > 0.00) {
                                    i.mPrice.setText(String.format(Locale.getDefault(), "€%.2f", i.price));
                                    //i.prices.add(i.price);
                                    //Log.d(TAG, i.name + ": " + i.prices.toString());
                                    if (i.priceDifference < 0) {
                                        i.mPriceDifference.setText(String.format(Locale.getDefault(), "%.2f", i.priceDifference));
                                        i.mPriceDifference.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.green));
                                    } else if (i.priceDifference > 0) {
                                        i.mPriceDifference.setText(String.format(Locale.getDefault(), "+%.2f", i.priceDifference));
                                        i.mPriceDifference.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.red));
                                    } else {
                                        i.mPriceDifference.setText(String.format(Locale.getDefault(), "+%.2f", i.priceDifference));
                                        i.mPriceDifference.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));
                                    }
                                } else {
                                    i.price = 0.00;
                                    i.mPrice.setText(getString(R.string.sold));
                                    i.priceDifference = 0.00;
                                    i.mPriceDifference.setText(getString(R.string.out));
                                    i.mPriceDifference.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));
                                }
                                updatePrices();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });
                /*
                myRef.child("Drinks").child(dataSnapshotDrink.child("name").getValue(String.class)).child("prices").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DrinkUI i : DrinkUI.uidrinks) {
                            if (i.name.equals(dataSnapshotDrink.child("name").getValue(String.class))) {
                                i.prices = (ArrayList<Double>) dataSnapshot.getValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });
                */

                // TODO: reduce internet use
                // Admin only
                //if (accountType == 2) {
                myRef.child("Drinks").child(dataSnapshotDrink.child("name").getValue(String.class)).child("startPrice").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DrinkUI i : DrinkUI.uidrinks) {
                            if (dataSnapshotDrink.child("name").getValue(String.class).equals(i.getName())) {
                                try {
                                    i.startPrice = dataSnapshot.getValue(Double.class);
                                } catch (NullPointerException e) {
                                    Log.d(TAG, "" + e);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });
                myRef.child("Drinks").child(dataSnapshotDrink.child("name").getValue(String.class)).child("crashPrice").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DrinkUI i : DrinkUI.uidrinks) {
                            try {
                                i.crashPrice = dataSnapshot.getValue(Double.class);
                            } catch (NullPointerException e) {
                                Log.d(TAG, "" + e);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });
                myRef.child("Drinks").child(dataSnapshotDrink.child("name").getValue(String.class)).child("countCurrent").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DrinkUI i : DrinkUI.uidrinks) {
                            if (dataSnapshotDrink.child("name").getValue(String.class).equals(i.getName())) {
                                try {
                                    i.countCurrent = dataSnapshot.getValue(Long.class);
                                    i.mCountCurrent.setText(String.format(Locale.getDefault(), "%1d", i.countCurrent));
                                } catch (NullPointerException e) {
                                    Log.d(TAG, e.toString());
                                }
                                updateCounters();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });
                myRef.child("Drinks").child(dataSnapshotDrink.child("name").getValue(String.class)).child("countDifference").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DrinkUI i : DrinkUI.uidrinks) {
                            if (dataSnapshotDrink.child("name").getValue(String.class).equals(i.getName())) {
                                i.countDifference = dataSnapshot.getValue(Long.class);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });
                myRef.child("Drinks").child(dataSnapshotDrink.child("name").getValue(String.class)).child("countLast").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DrinkUI i : DrinkUI.uidrinks) {
                            if (dataSnapshotDrink.child("name").getValue(String.class).equals(i.getName())) {
                                i.countLast = dataSnapshot.getValue(Long.class);
                                i.mCountLast.setText(String.format(Locale.getDefault(), "(%1d)", i.countLast));
                            }
                            updateCounters();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });
                myRef.child("Drinks").child(dataSnapshotDrink.child("name").getValue(String.class)).child("countSecondLast").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DrinkUI i : DrinkUI.uidrinks) {
                            if (dataSnapshotDrink.child("name").getValue(String.class).equals(i.getName())) {
                                i.countSecondLast = dataSnapshot.getValue(Long.class);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });
                myRef.child("Drinks").child(dataSnapshotDrink.child("name").getValue(String.class)).child("min").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DrinkUI i : DrinkUI.uidrinks) {
                            if (dataSnapshotDrink.child("name").getValue(String.class).equals(i.getName())) {
                                i.min = round(dataSnapshot.getValue(Double.class));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });
                myRef.child("Drinks").child(dataSnapshotDrink.child("name").getValue(String.class)).child("max").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DrinkUI i : DrinkUI.uidrinks) {
                            if (dataSnapshotDrink.child("name").getValue(String.class).equals(i.getName())) {
                                i.max = round(dataSnapshot.getValue(Double.class));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });
                // Can't change the name
                myRef.child("Drinks").child(dataSnapshotDrink.child("name").getValue(String.class)).child("partyCount").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DrinkUI i : DrinkUI.uidrinks) {
                            if (dataSnapshotDrink.child("name").getValue(String.class).equals(i.getName())) {
                                i.partyCount = dataSnapshot.getValue(Long.class);
                                i.mPartyCount.setText(String.format(Locale.getDefault(), "%1d", i.partyCount));
                            }
                            updateCounters();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });
                myRef.child("Drinks").child(dataSnapshotDrink.child("name").getValue(String.class)).child("partyRevenue").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DrinkUI i : DrinkUI.uidrinks) {
                            if (dataSnapshotDrink.child("name").getValue(String.class).equals(i.getName())) {
                                i.partyRevenue = round(dataSnapshot.getValue(Double.class));
                                i.mPartyRevenue.setText(String.format(Locale.getDefault(), "€%.2f", i.partyRevenue));
                            }
                            updateCounters();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });
                myRef.child("Drinks").child(dataSnapshotDrink.child("name").getValue(String.class)).child("priceLast").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DrinkUI i : DrinkUI.uidrinks) {
                            if (dataSnapshotDrink.child("name").getValue(String.class).equals(i.getName())) {
                                i.priceLast = round(dataSnapshot.getValue(Double.class));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });
                //}

                if (fragmentManager.findFragmentByTag("Prices") != null) {
                    fragmentManager.beginTransaction().replace(R.id.container, new PricesFragment(), "Prices").commit();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                updateCounters();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                for (DrinkUI i : DrinkUI.uidrinks) {
                    if (dataSnapshot.child("name").getValue(String.class).equals(i.name)) {
                        if (CountersFragment.verticalLayoutCounters != null) {
                            CountersFragment.verticalLayoutCounters.removeView(i.horizontalLayoutCounters);
                        }
                        if (OrderFragment.verticalLayoutOrders != null) {
                            OrderFragment.verticalLayoutOrders.removeView(i.horizontalLayoutOrders);
                        }
                        if (PricesFragment.verticalLayoutPrices != null) {
                            PricesFragment.verticalLayoutPrices.removeView(i.horizontalLayoutPrices);
                        }
                        DrinkUI.uidrinks.remove(i);

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
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        // TODO: reduce internet use
        // Admin only
        //if (accountType == 2) {
        myRef.child("countTotalCurrent").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Drink.countTotalCurrent = dataSnapshot.getValue(Long.class);
                if (CountersFragment.mCountTotalCurrent != null) {
                    CountersFragment.mCountTotalCurrent.setText(String.format(Locale.getDefault(), "%1d", Drink.countTotalCurrent));
                }
                updateCounters();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        myRef.child("countTotalDifference").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Drink.countTotalDifference = dataSnapshot.getValue(Long.class);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        myRef.child("countTotalLast").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Drink.countTotalLast = dataSnapshot.getValue(Long.class);
                if (CountersFragment.mCountTotalLast != null) {
                    CountersFragment.mCountTotalLast.setText(String.format(Locale.getDefault(), "(%1d)", Drink.countTotalLast));
                }
                updateCounters();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        myRef.child("countTotalSecondLast").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Drink.countTotalSecondLast = dataSnapshot.getValue(Long.class);
            }


            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        myRef.child("partyCountTotal").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Drink.partyCountTotal = dataSnapshot.getValue(Long.class);
                if (CountersFragment.mPartyCountTotal != null) {
                    CountersFragment.mPartyCountTotal.setText(String.format(Locale.getDefault(), "%1d", Drink.partyCountTotal));
                }
                updateCounters();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        myRef.child("partyRevenueTotal").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Drink.partyRevenueTotal = round(dataSnapshot.getValue(Double.class));
                if (CountersFragment.mPartyRevenueTotal != null) {
                    CountersFragment.mPartyRevenueTotal.setText(String.format(Locale.getDefault(), "€%.2f", Drink.partyRevenueTotal));
                }
                updateCounters();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        myRef.child("partyStarted").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                AdminOnlyFragment.partyStarted = dataSnapshot.getValue(Boolean.class);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        myRef.child("timeCrashLast").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DrinkUI.timeCrashLast = dataSnapshot.getValue(Long.class);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        myRef.child("maxOrder").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                OrderFragment.maxOrder = dataSnapshot.getValue(Long.class);
                if (PricesFragment.maxOrderName.equals("")) {
                    if (PricesFragment.mWolf != null) {
                        PricesFragment.mWolf.setText(String.format(Locale.getDefault(), getString(R.string.wolf), "", OrderFragment.maxOrder));
                    }
                } else {
                    if (PricesFragment.mWolf != null) {
                        PricesFragment.mWolf.setText(String.format(Locale.getDefault(), getString(R.string.wolf), PricesFragment.maxOrderName, OrderFragment.maxOrder));
                    }
                }

                updatePrices();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        myRef.child("maxOrderName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(String.class) == null) {
                    PricesFragment.maxOrderName = "";
                } else {
                    PricesFragment.maxOrderName = dataSnapshot.getValue(String.class);
                }
                if (PricesFragment.maxOrderName.equals("")) {
                    if (PricesFragment.mWolf != null) {
                        PricesFragment.mWolf.setText(String.format(Locale.getDefault(), getString(R.string.wolf), "", OrderFragment.maxOrder));
                    }
                } else {
                    if (PricesFragment.mWolf != null) {
                        PricesFragment.mWolf.setText(String.format(Locale.getDefault(), getString(R.string.wolf), PricesFragment.maxOrderName, OrderFragment.maxOrder));
                    }
                }

                updatePrices();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        myRef.child("allTimeWolf").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                OrderFragment.allTimeWolf = dataSnapshot.getValue(Long.class);
                updatePrices();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        myRef.child("allTimeWolfName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(String.class) == null) {
                    PricesFragment.allTimeWolfName = "";
                } else {
                    PricesFragment.allTimeWolfName = dataSnapshot.getValue(String.class);
                }
                updatePrices();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
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
            PricesFragment.mUpdated.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
    }

    void updateCounters() {
        // Adjust time in mCountersUpdated
        CountersFragment.updated = new Date();
        CountersFragment.seen = false;
        if (CountersFragment.mUpdated != null) {
            CountersFragment.mUpdated.setText(getString(R.string.updated, MainActivity.sdf.format(CountersFragment.updated)));
            CountersFragment.mUpdated.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
    }

    // Navigation
    @Override
    public void onAboutNextPressed() {
        fragmentManager.beginTransaction().replace(R.id.container, new ExplanationFragment()).commit();
        setTitle(getString(R.string.nav_explanation));
    }

    @Override
    public void onExplanationNextPressed() {
        fragmentManager.beginTransaction().replace(R.id.container, new PricesFragment(), "Prices").commit();
        setTitle(getString(R.string.nav_prices));
    }

    @Override
    public void onLogInBackPressed() {
        fragmentManager.beginTransaction().replace(R.id.container, new PricesFragment(), "Prices").commit();
        setTitle(getString(R.string.nav_prices));
    }

    @Override
    public void onCountersNextPressed() {
        if (CountersFragment.verticalLayoutCounters != null) {
            CountersFragment.verticalLayoutCounters.removeAllViews();
        }
        fragmentManager.beginTransaction().replace(R.id.container, new AdminOnlyFragment()).commit();
        setTitle(getString(R.string.nav_admin_only));
    }

    @Override
    public void onAdminOnlyBackPressed() {
        fragmentManager.beginTransaction().replace(R.id.container, new CountersFragment()).commit();
        setTitle(getString(R.string.nav_counters));
    }

    @Override
    public void onDrinkBackPressed() {
        fragmentManager.beginTransaction().replace(R.id.container, new CountersFragment()).commit();
        setTitle(getString(R.string.nav_counters));
    }
}