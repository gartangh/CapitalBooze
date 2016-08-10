package com.tanghe.garben.capitalbooze;

import android.os.Vibrator;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    protected int drink1count = 0;
    protected int drink2count = 0;
    protected int drink3count = 0;
    protected int drink4count = 0;
    protected int drink5count = 0;
    protected int drink6count = 0;
    protected int drink7count = 0;
    protected int drink8count = 0;
    protected int drink9count = 0;
    protected int drink10count = 0;
    protected int drink11count = 0;
    protected int drink12count = 0;
    protected int drinkTotaalCount = 0;

    protected final int INTERVAL = 10;
    protected final long[] pattern = {0,100,100,50};

    protected final int numberOfDrinks = 12;

    protected ArrayList<Integer> last = new ArrayList<>(numberOfDrinks);
    protected ArrayList<Integer> secondLast;
    protected int secondLastTotaal;
    protected int lastTotaal = 0;
    protected boolean firstTime = true;
    protected int difference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        final TextView drink1 = (TextView) findViewById(R.id.drink1);
        final TextView drink2 = (TextView) findViewById(R.id.drink2);
        final TextView drink3 = (TextView) findViewById(R.id.drink3);
        final TextView drink4 = (TextView) findViewById(R.id.drink4);
        final TextView drink5 = (TextView) findViewById(R.id.drink5);
        final TextView drink6 = (TextView) findViewById(R.id.drink6);
        final TextView drink7 = (TextView) findViewById(R.id.drink7);
        final TextView drink8 = (TextView) findViewById(R.id.drink8);
        final TextView drink9 = (TextView) findViewById(R.id.drink9);
        final TextView drink10 = (TextView) findViewById(R.id.drink10);
        final TextView drink11 = (TextView) findViewById(R.id.drink11);
        final TextView drink12 = (TextView) findViewById(R.id.drink12);
        */

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
        final TextView countTotaal = (TextView) findViewById(R.id.countTotaal);

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                        v.vibrate(pattern,-1);

                        if (firstTime) {
                            last.add(drink1count);
                            last.add(drink2count);
                            last.add(drink3count);
                            last.add(drink4count);
                            last.add(drink5count);
                            last.add(drink6count);
                            last.add(drink7count);
                            last.add(drink8count);
                            last.add(drink9count);
                            last.add(drink10count);
                            last.add(drink11count);
                            last.add(drink12count);

                            firstTime = false;
                        } else {
                            secondLast = new ArrayList<>(numberOfDrinks);
                            secondLastTotaal = lastTotaal;
                            lastTotaal = 0;
                            for (int i:last) {
                                secondLast.add(i);
                            }

                            last = new ArrayList<>(numberOfDrinks);
                            last.add(drink1count);
                            last.add(drink2count);
                            last.add(drink3count);
                            last.add(drink4count);
                            last.add(drink5count);
                            last.add(drink6count);
                            last.add(drink7count);
                            last.add(drink8count);
                            last.add(drink9count);
                            last.add(drink10count);
                            last.add(drink11count);
                            last.add(drink12count);
                        }

                        lastTotaal = drinkTotaalCount;

                        setCountersToZero();

                        count1.setText("0");
                        count2.setText("0");
                        count3.setText("0");
                        count4.setText("0");
                        count5.setText("0");
                        count6.setText("0");
                        count7.setText("0");
                        count8.setText("0");
                        count9.setText("0");
                        count10.setText("0");
                        count11.setText("0");
                        count12.setText("0");
                        countTotaal.setText("0");

                        difference = lastTotaal-secondLastTotaal;

                        Log.d("debug", "Timertask executed");
                    }
                });

            }
        };

        Log.d("debug","TimerTask set!");
        //timer.schedule(timerTask,INTERVAL*60*1000L,INTERVAL*60*1000L);
        timer.schedule(timerTask,5*1000L,5*1000L);

        drink1green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count1.setText(Integer.toString(++drink1count));
                countTotaal.setText(Integer.toString(++drinkTotaalCount));
            }
        });

        drink1red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drink1count > 0) {
                    count1.setText(Integer.toString(--drink1count));
                    countTotaal.setText(Integer.toString(--drinkTotaalCount));
                }
            }
        });

        drink2green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count2.setText(Integer.toString(++drink2count));
                countTotaal.setText(Integer.toString(++drinkTotaalCount));
            }
        });

        drink2red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drink2count > 0) {
                    count2.setText(Integer.toString(--drink2count));
                    countTotaal.setText(Integer.toString(--drinkTotaalCount));
                }
            }
        });

        drink3green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count3.setText(Integer.toString(++drink3count));
                countTotaal.setText(Integer.toString(++drinkTotaalCount));
            }
        });

        drink3red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drink3count > 0) {
                    count3.setText(Integer.toString(--drink3count));
                    countTotaal.setText(Integer.toString(--drinkTotaalCount));
                }
            }
        });

        drink4green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count4.setText(Integer.toString(++drink4count));
                countTotaal.setText(Integer.toString(++drinkTotaalCount));
            }
        });

        drink4red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drink4count > 0) {
                    count4.setText(Integer.toString(--drink4count));
                    countTotaal.setText(Integer.toString(--drinkTotaalCount));
                }
            }
        });

        drink5green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count5.setText(Integer.toString(++drink5count));
                countTotaal.setText(Integer.toString(++drinkTotaalCount));
            }
        });

        drink5red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drink5count > 0) {
                    count5.setText(Integer.toString(--drink5count));
                    countTotaal.setText(Integer.toString(--drinkTotaalCount));
                }
            }
        });

        drink6green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count6.setText(Integer.toString(++drink6count));
                countTotaal.setText(Integer.toString(++drinkTotaalCount));
            }
        });

        drink6red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drink6count > 0) {
                    count6.setText(Integer.toString(--drink6count));
                    countTotaal.setText(Integer.toString(--drinkTotaalCount));
                }
            }
        });

        drink7green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count7.setText(Integer.toString(++drink7count));
                countTotaal.setText(Integer.toString(++drinkTotaalCount));
            }
        });

        drink7red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drink7count > 0) {
                    count7.setText(Integer.toString(--drink7count));
                    countTotaal.setText(Integer.toString(--drinkTotaalCount));
                }
            }
        });

        drink8green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count8.setText(Integer.toString(++drink8count));
                countTotaal.setText(Integer.toString(++drinkTotaalCount));
            }
        });

        drink8red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drink8count > 0) {
                    count8.setText(Integer.toString(--drink8count));
                    countTotaal.setText(Integer.toString(--drinkTotaalCount));
                }
            }
        });

        drink9green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count9.setText(Integer.toString(++drink9count));
                countTotaal.setText(Integer.toString(++drinkTotaalCount));
            }
        });

        drink9red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drink9count > 0) {
                    count9.setText(Integer.toString(--drink9count));
                    countTotaal.setText(Integer.toString(--drinkTotaalCount));
                }
            }
        });

        drink10green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count10.setText(Integer.toString(++drink10count));
                countTotaal.setText(Integer.toString(++drinkTotaalCount));
            }
        });

        drink10red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drink10count > 0) {
                    count10.setText(Integer.toString(--drink10count));
                    countTotaal.setText(Integer.toString(--drinkTotaalCount));
                }
            }
        });

        drink11green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count11.setText(Integer.toString(++drink11count));
                countTotaal.setText(Integer.toString(++drinkTotaalCount));
            }
        });

        drink11red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drink11count > 0) {
                    count11.setText(Integer.toString(--drink11count));
                    countTotaal.setText(Integer.toString(--drinkTotaalCount));
                }
            }
        });

        drink12green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count12.setText(Integer.toString(++drink12count));
                countTotaal.setText(Integer.toString(++drinkTotaalCount));
            }
        });

        drink12red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drink12count > 0) {
                    count12.setText(Integer.toString(--drink12count));
                    countTotaal.setText(Integer.toString(--drinkTotaalCount));
                }
            }
        });
    }

    protected void setCountersToZero() {
        drink1count = 0;
        drink2count = 0;
        drink3count = 0;
        drink4count = 0;
        drink5count = 0;
        drink6count = 0;
        drink7count = 0;
        drink8count = 0;
        drink9count = 0;
        drink10count = 0;
        drink11count = 0;
        drink12count = 0;
        drinkTotaalCount = 0;
    }
}
