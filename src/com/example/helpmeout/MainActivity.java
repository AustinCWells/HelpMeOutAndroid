package com.example.helpmeout;

import java.io.IOException;

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
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button mLoginButton = (Button) findViewById(R.id.loginButton);
		Button mSignupButton = (Button) findViewById(R.id.signupButton);
		mSignupButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toSignUp();
				
			}
		});
		mLoginButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				commenceLoginProcess(); 
				
			}
		});
	}
	private void toSignUp(){
		Intent intent = new Intent(this,SignUp.class); 
		startActivity(intent); 
	}

	public void commenceLoginProcess(){
		boolean loginInfoIsValid = isLoginInfoValid(); 
		if(loginInfoIsValid){
			Log.i("ACW","Loggin in"); 
			 new LoginUserTask().execute("http://107.170.79.251/HelpMeOut/api/login");
		}
		return; 
	}
	
	private boolean isLoginInfoValid(){
		EditText emailView = (EditText) findViewById(R.id.email);
		EditText passwordView = (EditText) findViewById(R.id.password);
		CharSequence email = emailView.getText();
		CharSequence password = passwordView.getText(); 
		
		// verify the input is valid 
		boolean setError = false; 
		if(!email.toString().contains("@")){
			emailView.setError( "Email must be well formed!" );
			setError = true; 
		}
		if ((password.length() < 8 || password.length() > 25)){
			passwordView.setError( "Password format must be between 8 and 25 characters." );
			setError = true; 
		}
		if(setError)
		{
			return false; 
		}
		return true;
	}
	
	private class LoginUserTask extends AsyncTask<String, Void, String> {
	    /** The system calls this to perform work in a worker thread and
	      * delivers it the parameters given to AsyncTask.execute() */
	    protected String doInBackground(String... urls) {
	        return logInUser(urls[0]);
	    }
	    
	    /** The system calls this to perform work in the UI thread and delivers
	      * the result from doInBackground() */
	    protected void onPostExecute(String loginResult) {
	    	boolean loginIsSuccess = checkIfLoginIsSuccess(loginResult); 
	    	if (loginIsSuccess){
	    		String userId = getUserId(loginResult); 
	    		toHomePage(userId); 
	    	} else {
	    		Context context = getApplicationContext();
				int duration = Toast.LENGTH_LONG;
				Toast toast;   
		    	toast = Toast.makeText(context, "Login information incorrect", duration);
			    toast.show();
	    	}
	    	Log.i("THREAD","Thread executed"); 
	    	
	    }
	    
	    private boolean checkIfLoginIsSuccess(String loginResult){
	    	JSONObject object;
	    	String query = null; 
	    	try {
				object = (JSONObject) new JSONTokener(loginResult).nextValue();
				query = object.getString("userID");

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	Log.i("JSON RETURNED",query);
	    	if(query.equalsIgnoreCase("0")){
	    		return false; 
	    	}
	    	return true; 
	    }
	    
	    private String getUserId(String loginResult){
	    	JSONObject object;
	    	String query = null; 
	    	try {
				object = (JSONObject) new JSONTokener(loginResult).nextValue();
				query = object.getString("userID");

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	return query;
	    }
	    
	    private void toHomePage(String userID){
			Intent intent = new Intent(MainActivity.this, HomePage.class);
			intent.putExtra("user_id", userID); 
			startActivity(intent); 
	    }
	    
	    private String logInUser(String url){
	   
	    	EditText emailView = (EditText) findViewById(R.id.email);
			EditText passwordView = (EditText) findViewById(R.id.password);
			CharSequence email = emailView.getText();
			CharSequence password = passwordView.getText();
			
	    	Context context = getApplicationContext();
			int duration = Toast.LENGTH_SHORT;
			Toast toast;
	    	// Send a HttpPost Request
			 HttpClient httpclient = new DefaultHttpClient();
			 HttpPost httppost = new HttpPost(url);
			 HttpResponse response = null;
			 HttpEntity entity = null;
			 JSONObject json = new JSONObject(); 
		 try {
			        // Add data
			 		json.put("email", email.toString());
			 		json.put("password", password.toString());
			 		StringEntity se = new StringEntity( json.toString()); 
			 		se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			 		httppost.setEntity(se); 

			        // Execute HTTP Post Request
			        response = httpclient.execute(httppost);
			        entity = response.getEntity();
			        


			      
			    } catch (ClientProtocolException e) {
			        // TODO Auto-generated catch block
			   } catch (IOException e) {
			        // TODO Auto-generated catch block
			   } catch (Exception e){
				   Log.e("MAIN_ACTIVITY", "exception thrown", e);
				   toast = Toast.makeText(context, e.toString(), duration);
			       toast.show();
			   }
		 	if(null != entity){
		 		try {
		 			 
		 			 return EntityUtils.toString(entity);
				} catch (ParseException e) {
					Log.i("ACW","MainActivity. Line 202. JSON Parse Exception");
					
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					Log.i("ACW","MainActivity. Lin 207. IOException"); 
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		 	}
		 	Log.i("ACW","MainActivity. Line 212. Request not parsed correctly"); 
		 	return "get failed"; 
	    }
	   
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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



		




}
