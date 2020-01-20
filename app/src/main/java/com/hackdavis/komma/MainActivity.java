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
import android.widget.ListView;
import android.widget.TextView;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeFields();


        // Construct the data source
        ArrayList<MyEvent> arrayOfUsers = new ArrayList<MyEvent>();

        UsersAdapter adapter = new UsersAdapter(this, arrayOfUsers);

        ListView listView = (ListView) findViewById(R.id.main_listView);
        listView.setAdapter(adapter);

        MyEvent myEventtwo = new MyEvent("Culture Meeting", "Talk about Culture", "10:00", "Jan 22", 12, "Haring Hall");
        adapter.add(myEventtwo);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser == null)
            sendUserToLoginActivityWithFlags();
        //comment
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

    public void quickSort(int arr[], int begin, int end) {
        if (begin < end) {
            int partitionIndex = partition(arr, begin, end);
            quickSort(arr, begin, partitionIndex - 1);
            quickSort(arr, partitionIndex + 1, end);
        }
    }

    private int partition(int arr[], int begin, int end) {
        int pivot = arr[end];
        int i = (begin - 1);
        for (int j = begin; j < end; j++) {
            if (arr[j] <= pivot) {
                i++;
                int swapTemp = arr[i];
                arr[i] = arr[j];
                arr[j] = swapTemp;

            }
            int swapTemp = arr[i + 1];
            arr[i + 1] = arr[end];
            arr[end] = swapTemp;
        }
        return (i + 1);
    }
}
