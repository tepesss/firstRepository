package com.depech.utils;

import java.util.HashMap;
import java.util.Map;

public class MimeTypes {
	
	private Map<String, String> mimeTypesMap;
	public MimeTypes() {
		mimeTypesMap = new HashMap<String,String>();
	}
	public void put(String type, String extension) {
		mimeTypesMap.put(type, extension);
	}
	public String getMimeType(String filename) {
		String mimetype = mimeTypesMap.get(getExtension(filename));
		return mimetype;
	}
	public static String getExtension(String uri) {
		if (uri == null)return null;
		int dot = uri.lastIndexOf(".");
		if (dot >= 0)return uri.substring(dot);
		else return "";// No extension.
	}
}
