package com.tanghe.garben.capitalbooze;

import android.content.res.Configuration;
import android.support.design.widget.NavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.os.Vibrator;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;

    //Times 60 for minutes in stead of seconds!
    protected final long INTERVAL = 15*1000L;

    protected final long[] PATTERN = {0,100,100,50};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment fragment = new CountersFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container,fragment).commit();

        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Find our drawer view
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        // Setup drawer view
        setupDrawerContent(nvDrawer);
        drawerToggle = setupDrawerToggle();

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);

        final TextView drink1name = (TextView) findViewById(R.id.drink1);
        final TextView drink2name = (TextView) findViewById(R.id.drink2);
        final TextView drink3name = (TextView) findViewById(R.id.drink3);
        final TextView drink4name = (TextView) findViewById(R.id.drink4);
        final TextView drink5name = (TextView) findViewById(R.id.drink5);
        final TextView drink6name = (TextView) findViewById(R.id.drink6);
        final TextView drink7name = (TextView) findViewById(R.id.drink7);
        final TextView drink8name = (TextView) findViewById(R.id.drink8);
        final TextView drink9name = (TextView) findViewById(R.id.drink9);
        final TextView drink10name = (TextView) findViewById(R.id.drink10);
        final TextView drink11name = (TextView) findViewById(R.id.drink11);
        final TextView drink12name = (TextView) findViewById(R.id.drink12);

        final Button drink1green = (Button) findViewById(R.id.button1green);
        final Button drink2green = (Button) findViewById(R.id.button2green);
        final Button drink3green = (Button) findViewById(R.id.button3green);
        final Button drink4green = (Button) findViewById(R.id.button4green);
        final Button drink5green = (Button) findViewById(R.id.button5green);
        final Button drink6green = (Button) findViewById(R.id.button6green);
        final Button drink7green = (Button) findViewById(R.id.button7green);
        final Button drink8green = (Button) findViewById(R.id.button8green);
        final Button drink9green = (Button) findViewById(R.id.button9green);
        final Button drink10green = (Button) findViewById(R.id.button10green);
        final Button drink11green = (Button) findViewById(R.id.button11green);
        final Button drink12green = (Button) findViewById(R.id.button12green);

        final Button drink1red = (Button) findViewById(R.id.button1red);
        final Button drink2red = (Button) findViewById(R.id.button2red);
        final Button drink3red = (Button) findViewById(R.id.button3red);
        final Button drink4red = (Button) findViewById(R.id.button4red);
        final Button drink5red = (Button) findViewById(R.id.button5red);
        final Button drink6red = (Button) findViewById(R.id.button6red);
        final Button drink7red = (Button) findViewById(R.id.button7red);
        final Button drink8red = (Button) findViewById(R.id.button8red);
        final Button drink9red = (Button) findViewById(R.id.button9red);
        final Button drink10red = (Button) findViewById(R.id.button10red);
        final Button drink11red = (Button) findViewById(R.id.button11red);
        final Button drink12red = (Button) findViewById(R.id.button12red);

        final TextView count1 = (TextView) findViewById(R.id.count1);
        final TextView count2 = (TextView) findViewById(R.id.count2);
        final TextView count3 = (TextView) findViewById(R.id.count3);
        final TextView count4 = (TextView) findViewById(R.id.count4);
        final TextView count5 = (TextView) findViewById(R.id.count5);
        final TextView count6 = (TextView) findViewById(R.id.count6);
        final TextView count7 = (TextView) findViewById(R.id.count7);
        final TextView count8 = (TextView) findViewById(R.id.count8);
        final TextView count9 = (TextView) findViewById(R.id.count9);
        final TextView count10 = (TextView) findViewById(R.id.count10);
        final TextView count11 = (TextView) findViewById(R.id.count11);
        final TextView count12 = (TextView) findViewById(R.id.count12);
        final TextView countTotal = (TextView) findViewById(R.id.countTotal);

        final TextView count1last = (TextView) findViewById(R.id.count1last);
        final TextView count2last = (TextView) findViewById(R.id.count2last);
        final TextView count3last = (TextView) findViewById(R.id.count3last);
        final TextView count4last = (TextView) findViewById(R.id.count4last);
        final TextView count5last = (TextView) findViewById(R.id.count5last);
        final TextView count6last = (TextView) findViewById(R.id.count6last);
        final TextView count7last = (TextView) findViewById(R.id.count7last);
        final TextView count8last = (TextView) findViewById(R.id.count8last);
        final TextView count9last = (TextView) findViewById(R.id.count9last);
        final TextView count10last = (TextView) findViewById(R.id.count10last);
        final TextView count11last = (TextView) findViewById(R.id.count11last);
        final TextView count12last = (TextView) findViewById(R.id.count12last);
        final TextView countTotalLast = (TextView) findViewById(R.id.countTotalLast);

        final Drink drink1 = new Drink("Stella",1.4,1.3,1.7);
        final Drink drink2 = new Drink("Duvel",1.4,1.3,1.7);
        final Drink drink3 = new Drink("Leffe Blond",1.4,1.3,1.7);
        final Drink drink4 = new Drink("Kriek",1.4,1.3,1.7);
        final Drink drink5 = new Drink("Vedett",1.4,1.3,1.7);
        final Drink drink6 = new Drink("Peerdevisscher",1.4,1.3,1.7);
        final Drink drink7 = new Drink("Omer",1.4,1.3,1.7);
        final Drink drink8 = new Drink("Plat",1.4,1.3,1.7);
        final Drink drink9 = new Drink("Spuit",1.4,1.3,1.7);
        final Drink drink10 = new Drink("Cola",1.4,1.3,1.7);
        final Drink drink11 = new Drink("Zero",1.4,1.3,1.7);
        final Drink drink12 = new Drink("Somersby",1.4,1.3,1.7);

        drink1name.setText(drink1.getName());
        drink2name.setText(drink2.getName());
        drink3name.setText(drink3.getName());
        drink4name.setText(drink4.getName());
        drink5name.setText(drink5.getName());
        drink6name.setText(drink6.getName());
        drink7name.setText(drink7.getName());
        drink8name.setText(drink8.getName());
        drink9name.setText(drink9.getName());
        drink10name.setText(drink10.getName());
        drink11name.setText(drink11.getName());
        drink12name.setText(drink12.getName());

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                        v.vibrate(PATTERN,-1);

                        count1last.setText(String.format(Locale.getDefault(),"(%1$d)",drink1.getCount()));
                        count2last.setText(String.format(Locale.getDefault(),"(%1$d)",drink2.getCount()));
                        count3last.setText(String.format(Locale.getDefault(),"(%1$d)",drink3.getCount()));
                        count4last.setText(String.format(Locale.getDefault(),"(%1$d)",drink4.getCount()));
                        count5last.setText(String.format(Locale.getDefault(),"(%1$d)",drink5.getCount()));
                        count6last.setText(String.format(Locale.getDefault(),"(%1$d)",drink6.getCount()));
                        count7last.setText(String.format(Locale.getDefault(),"(%1$d)",drink7.getCount()));
                        count8last.setText(String.format(Locale.getDefault(),"(%1$d)",drink8.getCount()));
                        count9last.setText(String.format(Locale.getDefault(),"(%1$d)",drink9.getCount()));
                        count10last.setText(String.format(Locale.getDefault(),"(%1$d)",drink10.getCount()));
                        count11last.setText(String.format(Locale.getDefault(),"(%1$d)",drink11.getCount()));
                        count12last.setText(String.format(Locale.getDefault(),"(%1$d)",drink12.getCount()));
                        countTotalLast.setText(String.format(Locale.getDefault(),"(%1$d)",Drink.countTotal));

                        count1.setText(String.format(Locale.getDefault(),"%1$d",0));
                        count2.setText(String.format(Locale.getDefault(),"%1$d",0));
                        count3.setText(String.format(Locale.getDefault(),"%1$d",0));
                        count4.setText(String.format(Locale.getDefault(),"%1$d",0));
                        count5.setText(String.format(Locale.getDefault(),"%1$d",0));
                        count6.setText(String.format(Locale.getDefault(),"%1$d",0));
                        count7.setText(String.format(Locale.getDefault(),"%1$d",0));
                        count8.setText(String.format(Locale.getDefault(),"%1$d",0));
                        count9.setText(String.format(Locale.getDefault(),"%1$d",0));
                        count10.setText(String.format(Locale.getDefault(),"%1$d",0));
                        count11.setText(String.format(Locale.getDefault(),"%1$d",0));
                        count12.setText(String.format(Locale.getDefault(),"%1$d",0));
                        countTotal.setText(String.format(Locale.getDefault(),"%1$d",0));

                        Drink.task();

                        Log.d("debug", "Timertask executed");
                    }
                });
            }
        };

        timer.schedule(timerTask,INTERVAL,INTERVAL);


        /*Menu menu = nvDrawer.getMenu();
        MenuItem menuItem = menu.findItem(R.id.nav_switch);
        final View actionView = MenuItemCompat.getActionView(menuItem);
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // what to do when clicked

            }
        });*/


        drink1green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] array = drink1.green();
                count1.setText(array[0]);
                countTotal.setText(array[1]);
            }
        });

        drink1red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] array = drink1.red();
                count1.setText(array[0]);
                countTotal.setText(array[1]);
            }
        });

        drink2green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] array = drink2.green();
                count2.setText(array[0]);
                countTotal.setText(array[1]);
            }
        });

        drink2red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] array = drink2.red();
                count2.setText(array[0]);
                countTotal.setText(array[1]);
            }
        });

        drink3green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] array = drink3.green();
                count3.setText(array[0]);
                countTotal.setText(array[1]);
            }
        });

        drink3red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] array = drink3.red();
                count3.setText(array[0]);
                countTotal.setText(array[1]);
            }
        });

        drink4green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] array = drink4.green();
                count4.setText(array[0]);
                countTotal.setText(array[1]);
            }
        });

        drink4red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] array = drink4.red();
                count4.setText(array[0]);
                countTotal.setText(array[1]);
            }
        });

        drink5green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] array = drink5.green();
                count5.setText(array[0]);
                countTotal.setText(array[1]);
            }
        });

        drink5red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] array = drink5.red();
                count5.setText(array[0]);
                countTotal.setText(array[1]);
            }
        });

        drink6green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] array = drink6.green();
                count6.setText(array[0]);
                countTotal.setText(array[1]);
            }
        });

        drink6red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] array = drink6.red();
                count6.setText(array[0]);
                countTotal.setText(array[1]);
            }
        });

        drink7green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] array = drink7.green();
                count7.setText(array[0]);
                countTotal.setText(array[1]);
            }
        });

        drink7red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] array = drink7.red();
                count7.setText(array[0]);
                countTotal.setText(array[1]);
            }
        });

        drink8green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] array = drink8.green();
                count8.setText(array[0]);
                countTotal.setText(array[1]);
            }
        });

        drink8red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] array = drink8.red();
                count8.setText(array[0]);
                countTotal.setText(array[1]);
            }
        });

        drink9green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] array = drink9.green();
                count9.setText(array[0]);
                countTotal.setText(array[1]);
            }
        });

        drink9red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] array = drink9.red();
                count9.setText(array[0]);
                countTotal.setText(array[1]);
            }
        });

        drink10green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] array = drink10.green();
                count10.setText(array[0]);
                countTotal.setText(array[1]);
            }
        });

        drink10red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] array = drink10.red();
                count10.setText(array[0]);
                countTotal.setText(array[1]);
            }
        });

        drink11green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] array = drink11.green();
                count11.setText(array[0]);
                countTotal.setText(array[1]);
            }
        });

        drink11red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] array = drink11.red();
                count11.setText(array[0]);
                countTotal.setText(array[1]);
            }
        });

        drink12green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] array = drink12.green();
                count12.setText(array[0]);
                countTotal.setText(array[1]);
            }
        });

        drink12red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] array = drink12.red();
                count12.setText(array[0]);
                countTotal.setText(array[1]);
            }
        });
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
            case R.id.nav_switch:
                fragmentClass = CountersFragment.class;
                break;
            case R.id.nav_third_fragment:
                fragmentClass = CountersFragment.class;
                break;
            default:
                fragmentClass = CountersFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, R.string.drawer_open,  R.string.drawer_close);
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
}