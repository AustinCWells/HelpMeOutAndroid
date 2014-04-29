package com.example.helpmeout;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class TimeActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TaskTimePicker.createAndShow(getFragmentManager());
    }
    
    public void onTimeSelected(final int hour, final int minute) {
        Toast.makeText(this, hour + ":" + minute, Toast.LENGTH_LONG).show();
    }
}
