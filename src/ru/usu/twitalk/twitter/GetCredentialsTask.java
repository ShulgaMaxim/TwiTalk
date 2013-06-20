package ru.usu.twitalk.twitter;

import oauth.signpost.OAuthConsumer;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import ru.usu.twitalk.App;
import ru.usu.twitalk.Data;
import ru.usu.twitalk.activities.TwiTalkActivity;
import android.os.AsyncTask;
import android.util.Log;

public class GetCredentialsTask extends AsyncTask<Void, Void, Boolean> {

	private OAuthConsumer mConsumer;
	private static final String TAG = "GetCredentialsTask";
	
	private Data instance = Data.getInstance();
	
	public GetCredentialsTask(OAuthConsumer mConsumer) {
		this.mConsumer = mConsumer;
	}
	
	@Override
	protected void onPreExecute() {
		//TwiTalkActivity.loadingUserDataDialog.setTitle("WELCOME");
		TwiTalkActivity.loadingUserDataDialog.setMessage("Gathering user data...");
		TwiTalkActivity.loadingUserDataDialog.show();
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
			Log.d(TAG, "user:" + instance.infAbOAuthUser.get(Data.USER_NAME));
			Log.d(TAG, "id:" + instance.infAbOAuthUser.get(Data.ID_AUTH_USER));
			Log.d(TAG, "screen_name:" + instance.infAbOAuthUser.get(Data.USER_SCREEN_NAME));
			new GetFriendsList(mConsumer).execute(App.GET_FRIENDS_LIST_URL);
		} else {
			Log.d(TAG, "fail");
		}
		TwiTalkActivity.loadingUserDataDialog.dismiss();
	}
	
	private void parseVerifyUserJSONObject(JSONObject object) throws Exception {

		instance.infAbOAuthUser.put(Data.USER_NAME, object.getString("name"));
		instance.infAbOAuthUser.put(Data.ID_AUTH_USER, object.getString("id_str"));
		instance.infAbOAuthUser.put(Data.USER_SCREEN_NAME, object.getString("screen_name"));

	}

}
