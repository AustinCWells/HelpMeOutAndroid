package com.example.helpmeout;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

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
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HomePage extends ActionBarActivity {
	public static String mUserId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_page);
		Button mPostJob, mGetJob, mProfile, mMyJobs, mSignOut, mJobsINeedDone, mJobsImDoing;
		TextView mUserName;
		mJobsINeedDone = (Button) findViewById(R.id.jobsINeedDoneButton);
		mJobsImDoing = (Button) findViewById(R.id.jobsImDoingButton);
		mGetJob = (Button) findViewById(R.id.getJobButton);
		mPostJob = (Button) findViewById(R.id.postJobButton);
		mProfile = (Button) findViewById(R.id.profileButton);
		mSignOut = (Button) findViewById(R.id.signOutButton);
		mUserName = (TextView) findViewById(R.id.name);

		Intent i = getIntent();
		mUserId = i.getStringExtra("user_id");

		Log.i("ACW", "Executing useraccount info");
		new displayUserInfoTask()
				.execute("http://107.170.79.251/HelpMeOut/api/useraccount");
		mSignOut.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				toMainPage();

			}
		});
		mJobsINeedDone.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				toJobsINeedDone();

			}
		});
		mJobsImDoing.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				toJobsImDoing();

			}
		});
		mProfile.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				toProfile();

			}
		});
		mPostJob.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				toPostJob();

			}
		});
		mGetJob.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				toAvailableJobs();

			}
		});

	}

	private void toMainPage() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

	private void toJobsINeedDone() {
		Intent intent = new Intent(this, DisplayJobsINeedDone.class);
		startActivity(intent);
	}
	private void toJobsImDoing() { 
		Intent intent = new Intent(this, DisplayJobsImDoing.class);
		startActivity(intent);
		
	}

	private void toProfile() {
		Intent intent = new Intent(this, ProfilePage.class);
		startActivity(intent);
	}

	private void toAvailableJobs() {
		Intent intent = new Intent(this, JobsAvailable.class);
		startActivity(intent);
	}

	private void toPostJob() {
		Intent intent = new Intent(this, PostJob.class);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_page, menu);
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

	private class displayUserInfoTask extends AsyncTask<String, Void, String> {
		/**
		 * The system calls this to perform work in a worker thread and delivers
		 * it the parameters given to AsyncTask.execute()
		 */
		@Override
		protected String doInBackground(String... urls) {

			String userId;

			userId = getUserId();
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(urls[0]);
			HttpResponse response = null;
			HttpEntity entity = null;
			JSONObject json = new JSONObject();
			try {
				// Add data
				json.put("user_id", getUserId());
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

			updateUserInformation(userInformation);

			return;

		}

		private void updateUserInformation(String userInformation) {

			JSONObject object;
			try {
				object = (JSONObject) new JSONTokener(userInformation)
						.nextValue();
				String fName = object.getString("first_name");
				String lName = object.getString("last_name");
				String tokens = object.getString("tokens");
				String email = object.getString("email"); 
				TextView nameView = (TextView) findViewById(R.id.name);
				TextView tokensView = (TextView) findViewById(R.id.tokens);
				TextView emailView = (TextView) findViewById(R.id.email);

				nameView.setText("Name: " + fName + " " + lName);
				tokensView.setText("Tokens: " + tokens);
				emailView.setText("Email: " + email);
				// #TODO update with rating info not user info

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return;
		}

		private String getUserId() {
			Log.i("ACW", "getting users ID");
			Intent i = getIntent();
			String id = i.getStringExtra("user_id");

			return id;
		}
	}

	private static String getResponseText(InputStream stream) {
		// shortcut for getting entire stream:
		// http://weblogs.java.net/blog/pat/archive/2004/10/stupid_scanner_1.html
		Scanner scanner = new Scanner(stream);
		final String result = scanner.useDelimiter("\\A").next();
		scanner.close();
		return result;
	}

}
