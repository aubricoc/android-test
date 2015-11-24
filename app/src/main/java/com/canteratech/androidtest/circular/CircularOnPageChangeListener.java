package com.canteratech.androidtest.circular;

import android.support.v4.view.ViewPager;

import java.util.List;

public class CircularOnPageChangeListener implements ViewPager.OnPageChangeListener {

	private Object currentObject = null;

	private CircularFragmentPagerAdapter adapter;

	private List<ViewPager.OnPageChangeListener> onPageChangeListeners;

	public CircularOnPageChangeListener(CircularFragmentPagerAdapter adapter, List<ViewPager.OnPageChangeListener> onPageChangeListeners) {
		this.adapter = adapter;
		this.onPageChangeListeners = onPageChangeListeners;
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		for (ViewPager.OnPageChangeListener onPageChangeListener : onPageChangeListeners) {
			onPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
		}
	}

	@Override
	public final void onPageSelected(int position) {
		Object object = adapter.getItem(position);

		if (currentObject != object) {
			currentObject = object;
			for (ViewPager.OnPageChangeListener onPageChangeListener : onPageChangeListeners) {
				onPageChangeListener.onPageSelected(position);
			}
		}
		adapter.moveToCenter(position);
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		for (ViewPager.OnPageChangeListener onPageChangeListener : onPageChangeListeners) {
			onPageChangeListener.onPageScrollStateChanged(state);
		}
	}
}
