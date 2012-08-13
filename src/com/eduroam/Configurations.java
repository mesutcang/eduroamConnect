package com.eduroam;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class Configurations extends Activity implements OnClickListener{
	Button btnDownload,btnDelete,btnSDCard;
	EditText input;
	Options options = new Options();
	ListView lvConfigurations;
	String[] defaultValues = new String[] {  };
	ArrayList<String> listeTurkcell;

			// First paramenter - Context
			// Second parameter - Layout for the row
			// Third parameter - ID of the TextView to which the data is written
			// Forth - the Array of data
		
	public static final Pattern WEB_URL_PATTERN
    = Pattern.compile(
        "((?:(http|https|Http|Https):\\/\\/(?:(?:[a-zA-Z0-9\\$\\-\\_\\.\\+\\!\\*\\'\\(\\)"
        + "\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,64}(?:\\:(?:[a-zA-Z0-9\\$\\-\\_"
        + "\\.\\+\\!\\*\\'\\(\\)\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,25})?\\@)?)?"
        + "((?:(?:[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}\\.)+"   // named host
        + "(?:"   // plus top level domain
        + "(?:aero|arpa|asia|a[cdefgilmnoqrstuwxz])"
        + "|(?:biz|b[abdefghijmnorstvwyz])"
        + "|(?:cat|com|coop|c[acdfghiklmnoruvxyz])"
        + "|d[ejkmoz]"
        + "|(?:edu|e[cegrstu])"
        + "|f[ijkmor]"
        + "|(?:gov|g[abdefghilmnpqrstuwy])"
        + "|h[kmnrtu]"
        + "|(?:info|int|i[delmnoqrst])"
        + "|(?:jobs|j[emop])"
        + "|k[eghimnrwyz]"
        + "|l[abcikrstuvy]"
        + "|(?:mil|mobi|museum|m[acdghklmnopqrstuvwxyz])"
        + "|(?:name|net|n[acefgilopruz])"
        + "|(?:org|om)"
        + "|(?:pro|p[aefghklmnrstwy])"
        + "|qa"
        + "|r[eouw]"
        + "|s[abcdeghijklmnortuvyz]"
        + "|(?:tel|travel|t[cdfghjklmnoprtvwz])"
        + "|u[agkmsyz]"
        + "|v[aceginu]"
        + "|w[fs]"
        + "|y[etu]"
        + "|z[amw]))"
        + "|(?:(?:25[0-5]|2[0-4]" // or ip address
        + "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(?:25[0-5]|2[0-4][0-9]"
        + "|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(?:25[0-5]|2[0-4][0-9]|[0-1]"
        + "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(?:25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}"
        + "|[1-9][0-9]|[0-9])))"
        + "(?:\\:\\d{1,5})?)" // plus option port number
        + "(\\/(?:(?:[a-zA-Z0-9\\;\\/\\?\\:\\@\\&\\=\\#\\~"  // plus option query params
        + "\\-\\.\\+\\!\\*\\'\\(\\)\\,\\_])|(?:\\%[a-fA-F0-9]{2}))*)?"
        + "(?:\\b|$)"); // and finally, a word boundary or end of
                        // input.  This is to stop foo.sure from
                        // matching as foo.su

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.configurations);
		btnDownload = (Button) findViewById(R.id.btnDownload);
		btnDownload.setOnClickListener(this);
		btnDelete = (Button) findViewById(R.id.btnDelete);
		btnDelete.setOnClickListener(this);
		btnSDCard = (Button) findViewById(R.id.btnSDCard);
		btnSDCard.setOnClickListener(this);
		
		lvConfigurations = (ListView) findViewById(R.id.list);
		fillConfigurationListView();
		
	}
	
	private void fillConfigurationListView() {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				   R.id.list, defaultValues);
		listeTurkcell=new ArrayList<String>();
		lvConfigurations.setAdapter(adapter);
		//Burada listeTurkcell değişkenine eklenen her veri listViewde görüntülenicek.
		listeTurkcell.add("ListViewe Yazı Ekledim");
		
	}

	public void onClick(View v) {
		if (v.getId() == R.id.btnDownload) {
			AlertDialog.Builder alert = new AlertDialog.Builder(this);

			alert.setTitle("Download Configuration");
			alert.setMessage("Please provide an URL that contains mobileconfig file.");

			// Set an EditText view to get user input 
			input = new EditText(this);
			alert.setView(input);

			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			  //
			  checkEntry();
			  }
			});

			alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			  public void onClick(DialogInterface dialog, int whichButton) {
			    // Canceled.
			  }
			});

			alert.show();
		}else if (v.getId() == R.id.btnDelete) {
			File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+File.separatorChar+options.getStorageDirectory());
			File[] files = f.listFiles();
			for (File inFile : files) {
			    Toast.makeText(this, inFile.getName(), Toast.LENGTH_LONG).show();
			}

		}else if (v.getId() == R.id.btnSDCard) {
			/*File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+File.separatorChar+options.getStorageDirectory());
			if (dir.isDirectory()) {
		        String[] children = dir.list();
		        for (int i = 0; i < children.length; i++) {
		            new File(dir, children[i]).delete();
		        }
		    }*/
			
			//pickFile();
			listeTurkcell.add("ListViewe 2");
			
		

		}
		
	}
	
	private void pickFile() {
		mPath = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
		loadFileList();
		Dialog a = onCreateDialog(DIALOG_LOAD_FILE);
		a.show();
		Toast.makeText(this, mPath.getAbsolutePath(), Toast.LENGTH_LONG).show();
		
		
		
	}

	protected void checkEntry() {
		String value = input.getText().toString().trim();
		if (!value.trim().equals("")) {
			if (!value.contains("http://")) {
				value = "http://" + value;
				
			}
			
			if (WEB_URL_PATTERN.matcher(value).matches() ) {
				downloadFile(value);
				
			}else {
				Toast.makeText(this, "Given URL is invalid.", Toast.LENGTH_LONG).show();
			}
			
		}else {
			Toast.makeText(this, "Value is empty.", Toast.LENGTH_LONG).show();
		}
		
		
	}

	/*
	 * Downloads specified file from url.
	 */
	private void downloadFile(String url) {
		
		 try {
			 String fileName= url.substring(url.lastIndexOf("/"));
			 if (fileName.trim().equals("")) {
				fileName = "index"+Math.random();
			}
			 
			URL u = new URL(url);

			HttpURLConnection c = (HttpURLConnection) u.openConnection();
			c.setRequestMethod("GET");
			c.setDoOutput(true);
			c.connect();
			//Toast.makeText(this, Environment.getExternalStorageDirectory().getAbsolutePath()+File.separatorChar+options.getStorageDirectory(), Toast.LENGTH_LONG).show();
			
			File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+File.separatorChar+options.getStorageDirectory(),fileName);
			FileOutputStream f = new FileOutputStream(file);


			InputStream in = c.getInputStream();

			byte[] buffer = new byte[1024];
			Integer len1 = 0;
			while ( (len1 = in.read(buffer)) > 0 ) {
				Log.d("downloading", len1.toString());
		         f.write(buffer,0, len1);
			}

			//Toast.makeText(this, file.getAbsolutePath(), Toast.LENGTH_LONG).show();
			
			f.close();
		} catch (MalformedURLException e) {
			
			e.printStackTrace();
		} catch (ProtocolException e) {
						e.printStackTrace();
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}

	
	}
	private String[] mFileList;
	private File mPath = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
	private String mChosenFile="";
	private static final String FTYPE = "";    
	private static final int DIALOG_LOAD_FILE = 1000;
	
	

	private void loadFileList(){
	  
	  if(mPath.exists()){
	     FilenameFilter filter = new FilenameFilter(){
	         public boolean accept(File dir, String filename){
	            
	             return true;
	         }
	     };
	     mFileList = mPath.list(filter);
	  }
	  else{
	    mFileList= new String[0];
	  }
	}

	  protected Dialog onCreateDialog(int id){
	  Dialog dialog = null;
	  AlertDialog.Builder builder = new Builder(this);

	  switch(id){
	  case DIALOG_LOAD_FILE:
	   builder.setTitle("Choose your file");
	   if(mFileList == null){
	     //Log.e(TAG, "Showing file picker before loading the file list");
	     dialog = builder.create();
	     return dialog;
	   }
	     builder.setItems(mFileList, new DialogInterface.OnClickListener(){
	       public void onClick(DialogInterface dialog, int which){
	          mChosenFile = mFileList[which];
	          mPath = new File(mPath.getAbsolutePath()+File.separatorChar+mChosenFile);
	  		if (mPath.isDirectory()) {
	  			loadFileList();
	  			Dialog a = onCreateDialog(DIALOG_LOAD_FILE);
	  			a.show();
			}
	  			
	  		
	          
	         
	          //you can do stuff with the file here too
	       }
	      });
	  break;
	  }
	  dialog = builder.show();
	  return dialog;
	 } 

}
