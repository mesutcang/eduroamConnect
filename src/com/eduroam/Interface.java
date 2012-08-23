package com.eduroam;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

public class Interface extends Activity implements OnClickListener,Serializable {
	 XmlParser xml;
	   ConfigurationRegularExpressionFieldParser parse = new ConfigurationRegularExpressionFieldParser();
	  
	private WifiManager wifi;
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
	Options options = new Options();
	Button btnDownloadConfiguration,btnConnect,btn1;
	EditText input;
	File file;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.view);
	 
	  
	    
	    btnDownloadConfiguration = (Button) findViewById(R.id.btnDownloadConfiguration);
	    btnDownloadConfiguration.setOnClickListener(this);
	    
	   
	    
	    btn1 = (Button) findViewById(R.id.button1);
	    btn1.setOnClickListener(this);
	    
	    input=new EditText(this);
	    input.setText(options.getDefaultConfigurationURL());
	    
	    
	  
	   
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		
		MenuInflater inflater = new MenuInflater(this);
	    inflater.inflate(R.menu.optionsmenu, menu);
	    return super.onCreateOptionsMenu(menu);
	}

@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case R.id.itemQuit:
		finish();
		break;

	default:
		break;
	}
		return super.onOptionsItemSelected(item);
	}
public void onClick(View v) {
	if (v.getId() == R.id.btnDownloadConfiguration) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Download Configuration");
		alert.setMessage("Please provide an URL that contains mobileconfig file.");

		// Set an EditText view to get user input 
		input = new EditText(this);
		input.setText(options.getDefaultConfigurationURL());
		
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
	
}else if (v.getId() == R.id.btnConnect) {
	
	/*
	File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+File.separatorChar+options.getStorageDirectory());
	if (dir.isDirectory()) {
        String[] children = dir.list();
        for (int i = 0; i < children.length; i++) {
            new File(dir, children[i]).delete();
        }
    }
	
	
	File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+File.separatorChar+options.getStorageDirectory());
	File[] files = f.listFiles();
	for (File inFile : files) {
	    Toast.makeText(this, inFile.getName(), Toast.LENGTH_LONG).show();
	}
	
	*/
	
	
	if (file== null || !file.isFile()) {
		Toast.makeText(this, "Please define a valid configuration file.", Toast.LENGTH_LONG).show();
		return;
		
		
	}
	
	
	
	
	

}else if (v.getId() == R.id.button1) {
	file = new File(this.getCacheDir()+"/dot1x.mobileconfig");
	  Intent intent = new Intent(this, Preview.class);
	  intent.putExtra("param", this.getCacheDir()+"/dot1x.mobileconfig");
      startActivity(intent);

	
/*
	file = new File(this.getCacheDir()+"/dot1x.mobileconfig");
	
	File f = this.getCacheDir();
	File[] files = f.listFiles();
	for (File inFile : files) {
	    Toast.makeText(this, inFile.getName(), Toast.LENGTH_LONG).show();
	}
	*/
	
	

	
	
	/*Intent i = new Intent();
	i.setAction(android.content.Intent.ACTION_VIEW);
	i.setData(Uri.fromFile(videoFile2Play));
	//i.setDataAndType(Uri.fromFile(videoFile2Play), "text/xml");
	startActivity(i);*/
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
			
		   // file = new File(path.getAbsolutePath()+File.separatorChar+options.getStorageDirectory(),fileName);
		 file = new File(this.getCacheDir().getAbsolutePath(),fileName);
			FileOutputStream f = new FileOutputStream(file,false);


			InputStream in = c.getInputStream();

			byte[] buffer = new byte[1024];
			Integer len1 = 0;
			while ( (len1 = in.read(buffer)) > 0 ) {
				Log.d("downloading", len1.toString());
		         f.write(buffer,0, len1);
			}

			Toast.makeText(this, "Configuration file is successfully downloaded.", Toast.LENGTH_LONG).show();
			
			 Intent intent = new Intent(this, Preview.class);
			  intent.putExtra("param", this.getCacheDir().getAbsolutePath()+File.separatorChar+fileName);
		      startActivity(intent);
			
			
			f.close();
		} catch (MalformedURLException e) {
			
			Toast.makeText(this, "error: " +e.getMessage(), Toast.LENGTH_LONG).show();	
		} catch (ProtocolException e) {
			Toast.makeText(this, "error: " +e.getMessage(), Toast.LENGTH_LONG).show();	
		} catch (FileNotFoundException e) {
			
			Toast.makeText(this, "error: " +e.getMessage(), Toast.LENGTH_LONG).show();	
		} catch (IOException e) {
			
			Toast.makeText(this, "error: " +e.getMessage(), Toast.LENGTH_LONG).show();	
		}

	
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

	
	
	
}
