package com.tanghe.garben.capitalbooze;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class AdminOnlyFragment extends Fragment {

    private OnAdminOnlyFragmentInteractionListener mListener;
    private final static String TAG = "AdminOnlyFragment";

    protected static boolean partyStarted = false;
    // TODO: set to 12 minutes
    private final static long INTERVAL = 1 * 60 * 1000L;
    private final static long[] PATTERN = {0L, 100L, 100L, 50L};

    public AdminOnlyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_only, container, false);

        final Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        final EditText mTypeWolfHere = view.findViewById(R.id.mTypeWolfHere);
        mTypeWolfHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTypeWolfHere.setText("");
            }
        });
        mTypeWolfHere.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (mTypeWolfHere.getText().toString().equals("")) {
                    MainActivity.myRef.child("maxOrderName").setValue("");
                } else if (mTypeWolfHere.getText().length() > 1 && partyStarted) {
                    MainActivity.myRef.child("maxOrderName").setValue(mTypeWolfHere.getText().toString().toUpperCase(Locale.getDefault()));
                } else {
                    Toast.makeText(getActivity(), getString(R.string.invalid_length), Toast.LENGTH_LONG).show();
                }
                mTypeWolfHere.setText("");
                return false;
            }
        });

        final EditText mTypeAllTimeWolfHere = view.findViewById(R.id.mTypeAllTimeWolfHere);
        mTypeAllTimeWolfHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTypeAllTimeWolfHere.setText("");
            }
        });
        mTypeAllTimeWolfHere.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (mTypeAllTimeWolfHere.getText().toString().equals("")) {
                    MainActivity.myRef.child("allTimeWolfName").setValue("");
                } else if (mTypeAllTimeWolfHere.getText().length() > 1 && partyStarted) {
                    MainActivity.myRef.child("allTimeWolfName").setValue(mTypeAllTimeWolfHere.getText().toString().toUpperCase(Locale.getDefault()));
                } else {
                    Toast.makeText(getActivity(), getString(R.string.invalid_length), Toast.LENGTH_LONG).show();
                }
                mTypeAllTimeWolfHere.setText("");
                return false;
            }
        });

        final Button mStart = view.findViewById(R.id.mStart);
        mStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!partyStarted) {
                    if (v != null)
                        v.vibrate(PATTERN, -1);

                    Drink.countTotalCurrent = 0;
                    MainActivity.myRef.child("countTotalCurrent").setValue(Drink.countTotalCurrent);
                    Drink.countTotalLast = 0;
                    MainActivity.myRef.child("countTotalLast").setValue(Drink.countTotalLast);
                    Drink.countTotalSecondLast = 0;
                    MainActivity.myRef.child("countTotalSecondLast").setValue(Drink.countTotalSecondLast);
                    Drink.countTotalDifference = 0;
                    MainActivity.myRef.child("countTotalDifference").setValue(Drink.countTotalDifference);
                    Drink.partyCountTotal = 0;
                    MainActivity.myRef.child("partyCountTotal").setValue(Drink.partyCountTotal);
                    Drink.partyRevenueTotal = 0.00;
                    MainActivity.myRef.child("partyRevenueTotal").setValue(Drink.partyRevenueTotal);
                    OrderFragment.maxOrder = 0L;
                    MainActivity.myRef.child("maxOrder").setValue(OrderFragment.maxOrder);
                    PricesFragment.maxOrderName = "";
                    MainActivity.myRef.child("maxOrderName").setValue("");

                    for (DrinkUI i : DrinkUI.uidrinks) {
                        //i.prices = new ArrayList<>();

                        i.price = i.startPrice;
                        MainActivity.myRef.child("Drinks").child(i.name).child("price").setValue(i.startPrice);
                        i.countCurrent = 0L;
                        MainActivity.myRef.child("Drinks").child(i.name).child("countCurrent").setValue(i.countCurrent);
                        i.partyRevenue = 0.00;
                        MainActivity.myRef.child("Drinks").child(i.name).child("partyRevenue").setValue(i.partyRevenue);
                        i.countLast = 0L;
                        MainActivity.myRef.child("Drinks").child(i.name).child("countLast").setValue(i.countLast);
                        i.countDifference = 0L;
                        MainActivity.myRef.child("Drinks").child(i.name).child("countDifference").setValue(i.countDifference);
                        i.countSecondLast = 0L;
                        MainActivity.myRef.child("Drinks").child(i.name).child("countSecondLast").setValue(i.countSecondLast);
                        i.partyCount = 0L;
                        MainActivity.myRef.child("Drinks").child(i.name).child("partyCount").setValue(i.partyCount);
                        i.priceDifference = 0.00;
                        MainActivity.myRef.child("Drinks").child(i.name).child("priceDifference").setValue(i.priceDifference);
                        i.priceLast = 0.00;
                        MainActivity.myRef.child("Drinks").child(i.name).child("priceLast").setValue(i.priceLast);
                    }

                    final Handler handler = new Handler();
                    final Runnable runnable = new Runnable() {
                        public void run() {
                            handler.post(new Runnable() {
                                public void run() {
                                    if (v != null)
                                        v.vibrate(PATTERN, -1);

                                    DrinkUI.task();

                                    // Context needed, for when the fragment is not connected to the activity.
                                    //Toast.makeText(getActivity(), getString(R.string.task_executed), Toast.LENGTH_LONG).show();
                                    Log.d("Admin Only", "TimerTask executed!");
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

                    final int min = (int) INTERVAL / 1000 / 60;
                    Toast.makeText(getActivity(), String.format(Locale.getDefault(), getString(R.string.timertask), min), Toast.LENGTH_LONG).show();
                    Log.d(TAG, String.format(Locale.getDefault(), getString(R.string.timertask), min));
                    timer.schedule(timerTask, INTERVAL, INTERVAL);

                    partyStarted = true;
                    MainActivity.myRef.child("partyStarted").setValue(partyStarted);
                } else {
                    Toast.makeText(getActivity(), getString(R.string.party_already_started_error), Toast.LENGTH_LONG).show();
                    Log.d(TAG, getString(R.string.party_already_started_error));
                }
            }
        });
        mStart.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                partyStarted = false;
                MainActivity.myRef.child("partyStarted").setValue(partyStarted);

                return false;
            }
        });

        final Button mCrash = view.findViewById(R.id.mCrash);
        mCrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (partyStarted) {
                    DrinkUI.crash();
                } else {
                    Toast.makeText(getActivity(), getString(R.string.party_not_started_error), Toast.LENGTH_LONG).show();
                }
            }
        });

        final Button back = view.findViewById(R.id.admin_only_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onAdminOnlyBackPressed();
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
        } else {
            throw new RuntimeException(context.toString() + " must implement OnCountersFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    interface OnAdminOnlyFragmentInteractionListener {
        void onAdminOnlyBackPressed();
    }
}
