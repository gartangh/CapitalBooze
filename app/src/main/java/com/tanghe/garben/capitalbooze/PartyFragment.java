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
    private DatePicker datePicker;
    private TimePicker timePicker;
    private EditText interval;
    private Button enter;

    protected static final Calendar calendar = Calendar.getInstance();
    protected static int year = calendar.get(Calendar.YEAR);
    protected static int month = calendar.get(Calendar.MONTH);
    protected static int day = calendar.get(Calendar.DAY_OF_MONTH);
    protected static int hour = calendar.get(Calendar.HOUR_OF_DAY);
    protected static int min = calendar.get(Calendar.MINUTE);
    protected static int sec = 0;
    protected static Date date = new Date(year, month, day, hour, min, sec);
    protected static long INTERVAL = 15;

    private OnPartyFragmentInteractionListener mListener;

    public PartyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("debug", "Date = " + date);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_party, container, false);

        datePicker = (DatePicker) view.findViewById(R.id.datePicker);
        datePicker.setMinDate(System.currentTimeMillis());
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int month, int day) {
                Log.d("debug", "Date = " + date);
                PartyFragment.calendar.set(year, month, day, hour, min, 0);
                date = new Date(year, month, day, hour, min, sec);
                PartyFragment.year = year;
                PartyFragment.month = month;
                PartyFragment.day = day;
            }
        });

        timePicker = (TimePicker) view.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int min) {
                Log.d("debug", "Date = " + date);
                PartyFragment.calendar.set(year, month, day, hour, min, 0);
                date = new Date(year, month, day, hour, min, sec);
                PartyFragment.hour = hour;
                PartyFragment.min = min;
            }
        });

        interval = (EditText) view.findViewById(R.id.interval);
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

        enter = (Button) view.findViewById(R.id.enter);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("debug", "Date = " + date);
                mListener.onEnterPressed(date, INTERVAL*60*1000L);
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
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
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
        void onEnterPressed(Date date, long INTERVAL);
    }
}
