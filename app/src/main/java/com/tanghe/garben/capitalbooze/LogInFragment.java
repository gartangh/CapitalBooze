package com.tanghe.garben.capitalbooze;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class LogInFragment extends Fragment {

    private OnLogInFragmentInteractionListener mListener;

    private static final String TAG = "Log In";
    private final int MY_PERMISSIONS_REQUEST_GET_ACCOUNTS = 123;

    private TextView mStatus;
    private TextView mDetail;
    private LinearLayout mFields;
    private AutoCompleteTextView mEmail;
    private AutoCompleteTextView mPassword;
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
                    Log.d(TAG, "onAuthStateChanged: signed in: " + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged: signed out");
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
        mEmail = (AutoCompleteTextView) view.findViewById(R.id.mEmail);
        mEmail.setAdapter(getEmailAddressAdapter(this.getContext()));
        mPassword = (AutoCompleteTextView) view.findViewById(R.id.mPassword);
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
                MainActivity.accountType = 0L;
            }
        });

        final Button back = (Button) view.findViewById(R.id.log_in_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onLogInBackPressed();
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
                    Toast.makeText(getContext(), getString(R.string.auth_failed_error), Toast.LENGTH_LONG).show();
                } else {
                    final FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        MainActivity.myRef.child("Users").child(user.getUid()).setValue(user);
                        MainActivity.myRef.child("Users").child(user.getUid()).child("accountType").setValue(0L);
                        MainActivity.myRef.child("Users").child(user.getUid()).child("accountType").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                MainActivity.accountType = snapshot.getValue(Long.class);
                                Log.d("FireBase", "Data changed: set accountType to " + MainActivity.accountType);
                                updateUI(user);
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                // Failed to read value
                                Log.w(TAG, "Failed to read value.", error.toException());
                            }
                        });
                        Log.d("Log in", "Wrote " + user.getEmail() + ": " + user.getUid() + " to database");
                    }
                }

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
                    Toast.makeText(getContext(), getString(R.string.auth_failed_error), Toast.LENGTH_LONG).show();
                    mStatus.setText(getString(R.string.auth_failed_error));
                } else {
                    final FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        MainActivity.myRef.child("Users").child(user.getUid()).child("accountType").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                MainActivity.accountType = snapshot.getValue(Long.class);
                                Log.d("FireBase", "Data changed: set accountType to " + MainActivity.accountType);
                                updateUI(user);
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                // Failed to read value
                                Log.w(TAG, "Failed to read value.", error.toException());
                            }
                        });
                    }
                }

                mListener.hideProgressDialog();
            }
        });
    }

    private boolean validateForm() {
        boolean valid = true;

        // Check for empty text fields.
        String address = mEmail.getText().toString();
        if (TextUtils.isEmpty(address)) {
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

        // Check for valid address.
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(address).matches()) {
            valid = false;
        }

        if (code.length() < 4 || code.length() > 10) {
            valid = false;
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
        } else {
            mStatus.setText(getString(R.string.logged_out));
            mDetail.setText(null);

            mButtons.setVisibility(View.VISIBLE);
            mFields.setVisibility(View.VISIBLE);
            mSignOut.setVisibility(View.GONE);
        }
    }

    public interface OnLogInFragmentInteractionListener {
        void onLogInBackPressed();

        void showProgressDialog();

        void hideProgressDialog();
    }

    private ArrayAdapter<String> getEmailAddressAdapter(Context context) {
        if (ActivityCompat.checkSelfPermission(this.getActivity(), android.Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            // Permission GET_ACCOUNTS denied.
            Toast.makeText(getContext(), getString(R.string.get_accounts_denied), Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{android.Manifest.permission.GET_ACCOUNTS}, MY_PERMISSIONS_REQUEST_GET_ACCOUNTS);
            String[] addresses = new String[0];
            return new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, addresses);
        }
        // Permission GET_ACCOUNTS granted.
        Toast.makeText(getContext(), getString(R.string.get_accounts_granted), Toast.LENGTH_LONG).show();
        Account[] accounts = AccountManager.get(context).getAccounts();
        String[] addresses = new String[accounts.length];
        for (int i = 0; i < accounts.length; i++) {
            addresses[i] = accounts[i].name;
        }
        return new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, addresses);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        Log.d(TAG, "Got " + requestCode);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_GET_ACCOUNTS: {
                Log.d(TAG, "" + MY_PERMISSIONS_REQUEST_GET_ACCOUNTS);
                // If request is cancelled, grantResults is an empty array (length = 0).
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission GET_ACCOUNTS granted.
                    if (mEmail != null) {
                        mEmail.setAdapter(getEmailAddressAdapter(this.getContext()));
                        Log.d(TAG, "Adapter set!");
                    }
                    getEmailAddressAdapter(this.getContext());
                    Log.d(TAG, "mEmail was not defined!");
                } else {
                    // Permission GET_ACCOUNTS denied.
                    Toast.makeText(getContext(), getString(R.string.get_accounts_denied), Toast.LENGTH_LONG).show();
                }
                break;
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}