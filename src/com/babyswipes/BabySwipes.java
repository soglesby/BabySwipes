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
		
        Log.v(TAG, "START");
        
        trainingButton = (Button) findViewById(R.id.trainingButton);
        trainingButton.setOnClickListener(this);
        dataButton = (Button) findViewById(R.id.dataButton);
        dataButton.setOnClickListener(this);
        
        
        // Example Database Usage
        mDataBase = new BabySwipesDB(getBaseContext());
                
        int numRecent = 5;
        BabySwipesDB.BabySwipe[] bs = mDataBase.getRecentSwipes(numRecent);
        
        Log.d(TAG, bs.length + " most recent swipes: ");
        for(int i=0; i<bs.length; ++i)
        {
        	Log.d(TAG, "tag: " + bs[i].tagName + ", time: " + bs[i].swipeTime);
        }
    
        int numTags = mDataBase.getNumberOfTags();
        
        TextView theText = (TextView)findViewById(R.id.numTagText2);
        theText.setText("Number of tags recorded : " + numTags);
	}

	@Override
	protected void onPause() {
		super.onPause();
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
		}
	}


}
