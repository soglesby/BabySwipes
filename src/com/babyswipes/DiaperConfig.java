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

public class DiaperConfig extends Activity implements OnClickListener{
    private static final String TAG = "DiaperConfig";
    private Spinner diaperSpinner;
    private Button programButton;
    private Button cancelButton;
    private NfcAdapter nfcAdapter;
    private boolean inWriteMode;
    private String activityPayload;
    private BabySwipesDB mDataBase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diaper_config);
        
        mDataBase = new BabySwipesDB(getBaseContext());

        diaperSpinner = (Spinner) findViewById(R.id.diaperSpinner);
        programButton = (Button) findViewById(R.id.programButton);
        programButton.setOnClickListener(this);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(this);
        
        diaperSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
                Log.d(TAG, "Diaper item selected " + pos);
                switch(pos) {
                case 0:
                    activityPayload = "";
                    break;
                case 1:
                    activityPayload = "wet";
                    break;
                case 2:
                    activityPayload = "bm";
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
        getMenuInflater().inflate(R.menu.activity_diaper_config, menu);
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
            String activity = "diaper/" + activityPayload;
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            nfcResult = NfcUtil.programTag(tag, activity);
            if (nfcResult) {
                Toast.makeText(this, R.string.nfcSuccess, Toast.LENGTH_LONG).show();
                NavUtils.navigateUpFromSameTask(this);
                mDataBase.addTagType(activity);
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
