package com.eduroam;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

public class Interface extends Activity implements OnClickListener {
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
	EditText input,etUsername,etPassword;
	File file;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.view);
	 
	    wifi = (WifiManager)getSystemService(WIFI_SERVICE); 
	    
	    btnDownloadConfiguration = (Button) findViewById(R.id.btnDownloadConfiguration);
	    btnDownloadConfiguration.setOnClickListener(this);
	    
	    btnConnect = (Button) findViewById(R.id.btnConnect);
	    btnConnect.setOnClickListener(this);
	    
	    btn1 = (Button) findViewById(R.id.button1);
	    btn1.setOnClickListener(this);
	    
	    input=new EditText(this);
	    input.setText(options.getDefaultConfigurationURL());
	    
	    
	    etUsername = (EditText) findViewById(R.id.etUsername);
	    
	    etPassword = (EditText) findViewById(R.id.etPassword);
	   
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
	*/
	
	File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+File.separatorChar+options.getStorageDirectory());
	File[] files = f.listFiles();
	for (File inFile : files) {
	    Toast.makeText(this, inFile.getName(), Toast.LENGTH_LONG).show();
	}
	
	
	
	
	if (file== null || !file.isFile()) {
		Toast.makeText(this, "Please define a valid configuration file.", Toast.LENGTH_LONG).show();
		return;
		
		
	}
	
	String fileName = file.getName();
	fileName = fileName.substring(0, fileName.lastIndexOf("."));
	
	InputStream is = null;
	try {
		is = new FileInputStream(file);
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	if (xml ==null) {
		xml = new XmlParser(getResources(),"dot1x.mobileconfig",getResources().openRawResource(R.raw.dot1x));
		//xml = new XmlParser(getResources(),fileName,is);
	}
	
	
	configure();
	
	
	

}else if (v.getId() == R.id.button1) {
	
	File f = this.getCacheDir();
	File[] files = f.listFiles();
	for (File inFile : files) {
	    Toast.makeText(this, inFile.getName(), Toast.LENGTH_LONG).show();
	}
	
	

	
	
	/*Intent i = new Intent();
	i.setAction(android.content.Intent.ACTION_VIEW);
	i.setData(Uri.fromFile(videoFile2Play));
	//i.setDataAndType(Uri.fromFile(videoFile2Play), "text/xml");
	startActivity(i);*/
}
}

	private void configure() {
		ConfigurationRegularExpressionFieldParser parse = new ConfigurationRegularExpressionFieldParser();
		Options options = new Options();
		
		String conType = parse.getEncryptionType(xml);
		
		String eapType = options.getAcceptEAPTypeDefinition( parse.getAcceptEAPTypes(xml));
		
		if (eapType.equalsIgnoreCase("PEAP")) {
			
			try {
				String ssid = parse.getPayloadContentSSID_STR(xml);
				String hiddenSSID = parse.getHiddenNetwork(xml);
				String userName = parse.getUsernameField(xml);
				String userPassword = parse.getUserPassword(xml);
				
				if (!etUsername.getText().toString().trim().equals("")) {
					userName = etUsername.getText().toString().trim();
				}
				
				if (!etPassword.getText().toString().trim().equals("")) {
					userPassword = etPassword.getText().toString().trim();
				}
				connectDot1X(ssid,hiddenSSID,userName,userPassword,eapType);
				
				
			} catch (Exception e) {
				Toast.makeText(this, "error:" + e.getMessage(), Toast.LENGTH_LONG).show();
			}
			
		}else if (conType.equalsIgnoreCase("WPA")) {
			try {
				String ssid = parse.getPayloadContentSSID_STR(xml);
				String hiddenSSID = parse.getHiddenNetwork(xml);
				String password = parse.getPayloadContentPassword(xml);
				connectWPA(ssid, Boolean.valueOf(hiddenSSID), password);
				
				//Toast.makeText(this, password, Toast.LENGTH_LONG).show();
			} catch (Exception e) {
				Toast.makeText(this, "error:" + e.getMessage(), Toast.LENGTH_LONG).show();
			}
			
		}else if (conType.equalsIgnoreCase("WEP")) {
			String ssid = parse.getPayloadContentSSID_STR(xml);
			String hiddenSSID = parse.getHiddenNetwork(xml);
			String password = parse.getPayloadContentPassword(xml);
			connectWEP(ssid, Boolean.valueOf(hiddenSSID), password);
		}
		
	
}
	
	private void connectDot1X(String ssid2, String hiddenSSID, String userName2, String userPassword, String eapType) {
		final String INT_PRIVATE_KEY = "private_key";
	    final String INT_PHASE2 = "phase2";
	    final String INT_PASSWORD = "password";
	    final String INT_IDENTITY = "identity";
	    final String INT_EAP = "eap";
	    final String INT_CLIENT_CERT = "client_cert";
	    final String INT_CA_CERT = "ca_cert";
	    final String INT_ANONYMOUS_IDENTITY = "anonymous_identity";
	    final String INT_ENTERPRISEFIELD_NAME = "android.net.wifi.WifiConfiguration$EnterpriseField";
	    
	    //user credentials and ssid
	    String userName = userName2;
	    String password = userPassword; //password maked.
	    String ssid =  ssid2;
	    
	    
	    //connection properties
	    final String ENTERPRISE_EAP = eapType;
        final String ENTERPRISE_CLIENT_CERT = null;
        final String ENTERPRISE_PRIV_KEY = null;        
        
        //final String ENTERPRISE_PHASE2 = "auth=MSCHAP";
        final String ENTERPRISE_PHASE2 = "";
        final String ENTERPRISE_ANON_IDENT = null;
        
        
        
        WifiConfiguration selectedConfig = new WifiConfiguration();
        
        selectedConfig.SSID = "\""+ssid+"\"";
        selectedConfig.priority = 48;
        selectedConfig.hiddenSSID = false;

        selectedConfig.allowedKeyManagement.clear();
        selectedConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.IEEE8021X);
        selectedConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_EAP);

        
        selectedConfig.allowedGroupCiphers.clear();
        selectedConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        selectedConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        selectedConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
        selectedConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);

        
        selectedConfig.allowedPairwiseCiphers.clear();
        selectedConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        selectedConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);

        
        selectedConfig.allowedProtocols.clear();
        selectedConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        selectedConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);

        try {
            Class[] wcClasses = WifiConfiguration.class.getClasses();
            Class wcEnterpriseField = null;

            for (Class wcClass : wcClasses)
                if (wcClass.getName().equals(INT_ENTERPRISEFIELD_NAME)) 
                {
                    wcEnterpriseField = wcClass;
                    break;
                }
            
            boolean noEnterpriseFieldType = false; 
            
            if(wcEnterpriseField == null)
                noEnterpriseFieldType = true; // 

            Field wcefAnonymousId = null, wcefCaCert = null, wcefClientCert = null, wcefEap = null, wcefIdentity = null, wcefPassword = null, wcefPhase2 = null, wcefPrivateKey = null;
            Field[] wcefFields = WifiConfiguration.class.getFields();
            
            //Get fields from hidden api
            for (Field wcefField : wcefFields) 
            {
                if (wcefField.getName().equals(INT_ANONYMOUS_IDENTITY))
                    wcefAnonymousId = wcefField;
                else if (wcefField.getName().equals(INT_CA_CERT))
                    wcefCaCert = wcefField;
                else if (wcefField.getName().equals(INT_CLIENT_CERT))
                    wcefClientCert = wcefField;
                else if (wcefField.getName().equals(INT_EAP))
                    wcefEap = wcefField;
                else if (wcefField.getName().equals(INT_IDENTITY))
                    wcefIdentity = wcefField;
                else if (wcefField.getName().equals(INT_PASSWORD))
                    wcefPassword = wcefField;
                else if (wcefField.getName().equals(INT_PHASE2))
                    wcefPhase2 = wcefField;
                else if (wcefField.getName().equals(INT_PRIVATE_KEY))
                    wcefPrivateKey = wcefField;
            }


            Method wcefSetValue = null;
            if(!noEnterpriseFieldType)
            {
            for(Method m: wcEnterpriseField.getMethods())
                if(m.getName().trim().equals("setValue"))
                    wcefSetValue = m;
            }


            
            if(!noEnterpriseFieldType)
                wcefSetValue.invoke(wcefEap.get(selectedConfig), ENTERPRISE_EAP);

            
            if(!noEnterpriseFieldType)
            	wcefSetValue.invoke(wcefPhase2.get(selectedConfig), ENTERPRISE_PHASE2);

            if(!noEnterpriseFieldType)
                wcefSetValue.invoke(wcefAnonymousId.get(selectedConfig), ENTERPRISE_ANON_IDENT);

            if(!noEnterpriseFieldType)
                wcefSetValue.invoke(wcefPrivateKey.get(selectedConfig), ENTERPRISE_PRIV_KEY);

            if(!noEnterpriseFieldType)
                wcefSetValue.invoke(wcefIdentity.get(selectedConfig), userName);

            if(!noEnterpriseFieldType)
                wcefSetValue.invoke(wcefPassword.get(selectedConfig), password);

            if(!noEnterpriseFieldType)
                wcefSetValue.invoke(wcefClientCert.get(selectedConfig), ENTERPRISE_CLIENT_CERT);

            try{
            Field wcAdhoc = WifiConfiguration.class.getField("adhocSSID");
            Field wcAdhocFreq = WifiConfiguration.class.getField("frequency");

            wcAdhoc.setBoolean(selectedConfig, false);
            int freq = 2462;  
            wcAdhocFreq.setInt(selectedConfig, freq); 
            } catch (Exception e)
            {
                e.printStackTrace();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        //Now we can add eduroam profile.
        int ret = wifi.addNetwork(selectedConfig);
        
        boolean a = wifi.enableNetwork(ret, false);   
        
        boolean b = wifi.saveConfiguration();
        
        boolean c = wifi.enableNetwork(ret, true);   
        
        
		
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
			FileOutputStream f = new FileOutputStream(file);


			InputStream in = c.getInputStream();

			byte[] buffer = new byte[1024];
			Integer len1 = 0;
			while ( (len1 = in.read(buffer)) > 0 ) {
				Log.d("downloading", len1.toString());
		         f.write(buffer,0, len1);
			}

			Toast.makeText(this, "Configuration file is successfully downloaded.", Toast.LENGTH_LONG).show();
			
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
private void connectWPA(String ssid, Boolean hiddenSsid,String preSharedKey){
		
		
		try {
	     WifiManager wifi = (WifiManager)getSystemService(WIFI_SERVICE);
	     WifiConfiguration wc = new WifiConfiguration(); 
	     wc.SSID = "\"" + ssid + "\""; //IMP! This should be in Quotes!!
	     wc.hiddenSSID = hiddenSsid;
	     wc.status = WifiConfiguration.Status.ENABLED;     
	     wc.priority = 40;
	     wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
	     wc.allowedProtocols.set(WifiConfiguration.Protocol.WPA);

	     wc.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);

	     wc.preSharedKey = "\"" + preSharedKey + "\"";// 

	     Log.d("ssid : ", wc.SSID );

	    
	     
	      int ret = wifi.addNetwork(wc);
	        
	        boolean a = wifi.enableNetwork(ret, false);   
	        
	        boolean b = wifi.saveConfiguration();
	        
	        boolean c = wifi.enableNetwork(ret, true);
		}catch (Exception e) {
				Toast.makeText(this, "error:" + e.getMessage(), Toast.LENGTH_LONG).show();
			}

	}
	
	private void connectWEP(String ssid, Boolean hiddenSsid,String preSharedKey){
		
		
		try {
			
		   
			
	     WifiManager wifi = (WifiManager)getSystemService(WIFI_SERVICE);
	     WifiConfiguration wc = new WifiConfiguration(); 
	     wc.SSID = "\"" + ssid + "\""; //IMP! This should be in Quotes!!
	     wc.hiddenSSID = hiddenSsid;
	     wc.status = WifiConfiguration.Status.ENABLED;     
	     wc.priority = 40;
	     wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
	     wc.allowedProtocols.set(WifiConfiguration.Protocol.RSN); 
  	    wc.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
  	    wc.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
  	    wc.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
  	    wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
  	    wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
  	    wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
  	    wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
         wc.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);

	     wc.wepKeys[0] = "\"" + preSharedKey + "\"";// 
	     wc.wepTxKeyIndex = 0;

	     Log.d("ssid : ", wc.SSID );

	    
	     
	      int ret = wifi.addNetwork(wc);
	        
	        boolean a = wifi.enableNetwork(ret, false);   
	        
	        boolean b = wifi.saveConfiguration();
	        
	        boolean c = wifi.enableNetwork(ret, true);
		}catch (Exception e) {
				Toast.makeText(this, "error:" + e.getMessage(), Toast.LENGTH_LONG).show();
			}

	}
	
}
