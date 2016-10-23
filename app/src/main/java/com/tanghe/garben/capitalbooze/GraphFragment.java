package com.tanghe.garben.capitalbooze;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

/**
 * A simple {@link Fragment} subclass.
 */
public class GraphFragment extends Fragment {

    private OnGraphFragmentInteractionListener mListener;

    public GraphFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_graph, container, false);

        for (DrinkUI i : DrinkUI.uidrinks) {
            if (i.getName().equals("Stella")) {
                XYPlot plot = (XYPlot) view.findViewById(R.id.plot);
                XYSeries series = new SimpleXYSeries(i.prices, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Stella");

                LineAndPointFormatter series1Format = new LineAndPointFormatter(R.color.red, R.color.green, R.color.colorPrimary, null);

                plot.addSeries(series, series1Format);
            }
        }

        Button back = (Button) view.findViewById(R.id.graph_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onGraphBackPressed();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AboutFragment.OnAboutFragmentInteractionListener) {
            mListener = (OnGraphFragmentInteractionListener) context;
        }
        else {
            throw new RuntimeException(context.toString() + " must implement OnGraphFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnGraphFragmentInteractionListener {
        void onGraphBackPressed();
    }
}
