package com.canteratech.androidtest.draganddrop;

import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class Bucket {

	private String name;

	private int color;

	private List<BucketItem> items;

	private View bucketView;

	private ViewGroup container;

	private ViewGroup list;

	private View additionalView;

	private boolean active;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public List<BucketItem> getItems() {
		return items;
	}

	public void setItems(List<BucketItem> items) {
		this.items = items;
	}

	public View getBucketView() {
		return bucketView;
	}

	public void setBucketView(View bucketView) {
		this.bucketView = bucketView;
	}

	public ViewGroup getContainer() {
		return container;
	}

	public void setContainer(ViewGroup container) {
		this.container = container;
	}

	public ViewGroup getList() {
		return list;
	}

	public void setList(ViewGroup list) {
		this.list = list;
	}

	public View getAdditionalView() {
		return additionalView;
	}

	public void setAdditionalView(View additionalView) {
		this.additionalView = additionalView;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
