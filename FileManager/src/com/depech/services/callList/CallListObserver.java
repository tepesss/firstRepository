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
	private final String classUI = this.getClass().getSimpleName() +":: hashCode :: "+ this.hashCode(); 
	

	public CallListObserver(Handler handler, Context ctx) {
		super(handler);
		mContext = ctx;
	}

	public boolean deliverSelfNotifications() {
		return true;
	}

	public void onChange(boolean selfChange) {
		CallListHandler.writeCallToList(mContext);
		super.onChange(selfChange);
	}
}
