package com.example.helpmeout;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class JobsAvailable extends ActionBarActivity {
	public static JSONObject mJobsAvailable; 
	public static ArrayList<Job> mJobs; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jobs_available);
		new getJobsTask().execute("http://107.170.79.251/HelpMeOut/api/tasks");
		//mJobsAvailable = getJSONforTest(); 
		mJobs = new ArrayList<Job>(); 
		//get buttons for all jobs
		Button allJobs = (Button) findViewById(R.id.allJobsButton);
		Button foodJobs = (Button) findViewById(R.id.allJobsButton);
		Button laundryJobs = (Button) findViewById(R.id.laundryJobsButton);
		Button groceryJobs = (Button) findViewById(R.id.groceryJobsButton);
		Button cleaningJobs = (Button) findViewById(R.id.cleaningJobsButton);
		Button ridesJobs = (Button) findViewById(R.id.ridesJobsButton);
		Button techJobs = (Button) findViewById(R.id.techSupportJobsButton);
		Button maintenanceJobs = (Button) findViewById(R.id.maintenanceJobsButton);
		Button otherJobs = (Button) findViewById(R.id.otherJobsButton);
		
		// set click listeners for all jobs
		allJobs.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Log.i("ACW","Data to be sent to list view: " + mJobsAvailable.toString()); 
				
			}
		});
		laundryJobs.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				loadJobs(getCategoryJobs("laundry")); 
				
			}
		});
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.jobs_available, menu);
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
	
	private JSONArray getCategoryJobs(String categoryName){
		try {
			return mJobsAvailable.getJSONArray(categoryName);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null; 
	}
	
	private JSONObject getJSONforTest(){
		JSONObject object = new JSONObject(); 
		try{
			String json = "{\"laundry\":[{\"task_id\":1,\"fName\":\"austin\",\"lName\":\"wells\",\"short_description\":\"Thisisadescription\",\"note\":\"Hereisanote\",\"price\":5,\"time_frame_date\":\"12/23/34\",\"time_frame_time\":\"1:23\",\"location\":\"MyLocation\"},{\"task_id\":2,\"fName\":\"John\",\"lName\":\"Smith\",\"short_description\":\"Thisisadescription2\",\"note\":\"Hereisanote2\",\"price\":6,\"time_frame_date\":\"12/23/35\",\"time_frame_time\":\"1:25\",\"location\":\"MyLocation2\"}],\"food\":[{\"task_id\":3,\"fName\":\"bob\",\"lName\":\"wells\",\"short_description\":\"Thisisadescription\",\"note\":\"Hereisanote\",\"price\":5,\"time_frame_date\":\"12/23/34\",\"time_frame_time\":\"1:23\",\"location\":\"MyLocation\"},{\"task_id\":4,\"fName\":\"harly\",\"lName\":\"Smith\",\"short_description\":\"Thisisadescription2\",\"note\":\"Hereisanote2\",\"price\":6,\"time_frame_date\":\"12/23/35\",\"time_frame_time\":\"1:25\",\"location\":\"MyLocation2\"}]}";
			object = (JSONObject) new JSONTokener(json).nextValue();
			
		}
		catch(Exception e){
			Log.e("ACW","sample JSON failed"); 
		}
		return object; 
	}
	private void loadJobs(JSONArray jobs){
		if(jobs != null){
			try{
				Log.i("ACW","Jobs in array: " + jobs.length());
				for(int i = 0; i < jobs.length(); i++) {
					JSONObject currentJob = jobs.getJSONObject(i);
					Integer task_id = currentJob.getInt("task_id");
					String fName = currentJob.getString("first_name");
					String lName = currentJob.getString("last_name");
					String short_description = currentJob.getString("short_description"); 
					String note = currentJob.getString("notes");
					Integer price = currentJob.getInt("price"); 
					String time_frame_date = currentJob.getString("time_frame_date");
					String time_frame_time = currentJob.getString("time_frame_time");
					String location = currentJob.getString("location"); 
					Log.i("ACW","The item: " + task_id.toString() + " " + fName + " " + lName + " " + short_description + " " + note + " " + price.toString() + " "+ time_frame_date + " " + time_frame_time + " " + location); 
					mJobs.add(new Job(task_id,fName,lName,short_description,note,price,time_frame_date,time_frame_time,location));
							
				}
				Log.i("ACW","let's just see if anything crashes!");
			}
			catch (Exception e){
				Log.i("ACW","Exception when adding jobs: " + e); 
			}
		}else{
			Log.i("ACW", "JSON OBJECT NULL: Line 105");
		}
	}
	
	private class getJobsTask extends AsyncTask<String, Void, String> {
	    /** The system calls this to perform work in a worker thread and
	      * delivers it the parameters given to AsyncTask.execute() */
	    @Override
		protected String doInBackground(String... urls) {		
	    	HttpClient httpclient = new DefaultHttpClient();
			HttpGet httppost = new HttpGet(urls[0]);
			HttpResponse response = null;
			HttpEntity entity = null;
			try {
				// Execute HTTP Post Request
				response = httpclient.execute(httppost);
				entity = response.getEntity();
			} catch (ClientProtocolException e) {
			} catch (IOException e) {
			} catch (Exception e) {
				Log.e("ACW", "HomePage. Line 188, Exception e: " + e);
			}
			if (null != entity) {
				try {
					return EntityUtils.toString(entity);
				} catch (ParseException e) {
					Log.i("ACW", "MainActivity. Line 202. JSON Parse Exception");
					e.printStackTrace();
				} catch (IOException e) {
					Log.i("ACW", "MainActivity. Lin 207. IOException");
					e.printStackTrace();
				}
			}
			Log.i("ACW", "MainActivity. Line 212. Request not parsed correctly");
			return "get failed";		   
	    }
	    
	    /** The system calls this to perform work in the UI thread and delivers
	      * the result from doInBackground() */
	    protected void onPostExecute(String userInformation) {
	    	// #TODO 
	    	Log.i("ACW", "Info returned: " + userInformation);
	    	try {
				mJobsAvailable = (JSONObject) new JSONTokener(userInformation).nextValue();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	return;
	    	
	    }
	}
	

	
	public class Job{
		Integer task_id;
		String fName;
		String lName;
		String short_description;
		String note;
		Integer price;
		String time_frame_date;
		String time_frame_time;
		String location; 
		Job(Integer task_id,String fName,String lName,String short_description,String note,Integer price,String time_frame_date,String time_frame_time,String location){
			this.task_id = task_id;
			this.fName = fName; 
			this.lName = lName; 
			this.short_description = short_description; 
			this.note = note;
			this.price = price; 
			this.time_frame_date = time_frame_date; 
			this.time_frame_time = time_frame_time; 
			this.location = location; 
			
		}
	}


}
