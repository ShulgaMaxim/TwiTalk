package ru.usu.twitalk.twitter;

import oauth.signpost.OAuthConsumer;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import ru.usu.twitalk.App;
import ru.usu.twitalk.Data;
import ru.usu.twitalk.activities.TwiTalkActivity;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

public class GetFriendsList extends AsyncTask<String, Void, Void> {

	private Data instance = Data.getInstance();
	private static final String TAG = "Friend";
	private DefaultHttpClient mClient = new DefaultHttpClient();
	private OAuthConsumer mConsumer = TwiTalkActivity.getConsumer();


	@Override
	protected void onPreExecute() {
		TwiTalkActivity.loadingContactsDialog.setMessage("Loading contacts...");
		TwiTalkActivity.loadingContactsDialog.show();
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
		for (String name : instance.infAbFriends.keySet()) {
			long id = instance.infAbFriends.get(name);
			Log.d(TAG, name + " " + id);
			new GetUserTimeLine(id)
					.execute(App.USER_TIMELINE_URL);
		}
		TwiTalkActivity.loadingContactsDialog.dismiss();
	}

	private void parseTimelineJSONObject(JSONObject object) throws Exception {

		String name = object.getString("name");
		long id = object.getLong("id_str");
		if (instance.friends.contains(name)
				&& instance.infAbFriends.containsKey(name))
			return;
		instance.friends.add(name);
		instance.infAbFriends.put(name, id);

	}
}
