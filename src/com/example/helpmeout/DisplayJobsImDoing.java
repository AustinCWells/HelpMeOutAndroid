package com.example.helpmeout;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayJobsImDoing extends ActionBarActivity {
	JSONArray mJobsImDoing; 
	ArrayList<JobImDoing> mJobs;
	Context mContext;
	String mCurrentId;
	String mTaskId; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_jobs_im_doing);
		mJobs = new ArrayList<JobImDoing>(); 
		mContext = this; 
		Log.i("ACW","async task"); 
		new GetJobsImDoingTask().execute("http://107.170.79.251/HelpMeOut/api/tasksImDoing");


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_jobs_im_doing, menu);
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


	private class GetJobsImDoingTask extends AsyncTask<String, Void, String> {
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
				// Add data
				String userId = HomePage.mUserId;
				json.put("user_id", userId);
				StringEntity se = new StringEntity(json.toString());
				se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
						"application/json"));
				httppost.setEntity(se);

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

		/**
		 * The system calls this to perform work in the UI thread and delivers
		 * the result from doInBackground()
		 */
		protected void onPostExecute(String userInformation) {
			Log.i("ACW", "Jobs I'm Doing: " + userInformation);
			try {
				mJobsImDoing =  new JSONArray(userInformation); 
				loadJobs(mJobsImDoing);
				
				TextView noJobs = (TextView) findViewById(R.id.noJobs);
				if(mJobs.size() > 0){
					noJobs.setVisibility(View.GONE);
				} else {
					noJobs.setVisibility(View.VISIBLE);
				}
				
				JobsImDoingAdapter adapter = new JobsImDoingAdapter(mContext, mJobs); 
				ListView listView = (ListView) findViewById(R.id.myListView); 
				listView.setAdapter(adapter); 
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  //getJSONforTest(); 
			//			

		}
	}

	private void loadJobs(JSONArray jobs){
		try {
			
			for(int i = 0; i < jobs.length(); i++){
				JSONObject currentJob = jobs.getJSONObject(i); 
				String name = currentJob.getString("beggar_fName") + " " + currentJob.getString("beggar_lName");
				Integer job_id = currentJob.getInt("task_id");
				String contact_number = currentJob.getString("contact_number");
				String contact_email = currentJob.getString("contact_email");
				String short_description = currentJob.getString("short_description");
				Integer price = currentJob.getInt("price");
				String time_frame_date = currentJob.getString("time_frame_date");
				String time_frame_time = currentJob.getString("time_frame_time");
				String location = currentJob.getString("location");
				String notes = currentJob.getString("notes"); 
				Log.i("ACW3","Adding job: " + "1: " + name + " 2: " + job_id.toString() + " 3:" + contact_number + " 4:" + short_description + " 5: " + price.toString() + " 6: " + time_frame_date + " 7: " + time_frame_time + " 8: " + location + " :)" + notes); 
				mJobs.add(new JobImDoing(name,job_id,contact_number,contact_email,short_description,price,time_frame_date,time_frame_time,location,notes)); 
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	private JSONObject getJSONforTest() {
		JSONObject object = new JSONObject();
		try {
			String json = "{\"Jobs\":[{\"name\":\"AustinWells\",\"job_id\":\"1\",\"contact_number\":\"985-502-1061\",\"contact_email\":\"acwells@smu.edu\",\"short_description\":\"Thisisthedescription\",\"price\":5,\"time_frame_date\":\"4-20-14\",\"time_frame_time\":\"3:45\",\"location\":\"myplace\",\"notes\":\"helloboi\"},{\"name\":\"ChrisDunn\",\"job_id\":\"45\",\"contact_number\":\"985-34-1061\",\"contact_email\":\"cdunn@smu.edu\",\"short_description\":\"Thisisthedescription\",\"price\":20,\"time_frame_date\":\"4-24-14\",\"time_frame_time\":\"3:67\",\"location\":\"yourplace\",\"notes\":\"helloboi\"},{\"name\":\"SpencerBoi\",\"job_id\":\"9\",\"contact_number\":\"985-502-2345\",\"contact_email\":\"acwells@smu.edu\",\"short_description\":\"Thisisthedescription\",\"price\":5,\"time_frame_date\":\"4-23-14\",\"time_frame_time\":\"2:50\",\"location\":\"haiti\",\"notes\":\"whatareyoudoing\"}]}";
			object = (JSONObject) new JSONTokener(json).nextValue();

		} catch (Exception e) {
			Log.e("ACW", "sample JSON failed");
		}
		return object;
	}

	public class JobImDoing {

		String name;
		Integer job_id; 
		String contact_number;
		String contact_email;
		String short_description;
		Integer price;
		String time_frame_date;
		String time_frame_time;
		String location;
		String notes; 

		JobImDoing(String name, Integer job_id, String contact_number,
				String contact_email, String short_description, Integer price,
				String time_frame_date, String time_frame_time,
				String location, String notes) {
			this.name = name;
			this.job_id = job_id;
			this.contact_number = contact_number;
			this.short_description = short_description;
			this.price = price;
			this.time_frame_date = time_frame_date;
			this.time_frame_time = time_frame_time;
			this.location = location;
			this.notes = notes; 
		}
	}

	public class JobsImDoingAdapter extends ArrayAdapter<JobImDoing>{
		private ArrayList<JobImDoing> mAdapterJobs; 
		private Context context;
		public JobsImDoingAdapter(Context context, ArrayList<JobImDoing> jobs){	
			super(context,R.layout.job_im_doing_item,jobs); 
			Log.i("ACW","Constructing view"); 
			this.mAdapterJobs = jobs;
			this.context = context; 
		}

		public View getView(final int position, View convertView, ViewGroup parent){
			View v;
			Log.i("ACW","Getting View!"); 
			if(convertView == null){
				LayoutInflater inflater = LayoutInflater.from(mContext);
				v = inflater.inflate(R.layout.job_im_doing_item, null); 
			} else {
				v = convertView; 
			}

			TextView nameView = (TextView) v.findViewById(R.id.name); 
			TextView contactNumberView = (TextView) v.findViewById(R.id.contact_number); 
			TextView shortDescriptionView = (TextView) v.findViewById(R.id.short_description); 
			TextView priceView = (TextView) v.findViewById(R.id.price); 
			TextView timeFrameDateView = (TextView) v.findViewById(R.id.time_frame_date); 
			//TextView timeFrameTimeView = (TextView) v.findViewById(R.id.time_frame_time); 
			TextView locationView = (TextView) v.findViewById(R.id.location); 
			TextView notesView = (TextView) v.findViewById(R.id.notes); 
			
			final JobImDoing job = mAdapterJobs.get(position); 
			
			
			
			nameView.setText(job.name);
			contactNumberView.setText(job.contact_number);
			shortDescriptionView.setText( job.short_description);
			priceView.setText("$" + job.price.toString() + ".00");
			timeFrameDateView.setText("Due by " + job.time_frame_date + " at " + job.time_frame_time);
			//timeFrameTimeView.setText("End Time: " + job.time_frame_time);
			locationView.setText(job.location); 
			notesView.setText(job.notes);

			
			final String chooserId = HomePage.mUserId;
			final String taskId = job.job_id.toString();
			final JobsImDoingAdapter currentContext = this; 
			Button cancelTaskButton = (Button) v.findViewById(R.id.cancelJob);
			Log.i("ACW","Info" + chooserId + taskId);
			cancelTaskButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					mCurrentId = chooserId;
					mTaskId = taskId; 
					Log.i("ACW","ACCEPTING DECLINING AND CANCELING JOB TASK!"); 
					new CancelJobTask().execute("http://107.170.79.251/HelpMeOut/api/cancelTask");
					currentContext.remove(job);
					int duration = Toast.LENGTH_LONG;
					String message = "You've canceled your task!";
					Toast toast;   
			    	toast = Toast.makeText(mContext, message , duration);
				    toast.show();
					
				}
			});

			return v; 
		}
	}
	
	private class CancelJobTask extends AsyncTask<String, Void, String> {
		/**
		 * The system calls this to perform work in a worker thread and delivers
		 * it the parameters given to AsyncTask.execute()
		 */
		@Override
		protected String doInBackground(String... urls) {

			HttpClient httpclient = new DefaultHttpClient();
			Log.i("ACW","Canceling (" + mCurrentId + ","+mTaskId + ")"); 
			HttpGet httpget = new HttpGet(urls[0] + "/" + mCurrentId + "," + mTaskId);
			HttpResponse response = null;
			HttpEntity entity = null;
			try {
				// Add data
				

				// Execute HTTP Post Request
				response = httpclient.execute(httpget);
				entity = response.getEntity();

			} catch (ClientProtocolException e) {
			} catch (IOException e) {
			} catch (Exception e) {
				Log.e("ACW", "HomePage. Line 188, Exception e");
				Log.e("MAIN_ACTIVITY", "exception thrown", e);

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
			//Intent intent = new Intent(mContext, DisplayJobsImDoing.class); 
			//startActivity(intent); 
			return;

		}
	}


}
