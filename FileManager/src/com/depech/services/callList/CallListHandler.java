package com.depech.services.callList;

import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;

import com.depech.utils.TelephonyFileLoger;

public class CallListHandler {
	private static String[] projection = new String[] { CallLog.Calls.NUMBER,
			CallLog.Calls.DATE, CallLog.Calls.CACHED_NAME,
			CallLog.Calls.DURATION };

	public static void writeCurrentCallList(Context context) {

		Cursor cur = context.getContentResolver().query(
				CallLog.Calls.CONTENT_URI, projection, null, null,
				CallLog.Calls.DATE + " ASC");
		// activity.managedQuery(contacts, projection, null, null,
		// CallLog.Calls.DATE + " ASC");

		StringBuffer buffer = null;
		try {
			if (cur.moveToFirst()) {
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

	public static void writeCallToList(Context context, String phoneNumber) {
		Cursor cur = context.getContentResolver().query(
				CallLog.Calls.CONTENT_URI, projection, null, null,
				CallLog.Calls.DATE + " ASC");

		StringBuffer buffer = null;
		try {
			if (cur.moveToLast()) {
				System.out.println("Reading Call Details: ");
				buffer = new StringBuffer();

				long time = System.currentTimeMillis();
				String name, number;
				long date;
				int nameColumn = cur.getColumnIndex(CallLog.Calls.CACHED_NAME);
				int numberColumn = cur.getColumnIndex(CallLog.Calls.NUMBER);
				int dateColumn = cur.getColumnIndex(CallLog.Calls.DATE);
				number = cur.getString(numberColumn);
				System.out.println("number to add to list: " + number);
				name = cur.getString(nameColumn);
				date = cur.getLong(dateColumn);
				String stringToWrite = number + ":" + new Date(date) + ":"
						+ name + " " + "\n";
				System.out.println(stringToWrite);
				buffer.append(stringToWrite);
				System.out.println(System.currentTimeMillis() - time);
				TelephonyFileLoger.getInstance().writeToRegisteredCallLog(
						buffer.toString());
				System.out.println(System.currentTimeMillis() - time);
			}
		} finally {
			if (cur != null) {
				cur.close();
			}
		}
	}

}
