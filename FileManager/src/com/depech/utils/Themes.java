package com.depech.utils;

import android.app.Activity;


public class Themes {
	private static int cTheme;

	public final static int THEME_DEFAULT = 0;
	public final static int THEME_GREEN = 1;
	public final static int THEME_BLACK = 2;

	/**
	 * Set the theme of the Activity, and restart it by creating a new Activity
	 * of the same type.
	 */
	public static void changeTheme(int theme){
		cTheme = theme;
	}
	public static int getCurrentThemeInd(){
		return cTheme;
	}

	/** Set the theme of the activity, according to the configuration. */
	public static void onActivityCreateSetTheme(Activity activity)
	{
		switch (cTheme)
		{
		default:
		case THEME_DEFAULT:
			break;
		case THEME_GREEN:
			//activity.setTheme(R.style.Theme_Green);
			break;
		case THEME_BLACK:
			//activity.setTheme(R.style.Theme_Black);
			break;
		}
	}
}
