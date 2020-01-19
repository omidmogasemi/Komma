package com.hackdavis.komma;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentActivity extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment,
                container, false);
        return view;
    }



    public void setNameText(String text) {
        TextView view = (TextView) getView().findViewById(R.id.name_View);
        view.setText(text);
    }

    public void setLocationText(String text) {
        TextView view = (TextView) getView().findViewById(R.id.location_View);
        view.setText(text);
    }

    public void setDescriptionText(String text) {
        TextView view = (TextView) getView().findViewById(R.id.description_View);
        view.setText(text);
    }

    public void numberGoingText(String text) {
        TextView view = (TextView) getView().findViewById(R.id.numberGoing_View);
        view.setText(text);
    }

    public void dateTimeText(String text) {
        TextView view = (TextView) getView().findViewById(R.id.dateTime_View);
        view.setText(text);
    }
}