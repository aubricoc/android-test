package com.canteratech.androidtest;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public abstract class NotificationUtils {

	private static final int CURRENT_API_VERSION = Build.VERSION.SDK_INT;

	public static Notification createNotification(Context context, int requestCode, Intent onClickIntent, int icon, String tickerText, String contentTitle, String contentText) {

		PendingIntent pendingIntent = PendingIntent.getActivity(context, requestCode, onClickIntent, 0);

		Notification notification;
		if (CURRENT_API_VERSION >= Build.VERSION_CODES.LOLLIPOP) {
			notification = createNotificationApi21(context, pendingIntent, icon, tickerText, contentTitle, contentText);
		} else if (CURRENT_API_VERSION >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
			notification = createNotificationApi16(context, pendingIntent, icon, tickerText, contentTitle, contentText);
		} else {
			notification = createNotificationApi11(context, pendingIntent, icon, tickerText, contentTitle, contentText);
		}

		return notification;
	}

 	@SuppressWarnings("deprecation")
	@TargetApi(11)
	private static Notification createNotificationApi11(Context context, PendingIntent pendingIntent, int icon, String tickerText, String contentTitle, String contentText) {

		Notification.Builder notificationBuilder = new Notification.Builder(context);

		notificationBuilder.setAutoCancel(true);
		notificationBuilder.setSmallIcon(icon);
		notificationBuilder.setTicker(tickerText);
		notificationBuilder.setWhen(System.currentTimeMillis());
		notificationBuilder.setContentIntent(pendingIntent);
		notificationBuilder.setContentTitle(contentTitle);
		notificationBuilder.setContentText(contentText);

		return notificationBuilder.getNotification();
	}

	@TargetApi(16)
	private static Notification createNotificationApi16(Context context, PendingIntent pendingIntent, int icon, String tickerText, String contentTitle, String contentText) {

		Notification.Builder notificationBuilder = new Notification.Builder(context);

		notificationBuilder.setAutoCancel(true);
		notificationBuilder.setSmallIcon(icon);
		notificationBuilder.setTicker(tickerText);
		notificationBuilder.setWhen(System.currentTimeMillis());
		notificationBuilder.setContentIntent(pendingIntent);
		notificationBuilder.setContentTitle(contentTitle);
		notificationBuilder.setContentText(contentText);

		return notificationBuilder.build();
	}

	@TargetApi(21)
	private static Notification createNotificationApi21(Context context, PendingIntent pendingIntent, int icon, String tickerText, String contentTitle, String contentText) {

		Notification.Builder notificationBuilder = new Notification.Builder(context);

		notificationBuilder.setAutoCancel(true);
		notificationBuilder.setSmallIcon(icon);
		notificationBuilder.setTicker(tickerText);
		notificationBuilder.setWhen(System.currentTimeMillis());
		notificationBuilder.setContentIntent(pendingIntent);
		notificationBuilder.setContentTitle(contentTitle);
		notificationBuilder.setContentText(contentText);

		return notificationBuilder.build();
	}
}
