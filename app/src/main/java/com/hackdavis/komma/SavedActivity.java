package com.hackdavis.komma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SavedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);
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
        Intent homeIntent = new Intent(SavedActivity.this, MainActivity.class);
        startActivity(homeIntent);
    }

    private void sendUserToSearchActivity() {
        Intent searchIntent = new Intent(SavedActivity.this, SearchActivity.class);
        startActivity(searchIntent);
    }

    private void sendUserToAddActivity() {
        Intent addIntent = new Intent(SavedActivity.this, AddEventActivity.class);
        startActivity(addIntent);
    }

    private void sendUserToSavedActivity() {
        return;
    }
}
