package com.canteratech.androidtest;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class TestActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);

		findViewById(R.id.go).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				go();
			}
		});
		findViewById(R.id.stop).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				stop();
			}
		});
	}



	private void go() {
		Intent intent = new Intent(this, TestReceiver.class);
		PendingIntent pintent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarm.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, pintent);
	}

	private void stop() {
		AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
		Intent intent = new Intent(this, TestReceiver.class);
		PendingIntent pintent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		alarm.cancel(pintent);
	}
}
