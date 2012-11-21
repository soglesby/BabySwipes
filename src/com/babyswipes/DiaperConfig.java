package com.babyswipes;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.support.v4.app.NavUtils;

public class DiaperConfig extends Activity implements OnClickListener{
    private static final String TAG = "DiaperConfig";
    private Spinner diaperSpinner;
    private Button programButton;
    private Button cancelButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diaper_config);

        diaperSpinner = (Spinner) findViewById(R.id.diaperSpinner);
        programButton = (Button) findViewById(R.id.programButton);
        programButton.setOnClickListener(this);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(this);
        
        diaperSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
                Log.d(TAG, "Diaper item selected " + pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                
            }
        });
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
            NavUtils.navigateUpFromSameTask(this);
            break;
        case R.id.programButton:
            Log.d(TAG, "programButton onCLicked");
            //i = new Intent(this, DataActivity.class);
            //startActivity(i);
            break;
        }
    }
}