package com.example.userapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileUpdateActivity extends AppCompatActivity {
    private static final String TAG = "UpdateProfile";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListner;
    private String userID;
    private ProgressDialog progressDialog;
    private EditText profileUsernameBox, profileEmailBox, profilePswdBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Update Account");
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#B58A3F")));
        }

        profileUsernameBox = findViewById(R.id.profileUsernameBox);
        profileEmailBox = findViewById(R.id.profileEmailBox);
        profilePswdBox = findViewById(R.id.profilePswdBox);

        progressDialog = new ProgressDialog(ProfileUpdateActivity.this);
        progressDialog.setMessage("Please wait loading information...");
        progressDialog.show();

        //For getting user ID
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mRef = mFirebaseDatabase.getReference().child("users");
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null)
            userID = user.getUid();

        mAuthStateListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed in "+user.getUid());
                    //ToastMessage("Succssfully signed in : "+user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed out ");
                    //ToastMessage("Succssfully signed out");
                }
            }
        };

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Get data from the database and put them in variables
                String username = dataSnapshot.child(userID).child("name").getValue(String.class);
                String userEmail = dataSnapshot.child(userID).child("email").getValue(String.class);
                String userPswd = dataSnapshot.child(userID).child("password").getValue(String.class);

                //Set data of user in fields
                profileUsernameBox.setText(username);
                profileEmailBox.setText(userEmail);
                profilePswdBox.setText(userPswd);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


    }

    public void updateButton_Click(View view){
        final String username = profileUsernameBox.getText().toString().trim();
        final String userEmail = profileEmailBox.getText().toString().trim();
        final String userPswd = profilePswdBox.getText().toString().trim();


        findViewById(R.id.updateProfileButton).setEnabled(false);

        if(validateInput(username, userEmail, userPswd)){
            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(userID);
            //Get data from the fields
            //nameText = name.getText().toString().trim();
            //emailText = email.getText().toString().trim();

            //Save updated data to the database

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if(user!=null){
                user.updatePassword(userPswd).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            ref.child("name").setValue(username);
                            ref.child("email").setValue(userEmail);
                            ref.child("password").setValue(userPswd);
                            Toast.makeText(getBaseContext(), "Account updated successfully", Toast.LENGTH_LONG).show();
                            mAuth.signOut();
                            finish();
                        } else {
                            Toast.makeText(getBaseContext(), "Account not updated", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

            //Toast.makeText(getBaseContext(), "Account updated successfully", Toast.LENGTH_LONG).show();
            findViewById(R.id.updateProfileButton).setEnabled(true);
            progressDialog.dismiss();
        } else {
            Toast.makeText(getApplicationContext(), "Wrong inputs", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
            findViewById(R.id.updateProfileButton).setEnabled(true);
        }

    }

    private boolean validateInput(String username, String userEmail, String userPswd){
        boolean flag = true;
        InputValidation inputValidation = new InputValidation();

        if(inputValidation.isValidName(username)){
            profileUsernameBox.setError(null);
        } else{
            profileUsernameBox.setError("enter a valid name");
            flag = false;
        }

        if(inputValidation.isValidEmail(userEmail)){
            profileEmailBox.setError(null);
        } else{
            profileEmailBox.setError("enter a valid email address");
            flag = false;
        }

        if(inputValidation.isValidPassword(userPswd)){
            profilePswdBox.setError(null);
        } else{
            profilePswdBox.setError("between 5 and 10 alphanumeric characters");
            flag = false;
        }

        return flag;
    }
}
