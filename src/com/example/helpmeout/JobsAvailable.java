package com.example.helpmeout;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class JobsAvailable extends ActionBarActivity {
	JSONObject mJobsAvailable; 
	ArrayList<Job> mJobs; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jobs_available);
		mJobsAvailable = getJSONforTest(); 
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
				//get a specific category and fill the jobs array
				JSONtoJobs(getCategoryJobs("laundry")); 
				
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
	private void JSONtoJobs(JSONArray jobs){
		if(jobs != null){
			try{
				Log.i("ACW","Jobs in array: " + jobs.length());
				for(int i = 0; i < jobs.length(); i++) {
					JSONObject currentJob = jobs.getJSONObject(i);
					Integer task_id = currentJob.getInt("task_id");
					String fName = currentJob.getString("fName");
					String lName = currentJob.getString("lName");
					String short_description = currentJob.getString("short_description"); 
					String note = currentJob.getString("note");
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
