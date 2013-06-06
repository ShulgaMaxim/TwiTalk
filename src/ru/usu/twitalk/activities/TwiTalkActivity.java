package ru.usu.twitalk.activities;

import oauth.signpost.OAuthConsumer;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.json.JSONArray;
import org.json.JSONObject;

import ru.usu.twitalk.App;
import ru.usu.twitalk.Data;
import ru.usu.twitalk.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class TwiTalkActivity extends Activity {

	private static final String TAG = OAuthActivity.class.toString();
	private static int count = 0;

	private Button btnLogin;
	private Button btnLogoutTwitter;
	private TextView lblUserName;

	private OAuthConsumer mConsumer = null;

	private String mToken;
	private String mSecret;

	private SharedPreferences mSettings;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		mConsumer = ((App) getApplication()).getOAuthConsumer();

		lblUserName = (TextView) this.findViewById(R.id.lblUserName);
		lblUserName.setText(Data.USER_NAME);

		btnLogin = (Button) this.findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(new LoginButtonClickedListener());

		btnLogoutTwitter = (Button) findViewById(R.id.btnLogoutTwitter);
		btnLogoutTwitter.setOnClickListener(new LogoutButtonClickedListener());

		mSettings = PreferenceManager.getDefaultSharedPreferences(this);

	}

	@Override
	public void onResume() {
		super.onResume();

		count++;
		Log.d(TAG, "MainActivity: onResume()" + count);
		// We look for saved user keys
		if (mSettings.contains(App.USER_TOKEN)
				&& mSettings.contains(App.USER_SECRET)) {
			mToken = mSettings.getString(App.USER_TOKEN, null);
			mSecret = mSettings.getString(App.USER_SECRET, null);
			// If we find some we update the consumer with them
			if (!(mToken == null || mSecret == null)) {
				mConsumer.setTokenWithSecret(mToken, mSecret);
				btnLogin.setVisibility(View.GONE);
				btnLogoutTwitter.setVisibility(View.VISIBLE);
				(new GetCredentialsTask()).execute();
			}
		}
	}

	class GetCredentialsTask extends AsyncTask<Void, Void, Boolean> {

		private static final String TAG = "user_status";

		@Override
		protected void onPreExecute() {
			lblUserName.setText("Waiting....");
		}

		@Override
		protected Boolean doInBackground(Void... arg0) {

			DefaultHttpClient mClient = new DefaultHttpClient();

			JSONObject jso = null;
			HttpGet get = new HttpGet(App.VERIFY_URL_STRING);
			try {
				mConsumer.sign(get);
				String response = mClient.execute(get,
						new BasicResponseHandler());
				jso = new JSONObject(response);
				parseVerifyUserJSONObject(jso);
				return true;
			} catch (Exception e) {
				// Expected if we don't have the proper credentials saved away
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean loggedIn) {

			if (loggedIn) {
				Log.d(TAG, "user:" + Data.USER_NAME);
				lblUserName.setText(Data.USER_NAME);
				new GetFollowersList().execute(App.GET_FOLLOWERS_LIST_URL);
			} else {
				Log.d(TAG, "beda");
			}
		}
	}

	private void parseVerifyUserJSONObject(JSONObject object) throws Exception {

		Data.USER_NAME = object.getString("name");

	}

	private void parseTimelineJSONObject(JSONObject object) throws Exception {
		
		
		if (Data.FOLLOWERS.contains(object.getString("name")))
			return;
		Data.FOLLOWERS.add(object.getString("name"));

	}

	class GetFollowersList extends AsyncTask<String, Void, Void> {

		private static final String TAG = "Follower";
		DefaultHttpClient mClient = new DefaultHttpClient();

		@Override
		protected void onPreExecute() {
			Log.d(TAG, "Waitng");
		}

		@Override
		protected Void doInBackground(String... params) {
			JSONObject cursor = null;
			try {
				Uri sUri = Uri.parse(params[0]);
				Uri.Builder builder = sUri.buildUpon();

				HttpGet get = new HttpGet(builder.build().toString());
				mConsumer.sign(get);
				String response = mClient.execute(get,
						new BasicResponseHandler());
				cursor = new JSONObject(response);
				JSONArray followers = cursor.getJSONArray("users");
				for (int i = 0; i < followers.length(); ++i) {
					JSONObject user = followers.getJSONObject(i);
					parseTimelineJSONObject(user);
				}
			} catch (Exception e) {
				Log.e(TAG, "Get Followers Exception", e);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void nada) {
			for(String name : Data.FOLLOWERS) 
				Log.d(TAG,name);
			
		}

	}

	public HttpParams getParams() {
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setUseExpectContinue(params, false);
		return params;
	}

	class LoginButtonClickedListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			TwiTalkActivity.this.startActivity(new Intent(TwiTalkActivity.this,
					OAuthActivity.class));
		}
	}

	class LogoutButtonClickedListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			App.saveAuthInformation(mSettings, null, null);
			btnLogin.setVisibility(View.VISIBLE);
			btnLogoutTwitter.setVisibility(View.GONE);
			Data.USER_NAME = "user_name";
			lblUserName.setText(Data.USER_NAME);
			Data.FOLLOWERS.clear();

		}
	}

}
