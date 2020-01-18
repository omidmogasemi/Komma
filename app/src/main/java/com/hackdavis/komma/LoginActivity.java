package com.hackdavis.komma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
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

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    private DatabaseReference rootRef;

    private Button loginButton;
    private EditText userEmail, userPassword;
    private TextView createAccountLink, forgotPasswordLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /* connect to the Firebase authentication server and attempt to authenticate the current user
           while gathering the database location of their user ID */
        mAuth = FirebaseAuth.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference();
        initializeFields();

        // if the user clicks the "Create an account" text --> send them to the register activity
        createAccountLink.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sendUserToRegisterActivity();
            }
        });

        /* if the user clicks the "login" button --> try to log them in and redirect them to the
           main activity */
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                allowUserToLogin();
            }
        });
    }

    /* USER INPUT ERROR CHECKING SEQUENCE -
     * Reset the fields and do not advance the user past activity_register if:
     * their entered text is empty for either email or pw
     */
    private void allowUserToLogin() {
        // take the text from the email & pw fields upon the submit button being pressed
        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter an email...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter a password...", Toast.LENGTH_SHORT).show();
        }
        else {
            // creates the loading bar
            loadingBar.setTitle("Signing in");
            loadingBar.setMessage("Please wait while we are signing you in...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            /* Communicates with the Firebase Authentication server to try and log the user in
             * If successful the user will be sent back to the main activity with a success message
             * If un-successful the user will be prompted to log in again with a failure message
             */
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // store unique data in Firebase Database
                                sendUserToMainActivityWithFlags();
                                Toast.makeText(LoginActivity.this, "Logged in Successfully...", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                            else {
                                String message = task.getException().toString();
                                Toast.makeText(LoginActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        }
                    });
        }
    }

    // sends the user to the Main activity via a new intent with flags that disable the back button
    private void sendUserToMainActivityWithFlags() {
        // this doesn't allow the user to go back and closes the current task - check later to see if this should be added everywhere
        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    // sends the user to the Register activity via a new intent
    private void sendUserToRegisterActivity() {
        // this is bad design as it adds another intent to the stack
        // need to come back and fix later once working, perhaps with finish()?
        Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(registerIntent);
    }

    // initializes the input fields and loading bar
    private void initializeFields() {
        loginButton = (Button)findViewById(R.id.login_button);
        userEmail = (EditText)findViewById(R.id.login_email);
        userPassword = (EditText)findViewById(R.id.login_password);
        createAccountLink = (TextView)findViewById(R.id.create_account_link);
        forgotPasswordLink = (TextView)findViewById(R.id.forgot_password_link);
        loadingBar = new ProgressDialog(this);
    }
}