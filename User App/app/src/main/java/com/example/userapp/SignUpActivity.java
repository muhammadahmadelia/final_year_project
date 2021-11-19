package com.example.userapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "Sign In Activity";
    private EditText signUpUsernameBox, signUpEmailBox, signUpPswdBox;
    private ProgressDialog progressDialog;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference myDatabaseRef;
    //StorageReference myStorageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //For setting action bar
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle("Create Account");
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#B58A3F")));
        }

        //For firebase database Authentication
        mFirebaseAuth = FirebaseAuth.getInstance();
        //getting firebase database refernce
        myDatabaseRef = FirebaseDatabase.getInstance().getReference();

        //Getting reference of firebase storage
        //myStorageReference = FirebaseStorage.getInstance().getReference().child("uploads");

        //For uploading image to firebase
        //storage = FirebaseStorage.getInstance();
        //storageReference = storage.getReference();

        progressDialog = new ProgressDialog(SignUpActivity.this);
    }

    public void signInLink_Click(View view){
        // going back to Sign in activity
        finish();
    }

    public void signUpButton_Click(View view){
        signUpUsernameBox = findViewById(R.id.signUpUsernameBox);
        signUpEmailBox = findViewById(R.id.signUpEmailBox);
        signUpPswdBox = findViewById(R.id.signUpPswdBox);

        progressDialog.setMessage("Creating account. . .");
        progressDialog.show();
        findViewById(R.id.signUpButton).setEnabled(false);

        final String username = signUpUsernameBox.getText().toString();
        final String userEmail = signUpEmailBox.getText().toString();
        final String userPswd = signUpPswdBox.getText().toString();

        if(validateInput(username, userEmail, userPswd)){
            mFirebaseAuth.createUserWithEmailAndPassword(userEmail, userPswd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        mFirebaseAuth.signInWithEmailAndPassword(userEmail, userPswd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                task.getResult().getUser().getUid();
                                User user = new User(username, userEmail, userPswd);
                                myDatabaseRef.child("users")
                                        .child(mFirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    //uploadFile();
                                                    mFirebaseAuth.getCurrentUser()
                                                            .sendEmailVerification()
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(task.isSuccessful()){
                                                                        Toast.makeText(getApplicationContext(), "Verify yourself on email",Toast.LENGTH_LONG).show();
                                                                        if(!mFirebaseAuth.getCurrentUser().isEmailVerified()){
                                                                            Log.d(TAG, " registeredUser Name :"+username);
                                                                            Log.d(TAG, " registeredUser Email :"+userEmail);
                                                                            Toast.makeText(getApplicationContext(), "Account created successfully", Toast.LENGTH_LONG).show();
                                                                            mFirebaseAuth.signOut();
                                                                            findViewById(R.id.signUpButton).setEnabled(true);
                                                                            progressDialog.dismiss();
                                                                            finish();
                                                                            //startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
                                                                        }
                                                                    }else {
                                                                        if(task.getException() != null){
                                                                            Toast.makeText(getApplicationContext(), task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                                                            Log.d(TAG, "Error in verification acccount : "+task.getException().getMessage());
                                                                        }
                                                                    }
                                                                }
                                                            });
                                                } else {
                                                    //if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                                        Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();
                                                    //}
                                                    Toast.makeText(getApplicationContext(), "Not registered", Toast.LENGTH_SHORT).show();
                                                    findViewById(R.id.signUpButton).setEnabled(true);
                                                    progressDialog.dismiss();
                                                }
                                            }
                                        });
                            }
                        });
                    } else {
                        //if(task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();
                            findViewById(R.id.signUpButton).setEnabled(true);
                            progressDialog.dismiss();
                        //}
                    }
                }
            });
        } else{
            Toast.makeText(getApplicationContext(), "Wrong inputs", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
            findViewById(R.id.signUpButton).setEnabled(true);
        }
    }

    private boolean validateInput(String username, String userEmail, String userPswd){
        boolean flag = true;
        InputValidation inputValidation = new InputValidation();

        if(inputValidation.isValidName(username)){
            signUpUsernameBox.setError(null);
        } else{
            signUpUsernameBox.setError("enter a valid name");
            flag = false;
        }

        if(inputValidation.isValidEmail(userEmail)){
            signUpEmailBox.setError(null);
        } else{
            signUpEmailBox.setError("enter a valid email address");
            flag = false;
        }

        if(inputValidation.isValidPassword(userPswd)){
            signUpPswdBox.setError(null);
        } else{
            signUpPswdBox.setError("between 5 and 10 alphanumeric characters");
            flag = false;
        }

        return flag;
    }
}
