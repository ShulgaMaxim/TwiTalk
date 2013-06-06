package ru.usu.twitalk.twitter;

import oauth.signpost.OAuthConsumer;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import ru.usu.twitalk.Data;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

public class GetFollowersList extends AsyncTask<String, Void, Void> {

	private static final String TAG = "Follower";
	private DefaultHttpClient mClient = new DefaultHttpClient();
	private OAuthConsumer mConsumer;
	
	public GetFollowersList(OAuthConsumer mConsumer) {
		this.mConsumer = mConsumer;
	}
	
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
			String response = mClient.execute(get, new BasicResponseHandler());
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
		for (String name : Data.FOLLOWERS)
			Log.d(TAG, name);

	}

	private void parseTimelineJSONObject(JSONObject object) throws Exception {

		if (Data.FOLLOWERS.contains(object.getString("name")))
			return;
		Data.FOLLOWERS.add(object.getString("name"));

	}
}
