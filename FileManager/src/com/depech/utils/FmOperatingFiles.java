package com.depech.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.depech.filemanager.FileManagerActivity;
import com.depech.filemanager.R;


public class FmOperatingFiles {
	public static String operatedFile = null;
	public static String fileToPast = null;
	private static boolean cutState = false;

	public static void setOperatedFilePath(String file){
		operatedFile = file;
	}
	public static String getOperatedFilePath (){
		
		return operatedFile;
	}

	public static void copy(){
		if (operatedFile != null){
			fileToPast = operatedFile;
		}
	}
	public static void cut(){
		if (operatedFile != null){
			fileToPast = operatedFile;
			cutState = true;
		}
	}
	public static void paste (File currentDirectory, final Context context){
		if (fileToPast != null){
			String dir = currentDirectory.getAbsolutePath();
			File fileFrom = new File (fileToPast);
			File fileTo = new File (dir+"/"+fileFrom.getName());
			if (cutState == true){
				if (!fileTo.exists()){
				fileFrom.renameTo(fileTo);					
				}else Toast.makeText(context, "Error cannot move", Toast.LENGTH_SHORT).show();			
			}else {			
				InputStream inStream = null;
				OutputStream outStream = null;		 
		    		try{		 
		    			File afile =new File(fileToPast);
		    			File bfile =new File(fileTo.getAbsolutePath());		 
		    			inStream = new FileInputStream(afile);
		    			outStream = new FileOutputStream(bfile);		 
		    			byte[] buffer = new byte[1024];		 
		    			int length;
		    			//copy the file content in bytes 
		    			while ((length = inStream.read(buffer)) > 0){		 
		    				outStream.write(buffer, 0, length);		 
		    			}
		    			inStream.close();
		    			outStream.close();		 
		    		}catch(IOException e){
		    		e.printStackTrace();
		    		}
			}			
			cutState = false;
			fileToPast = null;
		}
	}

	public static void properties(final Context context){
		File file = new File (operatedFile);
		
		// custom dialog
		final Dialog dialog = new Dialog(context);
		dialog.setContentView(R.layout.dialog_prop);
		String textName;
		  if (file.getName().length()>30){
			  textName=file.getName().substring(0, 30)+"...";
			  dialog.setTitle(textName);
		  }else{
			  dialog.setTitle(file.getName());
		  }

		// set the custom dialog components - text, image and button
		  TextView title = (TextView) dialog.findViewById(R.id.propertise_Title);
		  title.setText("Properties");
		  TextView isInDirectory = (TextView) dialog.findViewById(R.id.properties_full_path);
		  isInDirectory.setText("Located: "+file.getParentFile().getAbsolutePath());
		 
		  TextView size = (TextView) dialog.findViewById(R.id.properties_size);
		  
		  size.setText("File size: "+fileSize(file));
		  
		  TextView type = (TextView) dialog.findViewById(R.id.properties_type);
		 
		  String mType = FileManagerActivity.getmMimeTypes().getMimeType(file.getName());
		  if (file.isDirectory()){
			  type.setText("Type: Directory");
		  }else if (mType == null || mType == ""){
			  type.setText("Type: Unknown");
		  }else {
			  type.setText("Type: "+mType);
		  }
		  
		  
		  TextView lastModified = (TextView) dialog.findViewById(R.id.properties_last_modified);
		  if (file.lastModified() == 0 || file.lastModified() == 0.0){
			  lastModified.setText("Last Modified: not available");
		  }else {
		  Date d = new Date(file.lastModified());
		  SimpleDateFormat dateformat =(new SimpleDateFormat("yyyy.MM.dd kk:mm"));
		  StringBuilder lmd = new StringBuilder(dateformat.format(d));
		  lastModified.setText("Last Modified: "+lmd);
		  }
		  

		Button dialogButton = (Button) dialog.findViewById(R.id.properties_button);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}
	//Returns file size
    public static String fileSize (final File file){
    	float b = file.length();
    	String size = "";
    		if (file.isDirectory()) {
    			b=getAllelementsSize(file);
    		}
		    if (b>=1073741824){
		    	b=b/1073741824;
		    	size=String.valueOf(b).substring(0,(String.valueOf(b).indexOf("."))+2 )+" Gb";   
		  	}else if (b>=1048576){
		  		b=b/1048576;
		  		size=String.valueOf(b).substring(0,(String.valueOf(b).indexOf("."))+2 )+" Mb";			   
		  	}else if (b>=1024){
		  		b=b/1024;
		  		size=String.valueOf(b).substring(0,(String.valueOf(b).indexOf("."))+2 )+" Kb";			  
		  	}else if(b<1024){
		  		size=String.valueOf(b).substring(0,(String.valueOf(b).indexOf("."))+2 )+" b";			  
		  	}
    		
    	return size;
	  }
	private static float getAllelementsSize(File file) {
			File[] list = file.listFiles();
			float all = 0;
			if (file.canRead()){
				for (File x: list){
					if (x.isFile()){
						all = all + x.length();
					}else if(x.isDirectory()){
						all = all + getAllelementsSize(x);
					}else ;
				}
			}				
		return all;
	}
}
