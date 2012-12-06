package com.babyswipes;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.support.v4.app.NavUtils;

public class Settings extends BaseActivity implements OnItemClickListener {

	private BabySwipesDB mDataBase;
    private ListView theList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        theList = (ListView) findViewById(R.id.settingsActivityListView);
        theList.setOnItemClickListener(this);

		// Connection to the db
		mDataBase = new BabySwipesDB(getBaseContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_settings, menu);
        return true;
    }
    
    //@Override
    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
        //Log.d(TAG, pos + " selected");
        switch(pos) {
        case 0:
        	new AlertDialog.Builder(this)
            .setTitle("Clear All Data")
            .setMessage("Are you sure you want to clear all programmed tags and data?")
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) { 
                	mDataBase.clearAllData();
                    Toast.makeText(Settings.this, R.string.eraseDB, Toast.LENGTH_LONG).show();
                }
             })
            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) { 
                    // do nothing
                }
             })
             .show();
            break;
        case 1:
            //Toast.makeText(Settings.this, "Not Implemented Yet", Toast.LENGTH_LONG).show();
            
            Intent i = new Intent(this, ClearSwipe.class);
            startActivity(i);
            
            break;   
        }
    }

}
