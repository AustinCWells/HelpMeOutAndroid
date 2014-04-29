package com.example.helpmeout;



import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;


public class TaskTimePicker extends DialogFragment implements DialogInterface.OnClickListener {
    private static final String sFragmentTag = "CalendarPicker";
    private  TimePicker mTime;
    public static TaskTimePicker createAndShow(FragmentManager manager) {
        Fragment frag = manager.findFragmentByTag(sFragmentTag);
        if (null != frag) {
            // Already have an instance of this dialog, don't make another
            return (TaskTimePicker) frag;
        }
        TaskTimePicker df = new TaskTimePicker();
        df.show(manager, sFragmentTag);
        return df;
    }

    public static boolean isActive(FragmentManager manager) {
        return manager.findFragmentByTag(sFragmentTag) != null;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //Use our best friend LayoutInflater to inflate the date picker
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.time_picker, (ViewGroup) getView(), false);
        
        //Create a dialog with a done button
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
        .setPositiveButton("Done", this)
        .create();
        
        //Get today's date
        //Calendar calendar = Calendar.getInstance();
       // final int month = calendar.get(Calendar.MONTH);
        //final int day = calendar.get(Calendar.DAY_OF_MONTH);
        //final int year = calendar.get(Calendar.YEAR);
        
        mTime = (TimePicker) v.findViewById(R.id.time);

        //Put the view inside the dialog
        dialog.setView(v);
        dialog.setTitle("Pick a Time"); //Use a string from resources
        //Make the dialog close when you touch outside of it
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == Dialog.BUTTON_POSITIVE) {
            final int hour = mTime.getCurrentHour();
            final int minute = mTime.getCurrentMinute();
            
            //getActivity() will get the activity hosting this fragment. In this case we assume
            //its CalendarActivity and explicitly cast it. This could be bad if we're wrong
            ((PostJob)getActivity()).onTimeSelected(hour,minute); 
        }
        
    }
}
