package com.canteratech.androidtest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class TouchableImageView extends ImageView {
	public TouchableImageView(Context context) {
		super(context);
	}

	public TouchableImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TouchableImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public boolean onTouchEvent(MotionEvent motionEvent) {
		System.out.println("ImageView onTouchEvent: " + motionEvent.getAction());
		boolean a =  super.onTouchEvent(motionEvent);
		a = true;
		System.out.println("ImageView onTouchEvent: " + motionEvent.getAction() + " = " + a);
		return a;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent motionEvent) {
		System.out.println("ImageView dispatchTouchEvent: " + motionEvent.getAction());
		boolean a =  super.dispatchTouchEvent(motionEvent);
		System.out.println("ImageView dispatchTouchEvent: " + motionEvent.getAction() + " = " + a);
		return a;
	}
}
