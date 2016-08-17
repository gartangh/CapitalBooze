package com.tanghe.garben.capitalbooze;

import android.content.Context;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Gebruiker on 13/08/2016.
 */
public class CountersFragment extends Fragment {

    protected static Context context;
    protected static long TIME;
    protected static long INTERVAL = 0L;
    protected static final long[] PATTERN = {0L,100L,100L,50L};
    protected static LinearLayout verticalLayout;
    protected static boolean partyStarted = false;

    public CountersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("debug", "OnCreate() called in CountersFragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d("debug","OnCreateView() called in CountersFragment");

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.counters_fragment, container, false);

        verticalLayout = (LinearLayout) view.findViewById(R.id.LinearLayoutDrinks);

        final TextView drink1name = (TextView) view.findViewById(R.id.drink1);
        final TextView drink2name = (TextView) view.findViewById(R.id.drink2);
        final TextView drink3name = (TextView) view.findViewById(R.id.drink3);
        final TextView drink4name = (TextView) view.findViewById(R.id.drink4);
        final TextView drink5name = (TextView) view.findViewById(R.id.drink5);
        final TextView drink6name = (TextView) view.findViewById(R.id.drink6);
        final TextView drink7name = (TextView) view.findViewById(R.id.drink7);
        final TextView drink8name = (TextView) view.findViewById(R.id.drink8);
        final TextView drink9name = (TextView) view.findViewById(R.id.drink9);
        final TextView drink10name = (TextView) view.findViewById(R.id.drink10);
        final TextView drink11name = (TextView) view.findViewById(R.id.drink11);
        final TextView drink12name = (TextView) view.findViewById(R.id.drink12);

        final Button drink1green = (Button) view.findViewById(R.id.button1green);
        final Button drink2green = (Button) view.findViewById(R.id.button2green);
        final Button drink3green = (Button) view.findViewById(R.id.button3green);
        final Button drink4green = (Button) view.findViewById(R.id.button4green);
        final Button drink5green = (Button) view.findViewById(R.id.button5green);
        final Button drink6green = (Button) view.findViewById(R.id.button6green);
        final Button drink7green = (Button) view.findViewById(R.id.button7green);
        final Button drink8green = (Button) view.findViewById(R.id.button8green);
        final Button drink9green = (Button) view.findViewById(R.id.button9green);
        final Button drink10green = (Button) view.findViewById(R.id.button10green);
        final Button drink11green = (Button) view.findViewById(R.id.button11green);
        final Button drink12green = (Button) view.findViewById(R.id.button12green);

        final Button drink1red = (Button) view.findViewById(R.id.button1red);
        final Button drink2red = (Button) view.findViewById(R.id.button2red);
        final Button drink3red = (Button) view.findViewById(R.id.button3red);
        final Button drink4red = (Button) view.findViewById(R.id.button4red);
        final Button drink5red = (Button) view.findViewById(R.id.button5red);
        final Button drink6red = (Button) view.findViewById(R.id.button6red);
        final Button drink7red = (Button) view.findViewById(R.id.button7red);
        final Button drink8red = (Button) view.findViewById(R.id.button8red);
        final Button drink9red = (Button) view.findViewById(R.id.button9red);
        final Button drink10red = (Button) view.findViewById(R.id.button10red);
        final Button drink11red = (Button) view.findViewById(R.id.button11red);
        final Button drink12red = (Button) view.findViewById(R.id.button12red);

        final TextView count1 = (TextView) view.findViewById(R.id.count1);
        final TextView count2 = (TextView) view.findViewById(R.id.count2);
        final TextView count3 = (TextView) view.findViewById(R.id.count3);
        final TextView count4 = (TextView) view.findViewById(R.id.count4);
        final TextView count5 = (TextView) view.findViewById(R.id.count5);
        final TextView count6 = (TextView) view.findViewById(R.id.count6);
        final TextView count7 = (TextView) view.findViewById(R.id.count7);
        final TextView count8 = (TextView) view.findViewById(R.id.count8);
        final TextView count9 = (TextView) view.findViewById(R.id.count9);
        final TextView count10 = (TextView) view.findViewById(R.id.count10);
        final TextView count11 = (TextView) view.findViewById(R.id.count11);
        final TextView count12 = (TextView) view.findViewById(R.id.count12);
        final TextView countTotal = (TextView) view.findViewById(R.id.countTotal);

        final TextView count1last = (TextView) view.findViewById(R.id.count1last);
        final TextView count2last = (TextView) view.findViewById(R.id.count2last);
        final TextView count3last = (TextView) view.findViewById(R.id.count3last);
        final TextView count4last = (TextView) view.findViewById(R.id.count4last);
        final TextView count5last = (TextView) view.findViewById(R.id.count5last);
        final TextView count6last = (TextView) view.findViewById(R.id.count6last);
        final TextView count7last = (TextView) view.findViewById(R.id.count7last);
        final TextView count8last = (TextView) view.findViewById(R.id.count8last);
        final TextView count9last = (TextView) view.findViewById(R.id.count9last);
        final TextView count10last = (TextView) view.findViewById(R.id.count10last);
        final TextView count11last = (TextView) view.findViewById(R.id.count11last);
        final TextView count12last = (TextView) view.findViewById(R.id.count12last);
        final TextView countTotalLast = (TextView) view.findViewById(R.id.countTotalLast);

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

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                        if (!partyStarted) {
                            v.vibrate(PATTERN, -1);

                            count1.setText(String.format(Locale.getDefault(), "%1$d", 0));
                            count2.setText(String.format(Locale.getDefault(), "%1$d", 0));
                            count3.setText(String.format(Locale.getDefault(), "%1$d", 0));
                            count4.setText(String.format(Locale.getDefault(), "%1$d", 0));
                            count5.setText(String.format(Locale.getDefault(), "%1$d", 0));
                            count6.setText(String.format(Locale.getDefault(), "%1$d", 0));
                            count7.setText(String.format(Locale.getDefault(), "%1$d", 0));
                            count8.setText(String.format(Locale.getDefault(), "%1$d", 0));
                            count9.setText(String.format(Locale.getDefault(), "%1$d", 0));
                            count10.setText(String.format(Locale.getDefault(), "%1$d", 0));
                            count11.setText(String.format(Locale.getDefault(), "%1$d", 0));
                            count12.setText(String.format(Locale.getDefault(), "%1$d", 0));
                            countTotal.setText(String.format(Locale.getDefault(), "%1$d", 0));

                            count1last.setText(String.format(Locale.getDefault(), "(%1$d)", drink1.getCount()));
                            count2last.setText(String.format(Locale.getDefault(), "(%1$d)", drink2.getCount()));
                            count3last.setText(String.format(Locale.getDefault(), "(%1$d)", drink3.getCount()));
                            count4last.setText(String.format(Locale.getDefault(), "(%1$d)", drink4.getCount()));
                            count5last.setText(String.format(Locale.getDefault(), "(%1$d)", drink5.getCount()));
                            count6last.setText(String.format(Locale.getDefault(), "(%1$d)", drink6.getCount()));
                            count7last.setText(String.format(Locale.getDefault(), "(%1$d)", drink7.getCount()));
                            count8last.setText(String.format(Locale.getDefault(), "(%1$d)", drink8.getCount()));
                            count9last.setText(String.format(Locale.getDefault(), "(%1$d)", drink9.getCount()));
                            count10last.setText(String.format(Locale.getDefault(), "(%1$d)", drink10.getCount()));
                            count11last.setText(String.format(Locale.getDefault(), "(%1$d)", drink11.getCount()));
                            count12last.setText(String.format(Locale.getDefault(), "(%1$d)", drink12.getCount()));
                            countTotalLast.setText(String.format(Locale.getDefault(), "(%1$d)", Drink.countTotal));

                            partyStarted = true;
                        }
                        else {
                            v.vibrate(PATTERN, -1);

                            Log.d("debug", "Send");
                            send();

                            count1last.setText(String.format(Locale.getDefault(), "(%1$d)", drink1.getCount()));
                            count2last.setText(String.format(Locale.getDefault(), "(%1$d)", drink2.getCount()));
                            count3last.setText(String.format(Locale.getDefault(), "(%1$d)", drink3.getCount()));
                            count4last.setText(String.format(Locale.getDefault(), "(%1$d)", drink4.getCount()));
                            count5last.setText(String.format(Locale.getDefault(), "(%1$d)", drink5.getCount()));
                            count6last.setText(String.format(Locale.getDefault(), "(%1$d)", drink6.getCount()));
                            count7last.setText(String.format(Locale.getDefault(), "(%1$d)", drink7.getCount()));
                            count8last.setText(String.format(Locale.getDefault(), "(%1$d)", drink8.getCount()));
                            count9last.setText(String.format(Locale.getDefault(), "(%1$d)", drink9.getCount()));
                            count10last.setText(String.format(Locale.getDefault(), "(%1$d)", drink10.getCount()));
                            count11last.setText(String.format(Locale.getDefault(), "(%1$d)", drink11.getCount()));
                            count12last.setText(String.format(Locale.getDefault(), "(%1$d)", drink12.getCount()));
                            countTotalLast.setText(String.format(Locale.getDefault(), "(%1$d)", Drink.countTotal));

                            count1.setText(String.format(Locale.getDefault(), "%1$d", 0));
                            count2.setText(String.format(Locale.getDefault(), "%1$d", 0));
                            count3.setText(String.format(Locale.getDefault(), "%1$d", 0));
                            count4.setText(String.format(Locale.getDefault(), "%1$d", 0));
                            count5.setText(String.format(Locale.getDefault(), "%1$d", 0));
                            count6.setText(String.format(Locale.getDefault(), "%1$d", 0));
                            count7.setText(String.format(Locale.getDefault(), "%1$d", 0));
                            count8.setText(String.format(Locale.getDefault(), "%1$d", 0));
                            count9.setText(String.format(Locale.getDefault(), "%1$d", 0));
                            count10.setText(String.format(Locale.getDefault(), "%1$d", 0));
                            count11.setText(String.format(Locale.getDefault(), "%1$d", 0));
                            count12.setText(String.format(Locale.getDefault(), "%1$d", 0));
                            countTotal.setText(String.format(Locale.getDefault(), "%1$d", 0));

                            Drink.task();

                            Log.d("debug", "Thread task executed");
                        }
                    }
                });
            }
        };
        final Timer timer = new Timer();
        final TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                startTaskThread(runnable);
            }
        };
        if (TIME >= System.currentTimeMillis() && INTERVAL > 0L) {
            Log.d("debug","TimerTask set");
            timer.schedule(timerTask, TIME, INTERVAL);
        }

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

        return view;
    }

    private void startTaskThread(Runnable runnable) {
        new Thread(runnable).start();
    }

    public static void setInterval(long INTERVAL) {
        CountersFragment.INTERVAL = INTERVAL;
    }

    public static void setTime(long TIME) {
        CountersFragment.TIME = TIME;
    }

    public void send() {
        //new Create_Part().execute();
    }

    public static void setArgument(Context context) {
        CountersFragment.context = context;
    }
}