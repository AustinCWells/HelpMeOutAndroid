package com.example.helpmeout;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class JobsAvailable extends ActionBarActivity {
	public static JSONObject mJobsAvailable;
	public static ArrayList<Job> mJobs;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jobs_available);

		new getJobsTask().execute("http://107.170.79.251/HelpMeOut/api/tasks");
		// mJobsAvailable = getJSONforTest();
		mContext = this;
		mJobs = new ArrayList<Job>();
		// get buttons for all jobs
		Button allJobs = (Button) findViewById(R.id.allJobsButton);
		Button foodJobs = (Button) findViewById(R.id.foodJobsButton);
		Button laundryJobs = (Button) findViewById(R.id.laundryJobsButton);
		Button groceryJobs = (Button) findViewById(R.id.groceryJobsButton);
		Button cleaningJobs = (Button) findViewById(R.id.cleaningJobsButton);
		Button rideJobs = (Button) findViewById(R.id.ridesJobsButton);
		Button techJobs = (Button) findViewById(R.id.techSupportJobsButton);
		Button maintenanceJobs = (Button) findViewById(R.id.maintenanceJobsButton);
		Button otherJobs = (Button) findViewById(R.id.otherJobsButton);

		// set click listeners for all jobs
		otherJobs.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				loadJobs(getCategoryJobs("other"));
				Intent intent = new Intent(mContext, DisplayAvailableJobs.class);
				startActivity(intent);
			}
		});
		maintenanceJobs.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				loadJobs(getCategoryJobs("maintenance"));
				Intent intent = new Intent(mContext, DisplayAvailableJobs.class);
				startActivity(intent);
			}
		});
		techJobs.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				loadJobs(getCategoryJobs("techSupport"));
				Intent intent = new Intent(mContext, DisplayAvailableJobs.class);
				startActivity(intent);
			}
		});
		rideJobs.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				loadJobs(getCategoryJobs("rides"));
				Intent intent = new Intent(mContext, DisplayAvailableJobs.class);
				startActivity(intent);
			}
		});
		cleaningJobs.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				loadJobs(getCategoryJobs("cleaning"));
				Intent intent = new Intent(mContext, DisplayAvailableJobs.class);
				startActivity(intent);
			}
		});
		groceryJobs.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				loadJobs(getCategoryJobs("groceries"));
				Intent intent = new Intent(mContext, DisplayAvailableJobs.class);
				startActivity(intent);
			}
		});
		laundryJobs.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				loadJobs(getCategoryJobs("laundry"));
				Intent intent = new Intent(mContext, DisplayAvailableJobs.class);
				startActivity(intent);
			}
		});
		foodJobs.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				loadJobs(getCategoryJobs("food"));
				Intent intent = new Intent(mContext, DisplayAvailableJobs.class);
				startActivity(intent);
			}
		});
		allJobs.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Log.i("ACW",
						"Data to be sent to list view: "
								+ mJobsAvailable.toString());

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

	private JSONArray getCategoryJobs(String categoryName) {
		try {
			return mJobsAvailable.getJSONArray(categoryName);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private void loadJobs(JSONArray jobs) {
		if (jobs != null) {
			mJobs.clear();
			try {
				Log.i("ACW", "Jobs in array: " + jobs.length());
				for (int i = 0; i < jobs.length(); i++) {
					JSONObject currentJob = jobs.getJSONObject(i);
					Integer task_id = currentJob.getInt("task_id");
					String fName = currentJob.getString("first_name");
					String lName = currentJob.getString("last_name");
					String short_description = currentJob
							.getString("short_description");
					String note = currentJob.getString("notes");
					Integer price = currentJob.getInt("price");
					String time_frame_date = currentJob
							.getString("time_frame_date");
					String time_frame_time = currentJob
							.getString("time_frame_time");
					String posterID = "1"; //currentJob.getString(""); 
					String location = currentJob.getString("location");
					Log.i("ACW", "The item: " + task_id.toString() + " "
							+ fName + " " + lName + " " + short_description
							+ " " + note + " " + price.toString() + " "
							+ time_frame_date + " " + time_frame_time + " "
							+ location);
					mJobs.add(new Job(task_id, fName, lName, short_description,
							note, price, time_frame_date, time_frame_time,
							location, posterID));

				}
				Log.i("ACW", "let's just see if anything crashes!");
			} catch (Exception e) {
				Log.i("ACW", "Exception when adding jobs: " + e);
			}
		} else {
			Log.i("ACW", "JSON OBJECT NULL: Line 105");
		}
	}

	private class getJobsTask extends AsyncTask<String, Void, String> {
		/**
		 * The system calls this to perform work in a worker thread and delivers
		 * it the parameters given to AsyncTask.execute()
		 */
		@Override
		protected String doInBackground(String... urls) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(urls[0]);
			HttpResponse response = null;
			HttpEntity entity = null;
			JSONObject json = new JSONObject(); 
			try {
				// Execute HTTP Post Request
				json.put("user_id", HomePage.mUserId);
				StringEntity se = new StringEntity( json.toString()); 
		 		se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		 		httppost.setEntity(se); 
		 		
		 		//execute HTTP post request
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

		/**
		 * The system calls this to perform work in the UI thread and delivers
		 * the result from doInBackground()
		 */
		protected void onPostExecute(String userInformation) {
			// #TODO
			Log.i("ACW", "Info returned: " + userInformation);
			try {
				mJobsAvailable = (JSONObject) new JSONTokener(userInformation)
						.nextValue();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;

		}
	}

	public class Job {
		Integer task_id;
		String fName;
		String lName;
		String short_description;
		String note;
		Integer price;
		String time_frame_date;
		String time_frame_time;
		String location;
		String posterID; 

		Job(Integer task_id, String fName, String lName,
				String short_description, String note, Integer price,
				String time_frame_date, String time_frame_time, String location, String posterID) {
			this.task_id = task_id;
			this.fName = fName;
			this.lName = lName;
			this.short_description = short_description;
			this.note = note;
			this.price = price;
			this.time_frame_date = time_frame_date;
			this.time_frame_time = time_frame_time;
			this.location = location;
			this.posterID = posterID; 

		}
	}

}
