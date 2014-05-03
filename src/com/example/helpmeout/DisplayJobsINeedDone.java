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

public class DisplayJobsINeedDone extends ActionBarActivity {
	JSONArray mJobsINeedDone;
	ArrayList<JobINeedDone> mJobs;
	Context mContext;
	String mCurrentId;
	String mTaskId; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_jobs_i_need_done);
		mJobs = new ArrayList<JobINeedDone>();
		mContext = this;
		new GetJobsINeedDoneTask().execute("http://107.170.79.251/HelpMeOut/api/getMyTasksAndPendingOffers");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_my_jobs, menu);
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

	private class AcceptDeclineCancelJobTask extends AsyncTask<String, Void, String> {
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
			return;

		}
	}

	private class GetJobsINeedDoneTask extends AsyncTask<String, Void, String> {
		/**
		 * The system calls this to perform work in a worker thread and delivers
		 * it the parameters given to AsyncTask.execute()
		 */
		@Override
		protected String doInBackground(String... urls) {
			String userId = HomePage.mUserId;
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(urls[0] + "/" + userId);
			HttpResponse response = null;
			HttpEntity entity = null;

			try {

				// Execute HTTP Post Request
				response = httpclient.execute(httpget);
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
			Log.i("ACW","Information for jobs I need done " + userInformation.toString());
			try {

				mJobsINeedDone = new JSONArray(userInformation);
				loadJobs(mJobsINeedDone);
				JobsINeedDoneAdapter adapter = new JobsINeedDoneAdapter(mContext,mJobs);
				ListView listView = (ListView) findViewById(R.id.myListView);
				listView.setAdapter(adapter);
				
				TextView noJobs = (TextView) findViewById(R.id.noJobs);
				if(mJobs.size() > 0){
					noJobs.setVisibility(View.GONE);
				} else {
					noJobs.setVisibility(View.VISIBLE);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Log.i("ACW", "JSON exception. :(" + e);
				e.printStackTrace();
			}

			


		}
	}

	public class JobINeedDone {

		boolean is_offer_for_help;
		Integer offer_id;
		Integer task_id;
		Integer begger_id;
		Integer price;
		Integer category_id;
		String short_description;
		String location;
		String date_posted;
		Integer chooser_id;
		String chooser_fName;
		String chooser_lName;
		String chooser_speed;
		String chooser_reliability;
		String notes;
		String time_frame_time;
		String time_frame_date;
		String is_custom;
		String custom_image_path;
		String contact_number;

		JobINeedDone(boolean is_offer_for_help, Integer offer_id,
				Integer task_id, Integer begger_id, Integer price,
				Integer category_id, String short_description, String location,
				String date_posted, Integer chooser_id, String chooser_fName,
				String chooser_lName, String chooser_speed,
				String chooser_reliability, String notes,
				String time_frame_time, String time_frame_date,
				String is_custom, String custom_image_path, String contact_number) {

			this.is_offer_for_help = is_offer_for_help;
			this.offer_id = offer_id;
			this.task_id = task_id;
			this.begger_id = begger_id;
			this.price = price;
			this.category_id = category_id;
			this.short_description = short_description;
			this.location = location;
			this.date_posted = date_posted;
			this.chooser_id = chooser_id;
			this.chooser_fName = chooser_fName;
			this.chooser_lName = chooser_lName;
			this.chooser_speed = chooser_speed;
			this.chooser_reliability = chooser_reliability;
			this.notes = notes;
			this.time_frame_time = time_frame_time;
			this.time_frame_date = time_frame_date;
			this.is_custom = is_custom;
			this.custom_image_path = custom_image_path;
			this.contact_number = contact_number;

		}
	}

	private void loadJobs(JSONArray jobs) {
		try {

			for (int i = 0; i < jobs.length(); i++) {

				JSONObject currentJob = jobs.getJSONObject(i);
				Log.i("ACW2",currentJob.toString());
				int intOffer = currentJob.getInt("is_offer_for_help");
				boolean is_offer_for_help;

				Integer offer_id;
				Integer task_id;
				Integer begger_id;
				Integer price;
				Integer category_id;
				String short_description;
				String location;
				String date_posted;
				Integer chooser_id;
				String chooser_fName;
				String chooser_lName;
				String chooser_speed;
				String chooser_reliability;
				String notes;
				String time_frame_time;
				String time_frame_date;
				String is_custom;
				String custom_image_path;
				String contact_number; 
				String contact_email;

				if (intOffer == 0)
					is_offer_for_help = false;
				else
					is_offer_for_help = true;

				task_id = currentJob.getInt("task_id");
				begger_id = currentJob.getInt("beggar_id");
				price = currentJob.getInt("price");
				category_id = currentJob.getInt("category_id");
				short_description = currentJob.getString("short_description");
				time_frame_time = currentJob.getString("time_frame_time");
				time_frame_date = currentJob.getString("time_frame_date");
				date_posted = currentJob.getString("date_posted");

				if (!is_offer_for_help) {
					location = currentJob.getString("location");
					notes = currentJob.getString("notes");
					offer_id = -1;
					chooser_id = currentJob.getInt("chooser_id");
					
					chooser_speed = "N/A";
					chooser_reliability = "N/A";
					is_custom = "N/A";
					custom_image_path = "N/A";
					if(chooser_id != 0){
						chooser_fName = currentJob.getString("chooser_fName");
						chooser_lName = currentJob.getString("chooser_lName");
						contact_number = currentJob.getString("contact_phone");
						contact_email = currentJob.getString("contact_email");
					}
					else {
						chooser_fName = "N/A";
						chooser_lName = "N/A";
						contact_number = "N/A"; 
						contact_email = "N/A"; 
					}
					
				} else {
					chooser_fName = currentJob.getString("chooser_fName");
					chooser_lName = currentJob.getString("chooser_lName");
					contact_number = "N/A"; 
					contact_email = "N/A"; 
					location = "N/A";
					notes = "N/A";
					offer_id = currentJob.getInt("offer_id");
					chooser_id = currentJob.getInt("chooser_id");
					
					chooser_speed = currentJob.getString("chooser_speed");
				
					chooser_reliability = currentJob
							.getString("chooser_reliability");
					is_custom = currentJob.getString("is_custom");
					custom_image_path = currentJob
							.getString("custom_image_path");
				}
				Log.i("ACW",
						"Adding! " + "New: " + offer_id.toString() + "New: "
								+ task_id.toString() + "New: "
								+ begger_id.toString() + "New: "
								+ price.toString() + "New: " + category_id.toString()
								+ "New: " + short_description + "New: "
								+ location + "New: " + date_posted + "New: "
								+ chooser_id.toString() + "New: "
								+ chooser_fName + "New: " + chooser_lName
								+ "New: " + chooser_speed + "New: "
								+ chooser_reliability + "New: " + notes
								+ "New: " + time_frame_time + "New: "
								+ time_frame_date + "New: " + is_custom
								+ "New: " + custom_image_path);
				mJobs.add(new JobINeedDone(is_offer_for_help, offer_id,
						task_id, begger_id, price, category_id,
						short_description, location, date_posted, chooser_id,
						chooser_fName, chooser_lName, chooser_speed,
						chooser_reliability, notes, time_frame_time,
						time_frame_date, is_custom, custom_image_path, contact_number));

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public class JobsINeedDoneAdapter extends ArrayAdapter<JobINeedDone> {
		private ArrayList<JobINeedDone> mAdapterJobs;
		private Context context;

		public JobsINeedDoneAdapter(Context context,
				ArrayList<JobINeedDone> jobs) {
			super(context, R.layout.job_i_need_done_item, jobs);
//			Log.i("ACW", "Constructing view");
			this.mAdapterJobs = jobs;
			this.context = context;
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View v;
//			Log.i("ACW", "Getting View!");
			if (convertView == null) {
				LayoutInflater inflater = LayoutInflater.from(mContext);
				v = inflater.inflate(R.layout.job_i_need_done_item, null);
			} else {
				v = convertView;
			}
			// FILL CONTENT 
			TextView shortDescriptionView = (TextView) v.findViewById(R.id.short_description);
			TextView contactNumberView = (TextView) v.findViewById(R.id.contact_number);
			TextView priceView = (TextView) v.findViewById(R.id.price); 
			TextView locationView = (TextView) v.findViewById(R.id.location);
			TextView chooserNameView = (TextView) v.findViewById(R.id.chooser_name);
			TextView chooserSpeedView = (TextView) v.findViewById(R.id.chooser_speed);
			TextView chooserReliabilityView = (TextView) v.findViewById(R.id.chooser_reliability);
			TextView notesView = (TextView) v.findViewById(R.id.notes);
			TextView timeFrameDateView = (TextView) v.findViewById(R.id.time_frame_date);
			TextView notesTitle = (TextView) v.findViewById(R.id.notesTitle);
			TextView helperTitle = (TextView) v.findViewById(R.id.helper);
			Button completedButton = (Button) v.findViewById(R.id.completedButton);
			Button cancelButton = (Button) v.findViewById(R.id.cancelButton);
			Button acceptButton = (Button) v.findViewById(R.id.acceptButton);
			Button declineButton = (Button) v.findViewById(R.id.declineButton); 
			
			final JobINeedDone job = mAdapterJobs.get(position);
			
			
			if (job.is_offer_for_help) {
				v.setBackgroundColor(getResources().getColor(R.color.lightBlue));
				
				priceView.setVisibility(View.GONE);
				helperTitle.setVisibility(View.GONE);
				shortDescriptionView.setText("OFFER: " + job.short_description);
				notesTitle.setVisibility(View.GONE);
				chooserNameView.setVisibility(View.VISIBLE);
				chooserNameView.setText(job.chooser_fName + " "+ job.chooser_lName);
				chooserSpeedView.setVisibility(View.VISIBLE);
				chooserSpeedView.setText("Speed: " + job.chooser_speed + "/100");
				chooserReliabilityView.setVisibility(View.VISIBLE);
				chooserReliabilityView.setText("Reliability: " + job.chooser_reliability + "/100");
				contactNumberView.setVisibility(View.GONE);
				locationView.setVisibility(View.GONE);
				notesView.setVisibility(View.GONE);
				timeFrameDateView.setVisibility(View.GONE);
				completedButton.setVisibility(View.GONE);
				cancelButton.setVisibility(View.GONE);
				acceptButton.setVisibility(View.VISIBLE);
				declineButton.setVisibility(View.VISIBLE); 

			} else {
				priceView.setVisibility(View.VISIBLE);
				priceView.setText("$" + job.price.toString() + ".00");
				v.setBackgroundColor(getResources().getColor(R.color.blue));
				
				helperTitle.setVisibility(View.VISIBLE);
								shortDescriptionView.setText(job.short_description);
				notesTitle.setVisibility(View.VISIBLE);
				locationView.setVisibility(View.VISIBLE);
				locationView.setText(job.location);
				notesView.setVisibility(View.VISIBLE);
				notesView.setText(job.notes);
				timeFrameDateView.setVisibility(View.VISIBLE);
				timeFrameDateView.setText("due by " + job.time_frame_date + " at " + job.time_frame_time);
				chooserNameView.setVisibility(View.GONE);
				chooserSpeedView.setVisibility(View.GONE);
				chooserReliabilityView.setVisibility(View.GONE);
				
				cancelButton.setVisibility(View.VISIBLE);
				acceptButton.setVisibility(View.GONE);
				declineButton.setVisibility(View.GONE); 
				
				//If someone is helping
				if(job.chooser_id != 0)
				{
					completedButton.setVisibility(View.VISIBLE);;
					helperTitle.setText(job.chooser_fName + " " + job.chooser_lName + " is helping you");
					contactNumberView.setVisibility(View.VISIBLE);
					contactNumberView.setText(job.contact_number);
				} else {
					completedButton.setVisibility(View.GONE);;
					helperTitle.setText("No one is helping you");
					contactNumberView.setVisibility(View.GONE);
				}
					
				
			}
			final Integer taskForEvent = job.task_id; 
			final String helpersName = job.chooser_fName + " " + job.chooser_lName; 
			final String chooserId = job.chooser_id.toString(); 
			final String beggerId = job.begger_id.toString(); 
			final String taskId = job.task_id.toString(); 
			final JobsINeedDoneAdapter currentContext = this;
			//PUT CLICK LISTENERS ON BUTTONS 
			Button completeTaskButton = (Button) v.findViewById(R.id.completedButton);
			Button cancelTaskButton = (Button) v.findViewById(R.id.cancelButton);
			Button acceptTaskButton = (Button) v.findViewById(R.id.acceptButton);
			Button declineTaskButton = (Button) v.findViewById(R.id.declineButton) ;
			completeTaskButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext, TaskComplete.class);
					currentContext.remove(job);
					intent.putExtra("job_id", taskForEvent);
					startActivity(intent); 
					
				}
			});
			acceptTaskButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mCurrentId = chooserId;
					mTaskId = taskId; 
					new AcceptDeclineCancelJobTask().execute("http://107.170.79.251/HelpMeOut/api/acceptOffer");
					currentContext.remove(job);
					int duration = Toast.LENGTH_LONG;
					String message = "You accepted " + helpersName + "'s offer for help!";
					Toast toast;   
			    	toast = Toast.makeText(mContext, message , duration);
				    toast.show();
				  
					
				}
			});
			declineTaskButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mCurrentId = chooserId;
					mTaskId = taskId; 
					new AcceptDeclineCancelJobTask().execute("http://107.170.79.251/HelpMeOut/api/declineOffer");
					currentContext.remove(job);
					int duration = Toast.LENGTH_LONG;
					String message = "You've declined " + helpersName + "'s offer for help!";
					Toast toast;   
			    	toast = Toast.makeText(mContext, message , duration);
				    toast.show();
				

					
				}
			});
			cancelTaskButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mCurrentId = beggerId;
					mTaskId = taskId; 
					Log.i("ACW","ACCEPTING DECLINING AND CANCELING JOB TASK!"); 
					new AcceptDeclineCancelJobTask().execute("http://107.170.79.251/HelpMeOut/api/cancelTask");
					currentContext.remove(job);
					int duration = Toast.LENGTH_LONG;
					String message = "You've canceled your task!";
					Toast toast;   
			    	toast = Toast.makeText(mContext, message , duration);
				    toast.show();
					
				}
			});

			// #TODO execute CancelHelpTask and CompleteHelpTask

			return v;
		}
	}

}
