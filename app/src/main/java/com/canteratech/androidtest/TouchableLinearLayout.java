package com.canteratech.androidtest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class TouchableLinearLayout extends LinearLayout {
	public TouchableLinearLayout(Context context) {
		super(context);
	}

	public TouchableLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TouchableLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public boolean onTouchEvent(MotionEvent motionEvent) {
		System.out.println("LinearLayout onTouchEvent: " + motionEvent.getAction());
		boolean a = super.onTouchEvent(motionEvent);
		a = true;
		System.out.println("LinearLayout onTouchEvent: " + motionEvent.getAction() + " = " + a);
		return a;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
		System.out.println("LinearLayout onInterceptTouchEvent: " + motionEvent.getAction());
		boolean a =  super.onInterceptTouchEvent(motionEvent);
		a = true;
		System.out.println("LinearLayout onInterceptTouchEvent: " + motionEvent.getAction() + " = " + a);
		return a;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent motionEvent) {
		System.out.println("---------------------------------------------");
		System.out.println("LinearLayout dispatchTouchEvent: " + motionEvent.getAction());
		boolean a =  super.dispatchTouchEvent(motionEvent);
		System.out.println("LinearLayout dispatchTouchEvent: " + motionEvent.getAction() + " = " + a);
		return a;
	}
}
