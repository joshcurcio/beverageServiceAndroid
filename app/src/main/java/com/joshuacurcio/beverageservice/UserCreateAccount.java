package com.joshuacurcio.beverageservice;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;


public class UserCreateAccount extends AppCompatActivity implements View.OnClickListener {
    private EditText mFirstNameField;
    private EditText mLastNameField;
    private EditText mEmailField;
    private EditText mAddressField;
    private EditText mPasswordField;
    private EditText mPasswordConfirmField;
    private String TAG;

    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_create_account);

        mFirstNameField = (EditText) findViewById(R.id.txtFirstName);
        mPasswordConfirmField = (EditText) findViewById(R.id.txtPasswordConfirm);
        mPasswordField = (EditText) findViewById(R.id.txtPassword);
        mAddressField = (EditText) findViewById(R.id.txtAddress);
        mEmailField = (EditText) findViewById(R.id.txtEmail);
        mLastNameField = (EditText) findViewById(R.id.txtLastName);

        //button
        findViewById(R.id.butConfirm).setOnClickListener(this);

        Singleton.mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    startActivity(new Intent(UserCreateAccount.this, UserHome.class));
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    private boolean validateForm()
    {
        boolean valid = true;

        String userFirstName = mFirstNameField.getText().toString();
        if (TextUtils.isEmpty(userFirstName)) {
            mFirstNameField.setError("Required.");
            valid = false;
        } else {
            mFirstNameField.setError(null);
        }

        String userLastName = mLastNameField.getText().toString();
        if (TextUtils.isEmpty(userLastName)) {
            mLastNameField.setError("Required.");
            valid = false;
        } else {
            mLastNameField.setError(null);
        }

        String userEmail = mEmailField.getText().toString();
        if (TextUtils.isEmpty(userEmail)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String userAddress = mAddressField.getText().toString();
        if (TextUtils.isEmpty(userAddress)) {
            mAddressField.setError("Required.");
            valid = false;
        } else {
            mAddressField.setError(null);
        }

        String userPassword = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(userPassword)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        String userConfirmPassword = mPasswordConfirmField.getText().toString();
        if (TextUtils.isEmpty(userConfirmPassword)) {
            mPasswordConfirmField.setError("Required.");
            valid = false;
        } else {
            mPasswordConfirmField.setError(null);
        }

        if(!userPassword.equals(userConfirmPassword))
        {
            mPasswordConfirmField.setError("Passwords do not match.");
            valid = false;
        }

        return valid;
    }

    public void createAccount(final String email, final String password)
    {
        //check to see if passwords match
        if(!validateForm()) {
            return;
        }
        Singleton.mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                if (!task.isSuccessful())
                {
                    Toast.makeText(UserCreateAccount.this, R.string.auth_failed,
                            Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.butConfirm) {
            Singleton.userProfile = new UserProfile(mFirstNameField.getText().toString(), mLastNameField.getText().toString(), mEmailField.getText().toString(), mAddressField.getText().toString());
            createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
            Singleton.mAuth.signInWithEmailAndPassword(mEmailField.getText().toString(), mPasswordField.getText().toString());

            //send to sign up page

        }
    }
}
