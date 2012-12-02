package com.babyswipes;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.StringTokenizer;

import android.app.Activity;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.widget.TextView;

public class TagActivity extends Activity {

	private TextView tagText;
	private TextView timeText;
	private TextView errorText;

	private BabySwipesDB mDataBase;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tag);

		// Connection to the db
		mDataBase = new BabySwipesDB(getBaseContext());

		// Init text fields
		tagText = (TextView) findViewById(R.id.textView_Activity);
		timeText = (TextView) findViewById(R.id.textView_Timestamp);
		errorText = (TextView) findViewById(R.id.textView_ErrorMessage);

		// Read Tag
		Intent intent = getIntent();
		if (intent.getType() != null
				&& intent.getType().equals(MimeType.NFC_MIME)) {

			// Read NdefData
			Parcelable[] rawMsgs = getIntent().getParcelableArrayExtra(
					NfcAdapter.EXTRA_NDEF_MESSAGES);
			NdefMessage msg = (NdefMessage) rawMsgs[0];
			NdefRecord activityNdef = msg.getRecords()[0];

			// Display Data
			String payload = new String(activityNdef.getPayload());
			StringTokenizer st = new StringTokenizer(payload, "/");
			String activityName = st.nextToken();
			if (st.hasMoreTokens()) {
			    String reminderTime = st.nextToken();
			}
			
			Calendar currentTime = Calendar.getInstance();

			// FIXME : maybe store time in long ?
			int timestamp = (int) currentTime.getTimeInMillis();
			SimpleDateFormat sdf = new SimpleDateFormat("h:mm:ss a");
			String displayDate = sdf.format(currentTime.getTime());

			if (mDataBase.addSwipe(activityName, timestamp)) {
				tagText.setText(activityName);
				errorText.setText("");
				timeText.setText("Recorded at " + displayDate);
			} else {
				tagText.setText(activityName + " not recorded");
				errorText.setText(activityName
						+ " has not been registered with this device");
				timeText.setText("");
			}
			
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_tag, menu);
		return true;
	}

}
