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

public class DataActivity extends Activity implements OnItemClickListener {
	
	private static final String DATA_TAG = "DataActivity";
	ListView tagTypeList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        tagTypeList = (ListView) findViewById(R.id.tagTypeList);
        tagTypeList.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_data, menu);
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
    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
        Intent i = null;
        Log.d(DATA_TAG, "tagType " + pos + " selected");
        switch(pos) {
        case 0: //Feedings
            i = new Intent(this, FeedingGraph.class);    
            break;
        case 1: //Diapers
            i = new Intent(this, DiaperGraph.class);
            break;
        case 2: //Nap
            i = new Intent(this, NapGraph.class);
            break;
        case 3: //Medicine
            i = new Intent(this, MedicineGraph.class);
             break;    
        }
        if (i != null) 
            startActivity(i);
    }

}