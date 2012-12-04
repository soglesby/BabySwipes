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
    private Spinner reminderSpinner;
    private Button programButton;
    private Button cancelButton;
    private NfcAdapter nfcAdapter;
    private boolean inWriteMode;
    private String activityPayload = "Nap";
    private String alertPayload;
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
                    alertPayload = "none";
                    break;
                case 1:
                    alertPayload = "1h";
                    break;
                case 2:
                    alertPayload = "2h";
                    break;
                case 3:
                    alertPayload = "3h";
                    break;
                case 4:
                    alertPayload = "4h";
                    break;
                case 5:
                    alertPayload = "5h";
                    break;
                case 6:
                    alertPayload = "6h";
                    break;
                case 7:
                    alertPayload = "7h";
                    break;
                case 8:
                    alertPayload = "8h";
                    break;
                case 9:
                    alertPayload = "9h";
                    break;
                case 10:
                    alertPayload = "10h";
                    break;
                case 11:
                    alertPayload = "11h";
                    break;
                case 12:
                    alertPayload = "12h";
                    break;
                case 13:
                    alertPayload = "24h";
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
