package com.babyswipes;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MotionEvent;
import android.widget.TextView;

public class TagActivity extends Activity implements OnGestureListener {

	private TextView tagText;
	private TextView timeText;
	private TextView errorText;
	
	private GestureDetector gDetector;
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
		if (intent.getType() != null && intent.getType().equals(MimeType.NFC_MIME)) {
			
			// Read NdefData
			Parcelable[] rawMsgs = getIntent().getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
			NdefMessage msg = (NdefMessage) rawMsgs[0];
			NdefRecord activityNdef = msg.getRecords()[0];
			
			// Display Data
			String activityName = new String(activityNdef.getPayload());
			
			Calendar currentTime = Calendar.getInstance();
			
			// FIXME : maybe store time in long ? 
			int timestamp = (int) currentTime.getTimeInMillis();
			SimpleDateFormat sdf = new SimpleDateFormat("Recorded at h:mm:ss a");
			String displayDate = sdf.format(currentTime.getTime());
			timeText.setText(displayDate);
			
			if(mDataBase.addSwipe(activityName, timestamp)){
				tagText.setText(activityName);
				errorText.setText("");
			} else {
				tagText.setText(activityName + " not recorded");
				errorText.setText(activityName + " has not been registered with this device");
			}
			
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_tag, menu);
		return true;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
}
