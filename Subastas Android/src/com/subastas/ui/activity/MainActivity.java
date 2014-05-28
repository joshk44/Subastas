package com.subastas.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.subastas.R;
import com.subastas.ui.adapter.MyFragmentPagerAdapter;
import com.subastas.ui.component.SlidingTabLayout;
import com.subastas.ui.component.SlidingTabLayout.TabColorizer;
import com.subastas.ui.fragment.ActualProducts;
import com.subastas.ui.fragment.FinishedProducts;
import com.subastas.ui.fragment.IncomingProducts;

public class MainActivity extends FragmentActivity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(
				this.getSupportFragmentManager(), true);
		try {
			adapter.addFragment(new FinishedProducts(), "Proximas Subastas");
			adapter.addFragment(new ActualProducts(), "Subastas Activas");
			adapter.addFragment(new IncomingProducts(), "Subastas Finalizadas");
		} catch (Exception e) {
			e.printStackTrace();
		}
		pager.setAdapter(adapter);
		SlidingTabLayout  titleIndicator = (SlidingTabLayout) findViewById(R.id.indicator);
		titleIndicator.setCustomTabColorizer(new TabColorizer() {
			
			@Override
			public int getIndicatorColor(int position) {
				switch (position){
				case 0:
					return Color.RED;
				case 1:
					return Color.BLUE;
				case 2:
					return Color.GREEN;
				}
				return 0;
			}
			
			@Override
			public int getDividerColor(int position) {
				
				return Color.GRAY;
			}
		});
		titleIndicator.setViewPager(pager);
	}
}
