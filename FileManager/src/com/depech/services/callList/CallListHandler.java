package com.depech.services.callList;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.util.Log;

import com.depech.utils.TelephonyFileLoger;

public class CallListHandler {
	private static Logger logger = LogManager.getLogger(CallListHandler.class
			.getSimpleName());

	private static final String[] projection = new String[] {
			CallLog.Calls.NUMBER, CallLog.Calls.DATE,
			CallLog.Calls.CACHED_NAME, CallLog.Calls.DURATION };

	public static void writeCurrentCallList(Context context) {
		final String sMethod = ":[writeCurrentCallList]:";

		Cursor cur = context.getContentResolver().query(
				CallLog.Calls.CONTENT_URI, projection, null, null,
				CallLog.Calls.DATE + " ASC");
		// activity.managedQuery(contacts, projection, null, null,
		// CallLog.Calls.DATE + " ASC");

		StringBuffer buffer = null;
		try {
			if (cur.moveToFirst()) {
				
				Log.i( sMethod,"Reading Call Details");
				System.out.println("Reading Call Details: ");
				buffer = new StringBuffer();

				long time = System.currentTimeMillis();
				String name, number, duration;
				long date;
				int nameColumn = cur.getColumnIndex(CallLog.Calls.CACHED_NAME);
				int numberColumn = cur.getColumnIndex(CallLog.Calls.NUMBER);
				int dateColumn = cur.getColumnIndex(CallLog.Calls.DATE);
				int durationClumn = cur.getColumnIndex(CallLog.Calls.DURATION);
				do {
					name = cur.getString(nameColumn);
					number = cur.getString(numberColumn);
					date = cur.getLong(dateColumn);
					duration = cur.getString(durationClumn);
					String stringToWrite = number + ":" + new Date(date) + ":"
							+ name + " " + duration + "\n";
					System.out.println(stringToWrite);
					buffer.append(stringToWrite);
				} while (cur.moveToNext());
				System.out.println(System.currentTimeMillis() - time);
				TelephonyFileLoger.getInstance().writeToCurrentCallLogFileName(
						buffer.toString());
				System.out.println(System.currentTimeMillis() - time);
			}
		} finally {
			if (cur != null) {
				cur.close();
			}
		}
	}

	public static void writeCallToList(Context context) {
		final String sMethod = ":[writeCallToList]:";
		Cursor cur = context.getContentResolver().query(
				CallLog.Calls.CONTENT_URI, projection, null, null,
				CallLog.Calls.DATE + " ASC");
		StringBuffer buffer = null;
		try {
			if (cur.moveToLast()) {
				buffer = new StringBuffer();
				long time = System.currentTimeMillis();
				String name, number, duration;
				long date;
				int nameColumn = cur.getColumnIndex(CallLog.Calls.CACHED_NAME);
				int numberColumn = cur.getColumnIndex(CallLog.Calls.NUMBER);
				int dateColumn = cur.getColumnIndex(CallLog.Calls.DATE);
				int durationClumn = cur.getColumnIndex(CallLog.Calls.DURATION);
				date = cur.getLong(dateColumn);
				if (date != TelephonyFileLoger.getInstance()
						.getPreviosRegistrDate()) {
					Log.i( sMethod,"Reading Call Details");
					TelephonyFileLoger.getInstance()
					.setPreviosRegistrDate(date);
					number = cur.getString(numberColumn);
					name = cur.getString(nameColumn);
					duration = cur.getString(durationClumn);
					String stringToWrite = number + ":" + new Date(date) + ":"
							+ name + " " + duration + "\n";
					System.out.println(stringToWrite);
					buffer.append(stringToWrite);
					TelephonyFileLoger.getInstance().writeToRegisteredCallLog(
							buffer.toString());
				}
			}
		} finally {
			if (cur != null) {
				cur.close();
			}
		}
	}

}
