package com.tanghe.garben.capitalbooze;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Vibrator;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity  {
    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    //Times 60 for minutes in stead of seconds!
    protected final long INTERVAL = 10*1000L;

    protected final long[] PATTERN = {0,100,100,50};

    protected Drink drink1;
    protected Drink drink2;
    protected Drink drink3;
    protected Drink drink4;
    protected Drink drink5;
    protected Drink drink6;
    protected Drink drink7;
    protected Drink drink8;
    protected Drink drink9;
    protected Drink drink10;
    protected Drink drink11;
    protected Drink drink12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //mPlanetTitles = getResources().getStringArray(R.array.planets_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mPlanetTitles));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mTitle = mDrawerTitle = getTitle();
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                myToolbar, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

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
        final TextView total = (TextView) findViewById(R.id.total);

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

        drink1 = new Drink("Stella",1.4,1.3,1.7);
        drink2 = new Drink("Duvel",1.4,1.3,1.7);
        drink3 = new Drink("Leffe Blond",1.4,1.3,1.7);
        drink4 = new Drink("Kriek",1.4,1.3,1.7);
        drink5 = new Drink("Vedett",1.4,1.3,1.7);
        drink6 = new Drink("Peerdevisscher",1.4,1.3,1.7);
        drink7 = new Drink("Omer",1.4,1.3,1.7);
        drink8 = new Drink("Plat",1.4,1.3,1.7);
        drink9 = new Drink("Spuit",1.4,1.3,1.7);
        drink10 = new Drink("Cola",1.4,1.3,1.7);
        drink11 = new Drink("Zero",1.4,1.3,1.7);
        drink12 = new Drink("Somersby",1.4,1.3,1.7);

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

                        count1last.setText(String.format("(%1$d)",drink1.getCount()));
                        count2last.setText(String.format("(%1$d)",drink2.getCount()));
                        count3last.setText(String.format("(%1$d)",drink3.getCount()));
                        count4last.setText(String.format("(%1$d)",drink4.getCount()));
                        count5last.setText(String.format("(%1$d)",drink5.getCount()));
                        count6last.setText(String.format("(%1$d)",drink6.getCount()));
                        count7last.setText(String.format("(%1$d)",drink7.getCount()));
                        count8last.setText(String.format("(%1$d)",drink8.getCount()));
                        count9last.setText(String.format("(%1$d)",drink9.getCount()));
                        count10last.setText(String.format("(%1$d)",drink10.getCount()));
                        count11last.setText(String.format("(%1$d)",drink11.getCount()));
                        count12last.setText(String.format("(%1$d)",drink12.getCount()));
                        countTotalLast.setText(String.format("(%1$d)",Drink.countTotal));

                        count1.setText(String.format("%1$d",0));
                        count2.setText(String.format("%1$d",0));
                        count3.setText(String.format("%1$d",0));
                        count4.setText(String.format("%1$d",0));
                        count5.setText(String.format("%1$d",0));
                        count6.setText(String.format("%1$d",0));
                        count7.setText(String.format("%1$d",0));
                        count8.setText(String.format("%1$d",0));
                        count9.setText(String.format("%1$d",0));
                        count10.setText(String.format("%1$d",0));
                        count11.setText(String.format("%1$d",0));
                        count12.setText(String.format("%1$d",0));
                        countTotal.setText(String.format("%1$d",0));

                        Drink.task();

                        Log.d("debug", "Timertask executed");
                    }
                });
            }
        };

        timer.schedule(timerTask,INTERVAL,INTERVAL);

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

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }

        /** Swaps fragments in the main content view */
        private void selectItem(int position) {
            // Create a new fragment and specify the planet to show based on position
            Fragment fragment = new PlanetFragment();
            Bundle args = new Bundle();
            args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
            fragment.setArguments(args);

            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();

            // Highlight the selected item, update the title, and close the drawer
            mDrawerList.setItemChecked(position, true);
            setTitle(mPlanetTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        }

        public void setTitle(CharSequence title) {
            mTitle = title;
            getActionBar().setTitle(mTitle);
        }
    }
}