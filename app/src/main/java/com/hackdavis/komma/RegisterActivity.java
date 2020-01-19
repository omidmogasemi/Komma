package com.hackdavis.komma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private Button createAccountButton;
    private EditText userName, userEmail, userPassword;
    private TextView alreadyHaveAccountLink;

    private FirebaseAuth mAuth;

    private ProgressDialog loadingBar;

    private FirebaseFirestore db;

    private String name, email, password;

    /* Establishes the user password pattern, which must not be:
     * less than 8 characters, does not have at least 1 alphabet, 1 number, and 1 special character *
     * Establishes the user email pattern, which must contain:/
     * A character, an @, a character, a ., then a character.
     */
    final private String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
    final private String EMAIL_PATTERN = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // connect to the Firebase authentication server
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        initalizeFields();

        // if the user clicks the "already have account" text --> send them to the login activity
        alreadyHaveAccountLink.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sendUserToLoginActivity();
            }
        });

        /* if the user clicks the "create account" button --> try to create their account,
           log them in, and send them to the Main Activity */
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createNewAccount();
            }
        });
    }

    private void createNewAccount() {
        // take the text from the email & pw fields upon the submit button being pressed
        name = userName.getText().toString();
        email = userEmail.getText().toString();
        password = userPassword.getText().toString();

        /* USER INPUT ERROR CHECKING SEQUENCE CURRENTLY BROKEN -
         * Reset the fields and do not advance the user past activity_register if:
         * their entered text is empty for either email or pw
         * they do not provide a valid email address (must contain an @ and a .)
         * their PW is less than 8 characters, does not have at least 1 alphabet, 1 number, and 1 special character
         * IS THIS A PROBLEM THAT I'M DEALING WITH THE PASSWORD IN PLAIN TEXT?
         */
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please enter your name...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter your email...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter your password...", Toast.LENGTH_SHORT).show();
        }
        else if(!isEmailValid(email)) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
        }
        else if (!isPasswordValid(password)) {
            Toast.makeText(this, "Please ensure your password contains at least " +
                            "8 characters and has at least 1 alphabetic, 1 numberic, and 1 special character)",
                    Toast.LENGTH_LONG).show();
        }
        // if the user has given valid input - create their account
        else {
            // creates the loading bar
            loadingBar.setTitle("Creating New Account");
            loadingBar.setMessage("Please wait while we are creating your account for you...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            /* Communicates with the Firebase Authentication server to try and register the user
             * If successful the user will be sent back to the main activity with a success message
             * If un-successful the user will be prompted to log in again with a failure message
             */
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                createAccountInDatabase(name);
                            }
                            else {
                                String message = task.getException().toString();
                                Toast.makeText(RegisterActivity.this, "Error: " + message, Toast.LENGTH_LONG).show();
                                loadingBar.dismiss();
                            }
                        }
                    });
        }
    }

    // checks if the user's entered password is valid according to the specified Email Pattern
    private boolean isEmailValid(String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // checks if the user's entered password is valid according to the specified Password Pattern
    private boolean isPasswordValid(String password) {
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    private void createAccountInDatabase(String n) {
        String currentUserID = mAuth.getCurrentUser().getUid();
        Log.d("HERE1", currentUserID);
        Log.d("NAME", n);

        Map<String, Object> newUser = new HashMap<>();
        newUser.put("NAME", n);
        Log.d("HERE2", "You are here.");

        db.collection("users").document(currentUserID).set(newUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("HERE3", "Bruh");
                        email = "";
                        password = "";
                        sendUserToMainActivityWithFlags();
                        Toast.makeText(RegisterActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, "Error: " + e.toString(), Toast.LENGTH_LONG).show();
                    }
                });

    }

    // sends the user to the login activity via a new intent
    private void sendUserToLoginActivity() {
        // this is bad design as it adds another intent to the stack
        // need to come back and fix later once working, perhaps with finish()?
        Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(loginIntent);
    }

    private void sendUserToMainActivityWithFlags(){
        Intent settingsIntent = new Intent(RegisterActivity.this, MainActivity.class);
        settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(settingsIntent);
        finish();
    }

    // initializes the input fields and loading bar
    private void initalizeFields() {
        createAccountButton = (Button)findViewById(R.id.register_button);
        userName = (EditText)findViewById(R.id.register_name);
        userEmail = (EditText)findViewById(R.id.register_email);
        userPassword = (EditText)findViewById(R.id.register_password);
        alreadyHaveAccountLink = (TextView)findViewById(R.id.already_have_account_link);
        loadingBar = new ProgressDialog(this);
    }
}