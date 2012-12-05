package com.babyswipes;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.support.v4.app.NavUtils;

public class TrainingActivity extends BaseActivity implements OnItemClickListener {
    private static final String TAG = "TrainingActivity";
    ListView tagTypeList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        tagTypeList = (ListView) findViewById(R.id.tagTypeList);
        tagTypeList.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_training, menu);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
        Intent i = null;
        Log.d(TAG, "tagType " + pos + " selected");
        switch(pos) {
        case 0: //Feedings
            i = new Intent(this, FeedingConfig.class);
            break;
        case 1: //Diapers
            i = new Intent(this, DiaperConfig.class);
            break;
        case 2: //Medicine
            i = new Intent(this, MedicineConfig.class);
            break;
        case 3: //Nap
            i = new Intent(this, NapConfig.class);
            break;
        case 4: //Custom
            i = new Intent(this, CustomConfig.class);
            break;
        }
        if (i != null) 
            startActivity(i);
    }
}
