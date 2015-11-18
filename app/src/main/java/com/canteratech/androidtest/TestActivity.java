package com.canteratech.androidtest;

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
	}

	private void go() {
		printLocation(R.id.hello);
		printLocation(R.id.go);
		printLocation(R.id.scroll);
		printLocation(R.id.list);
		printLocation(R.id.paco);
		printLocation(R.id.pepe);
		printLocation(R.id.poco);
	}

	private void printLocation(int id) {
		int[] loc = new int[2];
		View view = findViewById(id);
		System.out.println("-----" + id);
		view.getLocationOnScreen(loc);
		System.out.println("getLocationOnScreen X:" + loc[0] + " Y: " + loc[1]);
		view.getLocationInWindow(loc);
		System.out.println("getLocationInWindow X:" + loc[0] + " Y: " + loc[1]);
		System.out.println("-----" + id);
	}
}
