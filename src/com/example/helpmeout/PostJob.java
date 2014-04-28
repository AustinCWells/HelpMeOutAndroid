package com.example.helpmeout;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

public class PostJob extends ActionBarActivity {
	Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_job);
		mContext = this; 
		
		//View child = getLayoutInflater().inflate(R.layout.custom_date_picker, null);
		//ViewGroup viewGroup = (ViewGroup) findViewById(R.id.customDatePickerView); 
		//viewGroup.addView(child);
		
		
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
		Button submitButton = (Button) findViewById(R.id.submitButton);
		Button timeButton = (Button) findViewById(R.id.timePicker);
		Button calendarButton = (Button) findViewById(R.id.datePicker);
		calendarButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				getDate();
				
			}
		});
		timeButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getTime();
				
			}
			
		});
		submitButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			//initiateJobSubmission(); 
				
			}
		});
	}
	
	public void getDate(){
		  CalendarPicker.createAndShow(getFragmentManager());
	}
	
	 public void onDateSelected(final int year, final int month, final int dayOfMonth) {
	        Toast.makeText(this, dayOfMonth + "/" + month + "/" + year, Toast.LENGTH_LONG).show();
	    }
	 
	public void getTime(){
		 TaskTimePicker.createAndShow(getFragmentManager());
	}
	
	 public void onTimeSelected(final int hour, final int minute) {
	        Toast.makeText(this, hour + ":" + minute, Toast.LENGTH_LONG).show();
	    }
	
    
   
	
	private void initiateJobSubmission(){
		new JobSubmitTask().execute("http://107.170.79.251/HelpMeOut/api/postatask");
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
	
	private class JobSubmitTask extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... urls) {
			Log.i("ACW","Here we go!");
			String userIdString = HomePage.mUserId;
			Spinner categorySpinner = (Spinner) findViewById(R.id.categorySpinner);
			Spinner paymentSpinner = (Spinner) findViewById(R.id.paymentAmountSpinner);
			EditText locationInput = (EditText) findViewById(R.id.meetingLocationInput);
			EditText descriptionInput = (EditText) findViewById(R.id.descriptionInput);
			EditText notesInput = (EditText) findViewById(R.id.notesInput);
			//TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
			DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);
		
			String category = new Integer(categorySpinner.getSelectedItemPosition()).toString();
			String userId = userIdString; 
			String payment = new Integer(paymentSpinner.getSelectedItemPosition()+3).toString(); 
			String location = locationInput.getText().toString();
			String description = descriptionInput.getText().toString();
			String notes = notesInput.getText().toString();
			//String time = timePicker.getCurrentHour().toString() + ":" + timePicker.getCurrentMinute().toString(); 
			String date = Integer.toString(datePicker.getYear()) + "-" + Integer.toString(datePicker.getDayOfMonth()) + "-" + Integer.toString(datePicker.getDayOfMonth());
			
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(urls[0]);
			HttpResponse response = null;
			HttpEntity entity = null;
			JSONObject json = new JSONObject();
			try {
				// Add data
				json.put("userID", userId);
				json.put("category", category);
				json.put("description", description);
				json.put("price", payment);
				json.put("location", location);
				//json.put("deadlineTime", time);
				json.put("deadlineDate", date);
				json.put("notes", notes);
				StringEntity se = new StringEntity(json.toString());
				se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
						"application/json"));
				httppost.setEntity(se);

				// Execute HTTP Post Request
				response = httpclient.execute(httppost);
				entity = response.getEntity();

			} catch (ClientProtocolException e) {
				Log.i("ACW", "PostJob. Line 199, Exception e: " + e);
			} catch (IOException e) {
				Log.i("ACW", "PostJob. Line 199, Exception e: " + e);
			} catch (Exception e) {
				Log.i("ACW", "PostJob. Line 199, Exception e");
				Log.i("ACW", "exception thrown", e);

			}
			return "post executed!";
		}
		
		 protected void onPostExecute(String jobInformation) {
			 Log.i("ACW","Job Posted: " + jobInformation);
			 Context context = getApplicationContext();
				CharSequence text = "Your job has been posted! ";
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();	
				Intent intent = new Intent(mContext,HomePage.class); 
				intent.putExtra("user_id", HomePage.mUserId);
				startActivity(intent);
				return;
		    	
		    }
		
	}
	
	 

}
