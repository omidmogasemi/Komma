package com.hackdavis.komma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class AddEventActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private EditText inputName, inputDescription;
    private static int day, month, year, hour, minute;
    private String tag;
    private static Button startDate, startTime;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        initFields();
    }

    private void initFields()
    {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        inputName = (EditText)findViewById(R.id.name);
        inputDescription = (EditText)findViewById(R.id.description);
        startDate = findViewById(R.id.start_date);
        startTime = findViewById(R.id.start_time);

        Spinner spinner = findViewById(R.id.tag_dropdown);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.tag_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
        tag = parent.getItemAtPosition(pos).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void send_data(View view) {
        String name = inputName.getText().toString();
        String description = inputDescription.getText().toString();

        final String currentUserID = mAuth.getCurrentUser().getUid();
        MyEvent event = new MyEvent(name, description, convertTime(hour, minute), convertDate(year, month, day), 0);
        String token = UUID.randomUUID().toString();

        Map<String, Object> newEvent = new HashMap<>();
        newEvent.put(token, event);
        db.collection("events").document(tag).set(newEvent, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AddEventActivity.this, "Event successfully added", Toast.LENGTH_SHORT);
                        sendUserToMainActivityWithFlags();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        public TimePickerFragment(){}
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int h = c.get(Calendar.HOUR);
            int m = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, h, m, DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int h, int m) {
            // Do something with the time chosen by the user
            hour = h;
            minute = m;
            startTime.setText(convertTime(hour, minute));
        }
    }
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        public DatePickerFragment(){}
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int d = c.get(Calendar.DAY_OF_MONTH);
            int m = c.get(Calendar.MONTH);
            int y = c.get(Calendar.YEAR);
            c.set(d, m, y);

            // Create a new instance of TimePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, 2020, 00, 19);
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            // Do something with the time chosen by the user
            day = dd;
            month = mm;
            year = yy;
            startDate.setText(convertDate(day, month, year));
        }
    }

    public void showTimePickerDialogStartDate(View view) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void showDatePickerDialogStartDate(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private static String convertDate(int dd, int mm, int yy) {
        String convertedDate = dd + "-" + mm + "-" + yy;
        return convertedDate;
    }

    private static String convertTime(int hh, int min) {
        String convertedTime = hh + ":" + min;
        return convertedTime;
    }

    private void sendUserToMainActivityWithFlags() {
        Intent mainIntent = new Intent(AddEventActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}