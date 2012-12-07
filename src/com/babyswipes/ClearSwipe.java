package com.babyswipes;

import java.text.SimpleDateFormat;

import com.babyswipes.BabySwipesDB.BabySwipe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

public class ClearSwipe extends BaseActivity implements OnItemSelectedListener, OnItemClickListener, OnClickListener {
    private static final String TAG = "ClearSwipe";

    BabySwipesDB myDB;
    
    String[] tagTypes;
    
    ListView theListView;
    
    String[] currentTags;
    long[] currentTimes;
    
    String clickedTag;
    long clickedTime;
    long currentMenuSelection;
    
    public void onCreate(Bundle savedInstanceState)
    {
    	Spinner theSpinner;
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clear_swipe);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        myDB = new BabySwipesDB(getBaseContext());
        
    	String[] data = myDB.getAllTagNames();
        
        if (data != null)
        {
        	String[] new_data = new String[data.length+1];
        	new_data[0] = "all";
        	
        	for(int i=0; i<data.length; ++i)
        	{
        		new_data[i+1] = data[i];
        	}
        	
        	tagTypes = new_data;
            
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
    				android.R.layout.simple_list_item_1, new_data);

            theSpinner = (Spinner) findViewById(R.id.theSpinner);
            theSpinner.setAdapter(adapter);
            theSpinner.setOnItemSelectedListener(this);
        }

    	theListView = (ListView) findViewById(R.id.theClearSwipeListView);
    	theListView.setOnItemClickListener(this);
    }

	public ClearSwipe()
	{
	}

	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3)
	{
		Log.d(TAG, "Item Selected: " + arg3 + " (" + tagTypes[(int) arg3] + ")");
		
		currentMenuSelection = arg3;

        SimpleDateFormat formatter = new SimpleDateFormat("EEEE, MMM d");
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm:ss a");
        
        if(arg3 == 0)
        {
        	BabySwipe recent[];
        	recent = myDB.getRecentSwipes(999999999);
        	
        	if(recent != null)
        	{
	            String[] items = new String[recent.length];
	
	            currentTags = new String[recent.length];
	            currentTimes = new long[recent.length];
	        	
	        	for(int i=0; i<recent.length; ++i)
	        	{
	        		items[i] = recent[i].tagName + ": " + formatter.format(recent[i].swipeTime) + " at " + timeFormat.format(recent[i].swipeTime);
	        		currentTags[i] = recent[i].tagName;
	        		currentTimes[i] = recent[i].swipeTime;
	        	}
	            
	            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	    				android.R.layout.simple_list_item_1, items);
	            
            	theListView = (ListView) findViewById(R.id.theClearSwipeListView);
                theListView.setAdapter(adapter);
            	theListView.setOnItemClickListener(this);
        	}
        	else
        	{
	            String[] items = new String[1];
	            
	            items[0] = "no swipes recorded!";
	            
	            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	    				android.R.layout.simple_list_item_1, items);
	            
            	theListView = (ListView) findViewById(R.id.theClearSwipeListView);
                theListView.setAdapter(adapter);
            	theListView.setOnItemClickListener(null);
        	}
        }
        else
        {
            long recent[]; 
        	recent = myDB.getAllSwipeTimesForTag(tagTypes[(int) arg3]);

            String[] items = new String[recent.length];

            currentTags = new String[recent.length];
            currentTimes = new long[recent.length];
        	
        	for(int i=0; i<recent.length; ++i)
        	{
        		items[i] = formatter.format(recent[i]) + " at " + timeFormat.format(recent[i]);
        		
                currentTags[i] = tagTypes[(int) arg3];
        		currentTimes[i] = recent[i];
        	}
        	
        	if(recent.length == 0)
        	{
	            items = new String[1];
	            
	            items[0] = "no swipes recorded!";
        	}
            
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
    				android.R.layout.simple_list_item_1, items);
            
            if (recent != null)
            {
            	theListView = (ListView) findViewById(R.id.theClearSwipeListView);
                theListView.setAdapter(adapter);
                
                if(recent.length != 0)
                	theListView.setOnItemClickListener(this);
                else
                	theListView.setOnItemClickListener(null);
            }
        }
	}

	public void onNothingSelected(AdapterView<?> arg0)
	{
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		Log.d(TAG, "list item clicked: " + currentTags[(int) arg3] + " at " + currentTimes[(int) arg3]);

		clickedTag = currentTags[(int) arg3];
		clickedTime = currentTimes[(int) arg3];

        SimpleDateFormat formatter = new SimpleDateFormat("EEEE, MMM d");
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm:ss a");
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
 
		alertDialogBuilder.setTitle("Delete " + clickedTag + " swipe?");
 
		alertDialogBuilder.setMessage(formatter.format(currentTimes[(int) arg3]) + " at " + timeFormat.format(currentTimes[(int) arg3]));
		alertDialogBuilder.setCancelable(false);
		alertDialogBuilder.setPositiveButton("Yes", this);
		
		alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				dialog.cancel();
			}
		});
 
		AlertDialog alertDialog = alertDialogBuilder.create();
 
		alertDialog.show();
	}

	public void onClick(DialogInterface dialog, int which)
	{
		myDB.removeSwipe(clickedTag, clickedTime);
		
		this.onItemSelected(null, null, 0, currentMenuSelection);
	}
}
