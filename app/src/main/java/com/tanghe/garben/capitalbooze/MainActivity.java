package com.tanghe.garben.capitalbooze;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements
        AboutFragment.OnAboutFragmentInteractionListener,
        LogInFragment.OnLogInFragmentInteractionListener,
        DrinkFragment.OnDrinkFragmentInteractionListener,
        OrderFragment.OnOrderFragmentInteractionListener,
        CountersFragment.OnCountersFragmentInteractionListener,
        AdminOnlyFragment.OnAdminOnlyFragmentInteractionListener,
        PricesFragment.OnPricesFragmentInteractionListener {

    private final static String TAG = "MainActivity";

    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle drawerToggle;
    FragmentManager fragmentManager = getSupportFragmentManager();
    private static ProgressDialog mProgressDialog;

    public static Long accountType = 0L;
    public static Firebase ref2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Firebase.setAndroidContext(this);

        DrinkUI.setArgument(MainActivity.this);
        PricesFragment.setArgument(MainActivity.this);
        AdminOnlyFragment.setArgument(MainActivity.this);

        // Set a Toolbar to replace the ActionBar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        catch (NullPointerException e) {
            Log.d(TAG, "Did NOT set DisplayHomeAsUpEnabled to true");
        }
        getSupportActionBar().setHomeButtonEnabled(true);

        // Find our drawer view
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

        showProgressDialog();

        ref2 = new Firebase(getResources().getString(R.string.url));
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            ref2.child("Users").child(user.getUid()).child("accountType").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    accountType = (Long) snapshot.getValue();
                    Log.d("FireBase", "Data changed: set accountType to " + accountType);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    Log.d("FireBase", "The read failed: " + firebaseError.getMessage());
                }
            });
        }

        ref2.child("Drinks").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                boolean geldig = true;
                for (DrinkUI i :
                        DrinkUI.uidrinks) {
                    if (dataSnapshot.child("name").getValue().equals(i.name)) {
                        geldig = false;
                    }
                }
                if (geldig) {
                    new DrinkUI((String) dataSnapshot.child("name").getValue(), (Double) dataSnapshot.child("price").getValue(), (Double) dataSnapshot.child("min").getValue(), (Double) dataSnapshot.child("max").getValue());
                    Log.d("FireBase", "new Drink: " + dataSnapshot.child("name").getValue() + " added");
                } else {
                    Log.d("FireBase","new Drink: " + dataSnapshot.child("name").getValue() + " already in DrinkUI.uidrinks");
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                for (DrinkUI i :
                        DrinkUI.uidrinks) {
                    if (dataSnapshot.child("Name").getValue().equals(i.name)) {
                        DrinkUI.uidrinks.remove(i);
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

        hideProgressDialog();
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
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
                accountType = 0L;
                finish();
                System.exit(0);
            default:
                fragmentClass = AboutFragment.class;
        }

        try {
            if (accountType == 0 && (fragmentClass == DrinkFragment.class || fragmentClass == OrderFragment.class || fragmentClass == CountersFragment.class || fragmentClass == AdminOnlyFragment.class)) {
                fragmentClass = AboutFragment.class;
            }
            if (accountType == 1 && (fragmentClass == AdminOnlyFragment.class)) {
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
    public void onAboutNextPressed() {
        fragmentManager.beginTransaction().replace(R.id.container, new LogInFragment()).commit();
        setTitle(getString(R.string.nav_log_in));
    }

    @Override
    public void onLogInBackPressed() {
        fragmentManager.beginTransaction().replace(R.id.container, new AboutFragment()).commit();
        setTitle(getString(R.string.nav_about));
    }

    @Override
    public void onLogInNextPressed() {
        if (accountType != 0) {
            fragmentManager.beginTransaction().replace(R.id.container, new DrinkFragment()).commit();
            setTitle(getString(R.string.nav_drink));
        } else {
            fragmentManager.beginTransaction().replace(R.id.container, new PricesFragment()).commit();
            setTitle(getString(R.string.nav_prices));
        }
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

    @Override
    public void onDrinkBackPressed() {
        fragmentManager.beginTransaction().replace(R.id.container, new LogInFragment()).commit();
        setTitle(getString(R.string.nav_log_in));
    }

    @Override
    public void onDrinkNextPressed() {
        fragmentManager.beginTransaction().replace(R.id.container, new OrderFragment()).commit();
        setTitle(getString(R.string.nav_order));
    }

    @Override
    public void onOrderBackPressed() {
        fragmentManager.beginTransaction().replace(R.id.container, new DrinkFragment()).commit();
        setTitle(getString(R.string.nav_drink));
    }

    @Override
    public void onOrderNextPressed() {
        fragmentManager.beginTransaction().replace(R.id.container, new CountersFragment()).commit();
        setTitle(getString(R.string.nav_counters));
    }

    @Override
    public void onCountersBackPressed() {
        fragmentManager.beginTransaction().replace(R.id.container, new OrderFragment()).commit();
        setTitle(getString(R.string.nav_order));
    }

    @Override
    public void onCountersNextPressed() {
        if (accountType != 2) {
            fragmentManager.beginTransaction().replace(R.id.container, new PricesFragment()).commit();
            setTitle(getString(R.string.nav_prices));
        }
        else {
            fragmentManager.beginTransaction().replace(R.id.container, new AdminOnlyFragment()).commit();
            setTitle(getString(R.string.nav_admin_only));
        }
    }

    @Override
    public void onAdminOnlyBackPressed() {
        fragmentManager.beginTransaction().replace(R.id.container, new CountersFragment()).commit();
        setTitle(getString(R.string.nav_counters));
    }

    @Override
    public void onAdminOnlyNextPressed() {
        fragmentManager.beginTransaction().replace(R.id.container, new PricesFragment()).commit();
        setTitle(getString(R.string.nav_prices));
    }

    @Override
    public void onPricesBackPressed() {
        if (accountType == 1) {
            fragmentManager.beginTransaction().replace(R.id.container, new CountersFragment()).commit();
            setTitle(getString(R.string.nav_counters));
        }
        else if (accountType == 2) {
            fragmentManager.beginTransaction().replace(R.id.container, new AdminOnlyFragment()).commit();
            setTitle(getString(R.string.nav_admin_only));
        }
        else {
            fragmentManager.beginTransaction().replace(R.id.container, new LogInFragment()).commit();
            setTitle(getString(R.string.nav_log_in));
        }
    }
}