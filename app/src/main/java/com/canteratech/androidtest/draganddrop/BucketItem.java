package com.canteratech.androidtest.draganddrop;

import android.view.View;

public class BucketItem {

	private String name;

	private Bucket bucket;

	private View view;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Bucket getBucket() {
		return bucket;
	}

	public void setBucket(Bucket bucket) {
		this.bucket = bucket;
	}

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}
}
