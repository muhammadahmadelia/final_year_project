package com.example.userapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SignInActivity extends AppCompatActivity {
    private static final String TAG = "Sign In Activity";
    private EditText signInEmailBox, signInPswdBox;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Removing Action bar
        if(getSupportActionBar() != null)
            getSupportActionBar().hide();

        firebaseAuth = FirebaseAuth.getInstance();
        signInEmailBox = findViewById(R.id.signInEmailBox);
        signInPswdBox = findViewById(R.id.signInPswdBox);
    }

    public void signUpLink_Click(View view){
        // opening SignUp form
        startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Exit");
        builder.setMessage("Are you sure you want to leave?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
                finish();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public void signInButton_onClick(View view){

        progressDialog=new ProgressDialog(SignInActivity.this);
        progressDialog.setMessage("Authenticating. . .");
        progressDialog.show();
        findViewById(R.id.signInButton).setEnabled(false);
        final String userEmail = ((TextView) findViewById(R.id.signInEmailBox)).getText().toString();
        final String userPswd = ((TextView) findViewById(R.id.signInPswdBox)).getText().toString();
        if(validateInput(userEmail, userPswd)){
            firebaseAuth.signInWithEmailAndPassword(userEmail, userPswd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        checkEmailVerification(userEmail, userPswd);
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "You are not registered user please register yourself", Toast.LENGTH_SHORT).show();
                        findViewById(R.id.signInButton).setEnabled(true);
                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Wrong inputs", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
            findViewById(R.id.signInButton).setEnabled(true);
        }
    }

    private void checkEmailVerification(String userEmail, String userPswd) {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null){
            if(firebaseUser.isEmailVerified()){
                findViewById(R.id.signInButton).setEnabled(true);
                Log.d(TAG, " logedInUser Name :"+userEmail);
                Log.d(TAG, " logedInUser Password :"+userPswd);

                progressDialog.dismiss();
                finish();
                startActivity(new Intent(SignInActivity.this, HomeActivity.class));
            } else{
                //progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Please verify your email by clicking on link in email", Toast.LENGTH_SHORT).show();
                findViewById(R.id.signInButton).setEnabled(true);
            }
        }
    }

    public boolean validateInput(String userEmail, String userPswd){
        InputValidation inputValidation = new InputValidation();
        boolean flag = true;
        if(inputValidation.isValidEmail(userEmail)){
            signInEmailBox.setError(null);
        } else{
            signInEmailBox.setError("enter a valid email address");
            flag = false;
        }

        if(inputValidation.isValidPassword(userPswd)){
            signInPswdBox.setError(null);
        } else{
            signInPswdBox.setError("between 5 and 10 alphanumeric characters");
            flag = false;
        }

        return flag;
    }

}
