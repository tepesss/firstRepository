package com.depech.filemanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.depech.filemanager.R;
import com.depech.filemanager.R.id;
import com.depech.filemanager.R.layout;
import com.depech.services.callList.CallListHandler;
import com.depech.utils.Themes;

public class Settings extends Activity implements OnClickListener {
	// theme to apply
	Button start, stop;

	String[] themes = { "Default", "Green", "Black" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		chooseThem();
		start = (Button) findViewById(R.id.startService);
		stop = (Button) findViewById(R.id.stopService);
		start.setOnClickListener(this);
		stop.setOnClickListener(this);

	}

	private void chooseThem() {
		ArrayAdapter<String> themesAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, themes);
		themesAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		Spinner themesSpiner = (Spinner) findViewById(R.id.themSpinner);
		themesSpiner.setAdapter(themesAdapter);
		themesSpiner.setPrompt("Choose Theme");

		themesSpiner.setSelection(Themes.getCurrentThemeInd());
		themesSpiner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long id) {
				Themes.changeTheme(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			startActivity(new Intent(Settings.this, FileManagerActivity.class));
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		if (v == start) {
			new CallListHandler().writeCurrentCallList(this);;
		} else if (v == stop) {

		}

	}

}
