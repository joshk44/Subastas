package com.subastas.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.subastas.R;
import com.subastas.ui.adapter.MyFragmentPagerAdapter;
import com.subastas.ui.fragment.ActualProducts;
import com.subastas.ui.fragment.FinishedProducts;
import com.subastas.ui.fragment.IncomingProducts;
import com.viewpagerindicator.TabPageIndicator;
import com.viewpagerindicator.UnderlinePageIndicator;

public class MainActivity extends FragmentActivity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(
				this.getSupportFragmentManager(), true);
		try {
			adapter.addFragment(new FinishedProducts(), "Proximos");
			adapter.addFragment(new ActualProducts(), "Actual");
			adapter.addFragment(new IncomingProducts(), "Finalizado");
		} catch (Exception e) {
			e.printStackTrace();
		}
		pager.setAdapter(adapter);
		TabPageIndicator  titleIndicator = (TabPageIndicator) findViewById(R.id.indicator);
		titleIndicator.setViewPager(pager);
	}
}
