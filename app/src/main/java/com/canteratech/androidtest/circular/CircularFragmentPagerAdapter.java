package com.canteratech.androidtest.circular;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class CircularFragmentPagerAdapter extends FragmentPagerAdapter {

	private CircularViewPager viewPager;

	private List<Fragment> fragments = new ArrayList<>();

	public CircularFragmentPagerAdapter(CircularViewPager viewPager, FragmentManager fm) {
		super(fm);
		this.viewPager = viewPager;
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	public void add(Fragment fragment) {
		fragments.add(fragment);
		notifyDataSetChanged();
	}

	public void addAndGo(Fragment fragment) {
		add(fragment);
		viewPager.setCurrentItem(getCount() - 1);
	}

	public void remove(Fragment fragment) {
		fragments.remove(fragment);
		notifyDataSetChanged();
	}

	@Override
	public int getItemPosition(Object object) {
		Fragment fragment = (Fragment) object;
		int indexOf = fragments.indexOf(fragment);
		if (indexOf < 0) {
			return POSITION_NONE;
		}
		return indexOf;
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).hashCode();
	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}

	public void moveToCenter(int position) {
		int size = fragments.size();
		if (size < 3) {
			return;
		}
		int newPosition = size / 2;
		while (position < newPosition) {
			Fragment aux = fragments.remove(size - 1);
			fragments.add(0, aux);
			position++;
		}
		while (position > newPosition) {
			Fragment aux = fragments.remove(0);
			fragments.add(aux);
			position--;
		}
		notifyDataSetChanged();
	}
}
