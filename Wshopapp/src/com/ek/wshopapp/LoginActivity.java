package com.ek.wshopapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends MainActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		Button button = (Button) findViewById(R.id.loginbutton);

		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				CreateUserOnServer CreateUserOnServer = new CreateUserOnServer(
						ip);
				boolean Empty = true;

				EditText emailname = (EditText) findViewById(R.id.emailloginview);
				EditText password = (EditText) findViewById(R.id.passwordloginview);

				if (emailname.getText().toString().equals("")) {
					emailname.setError(" please write email");
					Empty = false;
				}
				if (password.getText().toString().equals("")) {
					password.setError(" please write password");
					Empty = false;
				}
				if (emailname.getText().toString().equals("")
						&& password.getText().toString().equals("")) {
					emailname.setError(" please write email");
					password.setError(" please write password");
					Empty = false;
				}
				
				CreateUserOnServer.setUserName(emailname.getText().toString());
				CreateUserOnServer.setPassword(password.getText().toString());
				
				if(Empty = true){
					CreateUserOnServer.execute();					
				}
			}
		});

	}//End of onCreate
	
	class CreateUserOnServer extends AsyncTask<Void, Void, Boolean> {
		private String ip;
		private String email;
		private String password;

		public CreateUserOnServer(String ip) {
			this.ip = ip;
		}

		public void setUserName(String email) {
			this.email = email;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				HttpPost post = new HttpPost("http://" + ip
						+ ":9000/checkInuser");
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("username", email));
				nameValuePairs
						.add(new BasicNameValuePair("password", password));
				post.setEntity(new UrlEncodedFormEntity(nameValuePairs));


				DefaultHttpClient client = new DefaultHttpClient();
				client.execute(post);
				cookies = client.getCookieStore().getCookies();
				
				for (Cookie cookie : cookies) {
					if (cookie.getName().equals("PLAY_FLASH")) {
						 return false;
					}
				}
				return true;
			} catch (Exception e) {
				Log.e("Error login", e.getMessage());

				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean success) {
			if (success == true) {
				Toast.makeText(getApplicationContext(), "You are logged in!",
						Toast.LENGTH_LONG).show();
				Intent intent = new Intent(getApplicationContext(), ShowMessageActivity.class);
    			startActivity(intent);
    			//******
    			overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    			
				finish();
				
			} else {
				Toast.makeText(getApplicationContext(), "You are not logged in!",
						Toast.LENGTH_LONG).show();
			}
		}
	}

}
