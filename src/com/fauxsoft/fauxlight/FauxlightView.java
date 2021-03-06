package com.fauxsoft.fauxlight;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class FauxlightView extends View {

	public FauxlightView(Context context) {
		super(context);
		setBackgroundColor(0xffffffff);
		setLongClickable(true);
		setKeepScreenOn(true);
		setFocusableInTouchMode(true);
		setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				int red = (int) (event.getY() / v.getHeight() * 255);
				int green = (int) (event.getX() / v.getWidth() * 255);
				int blue = (int) ((event.getPressure() - .1) / .40 * 255);

				if (red > 255)
					red = 255;
				if (green > 255)
					green = 255;
				if (blue > 255)
					blue = 255;
				if (red < 0)
					red = 0;
				if (green < 0)
					green = 0;
				if (blue < 0)
					blue = 0;

				// Log.d(FauxlightActivity.TAG, "red:   " +
				// Integer.toString(red)
				// + " green: " + Integer.toString(green) + " blue:  "
				// + Integer.toString(blue) + " press="
				// + event.getPressure());

				blue <<= 0;
				green <<= 8;
				red <<= 16;

				int color = red | green | blue | 255 << 24;
				// Log.d(FauxlightActivity.TAG, "color: "
				// + Integer.toHexString(color) + " red:   "
				// + Integer.toString(red) + " green: "
				// + Integer.toString(green) + " blue:  "
				// + Integer.toString(blue) + " press="
				// + event.getPressure());
				v.setBackgroundColor(color);
				return true;
			}
		});
	}

	private Activity activity = null;

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	private final Toast max = Toast.makeText(getContext(),
			R.string.max_brightness, 2);
	private final Toast min = Toast.makeText(getContext(),
			R.string.min_brightness, 2);

	private boolean adjustScreenBrightness(float brightness) {
		WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
		if (lp != null) {
			lp.screenBrightness += brightness;
			if (lp.screenBrightness > 1) {
				lp.screenBrightness = 1;
				max.show();
			} else if (lp.screenBrightness <= 0.1f) {
				lp.screenBrightness = 0.1f;
				min.show();
			} else {
				min.cancel();
				max.cancel();
			}

			Log.d(FauxlightActivity.TAG, "brightness: " + lp.screenBrightness);
			activity.getWindow().setAttributes(lp);
			return true;
		} else {
			Log.d(FauxlightActivity.TAG, "no layout params");
			return false;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.d(FauxlightActivity.TAG, event.toString());
		if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER) {
			setBackgroundColor(0xffffffff);
			return true;
		}

		switch (event.getKeyCode()) {
		case KeyEvent.KEYCODE_DPAD_DOWN:
		case KeyEvent.KEYCODE_DPAD_LEFT:
			return adjustScreenBrightness(-0.2f);
		case KeyEvent.KEYCODE_DPAD_UP:
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			return adjustScreenBrightness(0.2f);
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onTrackballEvent(MotionEvent event) {
		Log.d(FauxlightActivity.TAG, event.toString());
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			setBackgroundColor(0xffffffff);
			return true;
		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			return adjustScreenBrightness(event.getX() / 10 + event.getY() / 10);
		}
		return super.onTrackballEvent(event);
	}
}
