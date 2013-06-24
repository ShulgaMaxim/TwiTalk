package ru.usu.twitalk.activities;

import ru.usu.twitalk.Data;
import ru.usu.twitalk.R;
import ru.usu.twitalk.twitter.PostTwitt;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MsgsActivity extends Activity {

	private Data instance = Data.getInstance();
	private TextView tvView;
	private EditText editMessage;
	public static String chosenContact;
	public static ProgressDialog sendingMessageDialog;
	public ListView lvMsgs;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.msgs);
		
		sendingMessageDialog = new ProgressDialog(this);

		tvView = (TextView) findViewById(R.id.msgsHeader);

		synchronized (instance.users) {
			Intent intent = getIntent();
			chosenContact = intent.getStringExtra("chosenContact");

			tvView.setText("Tweets from " + chosenContact);

			lvMsgs = (ListView) findViewById(R.id.lvMsgs);

			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1,
					instance.users.get(chosenContact).getMsgs());

			lvMsgs.setAdapter(adapter);
		}

		editMessage = (EditText) findViewById(R.id.editMessage);

		Button btnSendMessage = (Button) findViewById(R.id.buttonSend);
		btnSendMessage.setOnClickListener(new SendButtonClickedListener());
	}

	class SendButtonClickedListener implements OnClickListener {

		@Override
		public void onClick(View v) {

			String msg = editMessage.getText().toString();
			
			editMessage.setText("");
			if (msg.length() > 0) {
				String screenName = instance.users.get(chosenContact).getScreenName();
				msg = "@".concat(screenName).concat(" ").concat(msg);
				PostTwitt pt = new PostTwitt();
				pt.execute(msg);
			}
		}
	}
}