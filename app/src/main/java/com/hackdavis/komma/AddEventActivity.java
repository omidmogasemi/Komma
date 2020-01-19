package com.hackdavis.komma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddEventActivity extends AppCompatActivity {
    private EditText inputName, inputDescription;
    private static int day, month, year, hour, minute;
    private String tag;

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

        Spinner dropdown = (Spinner) findViewById(R.id.tag_dropdown);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.tag_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
        Spinner spinner = (Spinner) parent;
        tag = parent.getItemAtPosition(pos).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void send_data(View view) {
        String name = inputName.getText().toString();
        String description = inputDescription.getText().toString();

        final String currentUserID = mAuth.getCurrentUser().getUid();
        MyEvent event = new MyEvent(inputName, inputDescription, startTime, startDate, endTime, endDate);
        String token = UUID.randomUUID().toString();

        Map<String, Object> newEvent = new HashMap<>();
        newEvent.put(token, event);
        db.collection("events").document(tag).set(newEvent, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

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

            // Create a new instance of TimePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, d, m, y);
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            // Do something with the time chosen by the user
            day = dd;
            month = mm;
            year = yy;
        }
    }

    public void showTimePickerDialogStartDate(View view) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void showTimePickerDialogEndDate(View view) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void showDatePickerDialogStartDate(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showDatePickerDialogEndDate(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
}
