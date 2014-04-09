package com.example.helpmeout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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
				toHomePage(); 
				
			}
		});
	}
	private void toSignUp(){
		Intent intent = new Intent(this,SignUp.class); 
		startActivity(intent); 
	}

	public void toHomePage(){
		// define variables for tests
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		Toast toast;

		
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
		if(setError){
			return; 
		}
		
		// Send a HttpPost Request
		 HttpClient httpclient = new DefaultHttpClient();
		 HttpPost httppost = new HttpPost("www.google.com");
		 
	 try {
		        // Add your data
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		        nameValuePairs.add(new BasicNameValuePair("email", email.toString()));
		        nameValuePairs.add(new BasicNameValuePair("password", password.toString()));
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		        // Execute HTTP Post Request
		        HttpResponse response = httpclient.execute(httppost);
		        toast = Toast.makeText(context, response.toString(), duration);
		        toast.show();
		      
		    } catch (ClientProtocolException e) {
		        // TODO Auto-generated catch block
		   } catch (IOException e) {
		        // TODO Auto-generated catch block
		   }
	 
		 
		
		//display toast for building
		
		//Intent intent = new Intent(this, HomePage.class ); 
		//startActivity(intent); 
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
