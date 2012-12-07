package com.babyswipes;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.Window;

public class ReminderActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_reminder);

		Intent intent = getIntent();

		int seconds = intent.getExtras().getInt("seconds");

		// Get the Android AlarmManager (works like a cron job)
		AlarmManager alarmManager = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);

		// Activity that shows up when we timer goes off
		Intent reminderReceiver = new Intent(this, ReminderReceiver.class);
		reminderReceiver.putExtras(intent.getExtras());
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, reminderReceiver, 0);
		Calendar cal = Calendar.getInstance();
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() + seconds * 1000, seconds * 1000, pi);
		
		finish(); // close window 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_reminder, menu);
		return true;
	}
}
