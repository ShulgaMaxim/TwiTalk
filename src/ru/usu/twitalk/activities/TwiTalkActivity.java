package ru.usu.twitalk.activities;

import oauth.signpost.OAuthConsumer;

import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import ru.usu.twitalk.App;
import ru.usu.twitalk.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TwiTalkActivity extends Activity {

	private Button btnLogin;

	private OAuthConsumer mConsumer = null;

	private String mToken;
	private String mSecret;

	private SharedPreferences mSettings;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		mConsumer = ((App) getApplication()).getOAuthConsumer();

		btnLogin = (Button) this.findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(new LoginButtonClickedListener());

		mSettings = PreferenceManager.getDefaultSharedPreferences(this);
	}

	@Override
	public void onResume() {
		super.onResume();

		// We look for saved user keys
		if (mSettings.contains(App.USER_TOKEN)
				&& mSettings.contains(App.USER_SECRET)) {
			mToken = mSettings.getString(App.USER_TOKEN, null);
			mSecret = mSettings.getString(App.USER_SECRET, null);
			// If we find some we update the consumer with them
			if (!(mToken == null || mSecret == null)) {
				mConsumer.setTokenWithSecret(mToken, mSecret);
			}
		}
	}

	// These parameters are needed to talk to the messaging service
	public HttpParams getParams() {
		// Tweak further as needed for your app
		HttpParams params = new BasicHttpParams();
		// set this to false, or else you'll get an Expectation Failed: error
		HttpProtocolParams.setUseExpectContinue(params, false);
		return params;
	}

	class LoginButtonClickedListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			TwiTalkActivity.this.startActivity(new Intent(TwiTalkActivity.this,
					OAuthActivity.class));
		}
	}

}
