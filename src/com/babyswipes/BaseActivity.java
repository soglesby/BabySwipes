package com.babyswipes;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;

public class BaseActivity extends Activity {
	
	/**
	 * Determine actions for the main horizontal toolbar
	 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	Log.d(getResources().getString(R.string.app_name), "Item Id : " + item.getItemId());
        switch(item.getItemId())
        {
        case android.R.id.home:
            NavUtils.navigateUpFromSameTask(this);
            return true;
        case R.id.menu_settings:
            Intent in = new Intent(this, Settings.class);
            startActivity(in); 
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }

    }

}
