package ru.usu.twitalk.activities;

import ru.usu.twitalk.Data;
import ru.usu.twitalk.R;
import ru.usu.twitalk.twitter.PostTwitt;
import android.app.Activity;
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

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.msgs);

		tvView = (TextView) findViewById(R.id.msgsHeader);

		Intent intent = getIntent();
		String chosenContact = intent.getStringExtra("chosenContact");

		tvView.setText("Tweets from " + chosenContact);

		ListView lvMsgs = (ListView) findViewById(R.id.lvMsgs);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1,
				instance.contactsWithMsgs.get(chosenContact));

		lvMsgs.setAdapter(adapter);

		editMessage = (EditText) findViewById(R.id.editMessage);

		Button btnSendMessage = (Button) findViewById(R.id.buttonSend);
		btnSendMessage.setOnClickListener(new SendButtonClickedListener());
	}

	class SendButtonClickedListener implements OnClickListener {

		@Override
		public void onClick(View v) {

			String msg = editMessage.getText().toString();

			if (msg.length() > 0) {
				new PostTwitt().execute();
			}

		}
	}
}