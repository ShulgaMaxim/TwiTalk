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
			JSONArray friends = cursor.getJSONArray("users");
			for (int i = 0; i < friends.length(); ++i) {
				JSONObject user = friends.getJSONObject(i);
				parseTimelineJSONObject(user);
			}
		} catch (Exception e) {
			Log.e(TAG, "Get Followers Exception", e);
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void nada) {

		// for (String usr : instance.users.keySet()) {
		// new TestSearchTwitts().execute(App.SEARCH_TWITTS_URL);
		new GetMentionsTimeLine().execute(App.MENTIONS_TIMELINE_URL);
		// }

		TwiTalkActivity.loadingContactsDialog.dismiss();
	}

	private void parseTimelineJSONObject(JSONObject object) throws Exception {
		String name;
		long id;
		String screen_name;
		User user;
		try {
			name = object.getString("name");
			id = object.getLong("id_str");
			screen_name = object.getString("screen_name");
			user = new User(name, id, screen_name);
		} catch (Exception e) {
			Log.e(TAG, "Can't take info from JSON");
			return;
		}
		if (instance.users.containsKey(name))
			return;
		instance.users.put(name, user);

	}
}
