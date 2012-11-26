package com.babyswipes;

import java.io.IOException;
import java.nio.charset.Charset;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class BabySwipes extends Activity implements OnClickListener {
	private static final String TAG = "BabySwipes";
	Button trainingButton;
	Button dataButton;
	private boolean mInWriteMode;

	private NfcAdapter mAdapter;
	private TextView mTextView;
	private Button mWriteTagButton;
	
    private BabySwipesDB mDataBase;
    private TextView myText = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_baby_swipes);

		trainingButton = (Button) findViewById(R.id.trainingButton);
		trainingButton.setOnClickListener(this);
		dataButton = (Button) findViewById(R.id.dataButton);
		dataButton.setOnClickListener(this);

		// grab our NFC Adapter
		mAdapter = NfcAdapter.getDefaultAdapter(this);

		// button that starts the tag-write procedure
		mWriteTagButton = (Button) findViewById(R.id.write_tag_button);
		mWriteTagButton.setOnClickListener(this);

		// TextView that we'll use to output messages to screen
		mTextView = (TextView) findViewById(R.id.text_view);
		
        Log.v(TAG, "START");
        
        trainingButton = (Button) findViewById(R.id.trainingButton);
        trainingButton.setOnClickListener(this);
        dataButton = (Button) findViewById(R.id.dataButton);
        dataButton.setOnClickListener(this);
        
        
        // Example Database Usage
        mDataBase = new BabySwipesDB(getBaseContext());
        
        mDataBase.clearAllData();              // only use if you want to wipe the data

        mDataBase.addTagType("Feeding");
        mDataBase.addTagType("Medicine");
        mDataBase.addTagType("Diaper");

        mDataBase.addSwipe("Feeding", 100);
        mDataBase.addSwipe("Feeding", 200);
        mDataBase.addSwipe("Feeding", 300);
        mDataBase.addSwipe("Feeding", 400);

        mDataBase.addSwipe("Medicine", 250);
        mDataBase.addSwipe("Medicine", 500);

        mDataBase.addSwipe("Diaper", 150);
        mDataBase.addSwipe("Diaper", 300);
        mDataBase.addSwipe("Diaper", 450);
        
        int numRecent = 5;
        BabySwipesDB.BabySwipe[] bs = mDataBase.getRecentSwipes(numRecent);
        
        Log.d(TAG, bs.length + " most recent swipes: ");
        for(int i=0; i<bs.length; ++i)
        {
        	Log.d(TAG, "tag: " + bs[i].tagName + ", time: " + bs[i].swipeTime);
        }
        

        int numTags = mDataBase.getNumberOfTags();
        
        TextView theText = (TextView)findViewById(R.id.numTagText2);
        theText.setText("" + numTags);
	}

	@Override
	protected void onPause() {
		super.onPause();
		disableWriteMode();
	}
	/**
	 * Force this Activity to get NFC events first
	 */
	private void enableWriteMode() {
		mInWriteMode = true;
		
		// set up a PendingIntent to open the app when a tag is scanned
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
            new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter[] filters = new IntentFilter[] { tagDetected };
        
		mAdapter.enableForegroundDispatch(this, pendingIntent, filters, null);
	}
	
	private void disableWriteMode() {
		mAdapter.disableForegroundDispatch(this);
	}

	public void onNewIntent(Intent intent) {
		if (mInWriteMode) {
			mInWriteMode = false;
			Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			writeTag(tag);
		}
	}

	/**
	 * TODO DELETE
	 * Temp code used to test the tag reading
	 * @param tag
	 * @return
	 */
	private boolean writeTag(Tag tag) {
		// record to launch Play Store if app is not installed
		NdefRecord appRecord = NdefRecord
				.createApplicationRecord("com.babyswipes");

		String activityName = "Diaper";

		// record that contains our custom "retro console" game data, using
		// custom MIME_TYPE
		byte[] activity = activityName.getBytes();
		byte[] mimeBytes = MimeType.NFC_MIME.getBytes(Charset.forName("US-ASCII"));
		NdefRecord activityNdef = new NdefRecord(NdefRecord.TNF_MIME_MEDIA,
				mimeBytes, new byte[0], activity);
		NdefMessage message = new NdefMessage(new NdefRecord[] { activityNdef,
				appRecord });

		try {
			// see if tag is already NDEF formatted
			Ndef ndef = Ndef.get(tag);
			if (ndef != null) {
				ndef.connect();

				if (!ndef.isWritable()) {
					displayMessage("Read-only tag.");
					return false;
				}

				// work out how much space we need for the data
				int size = message.toByteArray().length;
				if (ndef.getMaxSize() < size) {
					displayMessage("Tag doesn't have enough free space.");
					return false;
				}

				ndef.writeNdefMessage(message);
				displayMessage("Tag written successfully.");
				return true;
			} else {
				// attempt to format tag
				NdefFormatable format = NdefFormatable.get(tag);
				if (format != null) {
					try {
						format.connect();
						format.format(message);
						displayMessage("Tag written successfully!");
						return true;
					} catch (IOException e) {
						displayMessage("Unable to format tag to NDEF.");
						return false;
					}
				} else {
					displayMessage("Tag doesn't appear to support NDEF format.");
					return false;
				}
			}
		} catch (Exception e) {
			displayMessage("Failed to write tag");
		}

		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_baby_swipes, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		Intent i;
		switch (v.getId()) {
		case R.id.trainingButton:
			Log.d(TAG, "trainingButton onClicked");
			i = new Intent(this, TrainingActivity.class);
			startActivity(i);
			break;
		case R.id.dataButton:
			Log.d(TAG, "dataButton onCLicked");
			i = new Intent(this, DataActivity.class);
			startActivity(i);
			break;
		case R.id.write_tag_button:
			displayMessage("Touch and hold tag against phone to write.");
			enableWriteMode();
			break;
		}
	}

	private void displayMessage(String message) {
		mTextView.setText(message);
	}

}
