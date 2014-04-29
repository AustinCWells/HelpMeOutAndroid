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
import org.json.JSONObject;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class SignUp extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		
		Spinner genderSpinner = (Spinner) findViewById(R.id.genderSpinner);
		 ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gender_array, android.R.layout.simple_spinner_item);
		 adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		 genderSpinner.setAdapter(adapter); 
		 
		 Button backButton = (Button) findViewById(R.id.backButton);
		 Button signupButton = (Button) findViewById(R.id.signUpButton);
		 
		 backButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toMainPage(); 
				
			}
		});
		 signupButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toHomePage(); 
				
			}
		});
		 
	}
	
	private void toHomePage(){
		
		// variables for toast
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		Toast toast;
		EditText firstNameView = (EditText) findViewById(R.id.firstName);
		EditText lastNameView = (EditText) findViewById(R.id.lastName);
		Spinner genderView = (Spinner) findViewById(R.id.genderSpinner);
		EditText phoneNumberView = (EditText) findViewById(R.id.phoneNumber);
		EditText emailView = (EditText) findViewById(R.id.emailAddress); 
		EditText passwordView = (EditText) findViewById(R.id.password);
		EditText confirmPassView = (EditText) findViewById(R.id.confirmPassword); 
		
		String firstName = firstNameView.getText().toString(); 
		String lastName = lastNameView.getText().toString();
		String gender = genderView.getSelectedItem().toString(); 
		String phoneNumber = phoneNumberView.getText().toString(); 
		String email = emailView.getText().toString();
		String password = passwordView.getText().toString(); 
		String confirmPass = confirmPassView.getText().toString();
		
		//Check for errors
		boolean isError = false; 
		if(firstName.matches(".*\\d.*") || firstName.length() > 20 || firstName.length() < 2){
			firstNameView.setError("Name must not contain numbers and must be between 2 and 10 characters");
			isError = true; 
		}
		if(lastName.matches(".*\\d.*") || lastName.length() > 10 || firstName.length() < 2){
			lastNameView.setError("Name must not contain numbers and must be between 2 and 10 characters");
			isError = true; 
		}
		if(gender == null)
		{
			isError = true; 
		}
		if(phoneNumber.matches(".*\\[a-Z].*") || phoneNumber.length() < 6){
			phoneNumberView.setError("Phone number invalid");
			isError = true; 
		}
		if(!email.contains("@") || !email.contains(".")){
			emailView.setError("Email invalid");
			isError = true; 
		}
		if(!password.equals(confirmPass)){
			passwordView.setText("");
			confirmPassView.setText("");
			passwordView.setError("Password Did Not Match");
			isError = true; 
		}
		if(password.length() < 8 ){
			passwordView.setText("");
			confirmPassView.setText("");
			passwordView.setError("Password must be between 8 and 20 characters");
			isError = true;
		}
		if(isError){
			return; 
		}
		
		 new SignUpUserTask().execute("http://107.170.79.251/HelpMeOut/api/newaccount");
		 
	    toast = Toast.makeText(context, "Login Acount Created", duration);
		toast.show();
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}
	
	private void toMainPage(){
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_up, menu);
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
	private class SignUpUserTask extends AsyncTask<String, Void, String> {
	    /** The system calls this to perform work in a worker thread and
	      * delivers it the parameters given to AsyncTask.execute() */
	    protected String doInBackground(String... urls) {
	        return signUpUser(urls[0]);
	    }
	    
	    /** The system calls this to perform work in the UI thread and delivers
	      * the result from doInBackground() */
	    protected void onPostExecute(String result) {
	    	Log.i("THREAD","Thread executed"); 
	    	Context context = getApplicationContext();
			int duration = Toast.LENGTH_SHORT;
			Toast toast;   
	    	toast = Toast.makeText(context, result, duration);
		    toast.show();
	    }
	  private String signUpUser(String url){
		  
		  	// get form information 
		  	EditText firstNameView = (EditText) findViewById(R.id.firstName);
			EditText lastNameView = (EditText) findViewById(R.id.lastName);
			Spinner genderView = (Spinner) findViewById(R.id.genderSpinner);
			EditText phoneNumberView = (EditText) findViewById(R.id.phoneNumber);
			EditText emailView = (EditText) findViewById(R.id.emailAddress); 
			EditText passwordView = (EditText) findViewById(R.id.password);
			EditText confirmPassView = (EditText) findViewById(R.id.confirmPassword); 
			
			String firstName = firstNameView.getText().toString(); 
			String lastName = lastNameView.getText().toString();
			String gender = genderView.getSelectedItem().toString(); 
			String phoneNumber = phoneNumberView.getText().toString(); 
			String email = emailView.getText().toString();
			String password = passwordView.getText().toString(); 
			String confirmPass = confirmPassView.getText().toString();
	    	
			// initialize a toast for debugging 
	    	Context context = getApplicationContext();
			int duration = Toast.LENGTH_LONG;
			Toast toast;
	    	
			// Send a HttpPost Request
			 HttpClient httpclient = new DefaultHttpClient();
			 HttpPost httppost = new HttpPost(url);
			 HttpResponse response = null;
			 HttpEntity entity = null;
			 JSONObject json = new JSONObject(); 
		 try {
			     // Adding data
			 	json.put("email", email.toString());
		 		json.put("password", password.toString());
		 		json.put("firstName", firstName.toString());
		 		json.put("lastName", lastName.toString());
		 		json.put("phone", phoneNumber.toString());
		 		json.put("birthDate", "12/34/5678");
		 		json.put("gender", "1"); 
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		 	}
		 	return "get failed"; 
	    }
	}

}
