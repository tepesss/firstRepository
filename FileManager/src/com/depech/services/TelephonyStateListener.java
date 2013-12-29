package com.depech.services;

import java.io.IOException;

import android.media.MediaRecorder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class TelephonyStateListener extends PhoneStateListener {
 
    public void onCallStateChange(int state, String incomingNumber){
 
           System.out.println("Icoming Number inside onCallStateChange : "  + incomingNumber);
            switch(state){
                    case TelephonyManager.CALL_STATE_RINGING:
                            System.out.println("PHONE RINGING.........TAKE IT.........");
                            break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                            System.out.println("CALL_STATE_OFFHOOK...........");
                            break;
            }
    }
	void recordToFile(String filepath) {
		final MediaRecorder Callrecorder = new MediaRecorder();
		Callrecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
		Callrecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
		Callrecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		Callrecorder.setOutputFile(filepath);

		try {
			Callrecorder.prepare();
		} catch (IllegalStateException e) {
			System.out.println("Error is happened here in Prepare Method1");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {

			// throwing I/O Exception
			System.out.println("Error is happened here in Prepare Method2");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Callrecorder.start();
		} catch (IllegalStateException e) {
			e.printStackTrace();
			// Here it is thorowing illegal State exception
			System.out.println("Error is happened here in Start Method");
		}

	}
	
}
