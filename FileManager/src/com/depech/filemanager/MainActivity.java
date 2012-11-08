package com.depech.filemanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);       
        ImageView rotatedImage = (ImageView) findViewById(R.id.imageView2);
        Animation rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        rotateAnimation.setAnimationListener(new AnimationListener(){
        	@Override
        	public void onAnimationEnd(Animation animation) {
        		startActivity(new Intent (MainActivity.this, FileManagerActivity.class));
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
