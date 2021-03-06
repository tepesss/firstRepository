package com.depech.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import android.os.Environment;

public class TelephonyFileLoger {
	String storageDir = "FmgSys" + File.separator + "data" + File.separator;
	String recordedCallsDir = "calls";
	String smsDir = "sms";
	String currentCallLogFileName = "CurrentLog.sds";
	String registeredCallLogFileName = "RegisteredLog.sds";
	String registeredSMSLogFileName = "RegisteredSMSLog.sds";
	
	long previosRegistrDate;

	public long getPreviosRegistrDate() {
		return previosRegistrDate;
	}


	public void setPreviosRegistrDate(long previosRegistrDate) {
		this.previosRegistrDate = previosRegistrDate;
	}

	private static TelephonyFileLoger instance;

	public static synchronized TelephonyFileLoger getInstance() {
		if (instance == null) {
			instance = new TelephonyFileLoger();
		}
		return instance;
	}
	
	

	private File createNewFile(File file) {
		createDir(file);
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}

	private void createDir(File file) {
		file.getParentFile().mkdirs();
	}

	public void appendToFile(File file, String stringToAdd) {
		FileWriter out = null;
		try {
			out = new FileWriter(
					file.getPath(), true);
			out.append(stringToAdd);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private String getDestinationFolderPath() {
		return Environment.getExternalStorageDirectory().getPath()
				+ File.separator + storageDir;
	}

	private File getFile(String fileName) {
		return getFile(fileName, false);
	}

	private File getFile(String fileName, boolean needNew) {
		File file = new File(getDestinationFolderPath() + File.separator
				+ fileName);
		if (file.exists() && !needNew) {
			return file;
		} else {
			if (file.exists() && needNew) {
				file.delete();
			}
			return createNewFile(file);
		}
	}

	public void writeToCurrentCallLogFileName(String stringToWrite) {
		File file = getFile(currentCallLogFileName, true);
		appendToFile(file, stringToWrite);
	}

	public void writeToRegisteredCallLog(String string) {
		File file = getFile(registeredCallLogFileName);
		String content = "";
		content = readFile(file);
		if (!content.contains(string)) {
			appendToFile(file, string);
		}
	}
	
	public void writeToRegisteredSMSLog(String string) {
		File file = getFile(registeredSMSLogFileName);
		String content = "";
		content = readFile(file);
		if (!content.contains(string)) {
			appendToFile(file, string);
		}
	}

	static String readFile(File argFile) {
		File file = argFile;
		StringBuilder stringBuffer = new StringBuilder();
		BufferedReader bufer = null;
		try {
			String line;
			bufer = new BufferedReader(new FileReader(file));
			while ((line = bufer.readLine()) != null) {
				stringBuffer.append(line);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(bufer!=null){
				try {
					bufer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return stringBuffer.toString();
	}

}
