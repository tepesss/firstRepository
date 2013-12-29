package com.depech.filemanager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.depech.filemanager.R;
import com.depech.filemanager.R.drawable;
import com.depech.filemanager.R.id;
import com.depech.filemanager.R.layout;
import com.depech.filemanager.R.menu;
import com.depech.filemanager.R.string;
import com.depech.filemanager.R.xml;
import com.depech.utils.MimeTypeParser;
import com.depech.utils.MimeTypes;
import com.depech.utils.FmOperatingFiles;

public class FileManagerActivity extends ListActivity {
	private List<File> directoryEntries = new ArrayList<File>();
    private File currentDirectory = new File("/");
    private static MimeTypes mMimeTypes;
    private Button addNewTab;
    private static int currentTubId;
    private static int id = 1;
    private LinearLayout tabsLayout;
    //Necessary to maintain gesture 
    private int swipeMinDistance; 
    private int swipeMaxPath;
    private int swipeVelocity;
    //Necessary to divide directories and files
    private List<File> listFile = new ArrayList<File>();
    private List<File> listDir = new ArrayList<File>();
    //Necessary context menu

    private static final int ITEM_COPY = 2;
    private static final int ITEM_CUT = 3;
    private static final int ITEM_DELETE = 4;
    private static final int ITEM_PROPRTIES = 5;
    //Necessary to handle state of double click on back button from root directory
    private static boolean backState = false;
    
    
    @Override
    public void onCreate(Bundle icicle) {
    	super.onCreate(icicle);    	
        setContentView(R.layout.file_manager);        
        addNewTab();
        fillMimeTypesMap();
        addNewTab = (Button) findViewById(R.id.addNewTab);
    	addNewTab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addNewTab();	
			}
		});
    }
    
    private int currentTabPosition (){
    	int position=2;
    	int n = tabsLayout.getChildCount();
    	for (int i=0; i<n; i++){
    		if(tabsLayout.getChildAt(i).getId() == currentTubId){
    			position = i;
    		}
    	}
    	return position;
    }
    
    private void addNewTab(){
    	tabsLayout = (LinearLayout) findViewById(R.id.tabsLayout);
    	final TabButton newTab = new TabButton(this);  
    	id++;
    	newTab.setId(id);
    	//int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics());
    	
    	//newTab.setWidth(addNewTab.getHeight());
    	setCurrentTabId(newTab.getId());
    	
    	tabsLayout.addView(newTab);
    	
    	browseTo(new File (newTab.currentTabDirectory));
    	setTabsBackground(tabsLayout);
    	
    	newTab.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				browseToTab(v);
			}
		});
    	newTab.setOnLongClickListener(new View.OnLongClickListener() {	
			@Override
			public boolean onLongClick(View v) {
				//Toast.makeText(getApplicationContext(), "Sorry its the last tab!!!", Toast.LENGTH_LONG).show();
				closeTab(v);
				return true;
			}
        });
    }
    private void browseToTab(View tabToBrowse){
    	TabButton tab = (TabButton) tabToBrowse;
		setCurrentTabId(tab.getId());
		setTabsBackground(tabsLayout);
		browseTo(new File (tab.currentTabDirectory));
    }
    private static void setCurrentTabId(int tabId){
    	currentTubId = tabId;
    }
    //method witch sets BackResorces correspondingly to it's state 
    public void setTabsBackground(LinearLayout tabsLayout){
    	int childQuantity = tabsLayout.getChildCount();
    	for (int i=0; i<childQuantity; i++){
    		if (tabsLayout.getChildAt(i).getId() == currentTubId){
    			tabsLayout.getChildAt(i).setBackgroundResource(R.drawable.current_tab);
    		}else if (tabsLayout.getChildAt(i).getId() == R.id.addNewTab){
    			tabsLayout.getChildAt(i).setBackgroundResource(R.drawable.add_tab);
    		}else if (((TabButton)tabsLayout.getChildAt(i)).getText()=="Home"){
    			tabsLayout.getChildAt(i).setBackgroundResource(R.drawable.home);
    		}else tabsLayout.getChildAt(i).setBackgroundResource(R.drawable.tab);
    	}
    }
    


    private void closeTab(View tabToClose){
    	//Toast.makeText(getApplicationContext(), "Sorry its the last tab!!!", Toast.LENGTH_LONG).show();
    	final TabButton tab = (TabButton) tabToClose;
    	AlertDialog.Builder alertBuilder = new AlertDialog.Builder(FileManagerActivity.this);
    	alertBuilder.setMessage("Do you want to close this tab?");
    	alertBuilder.setCancelable(true);
    	alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {	
			@Override
			public void onClick(DialogInterface dialog, int which) {
				int n = tabsLayout.getChildCount();
				if (n<3){
					Toast.makeText(getApplicationContext(), "Sorry its the last tab!!!", Toast.LENGTH_LONG).show();
				}else if (tab.getId()==currentTubId && tab.getId()==tabsLayout.getChildAt(1).getId()){
					browseToTab(tabsLayout.getChildAt(2));
					tabsLayout.removeView(tab);
				}
				else if (tab.getId()==currentTubId && tab.getId()==tabsLayout.getChildAt(n-1).getId()){
					browseToTab(tabsLayout.getChildAt(n-2));
					tabsLayout.removeView(tab);
				}else if (tab.getId()==currentTubId){
					browseToTab(tabsLayout.getChildAt(currentTabPosition()-1));
					tabsLayout.removeView(tab);
				}else {
					tabsLayout.removeView(tab);
				}
					
			}
		});
    	alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();	
			}
		});
    	AlertDialog alert = alertBuilder.create();
    	alert.show();
    }


    public void browseTo(final File aDirectory){
    	//browse directory/open file
        if (aDirectory.isDirectory()){
        	TabButton currentTab = (TabButton) findViewById(currentTubId);
        	currentTab.currentTabDirectory = aDirectory.getAbsolutePath();
        	//currentTab.setText(aDirectory.getName());
        	String textName;
        	if (aDirectory.getParent() == null){
        		currentTab.setText("Home");
        	}
        	else if (aDirectory.getName().length()>7){
	  			textName=aDirectory.getName().substring(0, 7)+"...";
	  			currentTab.setText(textName);
	  		}else{
	  			currentTab.setText(aDirectory.getName());
	  	    }
        	//fill list with files from this directory
        	this.currentDirectory = aDirectory;
        	//try to fill 
        	
        		fill(aDirectory.listFiles());
            	TextView currentDirectoryName = (TextView) findViewById(R.id.current_directory);
            	currentDirectoryName.setText(aDirectory.getName());
            	TextView fullPathToCurrentDir = (TextView) findViewById(R.id.fullPathToCurrentDir);
            	fullPathToCurrentDir.setText(aDirectory.getAbsolutePath());
        	
        	
        } else if (aDirectory.isFile()){
        	openFile(aDirectory);
        }
    }
    private void openFile(File aFile) { 
   	 if (!aFile.exists()) {
   		 Toast.makeText(this, R.string.error_file_does_not_exists, Toast.LENGTH_SHORT).show();
   		 return;
   	 }
         Intent intent = new Intent(android.content.Intent.ACTION_VIEW);         
         final Uri data = Uri.fromFile(aFile);
         String type = getmMimeTypes().getMimeType(aFile.getName());
         intent.setDataAndType(data, type);
         //Log.d(ACTIVITY_SERVICE, type);
         try {
       	  startActivity(intent); 
         } catch (ActivityNotFoundException e) {
       	  Toast.makeText(this, R.string.application_not_available, Toast.LENGTH_SHORT).show();
         };
    }
    private void fillMimeTypesMap() {
	   	 MimeTypeParser mtp = new MimeTypeParser();
	   	 XmlResourceParser in = getResources().getXml(R.xml.mimetypes);
	   	 try {
	   		 setmMimeTypes(mtp.fromXmlResource(in));
	   	 } catch (XmlPullParserException e) {
	   		Log.d(ACTIVITY_SERVICE, "Exeption");
	   	 } catch (IOException e) {
	   		Log.d(ACTIVITY_SERVICE, "Exeption");
	   	 }
    }

    //fill list
    private void fill(File[] files) {
    	listDir.clear();
    	listFile.clear();
        directoryEntries.clear();  
        //add every file into list
        for (File file : files) {
        	// display only readable content
        	if (file.canRead()){
        		if (file.isDirectory()){
        			listDir.add(file);
        		}else{
        			listFile.add(file);
        		}
        	}
        }
        Collections.sort(listDir);
    	Collections.sort(listFile);
    	directoryEntries.addAll(listDir);
    	directoryEntries.addAll(listFile);
    	
        //adapter to show everything
        CustomAdapter directoryList = new CustomAdapter(this, R.layout.row, R.id.itemNameView, directoryEntries);
        this.setListAdapter(directoryList);
        
        DisplayMetrics dm = getResources().getDisplayMetrics();
        swipeMinDistance = (int)(120.0f * dm.densityDpi / 160.0f + 0.5); 
        swipeMaxPath = (int)(250.0f * dm.densityDpi / 160.0f + 0.5);
        swipeVelocity = (int)(200.0f * dm.densityDpi / 160.0f + 0.5);

        ListView lv = getListView();
       
        final GestureDetector gestureDetector = new GestureDetector(new MyGestureDetector());
        View.OnTouchListener gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event); 
            }};
        lv.setOnTouchListener(gestureListener);
        //Context menu set on listViev
        lv.setOnCreateContextMenuListener(this);
    }
    
    private class CustomAdapter extends ArrayAdapter<File> {
    	  public CustomAdapter(Context context, int resource,
				int textViewResourceId, List<File> objects) {
			super(context, resource, textViewResourceId, objects);
			// TODO Auto-generated constructor stub
    	  }

    	  @Override
    	  public View getView(int position, View convertView, ViewGroup parent){
    		  LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);    		  
    		  View row;
    		  if (position % 2 == 0){
    			  row = inflater.inflate(R.layout.row, parent, false); 
    		  }else if (position % 2 != 0){
    			  row = inflater.inflate(R.layout.row_odd, parent, false);    			 
    		  }else row = inflater.inflate(R.layout.row, parent, false);
    		 
    		  
    		  ImageView itemImage = (ImageView) row.findViewById(R.id.itemImageView);
    		  TextView itemName = (TextView) row.findViewById(R.id.itemNameView);
    		  TextView itemParameters =(TextView) row.findViewById(R.id.itemParametersView);
    		  File fileItem = directoryEntries.get(position);    		  
    		  String mimeType = getmMimeTypes().getMimeType(fileItem.getName());
    		  if (fileItem.isDirectory()){
    			  //if (fileItem.listFiles().length == 0)
    			  itemImage.setImageResource(R.drawable.folder);
    			  //else itemImage.setImageResource(R.drawable.folder_filled);
    		  }
    		  else{ 
    			  if (mimeType!= null && mimeType.startsWith("image")){
    				  itemImage.setImageResource(R.drawable.type_image);
    			  }
    			  else if (mimeType!= null && mimeType.startsWith("audio")){
    				  itemImage.setImageResource(R.drawable.type_audio);
    			  }
				  else if (mimeType!= null && mimeType.startsWith("application")){
					  itemImage.setImageResource(R.drawable.type_application);
				  }
				  else if (mimeType!= null && mimeType.startsWith("text")){
					  itemImage.setImageResource(R.drawable.type_text);
				  }
				  else if (mimeType!= null && mimeType.startsWith("video")){
					  itemImage.setImageResource(R.drawable.type_video);
				  }    			  
    			  else itemImage.setImageResource(R.drawable.unknown_file);

    			  itemParameters.setText(FmOperatingFiles.fileSize(fileItem));
    			  
    		  }     		  
    		  String textName;
			  if (fileItem.getName().length()>30){
				  textName=fileItem.getName().substring(0, 30)+"...";
				  itemName.setText(textName);
			  }else{
				  itemName.setText(fileItem.getName());
			  }    		
    		  return row;
    	  }    	  
    } 
    
    private void onLTRFling() {   	 	
   	 	if(tabsLayout.getChildAt(1).getId()==currentTubId) {
   	 		Toast.makeText(getApplicationContext(), "No tab before current!!!", Toast.LENGTH_LONG).show();
   	 	}else browseToTab(tabsLayout.getChildAt(currentTabPosition ()-1));	
    }

    private void onRTLFling() {
    	int n =tabsLayout.getChildCount();
   	 	if(tabsLayout.getChildAt(n-1).getId()==currentTubId) {
   	 		addNewTab();
   	 	}else browseToTab(tabsLayout.getChildAt(currentTabPosition ()+1));
    }

    class MyGestureDetector extends SimpleOnGestureListener{ 
        @Override 
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) { 
            if (Math.abs(e1.getY() - e2.getY()) > swipeMaxPath) 
                return false; 
            if(e1.getX() - e2.getX() > swipeMinDistance && 
                Math.abs(velocityX) > swipeVelocity) { 
                onRTLFling(); 
            }  else if (e2.getX() - e1.getX() > swipeMinDistance && 
                Math.abs(velocityX) > swipeVelocity) { 
                onLTRFling(); 
            } 
            return false; 
        } 

    } 
    //Returns file size
    
      //when clicked onto item 
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        //get selected file name
        int selectionRowID = position;
        String selectedFileString = (this.directoryEntries.get(selectionRowID)).getAbsolutePath();
        //browse to clicked file or directory using browseTo()
        File clickedObject = null;
        clickedObject = new File(selectedFileString);
        if (clickedObject != null)
        this.browseTo(clickedObject);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        	toParentDirectory();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    
    private void toParentDirectory(){
    	
    	if(currentDirectory.getParent() != null) {
        	browseTo(currentDirectory.getParentFile());
        //Handling double beck from current tab
        }else {
        	if (backState == false){
        		Toast.makeText(FileManagerActivity.this, "To exit press back again", Toast.LENGTH_SHORT/2).show();
        		new Thread() {
        	        public void run() {
        	            try{
        	                backState = true;
        	                sleep(2000);
        	                backState = false;
        	            } catch (Exception e) {
        	            }
        	        }
        	    }.start();  
        	}else {
        		finish();
        	}
        	
        }
    }
     
    // Context menu 
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) {
    	AdapterView.AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
    	FmOperatingFiles.setOperatedFilePath(directoryEntries.get(info.position).getAbsolutePath());
    	menu.add(0, ITEM_COPY, 0, "Copy");
    	menu.add(0, ITEM_CUT, 0, "Cut");
    	menu.add(0, ITEM_DELETE, 0, "Delete");
    	menu.add(0, ITEM_PROPRTIES, 0, "Properties");

    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	
      switch (item.getItemId()) {
      case ITEM_COPY:
	    	Toast.makeText(FileManagerActivity.this, "ITEM_COPY", Toast.LENGTH_SHORT).show();
	    	FmOperatingFiles.copy();
	    	break;
      case ITEM_CUT:
	    	Toast.makeText(FileManagerActivity.this, "ITEM_CUT", Toast.LENGTH_SHORT).show();
	    	FmOperatingFiles.cut();
	    	break;
      case ITEM_DELETE:	    	
	    	delate(FileManagerActivity.this);	
	    	break;	    	
      case ITEM_PROPRTIES:
	    	Toast.makeText(FileManagerActivity.this, "ITEM_POPERTIES", Toast.LENGTH_SHORT).show();
	    	FmOperatingFiles.properties(FileManagerActivity.this);
	    	break;
      }
      
      return super.onContextItemSelected(item);
    }
    public void delate(final Context context){
		if (FmOperatingFiles.operatedFile != null){
	    	AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
	    	alertBuilder.setMessage("Do you want to delete?");
	    	alertBuilder.setCancelable(true);
	    	alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {	
				@Override
				public void onClick(DialogInterface dialog, int which) {
					File file = new File (FmOperatingFiles.operatedFile);
					if (file.isDirectory()){
						File[] list = file.listFiles();
						for (File x: list){
							if (x.exists()){
								x.delete();
							}else;
						}
						file.delete();
						Toast.makeText(context, "Folder deleted", Toast.LENGTH_SHORT).show();
						browseTo(currentDirectory);
					}else if (file.delete()) {						
							Toast.makeText(context, "File deleted", Toast.LENGTH_SHORT).show();
							browseTo(currentDirectory);
					} else {
							Toast.makeText(context, "Error Cannot Delete", Toast.LENGTH_SHORT).show();
					}
						
				}
				
			});
	    	alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();	
				}
			});
	    	AlertDialog alert = alertBuilder.create();
	    	alert.show();
	    	
		}else {
			Toast.makeText(context, "Error Cannot Delete", Toast.LENGTH_SHORT).show();
			
		}
	}
    //Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();

    	inflater.inflate(R.menu.file_manager_menu, menu);
    	return super.onCreateOptionsMenu(menu);
    }
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.setGroupVisible(R.id.group1, FmOperatingFiles.fileToPast != null);
        return super.onPrepareOptionsMenu(menu);
      }
    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()){
    		case R.id.menu_settings:    			
    			startActivity(new Intent (FileManagerActivity.this, Settings.class));
    		case R.id.menu_exit:
    			finish(); 
    		case R.id.menu_paste:
    			OperatingFiles.paste(currentDirectory, FileManagerActivity.this);
    			browseTo(currentDirectory);
    	}
    	return super.onOptionsItemSelected(item);
    }
*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()){
    		case R.id.menu_settings:    			
    			startActivity(new Intent (FileManagerActivity.this, Settings.class));
    		case R.id.menu_exit:
    			finish(); 
    		case R.id.menu_paste:
    			FmOperatingFiles.paste(currentDirectory, FileManagerActivity.this);
    			browseTo(currentDirectory);
    	}
    	return super.onOptionsItemSelected(item);
    }
    
    
	public void setmMimeTypes(MimeTypes mMimeTypes) {
		FileManagerActivity.mMimeTypes = mMimeTypes;
	}

	public static MimeTypes getmMimeTypes() {
		return mMimeTypes;
	}
	
}
