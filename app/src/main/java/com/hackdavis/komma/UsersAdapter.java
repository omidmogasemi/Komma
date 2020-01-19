package com.hackdavis.komma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import java.util.ArrayList;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;

import org.w3c.dom.Text;

public class UsersAdapter extends ArrayAdapter<MyEvent> {
    public UsersAdapter(Context context, ArrayList<MyEvent> events) {
        super(context, 0, events);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        MyEvent myEvent = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_fragment, parent, false);
        }
        // Lookup view for the things
        TextView eventName = (TextView) convertView.findViewById(R.id.name_View);
        TextView eventDes = (TextView) convertView.findViewById(R.id.description_View);
        TextView numGoing = (TextView) convertView.findViewById(R.id.numberGoing_View);
        TextView time = (TextView) convertView.findViewById(R.id.dateTime_View);
        TextView location = (TextView) convertView.findViewById(R.id.location_View);
        // Populate the data into the template view using the data object

        eventName.setText(myEvent.getName());
        eventDes.setText(myEvent.getDescription());
        numGoing.setText(myEvent.getAttendees());
        time.setText(myEvent.getStartTime() + "\n" + myEvent.getStartDate());
        location.setText(myEvent.getLocation());

        // Return the completed view to render on screen
        return convertView;
    }
}
