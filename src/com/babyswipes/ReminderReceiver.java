package com.babyswipes;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ReminderReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent arg1) {
		
		String activityName = arg1.getExtras().getString("activity");
		
		int notificationId = activityName.length();
		
	    // Prepare intent which is triggered if the
	    // notification is selected
	    Intent intent = new Intent(context, ManualEntry.class);
	    PendingIntent pIntent = PendingIntent.getActivity(context, notificationId, intent, 0);

        NotificationManager notifier = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notify = new Notification.Builder(context)
        .setContentTitle(activityName)
        .setContentText("Time for " + activityName)
        .setWhen(System.currentTimeMillis())
        .setContentIntent(pIntent)
        .setSmallIcon(R.drawable.ic_launcher).getNotification();
        
        notifier.notify(notificationId, notify);	
    
	}
}
