package com.tanghe.garben.capitalbooze;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
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

    protected static Context context;

    protected final static Firebase ref2 = new Firebase("https://capital-booze.firebaseio.com");

    private static final String TAG = "Log In";

    private TextView status;
    private TextView detail;
    private LinearLayout fields;
    private EditText email;
    private EditText password;
    private LinearLayout buttons;
    private Button sign_in;
    private Button create_account;
    private Button sign_out;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private ProgressDialog mProgressDialog;

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
        //hideProgressDialog();
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
        status = (TextView) view.findViewById(R.id.status);
        detail = (TextView) view.findViewById(R.id.detail);
        fields = (LinearLayout) view.findViewById(R.id.fields);
        email = (EditText) view.findViewById(R.id.email);
        password = (EditText) view.findViewById(R.id.password);
        buttons = (LinearLayout) view.findViewById(R.id.buttons);
        sign_in = (Button) view.findViewById(R.id.sign_in);
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn(email.getText().toString(), password.getText().toString());
            }
        });
        create_account = (Button) view.findViewById(R.id.create_account);
        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount(email.getText().toString(), password.getText().toString());
            }
        });
        sign_out = (Button) view.findViewById(R.id.sign_out);
        sign_out.setOnClickListener(new View.OnClickListener() {
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
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        //showProgressDialog();

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
                ref2.child("Users").child(user.getUid()).setValue(user);
                ref2.child("Users").child(user.getUid()).child("isAdmin").setValue(false);
                ref2.child("Users").child(user.getUid()).child("isAdmin").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if ((Boolean) snapshot.getValue()) {
                            MainActivity.isAdmin = true;
                        } else {
                            MainActivity.isAdmin = false;
                        }
                        updateUI(user);
                        Log.d("FireBase", "Data changed: set isAdmin to " + MainActivity.isAdmin);
                    }
                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        Log.d("Firebas", "The read failed: " + firebaseError.getMessage());
                    }
                });
                Log.d("Log in", "Wrote " + user.getEmail() + ": "+ user.getUid() + " to database");

                //hideProgressDialog();
            }
        });
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        //showProgressDialog();

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
                    status.setText(R.string.auth_failed);
                }

                final FirebaseUser user = mAuth.getCurrentUser();
                ref2.child("Users").child(user.getUid()).child("isAdmin").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if ((Boolean) snapshot.getValue()) {
                            MainActivity.isAdmin = true;
                        } else {
                            MainActivity.isAdmin = false;
                        }
                        updateUI(user);
                        Log.d("FireBase", "Data changed: set isAdmin to " + MainActivity.isAdmin);
                    }
                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        Log.d("Firebas", "The read failed: " + firebaseError.getMessage());
                    }
                });

                //hideProgressDialog();
            }
        });
    }

    private boolean validateForm() {
        boolean valid = true;

        String adress = email.getText().toString();
        if (TextUtils.isEmpty(adress)) {
            email.setError("Required.");
            valid = false;
        } else {
            email.setError(null);
        }

        String code = password.getText().toString();
        if (TextUtils.isEmpty(code)) {
            password.setError("Required.");
            valid = false;
        } else {
            password.setError(null);
        }

        return valid;
    }

    private void updateUI(FirebaseUser user) {

        //hideProgressDialog();

        if (user != null) {
            status.setText(getString(R.string.emailpassword_status_fmt, user.getEmail()));
            detail.setText(getString(R.string.firebase_status_fmt, user.getUid()));

            buttons.setVisibility(View.GONE);
            fields.setVisibility(View.GONE);
            sign_out.setVisibility(View.VISIBLE);
        }
        else {
            status.setText(R.string.signed_out);
            detail.setText(null);

            buttons.setVisibility(View.VISIBLE);
            fields.setVisibility(View.VISIBLE);
            sign_out.setVisibility(View.GONE);
        }
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public static void setArgument(Context context) {
        CountersFragment.context = context;
    }
}