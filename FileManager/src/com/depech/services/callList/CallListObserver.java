package com.depech.services.callList;

import java.util.Date;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.provider.CallLog;
import android.util.Log;

import com.depech.utils.TelephonyFileLoger;

public class CallListObserver extends ContentObserver {
	Context mContext;
	

	public CallListObserver(Handler handler, Context ctx) {
		super(handler);
		mContext = ctx;
	}

	public boolean deliverSelfNotifications() {
		return true;
	}

	public void onChange(boolean selfChange) {
		try {
			StringBuffer buffer = null;
			Log.e("Info", "Notification on CallListObserver:: selfChange:: "
					+ selfChange);
			Cursor cursor = mContext.getContentResolver().query(
					CallLog.Calls.CONTENT_URI, null, null, null, null);
			if (cursor != null) {
				if (cursor.moveToLast()) {
					System.out.println("Reading Call Details: ");
					buffer = new StringBuffer();

					long time = System.currentTimeMillis();
					String name, number;
					long date;

					int dateColumn = cursor.getColumnIndex(CallLog.Calls.DATE);
					date = cursor.getLong(dateColumn);
					if (date != TelephonyFileLoger.getInstance().getPreviosRegistrDate()) {
						TelephonyFileLoger.getInstance().setPreviosRegistrDate(date);
						int nameColumn = cursor
								.getColumnIndex(CallLog.Calls.CACHED_NAME);
						int numberColumn = cursor
								.getColumnIndex(CallLog.Calls.NUMBER);
						number = cursor.getString(numberColumn);
						System.out.println("number to add to list: " + number);
						name = cursor.getString(nameColumn);

						String stringToWrite = number + ":" + new Date(date)
								+ ":" + name + " " + "\n";
						System.out.println(stringToWrite);
						buffer.append(stringToWrite);
						System.out.println(System.currentTimeMillis() - time);
						TelephonyFileLoger.getInstance()
								.writeToRegisteredCallLog(buffer.toString());
						System.out.println(System.currentTimeMillis() - time);
					}
				}
			} else
				Log.e("Info", "Send Cursor is Empty");
		} catch (Exception sggh) {
			Log.e("Error", "Error on onChange : " + sggh.toString());
		}
		super.onChange(selfChange);
	}
}
