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

import java.util.Date;

public class MainActivity
        extends
        AppCompatActivity
        implements
        AboutFragment.OnAboutFragmentInteractionListener,
        CountersFragment.OnCountersFragmentInteractionListener,
        DrinkFragment.OnDrinkFragmentInteractionListener,
        PartyFragment.OnPartyFragmentInteractionListener,
        PricesFragment.OnPricesFragmentInteractionListener,
        OrderFragment.OnOrderFragmentInteractionListener{

    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Drink.setArgument(MainActivity.this);
        CountersFragment.setArgument(MainActivity.this);
        PricesFragment.setArgument(MainActivity.this);
        OrderFragment.setArgument(MainActivity.this);

        // Default fragment
        fragmentManager.beginTransaction().replace(R.id.container, new AboutFragment()).commit();

        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        // Set action bar title
        setTitle(R.string.nav_about);

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Find our drawer view
        nvDrawer = (NavigationView) findViewById(R.id.nvView);

        // Setup drawer view
        setupDrawerContent(nvDrawer);
        drawerToggle = new ActionBarDrawerToggle(this, mDrawer, R.string.drawer_open,  R.string.drawer_close);

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);
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

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.nav_counters_fragment:
                fragmentClass = CountersFragment.class;
                break;
            case R.id.nav_drink_fragment:
                fragmentClass = DrinkFragment.class;
                break;
            case R.id.nav_party_fragment:
                fragmentClass = PartyFragment.class;
                break;
            case R.id.nav_prices_fragment:
                fragmentClass = PricesFragment.class;
                break;
            case R.id.nav_about_fragment:
                fragmentClass = AboutFragment.class;
                break;
            case R.id.nav_exit:
                finish();
            default:
                fragmentClass = CountersFragment.class;
        }

        try {
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
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAboutNextPressed() {
        fragmentManager.beginTransaction().replace(R.id.container, new PartyFragment()).commit();
        setTitle("Party");
    }

    @Override
    public void onCountersBackPressed() {
        fragmentManager.beginTransaction().replace(R.id.container, new DrinkFragment()).commit();
        setTitle("Drinks");
    }

    @Override
    public void onCountersPricesPressed() {
        fragmentManager.beginTransaction().replace(R.id.container, new PricesFragment()).commit();
        setTitle("Prices");
    }

    @Override
    public void onDrinkBackPressed() {
        fragmentManager.beginTransaction().replace(R.id.container, new PartyFragment()).commit();
        setTitle("Party");
    }

    @Override
    public void onDrinkNextPressed() {
        fragmentManager.beginTransaction().replace(R.id.container, new CountersFragment()).commit();
        setTitle("Counters");
    }

    @Override
    public void onPartyBackPressed() {
        fragmentManager.beginTransaction().replace(R.id.container, new AboutFragment()).commit();
        setTitle("About");
    }

    @Override
    public void onPartyNextPressed(Date date, long INTERVAL) {
        Log.d("debug", "Next button pressed");
        CountersFragment.setDate(date);
        CountersFragment.setInterval(INTERVAL);
        fragmentManager.beginTransaction().replace(R.id.container, new DrinkFragment()).commit();
        setTitle("Drinks");
    }

    @Override
    public void onPricesCountersPressed() {
        fragmentManager.beginTransaction().replace(R.id.container, new CountersFragment()).commit();
        setTitle("Counters");
    }

    @Override
    public void onOrder() {

    }
}