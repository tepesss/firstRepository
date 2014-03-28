package com.depech.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.depech.utils.Utils;

public class TelephonyReceviver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (!Utils.isServiceRuning(context, TelephonyService.class)) {
			context.startService(new Intent(context, TelephonyService.class));
		}
		// TelephonyManager telephony = (TelephonyManager) context
		// .getSystemService(Context.TELEPHONY_SERVICE);
		// TelephonyStateListener customPhoneListener = new
		// TelephonyStateListener();
		//
		// System.out.println("Action : " + intent.getAction()
		// + intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER));
		// if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
		// String number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
		// if (number != null) {
		// CallListHandler.writeCallToList(context, number);
		// }
		// System.out.println("Out : " + number);
		// } else if (intent.getAction().equals(
		// "android.intent.action.PHONE_STATE")) {
		// String number = intent
		// .getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
		// if (number != null) {
		// CallListHandler.writeCallToList(context, number);
		// }
		// System.out.println("income : " + number);
		// }
	}
}
