package com.depech.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.provider.CallLog;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.depech.services.callList.CallListObserver;
import com.depech.services.sms.SmsObserver;

public class TelephonyService extends IntentService{
	public TelephonyService(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	public TelephonyService() {
		super("TelephonyService");
		// TODO Auto-generated constructor stub
	}
	private final TelephonyBinder binder = new TelephonyBinder();

	public class TelephonyBinder extends Binder {
		TelephonyService getService() {
			return TelephonyService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		System.out.println("onStartCommand"+intent.getAction());
//		CallListHandler.writeCurrentCallList(getApplicationContext());
		final Uri SMS_STATUS_URI = Uri.parse("content://sms");
		SmsObserver smsSentObserver = new SmsObserver(new Handler(),getApplicationContext());
		getContentResolver().registerContentObserver(SMS_STATUS_URI, true, smsSentObserver);
		
		CallListObserver callObserver = new CallListObserver(new Handler(),getApplicationContext());
		getContentResolver().registerContentObserver(CallLog.Calls.CONTENT_URI, true, callObserver);
		
//		return super.onStartCommand(intent, flags, startId)START_STICKY;
		TelephonyManager telephony = (TelephonyManager) getApplicationContext()
				.getSystemService(Context.TELEPHONY_SERVICE);
		TelephonyStateListener customPhoneListener = new TelephonyStateListener();
		
		
		
		telephony.listen(customPhoneListener,
				PhoneStateListener.LISTEN_CALL_STATE);
		
		return START_STICKY;
	}
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		System.out.println("onStart"+intent.getAction());
		super.onStart(intent, startId);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		System.out.println("onHandleIntent"+intent.getAction());
	}

}
