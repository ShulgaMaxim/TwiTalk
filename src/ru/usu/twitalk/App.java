package ru.usu.twitalk;

import junit.framework.Assert;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import android.app.Application;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

public class App extends Application {

	static final String TAG = App.class.toString();

	public static final String VERIFY_URL_STRING = "https://api.twitter.com/1.1/account/verify_credentials.json";
	public static final String PUBLIC_TIMELINE_URL_STRING = "https://api.twitter.com/1.1/statuses/public_timeline.json";
	public static final String USER_TIMELINE_URL_STRING = "https://api.twitter.com/1.1/statuses/user_timeline.json";
	public static final String HOME_TIMELINE_URL_STRING = "https://api.twitter.com/1.1/statuses/home_timeline.json";
	public static final String FRIENDS_TIMELINE_URL_STRING = "https://api.twitter.com/1.1/statuses/friends_timeline.json";
	public static final String STATUSES_URL_STRING = "https://api.twitter.com/1.1/statuses/update.json";

	public static final String USER_TOKEN = "user_token";
	public static final String USER_SECRET = "user_secret";
	public static final String REQUEST_TOKEN = "request_token";
	public static final String REQUEST_SECRET = "request_secret";

	public static final String TWITTER_REQUEST_TOKEN_URL = "https://api.twitter.com/oauth/request_token";
	public static final String TWITTER_ACCESS_TOKEN_URL = "https://api.twitter.com/oauth/access_token";
	public static final String TWITTER_AUTHORIZE_URL = "https://api.twitter.com/oauth/authorize";

	public static final String CALLBACK_URL = "twitalk://twitt";
	public static final Uri CALLBACK_URI = Uri.parse(CALLBACK_URL);


	public static void saveRequestInformation(SharedPreferences settings,
			String token, String secret) {
		// null means to clear the old values
		SharedPreferences.Editor editor = settings.edit();
		if (token == null) {
			editor.remove(App.REQUEST_TOKEN);
			Log.d(TAG, "Clearing Request Token");
		} else {
			editor.putString(App.REQUEST_TOKEN, token);
			Log.d(TAG, "Saving Request Token: " + token);
		}
		if (secret == null) {
			editor.remove(App.REQUEST_SECRET);
			Log.d(TAG, "Clearing Request Secret");
		} else {
			editor.putString(App.REQUEST_SECRET, secret);
			Log.d(TAG, "Saving Request Secret: " + secret);
		}
		editor.commit();

	}

	public static void saveAuthInformation(SharedPreferences settings,
			String token, String secret) {
		// null means to clear the old values
		SharedPreferences.Editor editor = settings.edit();
		if (token == null) {
			editor.remove(App.USER_TOKEN);
			Log.d(TAG, "Clearing OAuth Token");
		} else {
			editor.putString(App.USER_TOKEN, token);
			Log.d(TAG, "Saving OAuth Token: " + token);
		}
		if (secret == null) {
			editor.remove(App.USER_SECRET);
			Log.d(TAG, "Clearing OAuth Secret");
		} else {
			editor.putString(App.USER_SECRET, secret);
			Log.d(TAG, "Saving OAuth Secret: " + secret);
		}
		editor.commit();

	}

	private OAuthConsumer consumer = null;
	private OAuthProvider provider = null;

	private KeysProvider keysProvider = new DefaultKeysProvider();

	private KeysProvider getKeysProvider() {
		return keysProvider;
	}

	public void setKeysProvider(KeysProvider kp) {
		keysProvider = kp;
	}

	public OAuthConsumer getOAuthConsumer() {
		return consumer;
	}

	public OAuthProvider getOAuthProvider() {
		return provider;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		consumer = new CommonsHttpOAuthConsumer(getKeysProvider()
				.getConsumerKey(), getKeysProvider().getConsumerSecret());

		provider = new CommonsHttpOAuthProvider(App.TWITTER_REQUEST_TOKEN_URL,
				App.TWITTER_ACCESS_TOKEN_URL, App.TWITTER_AUTHORIZE_URL);

		Assert.assertNotNull(consumer);
		Assert.assertNotNull(provider);

		provider.setOAuth10a(true);
	}

}
