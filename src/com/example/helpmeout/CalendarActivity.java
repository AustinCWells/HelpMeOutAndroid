package com.example.helpmeout;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class CalendarActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalendarPicker.createAndShow(getFragmentManager());
    }
    
    public void onDateSelected(final int year, final int month, final int dayOfMonth) {
        Toast.makeText(this, dayOfMonth + "/" + month + "/" + year, Toast.LENGTH_LONG).show();
    }
}
