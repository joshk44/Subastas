/**
 * 
 */
package com.subastas.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.subastas.R;
import com.subastas.ui.adapter.MyFragmentPagerAdapter;
import com.subastas.ui.fragment.Login;
import com.subastas.ui.fragment.SignUp;

/**
 * @author josh
 *
 */
public class LoginActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		ViewPager pager = (ViewPager) findViewById(R.id.login_pager);
		MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(this.getSupportFragmentManager(),false);
		try {
			adapter.addFragment(new Login());
			adapter.addFragment(new SignUp());
		} catch (Exception e) {
			e.printStackTrace();
		}
		pager.setAdapter(adapter);
		
	}
}
