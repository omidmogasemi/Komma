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
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    private static final String[] TAGS = new String[] {
            "Davis Computer Science Club", "History Club", "Philosophy Club", "Entrepreneurship Club", "Women in Computer Science"
    };
    private String text;
    private FirebaseFirestore db;
    private ImageView submitButton;

    Map<String, Object> map = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        db = FirebaseFirestore.getInstance();

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
        submitButton = (ImageView)findViewById(R.id.submit_image);
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("HI", "HEY");
                text = textView.getText().toString();
                populateEvents(text);
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("HI", "HEY");
                textView.setThreshold(0);
                text = textView.getText().toString();

                populateEvents(text);
            }
        });
    }

    private void populateEvents(String text)
    {
        //final DocumentReference docRef = db.collection("events").document(text);
        DocumentReference docRef = db.collection("events").document(text);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            /* The following code will pull the attendees value from each event
                               object that exists for each tag */
                            map = document.getData();
                            Log.d("1", map.toString());
                            String keys = map.keySet().toString();

                            Iterator it = map.entrySet().iterator();
                            while (it.hasNext()) {
                                Map.Entry pair = (Map.Entry)it.next();
                                Log.d("HERE", pair.getKey() + " = " + pair.getValue());
                                // processValue(pair.getValue().toString()); <-- shows just the attendees value
                                sendEventToUI(pair.getValue().toString());
                                it.remove(); // avoids a ConcurrentModificationException
                            }
                    } else {
                        Log.d("TEST", "test");
                        }
                }
            }
        });
    }

    private void sendEventToUI(String s) {
        String eventName = processValue(s, )
        String eventDescription = ;
        String startDate = ;
        String startTime = ;
        String attendees = ;
    }

    private String processValue(String s) {
        return s.substring(11, 12);
    }

    private String processValue(String s, int start, int end) {
        return s.substring(start, end + 1);
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