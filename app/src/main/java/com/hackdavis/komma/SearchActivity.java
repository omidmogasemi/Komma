package com.hackdavis.komma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    private static final String[] TAGS = new String[] {
            "Davis Computer Science Club", "History Club", "Philosophy Club", "Entrepreneurship Club", "Women in Computer Science"
    };
    private String text;
    private FirebaseFirestore db;
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

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, TAGS);
        final AutoCompleteTextView textView = (AutoCompleteTextView)findViewById(R.id.search_bar);
        textView.setThreshold(0);
        textView.setAdapter(adapter);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setThreshold(0);
                text = textView.getText().toString();
                populateEvents(text);
            }
        });
    }

    private void populateEvents(String text)
    {
        DocumentReference docRef = db.collection("events").document(text);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> map = new HashMap<>();
                        map = document.getData();
                        for (Map.Entry<String, Object> entry: map.entrySet()) {
                            Log.d("HERE", entry.getValue().toString());
                        }
                    }
                    else {}
                }
            }
        });
    }
    private void sendUserToHomeActivity(){
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
