package ru.usu.twitalk.twitter;

import oauth.signpost.OAuthConsumer;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import ru.usu.twitalk.App;
import ru.usu.twitalk.Data;
import android.os.AsyncTask;
import android.util.Log;

public class GetCredentialsTask extends AsyncTask<Void, Void, Boolean> {

	private OAuthConsumer mConsumer; 
	private static final String TAG = "GetCredentialsTask";
	
	public GetCredentialsTask(OAuthConsumer mConsumer) {
		this.mConsumer = mConsumer;
	}
	
	@Override
	protected void onPreExecute() {
		Log.d(TAG, "Waiting");
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
//			lblUserName.setText(Data.USER_NAME);
			new GetFollowersList(mConsumer).execute(App.GET_FOLLOWERS_LIST_URL);
		} else {
			Log.d(TAG, "beda");
		}
	}
	
	private void parseVerifyUserJSONObject(JSONObject object) throws Exception {

		Data.USER_NAME = object.getString("name");

	}

}
