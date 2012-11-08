package com.depech.filemanager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FileManagerActivity extends ListActivity {
	private List<String> directoryEntries = new ArrayList<String>();
    private File currentDirectory = new File("/");
    @Override
    public void onCreate(Bundle icicle) {
    	super.onCreate(icicle);
        setContentView(R.layout.file_manager);
        browseTo(new File("/"));
    }
  
    private void browseTo(final File aDirectory){
    	//browse directory
        if (aDirectory.isDirectory()){
        	//fill list with files from this directory
        	this.currentDirectory = aDirectory;
        	fill(aDirectory.listFiles());
        	TextView currentDirectoryName = (TextView) findViewById(R.id.current_directory);
        	currentDirectoryName.setText(aDirectory.getName());
        	TextView fullPathToCurrentDir = (TextView) findViewById(R.id.fullPathToCurrentDir);
        	fullPathToCurrentDir.setText(aDirectory.getAbsolutePath());
        } else if (aDirectory.isFile()){
        	Toast.makeText(this, aDirectory.getName().toString(), Toast.LENGTH_LONG).show();
        	// FILE OPEN HANDLER
        }
    }
    private void toParentDirectory(){
    	if(this.currentDirectory.getParent() != null) {
        	this.browseTo(this.currentDirectory.getParentFile());
        }
    }
      //fill list
      private void fill(File[] files) {
        this.directoryEntries.clear();     
        if (this.currentDirectory.getParent() != null)
          this.directoryEntries.add("..");   
        //add every file into list
        for (File file : files) {
          this.directoryEntries.add(file.getAbsolutePath());  
        }
        //adapter to show everything
        CustomAdapter directoryList = new CustomAdapter(this, R.layout.row, 
        		R.id.itemNameView, this.directoryEntries);

        this.setListAdapter(directoryList);
      }
      
      private class CustomAdapter extends ArrayAdapter<String> {
    	  public CustomAdapter(Context context, int resource,
				int textViewResourceId, List<String> objects) {
			super(context, resource, textViewResourceId, objects);
			// TODO Auto-generated constructor stub
    	  }
    	  @Override
    	  public View getView(int position, View convertView, ViewGroup parent){
    		  LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    		  View row = inflater.inflate(R.layout.row, parent, false); 		  
    		  
    		  ImageView itemImage = (ImageView) row.findViewById(R.id.itemImageView);
    		  TextView itemName = (TextView) row.findViewById(R.id.itemNameView);
    		  
    		  itemName.setText(directoryEntries.get(position));
    		  itemImage.setImageResource(R.drawable.start_logo); // need to write the method set image
    		  return row;
    	  }
      }
      
      //when clicked onto item 
      @Override
      protected void onListItemClick(ListView l, View v, int position, long id) {
        //get selected file name
        int selectionRowID = position;
        String selectedFileString = this.directoryEntries.get(selectionRowID);
        
        //if we select ".." then go upper
        if(selectedFileString.equals("..")){
        	toParentDirectory();
        } else {
        	//browse to clicked file or directory using browseTo()
        	File clickedObject = null;
        	clickedObject = new File(selectedFileString);
        	if (clickedObject != null)
        		this.browseTo(clickedObject);
        }
      }
}
