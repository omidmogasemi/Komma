package com.hackdavis.komma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeFields();

        linearLayout = findViewById(R.id.linear_layout_main);

        for(int i = 0; i < 50; i++)
        {
            TextView textView = new TextView(this);
            textView.setText("Event: " + i + "\n Location: Haring Hall \n Date: January 25 \n Time: 8:00PM");
            linearLayout.addView(textView);
        }
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser == null)
            sendUserToLoginActivityWithFlags();

        BottomNavigationView nav = (BottomNavigationView) findViewById(R.id.nav_view);
        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        sendUserToHomeActivity();
                        return true;
                    case R.id.search:
                        sendUserToSearchActivity();
                        return true;
                    case R.id.add:
                        sendUserToAddActivity();
                        return true;

                    case R.id.saved:
                        sendUserToSavedActivity();
                        return true;
                }
                return false;
            }
        });
    }

    private void sendUserToHomeActivity() {
        return;
    }

    private void sendUserToSearchActivity() {
        Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
        startActivity(searchIntent);
    }

    private void sendUserToAddActivity() {
        Intent addIntent = new Intent(MainActivity.this, AddEventActivity.class);
        startActivity(addIntent);
    }

    private void sendUserToSavedActivity() {
        Intent savedIntent = new Intent(MainActivity.this, SavedActivity.class);
        startActivity(savedIntent);

    }

    private void sendUserToLoginActivityWithFlags() {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    private void initializeFields() {

    }

    public void onLogOutClick(View view) {
        mAuth.signOut();
        sendUserToLoginActivityWithFlags();
    }

    public void onHomeClick(View view) {

    }

    public void onPopularClick(View view) {
    }
}
