package com.example.helpmeout;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

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
import android.widget.RatingBar;
import android.widget.Toast;

public class TaskComplete extends ActionBarActivity {
	Integer mJobId; 
	Context mThis;
	Integer mReliabilityRating;
	Integer mSpeedRating; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_complete);
		Intent intent = getIntent(); 
		mJobId = intent.getIntExtra("job_id", -1);
		mThis = this; 
		
		Button submitButton = (Button) findViewById(R.id.submitButton);
		submitButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				RatingBar speedRatingBar = (RatingBar) findViewById(R.id.speedRating);
				RatingBar reliabilityRatingBar = (RatingBar) findViewById(R.id.reliabilityRating);
				mReliabilityRating = (int) reliabilityRatingBar.getRating();
				mSpeedRating = (int) speedRatingBar.getRating(); 
				new SubmitRatingTask().execute("http://107.170.79.251/HelpMeOut/api/completeTask");
				Intent intent = new Intent(mThis,HomePage.class);
				intent.putExtra("user_id",HomePage.mUserId); 
				startActivity(intent); 
				int duration = Toast.LENGTH_LONG;
				Toast toast;   
		    	toast = Toast.makeText(mThis, "Your Task Has Been Completed!", duration);
			    toast.show();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.task_complete, menu);
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
	
	private class SubmitRatingTask extends AsyncTask<String, Void, String> {
		/**
		 * The system calls this to perform work in a worker thread and delivers
		 * it the parameters given to AsyncTask.execute()
		 */
		@Override
		protected String doInBackground(String... urls) {

			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httppost = new HttpGet(urls[0]+"/" + mJobId + "," + mSpeedRating + "," + mReliabilityRating);
			HttpResponse response = null;
			HttpEntity entity = null;
			
			try {
				// Execute HTTP Post Request
				response = httpclient.execute(httppost);
				entity = response.getEntity();	
				if (null != entity){
						return EntityUtils.toString(entity);
				}
				
			} catch (ClientProtocolException e) {
			} catch (IOException e) {
			} catch (Exception e) {
				Log.e("ACW", "HomePage. Line 188, Exception e: " + e);
			}
			
				
			
			Log.i("ACW", "MainActivity. Line 212. Request not parsed correctly");
			return "get failed";
		}

		/**
		 * The system calls this to perform work in the UI thread and delivers
		 * the result from doInBackground()
		 */
		protected void onPostExecute(String userInformation) {
			Log.i("ACW", "Posting successfull! " + userInformation);
		}
	}



}
