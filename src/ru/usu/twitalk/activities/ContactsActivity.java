package ru.usu.twitalk.activities;

import ru.usu.twitalk.App;
import ru.usu.twitalk.Data;
import ru.usu.twitalk.R;
import ru.usu.twitalk.twitter.GetMentionsTimeLine;
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

			lvContacts.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					TextView chosenContact = (TextView) view.findViewById(view
							.getId());

//					long idContact = instance.users.get(chosenContact.getText().toString()).getId();
//					Log.d(TAG, ""+idContact);
					
//					new GetUserTimeLine(idContact).execute(App.USER_TIMELINE_URL);
					new GetMentionsTimeLine().execute(App.MENTIONS_TIMELINE_URL);
					
					Intent intent = new Intent("ru.usu.intent.action.showmsgs");
					intent.putExtra("chosenContact", chosenContact.getText());
					Log.d("chosen_contact", chosenContact.getText().toString());

					startActivity(intent);
					
				}
			});
		}

	}
}
