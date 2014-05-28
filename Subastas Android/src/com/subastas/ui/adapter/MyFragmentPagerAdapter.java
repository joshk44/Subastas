package com.subastas.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * 
 * @author Joshk
 * 
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

	private List<Fragment> fragments;
	private List<String> pageTitles;
	private boolean hasTitle;

	public MyFragmentPagerAdapter(FragmentManager fm, boolean hasTitle) {
		super(fm);
		this.fragments = new ArrayList<Fragment>();
		this.pageTitles = new ArrayList<String>();
		this.hasTitle = hasTitle;
	}

	public void addFragment(Fragment fragment) throws Exception {
		if (!hasTitle) {
			this.fragments.add(fragment);
		} else {
			throw new Exception("You should insert a fragment with title.");
		}
	}

	public void addFragment(Fragment fragment, String title) {
		this.fragments.add(fragment);
		this.pageTitles.add(title);
	}

	@Override
	public Fragment getItem(int arg0) {
		return this.fragments.get(arg0);
	}

	@Override
	public int getCount() {
		return this.fragments.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return pageTitles.get(position);
	}
}
