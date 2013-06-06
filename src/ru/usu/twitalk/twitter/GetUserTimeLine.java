package ru.usu.twitalk.twitter;

import oauth.signpost.OAuthConsumer;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

public class GetUserTimeLine extends AsyncTask<String, Void, Void> {

	private static final String TAG = "User timeline";
	private OAuthConsumer mConsumer;
	private long user_id;
	private DefaultHttpClient mClient = new DefaultHttpClient();

	public GetUserTimeLine(OAuthConsumer mConsumer, long user_id) {
		this.mConsumer = mConsumer;
		this.user_id = user_id;
	}

	@Override
	protected Void doInBackground(String... params) {
		JSONArray array = null;
		try {
			Uri sUri = Uri.parse(params[0]);
			Uri.Builder builder = sUri.buildUpon();
			builder.appendQueryParameter("user_id", String.valueOf(user_id));
			builder.appendQueryParameter("count", "3");
			Log.d(TAG,builder.build().toString());
			HttpGet get = new HttpGet(builder.build().toString());
			mConsumer.sign(get);
			String response = mClient.execute(get, new BasicResponseHandler());
			array = new JSONArray(response);
			for (int i = 0; i < array.length(); ++i) {
				JSONObject status = array.getJSONObject(i);
				parseTimelineJSONObject(status);
			}
		} catch (Exception e) {
			Log.e(TAG, "Get Timeline Exception", e);
		}
		return null;
	}

	private void parseTimelineJSONObject(JSONObject object) throws Exception {
		JSONObject user = object.getJSONObject("user");
		JSONArray array = object.getJSONObject("entities").getJSONArray("user_mentions");
		Log.d(TAG, user.getString("name") + " " + object.getString("text"));
		for (int i = 0; i < array.length(); ++i) {
			JSONObject person = array.getJSONObject(i);
			Log.d(TAG, person.getString("name") + " " + person.getString("screen_name"));
		}
	}

}
	