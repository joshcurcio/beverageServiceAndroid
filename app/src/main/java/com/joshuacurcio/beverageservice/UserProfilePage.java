package com.joshuacurcio.beverageservice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.joshuacurcio.beverageservice.Singleton.mDatabase;

public class UserProfilePage extends AppCompatActivity
{
    private TextView mFirstName;
    private TextView mLastName;
    private TextView mAddress;
    private TextView mPIN;

    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_page);

        mFirstName = (TextView) findViewById(R.id.userFirstName);
        mLastName = (TextView) findViewById(R.id.userLastName);
        mAddress = (TextView) findViewById(R.id.userAddress);
        mPIN = (TextView) findViewById(R.id.userPIN);

        // Get a reference to our users
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        Singleton.mDatabase = database.getReference("users");
    }
}
