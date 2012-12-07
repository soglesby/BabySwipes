package com.babyswipes;

import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.support.v4.app.NavUtils;

public class NapConfig extends Activity implements OnClickListener {
    private static final String TAG = "NapConfig";
	private static final int NO_ALARM = -1;
    private Spinner reminderSpinner;
    private Button programButton;
    private Button cancelButton;
    private NfcAdapter nfcAdapter;
    private boolean inWriteMode;
    private String activityPayload = "Nap";
    private int alertPayload;
    private BabySwipesDB mDataBase;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nap_config);
        
        mDataBase = new BabySwipesDB(getBaseContext());

        reminderSpinner = (Spinner) findViewById(R.id.reminderSpinner);
        programButton = (Button) findViewById(R.id.programButton);
        programButton.setOnClickListener(this);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(this);
        
        reminderSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
                Log.d(TAG, "Reminder item selected " + pos);
                switch(pos) {
                case 0:
                    alertPayload = NO_ALARM;
                    break;
                case 1: // 60 seconds
                    alertPayload = 60;
                    break;
                case 2: // 30 minutes
                	alertPayload = 1800;
                	break;
                case 3: // 1 hours
                    alertPayload = 360;
                    break;
                case 4: // 2 hours
                    alertPayload = 720;
                    break;
                case 5: // 3 hours 
                    alertPayload = 1080;
                    break;
                case 6: // 4 hours
                    alertPayload = 1440;
                    break;
                case 7: // 5 hours
                    alertPayload = 1800;
                    break;
                case 8: // 6 hours
                    alertPayload = 2160;
                    break;
                case 9: // 7 hours
                    alertPayload = 2520;
                    break;
                case 10: // 8 hours
                    alertPayload = 2880;
                    break;
                case 11: // 9 hours
                    alertPayload = 3240;
                    break;
                case 12: // 10 hours
                    alertPayload = 3600;
                    break;
                case 13: // 11 hours
                    alertPayload = 3960;
                    break;
                case 14: // 12 hours
                    alertPayload = 4320;
                    break;
	            case 15: // 24 hours
	                alertPayload = 8640;
	                break;
	            }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                
            }
        });
        
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_nap_config, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        //Intent i;
        switch(v.getId()) {
        case R.id.cancelButton:
            Log.d(TAG, "cancelButton onClicked");
            if (inWriteMode) {
                disableTagWrite();
            }
            NavUtils.navigateUpFromSameTask(this);
            break;
        case R.id.programButton:
            Log.d(TAG, "programButton onCLicked");
            Toast.makeText(this, R.string.nfcPrompt, Toast.LENGTH_LONG).show();
            enableTagWrite();
            break;
        }
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        disableTagWrite();
    }
    
    @Override
    public void onNewIntent(Intent intent) {
        boolean nfcResult = false;
        if(inWriteMode) {
            inWriteMode = false;
            
            // write to newly scanned tag
            String activity = activityPayload + "/" + alertPayload;
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            nfcResult = NfcUtil.programTag(tag, activity);
            if (nfcResult) {
                Toast.makeText(this, R.string.nfcSuccess, Toast.LENGTH_LONG).show();
                NavUtils.navigateUpFromSameTask(this);
                mDataBase.addTagType(activityPayload);
                if(alertPayload > NO_ALARM){
                	Intent reminderIntent = new Intent(this, ReminderActivity.class);
                	reminderIntent.putExtra("activity", activityPayload);
                	reminderIntent.putExtra("seconds", alertPayload);
                	startActivity(reminderIntent);
                	mDataBase.setReminderForTag(activityPayload, alertPayload);
                }
            }
        }
    }
    
    public void enableTagWrite() {
        inWriteMode = true;
        
        // set up a PendingIntent to open the app when a tag is scanned
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
            new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter[] filters = new IntentFilter[] { tagDetected };
        
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, filters, null);
        
    }
    
    private void disableTagWrite() {
        nfcAdapter.disableForegroundDispatch(this);
    }
    
}
