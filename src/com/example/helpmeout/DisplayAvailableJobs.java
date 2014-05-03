package com.example.helpmeout;

import java.io.IOException;
import java.util.ArrayList;

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

import com.example.helpmeout.JobsAvailable.Job;

public class DisplayAvailableJobs extends ActionBarActivity {
	Context mContext; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_display_available_jobs);
		mContext = this; 
		AvailableJobsAdapter adapter = new AvailableJobsAdapter(mContext, JobsAvailable.mJobs);
		ListView listView = (ListView) findViewById(R.id.myListView); 
		listView.setAdapter(adapter);
		
		TextView noJobs = (TextView) findViewById(R.id.noJobs);
		if(JobsAvailable.mJobs != null && JobsAvailable.mJobs.size() > 0){
			noJobs.setVisibility(View.GONE);
		} else {
			noJobs.setVisibility(View.VISIBLE);
		}
		 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.all_jobs, menu);
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
	
	public class AvailableJobsAdapter extends ArrayAdapter<JobsAvailable.Job>{
		private ArrayList<Job> jobs;
		private Context context;
		public AvailableJobsAdapter(Context context, ArrayList<Job> jobs){
			
			super(context, R.layout.job_item, jobs); 
			this.jobs = jobs;
			this.context = context; 
			 
	
		}
		
		@Override 
		public View getView(final int position, View convertView, ViewGroup parent) {
			
			View v;
			if (convertView == null) {
				LayoutInflater inflater = LayoutInflater.from(mContext); 
				v = inflater.inflate(R.layout.job_item, null); 
				
			} else {
				v = convertView;
			}
			Log.i("ACW", "3");
			TextView nameView = (TextView) v.findViewById(R.id.name);
			TextView shortDescriptionView = (TextView) v.findViewById(R.id.short_description);
			TextView notesView = (TextView) v.findViewById(R.id.notes);
			TextView priceView = (TextView) v.findViewById(R.id.price);
			TextView timeFrameDateView = (TextView) v.findViewById(R.id.time_frame_date);
			//TextView timeFrameTimeView = (TextView) v.findViewById(R.id.time_frame_time);
			TextView locationView = (TextView) v.findViewById(R.id.location);
			 
		
			Job job = jobs.get(position);
			
			nameView.setText(" " + job.fName + " " + job.lName);
			Log.i("ACW",job.short_description);
			shortDescriptionView.setText(job.short_description);
			notesView.setText(job.note);
			priceView.setText(" $" + job.price.toString() + ".00");
			timeFrameDateView.setText("Due on " + job.time_frame_date + " at " + job.time_frame_time);
			locationView.setText(" " + job.location);
			
			Button helpButton = (Button) v.findViewById(R.id.offerHelp);
			final String taskId = job.task_id.toString();
			final String userID = HomePage.mUserId;
			final String posterID = job.posterID;
			
			helpButton.setVisibility(View.VISIBLE);
			if(userID.equals(posterID)){
				helpButton.setVisibility(View.GONE);
			}
			helpButton.setOnClickListener(null);
			helpButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Log.i("ACW","Ids are " + userID + " " + posterID);
					if(userID.equals(posterID)){
						int duration = Toast.LENGTH_SHORT;
						Toast toast = Toast.makeText(context, "You cannot accept your own job", duration);
						toast.show();	
						return; 
					}
					new OfferHelpTask().execute(taskId); 
				}
			});
			
			return v;
			
		}
	}
	
	private class OfferHelpTask extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... taskId) {

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://107.170.79.251/HelpMeOut/api/makeOffer");

			JSONObject json = new JSONObject();
			try {
				// Add data
				json.put("user_id", HomePage.mUserId);
				json.put("task_id", taskId[0]);
				StringEntity se = new StringEntity(json.toString());
				se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
						"application/json"));
				httppost.setEntity(se);

				// Execute HTTP Post Request
				httpclient.execute(httppost);
				

			} catch (ClientProtocolException e) {
				Log.e("ACW", "Display Available Jobs. Line 154, Client Protocol Exception");
			} catch (IOException e) {
				Log.e("ACW", "Display Available Jobs. Line 156, IOException ");
			} catch (Exception e) {
				Log.e("ACW", "Display Available Jobs. Line 158, Exception e");
				Log.e("ACW", "exception thrown", e);

			}
		
			
			return taskId[0];
		}
		protected void onPostExecute(String value){
			Context context = getApplicationContext();
			CharSequence text = "Your Help has been offered! ";
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
