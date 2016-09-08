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

import java.util.Timer;
import java.util.TimerTask;

public class AdminOnlyFragment extends Fragment {

    private OnAdminOnlyFragmentInteractionListener mListener;
    private static Context context;
    private final static String TAG = "AdminOnlyFragment";

    private static boolean partyStarted = false;
    private static long INTERVAL = 60*1000L;
    private static final long[] PATTERN = {0L, 100L, 100L, 50L};

    public AdminOnlyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_only, container, false);

        Button mStart = (Button) view.findViewById(R.id.mStart);
        mStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

                if (!partyStarted) {
                    v.vibrate(PATTERN, -1);

                    final Handler handler = new Handler();

                    final Runnable runnable = new Runnable() {
                        public void run() {
                            handler.post(new Runnable() {
                                public void run() {
                                    v.vibrate(PATTERN, -1);

                                    DrinkUI.task();

                                    Log.d(TAG, "Thread task executed");
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

                    Log.d(TAG,"TimerTaskset, interval set for " + INTERVAL/1000/60 + " minutes");
                    timer.schedule(timerTask, INTERVAL, INTERVAL);

                    partyStarted = true;
                }
            }
        });

        Button mCrash = (Button) view.findViewById(R.id.mCrash);
        mCrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DrinkUI.crash();

                Log.d(TAG, "CRASH");
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
