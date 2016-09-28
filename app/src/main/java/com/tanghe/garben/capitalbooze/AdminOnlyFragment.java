package com.tanghe.garben.capitalbooze;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class AdminOnlyFragment extends Fragment {

    private OnAdminOnlyFragmentInteractionListener mListener;
    private static Context context;
    private final static String TAG = "AdminOnlyFragment";

    protected static boolean partyStarted = false;
    // TODO: set interval to 15*60*1000L
    private final static long INTERVAL = 60*1000L;
    private final static long[] PATTERN = {0L, 100L, 100L, 50L};

    public AdminOnlyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_only, container, false);

        final Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        Button mStart = (Button) view.findViewById(R.id.mStart);
        mStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!partyStarted) {
                    v.vibrate(PATTERN, -1);

                    Drink.countTotalCurrent = 0;
                    MainActivity.ref2.child("countTotalCurrent").setValue(Drink.countTotalCurrent);
                    Drink.countTotalLast = 0;
                    MainActivity.ref2.child("countTotalLast").setValue(Drink.countTotalLast);
                    Drink.countTotalSecondLast = 0;
                    MainActivity.ref2.child("countTotalSecondLast").setValue(Drink.countTotalSecondLast);
                    Drink.countTotalDifference = 0;
                    MainActivity.ref2.child("countTotalDifference").setValue(Drink.countTotalDifference);
                    Drink.partyCountTotal = 0;
                    MainActivity.ref2.child("partyCountTotal").setValue(Drink.partyCountTotal);
                    Drink.partyRevenueTotal = 0.00;
                    MainActivity.ref2.child("partyRevenueTotal").setValue(Drink.partyRevenueTotal);
                    OrderFragment.maxOrder = 0L;
                    MainActivity.ref2.child("maxOrder").setValue(OrderFragment.maxOrder);
                    PricesFragment.maxOrderName = "";
                    MainActivity.ref2.child("maxOrderName").setValue("");

                    for (DrinkUI i : DrinkUI.uidrinks) {
                        i.countCurrent = 0L;
                        MainActivity.ref2.child("Drinks").child(i.name).child("countCurrent").setValue(i.countCurrent);
                        i.partyRevenue = 0.00;
                        MainActivity.ref2.child("Drinks").child(i.name).child("partyRevenue").setValue(i.partyRevenue);
                        i.countLast = 0L;
                        MainActivity.ref2.child("Drinks").child(i.name).child("countLast").setValue(i.countLast);
                        i.countDifference = 0L;
                        MainActivity.ref2.child("Drinks").child(i.name).child("countDifference").setValue(i.countDifference);
                        i.countSecondLast = 0L;
                        MainActivity.ref2.child("Drinks").child(i.name).child("countSecondLast").setValue(i.countSecondLast);
                        i.partyCount = 0L;
                        MainActivity.ref2.child("Drinks").child(i.name).child("partyCount").setValue(i.partyCount);
                        i.priceDifference = 0.00;
                        MainActivity.ref2.child("Drinks").child(i.name).child("priceDifference").setValue(i.priceDifference);
                        i.priceLast = 0.00;
                        MainActivity.ref2.child("Drinks").child(i.name).child("priceLast").setValue(i.priceLast);
                    }

                    final Handler handler = new Handler();

                    final Runnable runnable = new Runnable() {
                        public void run() {
                            handler.post(new Runnable() {
                                public void run() {
                                    v.vibrate(PATTERN, -1);

                                    DrinkUI.task();

                                    Toast.makeText(context, context.getResources().getString(R.string.task_executed), Toast.LENGTH_LONG).show();
                                    Log.d(TAG, container.getResources().getString(R.string.task_executed));
                                }
                            });
                        }
                    };

                    Timer timer = new Timer();
                    TimerTask timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            startTaskThread(runnable);
                        }
                    };

                    int min = (int) INTERVAL/1000/60;
                    Toast.makeText(context, String.format(Locale.getDefault(), context.getResources().getString(R.string.timertask), min), Toast.LENGTH_LONG).show();
                    Log.d(TAG, String.format(Locale.getDefault(), context.getResources().getString(R.string.timertask), min));
                    timer.schedule(timerTask, INTERVAL, INTERVAL);

                    partyStarted = true;
                    MainActivity.ref2.child("partyStarted").setValue(partyStarted);
                }
                else {
                    Toast.makeText(getContext(), getResources().getString(R.string.party_already_started), Toast.LENGTH_LONG).show();
                }
            }
        });

        Button mCrash = (Button) view.findViewById(R.id.mCrash);
        mCrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (partyStarted) {
                    //TODO: uncomment
                    //DrinkUI.crash();
                }
            }
        });

        final Button back = (Button) view.findViewById(R.id.admin_only_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onAdminOnlyBackPressed();
            }
        });
        final Button next = (Button) view.findViewById(R.id.admin_only_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onAdminOnlyNextPressed();
            }
        });

        return view;
    }

    private void startTaskThread(Runnable runnable) {
        new Thread(runnable).start();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAdminOnlyFragmentInteractionListener) {
            mListener = (OnAdminOnlyFragmentInteractionListener) context;
        }
        else {
            throw new RuntimeException(context.toString() + " must implement OnCountersFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnAdminOnlyFragmentInteractionListener {
        void onAdminOnlyBackPressed();
        void onAdminOnlyNextPressed();
    }

    public static void setArgument(Context context) {
        AdminOnlyFragment.context = context;
    }
}
