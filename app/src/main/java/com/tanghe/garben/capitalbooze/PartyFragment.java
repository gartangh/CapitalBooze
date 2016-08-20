package com.tanghe.garben.capitalbooze;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import java.util.Calendar;
import java.util.Date;


/*
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PartyFragment.OnPartyFragmentInteractionListener} interface
 * to handle interaction events.
*/
public class PartyFragment extends Fragment {
    private OnPartyFragmentInteractionListener mListener;

    protected final static  Calendar calendar = Calendar.getInstance();
    protected static int year = calendar.get(Calendar.YEAR);
    protected static int month = calendar.get(Calendar.MONTH);
    protected static int day = calendar.get(Calendar.DAY_OF_MONTH);
    protected static int hour = calendar.get(Calendar.HOUR_OF_DAY);
    protected static int min = calendar.get(Calendar.MINUTE);
    protected final static int sec = 0;
    protected static Long time;
    protected static Date date;

    protected static long INTERVAL = 1L;

    public PartyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        calendar.set(Calendar.SECOND, sec);
        time = calendar.getTimeInMillis();
        date = new Date(time);
        Log.d("debug", "Date = " + date);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_party, container, false);

        final DatePicker datePicker = (DatePicker) view.findViewById(R.id.datePicker);
        datePicker.setMinDate(time);
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int month, int day) {
                calendar.set(year, month, day, hour, min);
                time = calendar.getTimeInMillis();
                date = new Date(time);
                Log.d("debug", "date = " + date + " year = " + year);
                PartyFragment.year = year;
                PartyFragment.month = month;
                PartyFragment.day = day;
            }
        });

        final TimePicker timePicker = (TimePicker) view.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int min) {
                calendar.set(year, month, day, hour, min);
                time = calendar.getTimeInMillis();
                date = new Date(time);
                Log.d("debug", "date = " + date + " year = " + year);
                PartyFragment.hour = hour;
                PartyFragment.min = min;
            }
        });

        final EditText interval = (EditText) view.findViewById(R.id.interval);
        interval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interval.setText("");
            }
        });
        interval.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                try {
                    INTERVAL = Long.parseLong(interval.getText().toString());
                    if (INTERVAL >= 1 && INTERVAL <= 30) {
                        interval.setHint(Long.toString(INTERVAL) + " MIN");
                        interval.setText("");
                    }
                    else {
                        interval.setHint("5-30 MINUTES");
                        interval.setText("");
                    }
                }
                catch (NumberFormatException nfe) {
                    interval.setHint("INVALID");
                    interval.setText("");
                }
                return false;
            }
        });

        final Button back = (Button) view.findViewById(R.id.party_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onPartyBackPressed();
            }
        });
        final Button next = (Button) view.findViewById(R.id.party_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("debug", "Date = " + date);
                mListener.onPartyNextPressed(date, INTERVAL*60*1000L);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPartyFragmentInteractionListener) {
            mListener = (OnPartyFragmentInteractionListener) context;
        }
        else {
            throw new RuntimeException(context.toString() + " must implement OnPartyFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnPartyFragmentInteractionListener {
        void onPartyBackPressed();
        void onPartyNextPressed(Date date, long INTERVAL);
    }
}
