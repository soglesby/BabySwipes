package com.babyswipes;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.widget.TextView;

public class TagActivity extends Activity {

	private TextView tagText;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tag);

		tagText = (TextView) findViewById(R.id.textView1);

		// see if app was started from a tag and show game console
		Intent intent = getIntent();
		if (intent.getType() != null
				&& intent.getType().equals(MimeType.NFC_MIME)) {
			Parcelable[] rawMsgs = getIntent().getParcelableArrayExtra(
					NfcAdapter.EXTRA_NDEF_MESSAGES);
			NdefMessage msg = (NdefMessage) rawMsgs[0];
			NdefRecord activityNdef = msg.getRecords()[0];
			String activityName = new String(activityNdef.getPayload());
			displayActivity(activityName);
		}
	}

	private void displayActivity(String activityName) {

		 tagText.setText(activityName);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_tag, menu);
		return true;
	}
}
