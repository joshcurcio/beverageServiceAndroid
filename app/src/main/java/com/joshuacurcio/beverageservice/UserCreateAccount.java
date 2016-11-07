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
import com.joshuacurcio.beverageservice.Objects.UserProfile;


public class UserCreateAccount extends AppCompatActivity implements View.OnClickListener {
    private EditText mFirstNameField;
    private EditText mLastNameField;
    private EditText mEmailField;
    private EditText mAddressField;
    private EditText mPasswordField;
    private EditText mPasswordConfirmField;
    private EditText mPINField;
    private String TAG;

    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_create_account);

        Singleton.mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = Singleton.mAuth.getCurrentUser();
        if (user != null) {
            // User is signed in
            startActivity(new Intent(UserCreateAccount.this, UserHome.class));
        }

        mFirstNameField = (EditText) findViewById(R.id.txtFirstName);
        mPasswordConfirmField = (EditText) findViewById(R.id.txtPasswordConfirm);
        mPasswordField = (EditText) findViewById(R.id.txtPassword);
        mAddressField = (EditText) findViewById(R.id.txtAddress);
        mEmailField = (EditText) findViewById(R.id.txtEmail);
        mLastNameField = (EditText) findViewById(R.id.txtLastName);
        mPINField = (EditText) findViewById(R.id.txtPIN);

        //button
        findViewById(R.id.butConfirm).setOnClickListener(this);


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

        String userPIN = mPINField.getText().toString();
        if (TextUtils.isEmpty(userPIN)) {

            mPINField.setError("Required.");
            valid = false;
        } else {
            mPINField.setError(null);
        }
        if(userPIN.length() != 4)
        {
            mPINField.setError("PIN must be 4 digits.");
            valid = false;
        }

        return valid;
    }

    public void createAccount(String email, String password)
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

            createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());

            Singleton.userProfile = new UserProfile(mFirstNameField.getText().toString(), mLastNameField.getText().toString(), mEmailField.getText().toString(), mAddressField.getText().toString(), mPINField.getText().toString(), "User");

            Singleton.mAuth.signInWithEmailAndPassword(mEmailField.getText().toString(), mPasswordField.getText().toString());
            FirebaseUser user = Singleton.mAuth.getCurrentUser();
            if (user != null) {
                // User is signed in
                Singleton.mDatabase = FirebaseDatabase.getInstance().getReference();
                Singleton.mDatabase.child("users").child(user.getUid()).setValue(Singleton.userProfile);
                startActivity(new Intent(UserCreateAccount.this, UserHome.class));
            } else {
                // User is signed out
                Log.e(TAG, "Error with create account and sign in");
            }

        }
    }
}
