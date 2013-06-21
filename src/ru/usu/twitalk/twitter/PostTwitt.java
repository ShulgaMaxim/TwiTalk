package ru.usu.twitalk.twitter;

import java.util.ArrayList;
import java.util.List;

import oauth.signpost.OAuthConsumer;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import ru.usu.twitalk.App;
import ru.usu.twitalk.activities.TwiTalkActivity;
import android.os.AsyncTask;
import android.util.Log;



public class PostTwitt extends AsyncTask<String, Void, JSONObject> {


	private DefaultHttpClient mClient = new DefaultHttpClient();
	private OAuthConsumer mConsumer = TwiTalkActivity.getConsumer();
	private static final String TAG = "GetCredentialsTask";

	// These parameters are needed to talk to the messaging service
		public HttpParams getParams() {
			// Tweak further as needed for your app
			HttpParams params = new BasicHttpParams();
			// set this to false, or else you'll get an Expectation Failed: error
			HttpProtocolParams.setUseExpectContinue(params, false);
			return params;
		}
	
	@Override
	protected JSONObject doInBackground(String... params) {

		JSONObject jso = null;
		try {

			HttpPost post = new HttpPost(App.STATUSES_URL_STRING);
			List<BasicNameValuePair> out = new ArrayList<BasicNameValuePair>();
			out.add(new BasicNameValuePair("status", params[0]));
			post.setEntity(new UrlEncodedFormEntity(out, HTTP.UTF_8));
			post.setParams(getParams());
			mConsumer.sign(post);
			String response = mClient.execute(post,
					new BasicResponseHandler());
			jso = new JSONObject(response);
		} catch (Exception e) {
			Log.e(TAG, "Post Twitt Exception", e);
		}
		return jso;
	}

}