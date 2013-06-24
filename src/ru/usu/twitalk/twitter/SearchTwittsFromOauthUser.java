package ru.usu.twitalk.twitter;

import oauth.signpost.OAuthConsumer;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import ru.usu.twitalk.Data;
import ru.usu.twitalk.activities.TwiTalkActivity;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

public class SearchTwittsFromOauthUser extends AsyncTask<String, Void, Void> {

	private static final String TAG = "Twitt Search";
	private OAuthConsumer mConsumer = TwiTalkActivity.getConsumer();
	private DefaultHttpClient mClient = new DefaultHttpClient();
	private Data instance = Data.getInstance();
	private String destinationUser;

	public SearchTwittsFromOauthUser(String destinationUser) {
		this.destinationUser = destinationUser;
	}
	
	@Override
	protected void onPreExecute() {
		Log.d(TAG, "Waitng");
	}

	@Override
	protected Void doInBackground(String... params) {
		JSONObject jso = null;
		JSONArray jsa = null;
		String searchParametr = "";
		synchronized (instance.users) {
			try {
				searchParametr = searchParametr
						.concat(instance.infAbOAuthUser.get("screen_name"))
						.concat(" @")
						.concat(instance.users.get(destinationUser)
								.getScreenName());
				Log.e(TAG, searchParametr);
				Uri sUri = Uri.parse(params[0]);
				Uri.Builder builder = sUri.buildUpon();
				builder.appendQueryParameter("q", searchParametr);
				builder.appendQueryParameter("count", "10");
				HttpGet get = new HttpGet(builder.build().toString());
				mConsumer.sign(get);
				String response = mClient.execute(get,
						new BasicResponseHandler());
				jso = new JSONObject(response);
				jsa = jso.getJSONArray("statuses");
				for (int i = 0; i < jsa.length(); i++) {
					parseSearchTwittsJSONObject(jsa.getJSONObject(i));
				}

			} catch (Exception e) {
				Log.e(TAG, "EXCEPTION");
				e.printStackTrace();
			}
			return null;
		}
	}
	
	protected void onPostExecute(Void nada) {
	}

	private void parseSearchTwittsJSONObject(JSONObject object)
			throws Exception {

		try {
			String text = object.getString("text");
			if (instance.users.containsKey(destinationUser)) {
				instance.users.get(destinationUser).addMsg(text);
			}
			Log.d(TAG, text);
		} catch (Exception e) {
			Log.e(TAG, "JSON Exception");
		}
	}
}
