package ru.usu.twitalk.activities;

import ru.usu.twitalk.App;
import ru.usu.twitalk.Data;
import ru.usu.twitalk.R;
import ru.usu.twitalk.twitter.GetMentionsTimeLine;
import ru.usu.twitalk.twitter.SearchTwittsFromOauthUser;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ContactsActivity extends Activity {

	protected static final String TAG = "Contacts";
	private Data instance = Data.getInstance();
	public static ProgressDialog loadingMessagesDialog;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contacts);

		loadingMessagesDialog = new ProgressDialog(this);

		TextView tvView = (TextView) findViewById(R.id.contactsHeader);

		if (instance.users.isEmpty()) {
			tvView.setText("You don't have contacts");
		} else {
			Object[] contacts = instance.users.keySet().toArray();

			ListView lvContacts = (ListView) findViewById(R.id.lvContacts);

			ArrayAdapter<Object> adapter = new ArrayAdapter<Object>(this,
					R.layout.my_list_item, contacts);

			lvContacts.setAdapter(adapter);

			lvContacts.setOnItemClickListener(new OnContactClickListener());
		}

	}
	
	class OnContactClickListener implements OnItemClickListener {
		
		public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {

			TextView chosenContact = (TextView) view.findViewById(view.getId());

			String destinationUser = chosenContact.getText().toString();

			Log.d(TAG, destinationUser);
			
			loadingMessages(destinationUser);

			Intent intent = new Intent("ru.usu.intent.action.showmsgs");
			intent.putExtra("chosenContact", chosenContact.getText());
			Log.d("chosen_contact", chosenContact.getText().toString());

			startActivity(intent);
			
		}
		
		private void loadingMessages(String destinationUser) {
			
			new GetMentionsTimeLine().execute(App.MENTIONS_TIMELINE_URL);

			new SearchTwittsFromOauthUser(destinationUser).execute(App.SEARCH_TWITTS_URL);

		}
	}
}
