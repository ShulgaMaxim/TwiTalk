package ru.usu.twitalk.activities;

import ru.usu.twitalk.Data;
import ru.usu.twitalk.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MsgsActivity extends Activity {
	
	private Data instance = Data.getInstance();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.msgs);
		
	    TextView tvView = (TextView) findViewById(R.id.msgsHeader);
		
		Intent intent = getIntent();
	    String chosenContact = intent.getStringExtra("chosenContact");
	    
	    tvView.setText("Tweets from " + chosenContact);
	    
	    // ������� ������
	    ListView lvMsgs = (ListView) findViewById(R.id.lvMsgs);
	    
	    // ������� �������
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	    		android.R.layout.simple_list_item_1, instance.contactsWithMsgs.get(chosenContact));
	    
	    // ����������� ������� ������
	    lvMsgs.setAdapter(adapter);    
	}
}