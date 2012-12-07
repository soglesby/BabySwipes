package com.babyswipes;

import java.util.ArrayList;
import java.util.Arrays;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class DataActivity extends BaseActivity implements OnItemClickListener {

private static final String DATA_TAG = "DataActivity";

   // ListView tagTypeList; //OLD
    BabySwipesDB myDB;


    ListView mainListView = null;
    private ArrayAdapter<String> listAdapter;
    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        // START DYNAMIC LIST
        mainListView = (ListView) findViewById( R.id.tagTypeList ); //tagType ID in xml
        
        
        myDB = new BabySwipesDB(getBaseContext());
        int numTags = myDB.getNumberOfTags(); // get number of tag types
        String[] menuData = new String[numTags];  // string array to hold them
        menuData = myDB.getAllTagNames();         // retrieve them
        
        ArrayList<String> planetList = new ArrayList<String>();  
        planetList.addAll( Arrays.asList(menuData) );
        
        listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, planetList);
                     
        mainListView.setAdapter( listAdapter );
        
        mainListView.setOnItemClickListener(this);
        
        

                
        // END DYNAMIC LIST        
        //tagTypeList = (ListView) findViewById(R.id.tagTypeList);
        //tagTypeList.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_data, menu);
        return true;
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
    	   	
    	String text = parent.getItemAtPosition(pos).toString(); // tag name comes from DB
    	Log.d(DATA_TAG, "SELECTED ITEM"+text);    	
    	
        Intent i = null;
        i = new Intent(this, DiaperGraph.class);
        
        i.putExtra("key", text); // send tag name to new activity

        if (i != null)
            startActivity(i);
    }

}