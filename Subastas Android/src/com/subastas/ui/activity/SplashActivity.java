package com.subastas.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;

import com.subastas.R;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		ConnectingTask connecting = new ConnectingTask();
		connecting.execute(this);
	}
	
	protected void onLoadingFinish(){
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
		this.finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}

class ConnectingTask extends AsyncTask<SplashActivity, Integer, Boolean>{
	SplashActivity activity;
	
	@Override
	protected Boolean doInBackground(SplashActivity... params) {
		activity = params[0];
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		activity.onLoadingFinish();
	}
}
