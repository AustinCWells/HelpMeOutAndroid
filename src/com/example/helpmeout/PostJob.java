package com.example.helpmeout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;

public class PostJob extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_job);
		
		
		View child = getLayoutInflater().inflate(R.layout.custom_date_picker, null);
		ViewGroup viewGroup = (ViewGroup) findViewById(R.id.customDatePickerView); 
		viewGroup.addView(child);
		
		
		Spinner categoriesSpinner = (Spinner) findViewById(R.id.categorySpinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.jobCategories, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		categoriesSpinner.setAdapter(adapter); 
		
		Spinner paymentSpinner = (Spinner) findViewById(R.id.paymentAmountSpinner);
		ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.payment_array, android.R.layout.simple_spinner_item);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		paymentSpinner.setAdapter(adapter2); 
		
		Spinner negotiableSpinner = (Spinner) findViewById(R.id.negotiableSpinner);
		ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.negotiable_array, android.R.layout.simple_spinner_item);
		adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		negotiableSpinner.setAdapter(adapter3); 
		Button homeButton = (Button) findViewById(R.id.homeButton);
		Button submitButton = (Button) findViewById(R.id.submitButton);
		homeButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				goHome();
				
			}
		});
		submitButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			initiateJobSubmission(); 
				
			}
		});
	}
	
	private void initiateJobSubmission(){
		boolean isValidJob = checkIfValidJob();
	}
	
	private boolean checkIfValidJob(){
		
		return false; 
	}
	
	private void goHome(){
		Intent intent = new Intent(this,HomePage.class);
		startActivity(intent); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.post_job, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/* Code for preventing scroll problems from https://groups.google.com/forum/?fromgroups#!topic/android-developers/FkSfJI6dH8w */
	public class CustomDatePicker extends DatePicker
	{
	    public CustomDatePicker(Context context, AttributeSet attrs, int
	defStyle)
	    {
	        super(context, attrs, defStyle);
	    }

	    public CustomDatePicker(Context context, AttributeSet attrs)
	    {
	        super(context, attrs);
	    }

	    public CustomDatePicker(Context context)
	    {
	        super(context);
	    }

	    @Override
	    public boolean onInterceptTouchEvent(MotionEvent ev)
	    {
	        /* Prevent parent controls from stealing our events once we've
	gotten a touch down */
	        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN)
	        {
	            ViewParent p = getParent();
	            if (p != null)
	                p.requestDisallowInterceptTouchEvent(true);
	        }

	        return false;
	    }
	}


}
