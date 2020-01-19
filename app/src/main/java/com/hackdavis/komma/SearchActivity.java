package com.hackdavis.komma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

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
        Intent homeIntent = new Intent(SearchActivity.this, MainActivity.class);
        startActivity(homeIntent);
    }

    private void sendUserToSearchActivity() {
        return;
    }

    private void sendUserToAddActivity() {
        Intent searchIntent = new Intent(SearchActivity.this, AddEventActivity.class);
        startActivity(searchIntent);
    }

    private void sendUserToSavedActivity() {
        Intent savedIntent = new Intent(SearchActivity.this, SavedActivity.class);
        startActivity(savedIntent);

    }

}
