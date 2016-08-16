package com.tanghe.garben.capitalbooze;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PartyFragment.OnPartyFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PartyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PartyFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private DatePicker datePicker;
    private TimePicker timePicker;
    private EditText interval;
    private Button enter;

    private OnPartyFragmentInteractionListener mListener;

    public PartyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PartyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PartyFragment newInstance(String param1, String param2) {
        PartyFragment fragment = new PartyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_party, container, false);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        datePicker = (DatePicker) view.findViewById(R.id.datePicker);
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int month, int day) {
                mListener.onDateChanged(year, month, day);
            }
        });

        timePicker = (TimePicker) view.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
                mListener.onTimeChanged(hour, minute);
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
                    int INTERVAL = Integer.parseInt(interval.getText().toString());
                    if (INTERVAL >= 5 && INTERVAL <= 30) {
                        mListener.onIntervalSet(INTERVAL);
                        interval.setHint(Integer.toString(INTERVAL) + " MIN");
                        interval.setText("");
                    }
                    else {
                        interval.setHint("5-30 MINUTES");
                        interval.setText("");
                    }
                }
                catch (NumberFormatException nfe) {
                    interval.setText("");
                    interval.setText("INVALID");
                }

                return false;
            }
        });

        enter = (Button) view.findViewById(R.id.enter);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onEnterPressed();
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
        void onDateChanged(int year,int month,int day);
        void onTimeChanged(int hour, int min);
        void onIntervalSet(int interval);
        void onEnterPressed();
    }
}
