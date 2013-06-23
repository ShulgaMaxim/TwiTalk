package ru.usu.twitalk.twitter;

import oauth.signpost.OAuthConsumer;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ru.usu.twitalk.Data;
import ru.usu.twitalk.activities.ContactsActivity;
import ru.usu.twitalk.activities.TwiTalkActivity;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

public class GetUserTimeLine extends AsyncTask<String, Void, Void> {

	private static final String TAG = "User timeline";
	private OAuthConsumer mConsumer = TwiTalkActivity.getConsumer();
	private long user_id;
	private DefaultHttpClient mClient = new DefaultHttpClient();
	private Data instance = Data.getInstance();

	public GetUserTimeLine(long user_id) {
		this.user_id = user_id;
	}

	@Override
	protected void onPreExecute() {
		ContactsActivity.loadingMessagesDialog
				.setMessage("Loading messages...");
		ContactsActivity.loadingMessagesDialog.show();
		Log.d(TAG, "Waitng");
	}

	@Override
	protected Void doInBackground(String... params) {
		JSONArray array = null;
		synchronized (instance.users) {
			try {
				Uri sUri = Uri.parse(params[0]);
				Uri.Builder builder = sUri.buildUpon();
				builder.appendQueryParameter("user_id", String.valueOf(user_id));
				builder.appendQueryParameter("count", "2");
				HttpGet get = new HttpGet(builder.build().toString());
				mConsumer.sign(get);
				String response = mClient.execute(get,
						new BasicResponseHandler());
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
	}

	protected void onPostExecute(Void nada) {
		Log.d(TAG, "Messages Loaded");
		ContactsActivity.loadingMessagesDialog.dismiss();
	}

	private void parseTimelineJSONObject(JSONObject object) {
		JSONObject user;
		try {
			user = object.getJSONObject("user");
			String name = user.getString("name");
			String msg = object.getString("text");
			
			if (instance.users.containsKey(name)) {
				instance.users.get(name).addMsg(msg);
			}

			Log.d(TAG, name + " " + msg);
		} catch (JSONException e) {
			Log.e(TAG, "Couldn't take user data", e);
			return;
		}

	}

}
