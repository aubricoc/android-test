package com.canteratech.androidtest;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TestReceiver extends BroadcastReceiver {

	private int i = 0;

	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("TESTING... received!");

		if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
			i+=10;
			System.out.println("TESTING... booted!");
			Notification n = NotificationUtils.createNotification(context,
					456122, intent, R.drawable.marker_blue,
					"REBOOTED", "Reboot", "Touch to open it " + i);
			NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			notificationManager.notify(456122, n);
		} else {
			i++;
			System.out.println("TESTING... notify!");
			Notification n = NotificationUtils.createNotification(context,
					4561223, intent, R.drawable.marker_blue,
					"You receive a new communication", "You receive the communication #", "Touch to open it " + i);
			NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			notificationManager.notify(4561223, n);
		}
	}
}
