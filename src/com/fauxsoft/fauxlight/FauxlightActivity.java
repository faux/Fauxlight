package com.fauxsoft.fauxlight;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class FauxlightActivity extends Activity {
	public static final String TAG = "Fauxlight";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FauxlightView view = new FauxlightView(getApplicationContext());
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFeatureInt(Window.FEATURE_NO_TITLE, 1);
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.screenBrightness = 0.1f;
		lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
		lp.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		view.setActivity(this);
		setContentView(view);
	}
}