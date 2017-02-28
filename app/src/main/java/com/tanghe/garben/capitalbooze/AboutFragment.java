package com.tanghe.garben.capitalbooze;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class AboutFragment extends Fragment {

    private OnAboutFragmentInteractionListener mListener;

    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        TextView mSite = (TextView) view.findViewById(R.id.mSite);
        mSite.setText(Html.fromHtml("<a href='http://moederpeerdevisscher.com'>moederpeerdevisscher.com</a>"));
        mSite.setMovementMethod(LinkMovementMethod.getInstance());

        Button next = (Button) view.findViewById(R.id.about_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onAboutNextPressed();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAboutFragmentInteractionListener) {
            mListener = (OnAboutFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnAboutFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnAboutFragmentInteractionListener {
        void onAboutNextPressed();
    }
}
