package com.eduroam;


import java.io.File;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpHost;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.net.wifi.WifiConfiguration;

import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;

import android.widget.Button;
import android.widget.Toast;


public class EduroamConnectActivity extends Activity implements OnClickListener{
	
   Button connect,disconnect,parser,getXml,proxy,configure;
   XmlParser xml;
private WifiManager wifi;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
      
        
        wifi = (WifiManager)getSystemService(WIFI_SERVICE); 
        
        connect=(Button) findViewById(R.id.btnConnect);
        connect.setOnClickListener(this);
        
        disconnect=(Button) findViewById(R.id.btnDisconnect);
        disconnect.setOnClickListener(this);
        
        parser=(Button) findViewById(R.id.btnParser);
        parser.setOnClickListener(this);
        
        getXml=(Button) findViewById(R.id.btnGetXml);
        getXml.setOnClickListener(this);
        
        proxy=(Button) findViewById(R.id.btnSetProxy);
        proxy.setOnClickListener(this);
        
        configure=(Button) findViewById(R.id.btnConfigure);
        configure.setOnClickListener(this);
        
       
	}
	public void onClick(View v) {
		Boolean  returnValue;
		//connect button pressed.
		if (v.getId() == R.id.btnConnect) {
			// if an eduroam connection is already exists, we clear it.
			clearEduroamConnection();
			
			//if wifi is not enabled then we sure it is enabled.
			if (!wifi.isWifiEnabled()) {
				wifi.setWifiEnabled(true);
			}
			//Then we can start a new scan.
			returnValue = wifi.startScan();
			
			// to here it should be better to save.
			wifi.saveConfiguration();
			
			connectEduroam();
			
			
		}else if (v.getId() == R.id.btnDisconnect) {
			clearEduroamConnection();
			
		}else if (v.getId() == R.id.btnParser) {
			 xml = new XmlParser(getResources());
			 //Toast.makeText(this,"EncryptionType "+xml.getConfigurationObject("PayloadContent.EncryptionType"), Toast.LENGTH_LONG).show();
			
			 ArrayList<Object> item = (ArrayList<Object>) xml.getConfigurationObject("PayloadContent");
			 Toast.makeText(this, item.get(1).toString(), Toast.LENGTH_LONG).show();

		}else if (v.getId() == R.id.btnGetXml) {
			String url = "http://mesutcang.net23.net/dosya/wireless_profile.xml";
			downloadFile(url);
		}else if (v.getId() == R.id.btnConfigure) {
			if (xml ==null) {
				xml = new XmlParser(getResources());
			}
			configure();
		}else if (v.getId() == R.id.btnSetProxy) {
			String ip = "192.168.1.1";
			Integer port = 3128;
			setProxy(ip,port);
		}
		
	}
	private void configure() {
		String conType = getConnectionType();
		Toast.makeText(this,conType, Toast.LENGTH_LONG).show();
		
	}
	private String getConnectionType() {
		if (xml.getConfigurationObject("PayloadContent").toString().trim().equals("WPA")) {
			
		}
		return null;
	}
	private void setProxy(String ip,Integer port) {
		setProxyHostField(new HttpHost(ip, port));
		
	}
	
	
	// Problem is setting proxy in android versions before 2.3 especially with HTC. 
    private boolean setProxyHostField(HttpHost proxyServer) {
    // Getting network      
    Class networkClass = null;
    Object network = null;
    try {
        networkClass = Class.forName("android.webkit.Network");
        Field networkField = networkClass.getDeclaredField("sNetwork");
        network = getFieldValueSafely(networkField, null);
    } catch (Exception ex) {
    	Toast.makeText(this, "error getting network", Toast.LENGTH_LONG).show();
       
        return false;
    }
    if (network == null) {
    	Toast.makeText(this, "error getting network : null", Toast.LENGTH_LONG).show();
       
        return false;
    }
    Object requestQueue = null;
    try {
        Field requestQueueField = networkClass
                .getDeclaredField("mRequestQueue");
        requestQueue = getFieldValueSafely(requestQueueField, network);
    } catch (Exception ex) {
    	Toast.makeText(this, "error getting field value", Toast.LENGTH_LONG).show();
        
        return false;
    }
    if (requestQueue == null) {
    	Toast.makeText(this, "Request queue is null", Toast.LENGTH_LONG).show();
       
        return false;
    }
    Field proxyHostField = null;
    try {
        Class requestQueueClass = Class.forName("android.net.http.RequestQueue");
        proxyHostField = requestQueueClass
                .getDeclaredField("mProxyHost");
    } catch (Exception ex) {
    	Toast.makeText(this, "error getting proxy host field", Toast.LENGTH_LONG).show();
        
        return false;
    }       
  
        boolean temp = proxyHostField.isAccessible();
        try {
            proxyHostField.setAccessible(true);
            proxyHostField.set(requestQueue, proxyServer);
        } catch (Exception ex) {
        	Toast.makeText(this, "error setting proxy host", Toast.LENGTH_LONG).show();
            
        } finally {
            proxyHostField.setAccessible(temp);
        }
  
    return true;
}

private Object getFieldValueSafely(Field field, Object classInstance) throws IllegalArgumentException, IllegalAccessException {
    boolean oldAccessibleValue = field.isAccessible();
    field.setAccessible(true);
    Object result = field.get(classInstance);
    field.setAccessible(oldAccessibleValue);
    return result;      
}

	
	/*
	 * Downloads specified file from url.
	 */
	private void downloadFile(String url) {
		
		 try {
			URL u = new URL(url);

			HttpURLConnection c = (HttpURLConnection) u.openConnection();
			c.setRequestMethod("GET");
			c.setDoOutput(true);
			c.connect();
			File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"wireless_profile.xml");
			FileOutputStream f = new FileOutputStream(file);


			InputStream in = c.getInputStream();

			byte[] buffer = new byte[1024];
			Integer len1 = 0;
			while ( (len1 = in.read(buffer)) > 0 ) {
				Log.d("downloading", len1.toString());
		         f.write(buffer,0, len1);
			}

			Toast.makeText(this, file.getAbsolutePath(), Toast.LENGTH_LONG).show();
			
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
	/*
	 * connectEduroam method configure security and user parametres and 
	 * establishes an eduroam connection. This method uses java reflection 
	 * api to set parametres.
	 */
	private void connectEduroam() {
		//some constants for java reflection api
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
	    String userName = "mesut.gurle@deu.edu.tr";
	    String password = "xxxx"; //password maked.
	    String ssid = "eduroam";
	    
	    
	    //connection properties
	    final String ENTERPRISE_EAP = "TTLS";
        final String ENTERPRISE_CLIENT_CERT = null;
        final String ENTERPRISE_PRIV_KEY = null;        
        
        final String ENTERPRISE_PHASE2 = "auth=PAP";
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
	 * clearEduroamConnection method gets configured networks and if find a ssid name which contains
	 * "eduroam" then removes this ssid.
	 */
	private void clearEduroamConnection() {
		 List <WifiConfiguration> currentConfigs;
			currentConfigs = wifi.getConfiguredNetworks();
			
			for (WifiConfiguration currentConfig : currentConfigs) 
			{
				if (currentConfig.SSID.contains("eduroam")) 
					{
						wifi.removeNetwork(currentConfig.networkId);
						
					}
			}
			
		wifi.saveConfiguration();
	}
	
	
}
