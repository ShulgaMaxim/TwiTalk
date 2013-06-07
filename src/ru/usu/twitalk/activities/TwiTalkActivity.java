package ru.usu.twitalk.activities;

import oauth.signpost.OAuthConsumer;
import ru.usu.twitalk.App;
import ru.usu.twitalk.Data;
import ru.usu.twitalk.R;
import ru.usu.twitalk.twitter.GetCredentialsTask;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class TwiTalkActivity extends Activity {

	private Button btnLogin;
	private Button btnLogoutTwitter;
	private Button btnShowFriends;
	private TextView lblUserName;
	
	public static ProgressDialog pd;

	private OAuthConsumer mConsumer = null;

	private String mToken;
	private String mSecret;

	private SharedPreferences mSettings;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		mConsumer = ((App) getApplication()).getOAuthConsumer();

		this.
		lblUserName = (TextView) this.findViewById(R.id.lblUserName);
		lblUserName.setText(Data.USER_NAME);

		btnLogin = (Button) this.findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(new LoginButtonClickedListener());

		btnLogoutTwitter = (Button) findViewById(R.id.btnLogoutTwitter);
		btnLogoutTwitter.setOnClickListener(new LogoutButtonClickedListener());
		
		btnShowFriends = (Button) findViewById(R.id.btnFollowers);
		btnShowFriends.setOnClickListener(new FriendsButtonClickedListener());

		mSettings = PreferenceManager.getDefaultSharedPreferences(this);

	}

	@Override
	public void onResume() {
		super.onResume();

		if (mSettings.contains(App.USER_TOKEN)
				&& mSettings.contains(App.USER_SECRET)) {
			mToken = mSettings.getString(App.USER_TOKEN, null);
			mSecret = mSettings.getString(App.USER_SECRET, null);
			if (!(mToken == null || mSecret == null)) {
				mConsumer.setTokenWithSecret(mToken, mSecret);
				btnLogin.setVisibility(View.GONE);
				btnShowFriends.setVisibility(View.VISIBLE);
				btnLogoutTwitter.setVisibility(View.VISIBLE);
				pd = new ProgressDialog(this);
				pd.setTitle("WELCOME");
				pd.setMessage("Loading contacts...");
				pd.show();
				(new GetCredentialsTask(mConsumer)).execute();
			}
		}
	}

	class LoginButtonClickedListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			TwiTalkActivity.this.startActivity(new Intent(TwiTalkActivity.this,
					OAuthActivity.class));
		}
	}

	class LogoutButtonClickedListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			App.saveAuthInformation(mSettings, null, null);
			btnLogin.setVisibility(View.VISIBLE);
			btnShowFriends.setVisibility(View.GONE);
			btnLogoutTwitter.setVisibility(View.GONE);
			Data.USER_NAME = "";
			Data.ID_AUTH_USER = 0;
			lblUserName.setText(Data.USER_NAME);
			Data.FOLLOWERS.clear();

		}
	}
	
	class FriendsButtonClickedListener implements OnClickListener {

		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.main, menu);
			return true;
		}

		@Override
		public void onClick(View view) {
			Intent intent = new Intent("ru.usu.intent.action.showcontacts");
			startActivity(intent);
		}
	}
}
