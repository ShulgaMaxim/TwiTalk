package ru.usu.twitalk.twitter;

import java.util.ArrayList;

import oauth.signpost.OAuthConsumer;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ru.usu.twitalk.Data;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

public class GetUserTimeLine extends AsyncTask<String, Void, Void> {

	private static final String TAG = "User timeline";
	private OAuthConsumer mConsumer;
	private long user_id;
	private DefaultHttpClient mClient = new DefaultHttpClient();
	private Data instance = Data.getInstance();

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
			builder.appendQueryParameter("count", "2");
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

	private void parseTimelineJSONObject(JSONObject object) {
		JSONObject user;
		ArrayList<String> list;
		try {
			user = object.getJSONObject("user");
			String name = user.getString("name");
			String msg = object.getString("text");
			if (instance.contactsWithMsgs.containsKey(name))
				if (!instance.contactsWithMsgs.get(name).contains(msg))
					list = instance.contactsWithMsgs.get(name);
				else
					return;
			else
				list = new ArrayList<String>();

			list.add(msg);
			instance.contactsWithMsgs.put(name, list);
			Log.d(TAG, name + " " + msg);
		} catch (JSONException e) {
			Log.e(TAG, "Couldn't take user data", e);
			return;
		}
		// JSONArray array =
		// object.getJSONObject("entities").getJSONArray("user_mentions");

		// for (int i = 0; i < array.length(); ++i) {
		// JSONObject person = array.getJSONObject(i);
		// Log.d(TAG, person.getString("name") + " " +
		// person.getString("screen_name"));
		// }
	}

}
