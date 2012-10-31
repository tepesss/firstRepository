package com.equationandroidapplication;

import com.equationandroidapplication.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class EquationActivity extends Activity implements OnClickListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        View mStartButton = findViewById(R.id.start);
        mStartButton.setOnClickListener(this);
    }
    @Override
	public void onClick(View view) {
		Intent intent = new Intent(this, App.class);
		startActivity(intent);	
    }
}
