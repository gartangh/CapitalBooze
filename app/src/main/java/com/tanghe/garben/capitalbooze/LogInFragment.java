package com.tanghe.garben.capitalbooze;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LogInFragment.OnLogInFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class LogInFragment extends Fragment {

    private OnLogInFragmentInteractionListener mListener;

    private static final String TAG = "Log In";

    private TextView mStatus;
    private TextView mDetail;
    private LinearLayout mFields;
    private EditText mEmail;
    private EditText mPassword;
    private LinearLayout mButtons;
    private Button mSignOut;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public LogInFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }

        mListener.hideProgressDialog();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid() + ": "+ user.getEmail());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                updateUI(user);
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_log_in, container, false);

        // Views
        mStatus = (TextView) view.findViewById(R.id.mStatus);
        mDetail = (TextView) view.findViewById(R.id.mDetail);
        mFields = (LinearLayout) view.findViewById(R.id.mFields);
        mEmail = (EditText) view.findViewById(R.id.mEmail);
        mPassword = (EditText) view.findViewById(R.id.mPassword);
        mButtons = (LinearLayout) view.findViewById(R.id.mButtons);
        Button mSignIn = (Button) view.findViewById(R.id.mSignIn);
        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn(mEmail.getText().toString(), mPassword.getText().toString());
            }
        });
        Button mCreatAccount = (Button) view.findViewById(R.id.mCreateAccount);
        mCreatAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount(mEmail.getText().toString(), mPassword.getText().toString());
            }
        });
        mSignOut = (Button) view.findViewById(R.id.mSignOut);
        mSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
            }
        });

        final Button back = (Button) view.findViewById(R.id.log_in_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onLogInBackPressed();
            }
        });
        final Button next = (Button) view.findViewById(R.id.log_in_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onLogInNextPressed();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLogInFragmentInteractionListener) {
            mListener = (OnLogInFragmentInteractionListener) context;
        } else {
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
    public interface OnLogInFragmentInteractionListener {
        void onLogInBackPressed();
        void onLogInNextPressed();
        void showProgressDialog();
        void hideProgressDialog();
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        mListener.showProgressDialog();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.
                if (!task.isSuccessful()) {
                    Toast.makeText(getActivity(), R.string.auth_failed, Toast.LENGTH_SHORT).show();
                }

                final FirebaseUser user = mAuth.getCurrentUser();
                MainActivity.ref2.child("Users").child(user.getUid()).setValue(user);
                MainActivity.ref2.child("Users").child(user.getUid()).child("accountType").setValue(0L);
                MainActivity.ref2.child("Users").child(user.getUid()).child("accountType").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        MainActivity.accountType = (Long) snapshot.getValue();
                        Log.d("FireBase", "Data changed: set accountType to " + MainActivity.accountType);
                        updateUI(user);
                    }
                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        Log.d("Firebas", "The read failed: " + firebaseError.getMessage());
                    }
                });
                Log.d("Log in", "Wrote " + user.getEmail() + ": "+ user.getUid() + " to database");

                mListener.hideProgressDialog();
            }
        });
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        mListener.showProgressDialog();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.
                if (!task.isSuccessful()) {
                    Log.w(TAG, "signInWithEmail:failed", task.getException());
                    Toast.makeText(getActivity(), R.string.auth_failed, Toast.LENGTH_SHORT).show();
                    mStatus.setText(R.string.auth_failed);
                }

                final FirebaseUser user = mAuth.getCurrentUser();
                MainActivity.ref2.child("Users").child(user.getUid()).child("accountType").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        MainActivity.accountType = (Long) snapshot.getValue();
                        Log.d("FireBase", "Data changed: set accountType to " + MainActivity.accountType);
                        updateUI(user);
                    }
                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        Log.d("FireBase", "The read failed: " + firebaseError.getMessage());
                    }
                });

                mListener.hideProgressDialog();
            }
        });
    }

    private boolean validateForm() {
        boolean valid = true;

        String adress = mEmail.getText().toString();
        if (TextUtils.isEmpty(adress)) {
            mEmail.setError("Required.");
            valid = false;
        } else {
            mEmail.setError(null);
        }

        String code = mPassword.getText().toString();
        if (TextUtils.isEmpty(code)) {
            mPassword.setError("Required.");
            valid = false;
        } else {
            mPassword.setError(null);
        }

        return valid;
    }

    private void updateUI(FirebaseUser user) {

        if (user != null) {
            mStatus.setText(getString(R.string.emailpassword_status_fmt, user.getEmail()));
            mDetail.setText(getString(R.string.firebase_status_fmt, user.getUid()));

            mButtons.setVisibility(View.GONE);
            mFields.setVisibility(View.GONE);
            mSignOut.setVisibility(View.VISIBLE);
        }
        else {
            mStatus.setText(R.string.signed_out);
            mDetail.setText(null);

            mButtons.setVisibility(View.VISIBLE);
            mFields.setVisibility(View.VISIBLE);
            mSignOut.setVisibility(View.GONE);
        }
    }
}