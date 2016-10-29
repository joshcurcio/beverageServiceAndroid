package com.joshuacurcio.beverageservice;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.security.cert.CertificateParsingException;

public class UserCreateAccount extends AppCompatActivity
{
    private EditText mFirstNameField;
    private EditText mLastNameField;
    private EditText mEmailField;
    private EditText mAddressField;
    private EditText mPasswordField;
    private EditText mPasswordConfirmField;
    private String TAG;

    private UserProfile userProfile;

    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_create_account);

        mFirstNameField = (EditText) findViewById(R.id.txtFirstName);
        mFirstNameField = (EditText) findViewById(R.id.txtFirstName);
        mFirstNameField = (EditText) findViewById(R.id.txtFirstName);
        mFirstNameField = (EditText) findViewById(R.id.txtFirstName);
        mFirstNameField = (EditText) findViewById(R.id.txtFirstName);
        mFirstNameField = (EditText) findViewById(R.id.txtFirstName);
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

    public void createAccount()
    {
        //check to see if passwords match
        if(!validateForm()) {
            return;
        }


        Singleton.mAuth.createUserWithEmailAndPassword(mEmailField.getText().toString(), mPasswordField.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
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
                Singleton.mDatabase.child("users").child(Singleton.mAuth.getCurrentUser().getUid()).setValue(Singleton.userProfile);
            }
        });


    }


}
