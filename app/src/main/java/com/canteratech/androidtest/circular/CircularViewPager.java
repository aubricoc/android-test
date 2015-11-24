package com.canteratech.androidtest.circular;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

public class CircularViewPager extends ViewPager {

	private List<OnPageChangeListener> onPageChangeListeners = new ArrayList<>();

	public CircularViewPager(Context context) {
		super(context);
	}

	public CircularViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void setAdapter(PagerAdapter adapter) {
		if (!(adapter instanceof CircularFragmentPagerAdapter)) {
			throw new IllegalArgumentException("Adapter must be a CircularFragmentPagerAdapter");
		}

		CircularFragmentPagerAdapter circularFragmentPagerAdapter = (CircularFragmentPagerAdapter) adapter;
		super.setAdapter(adapter);
		circularFragmentPagerAdapter.moveToCenter(0);
		setCurrentItem(adapter.getCount() / 2, false);

		clearOnPageChangeListeners();
		super.addOnPageChangeListener(new CircularOnPageChangeListener(circularFragmentPagerAdapter, onPageChangeListeners));
	}

	@Override
	public void addOnPageChangeListener(OnPageChangeListener listener) {
		onPageChangeListeners.add(listener);
	}
}
