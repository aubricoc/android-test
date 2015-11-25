package com.canteratech.androidtest.circular;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.canteratech.androidtest.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CircularViewPagerActivity extends AppCompatActivity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link FragmentPagerAdapter} derivative, which will keep every
	 * loaded fragment in memory. If this becomes too memory intensive, it
	 * may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	private CircularFragmentPagerAdapter adapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	private CircularViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_circular_view_pager);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		mViewPager = (CircularViewPager) findViewById(R.id.container);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		adapter = new CircularFragmentPagerAdapter(mViewPager, getSupportFragmentManager());

		adapter.add(PlaceholderFragment.newInstance(1, android.R.color.holo_orange_dark));
		adapter.add(PlaceholderFragment.newInstance(2, android.R.color.holo_green_dark));
		adapter.add(PlaceholderFragment.newInstance(3, android.R.color.holo_blue_dark));
		adapter.add(PlaceholderFragment.newInstance(4, android.R.color.holo_purple));
		adapter.add(PlaceholderFragment.newInstance(5, android.R.color.holo_red_dark));
		adapter.add(PlaceholderFragment.newInstance(6, android.R.color.holo_green_light));
		adapter.add(PlaceholderFragment.newInstance(7, android.R.color.holo_orange_light));
		adapter.add(PlaceholderFragment.newInstance(8, android.R.color.holo_blue_light));

		// Set up the ViewPager with the sections adapter.

		mViewPager.setAdapter(adapter);
		mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				System.out.println("Page Selected: " + position);
			}
		});

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
						.setAction("Action", null).show();
			}
		});
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_circular_view_pager, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			adapter.addAndGo(PlaceholderFragment.newInstance(new Random().nextInt(), android.R.color.holo_red_dark));
		} else if (id == R.id.action_remove) {
			adapter.remove(adapter.getItem(adapter.getCount() - 1));
		} else if (id == R.id.action_info) {
			System.out.println("POSITION: " + mViewPager.getCurrentItem() + " COUNT: " + adapter.getCount());
		} else if (id == R.id.action_go) {
			mViewPager.setCurrentItem(0);
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";
		private static final String ARG_SECTION_COLOR = "section_color";

		/**
		 * Returns a new instance of this fragment for the given section
		 * number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber, int color) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			args.putInt(ARG_SECTION_COLOR, color);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
								 Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_circular_view_pager, container, false);
			TextView textView = (TextView) rootView.findViewById(R.id.section_label);
			textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
			rootView.setBackgroundColor(getResources().getColor(getArguments().getInt(ARG_SECTION_COLOR)));
			return rootView;
		}
	}
}
