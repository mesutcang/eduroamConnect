package com.eduroam;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Preview extends Activity implements OnClickListener {
	 XmlParser xml;
	File configurationFile;
	ConfigurationRegularExpressionFieldParser parse = new ConfigurationRegularExpressionFieldParser();
	TextView tvConnectionName,tvOrganization,tvDescription,tvReceived,tvContains,tvNetwork,tvEncryption;
	private WifiManager wifi;
	Button btnInstall ;
	Options options = new Options();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.preview);
		 wifi = (WifiManager)getSystemService(WIFI_SERVICE); 
		 
		 tvConnectionName = (TextView) findViewById(R.id.tvConnectionName);
		 tvOrganization = (TextView) findViewById(R.id.tvOrganization);
		 tvDescription = (TextView) findViewById(R.id.tvDescription);
		 tvReceived = (TextView) findViewById(R.id.tvReceived);
		 tvContains = (TextView) findViewById(R.id.tvContains);
		 tvNetwork = (TextView) findViewById(R.id.tvNetwork);
		 tvEncryption = (TextView) findViewById(R.id.tvEncryption);
		
		 btnInstall = (Button) findViewById(R.id.btnInstall);
		 btnInstall.setOnClickListener(this);
		 
		 
		 
		 
		 Bundle extra = getIntent().getExtras();
		 String filePath = (String) extra.get("param");
		 
		 configurationFile = new File(filePath);
		 
		 
		 String fileName = configurationFile.getName();
			fileName = fileName.substring(0, fileName.lastIndexOf("."));
			
			InputStream is = null;
			try {
				is = new FileInputStream(configurationFile);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				Toast.makeText(this, "error: "+e.getMessage(), Toast.LENGTH_LONG).show();
 			}
			if (xml ==null) {
				//xml = new XmlParser(getResources(),"dot1x.mobileconfig",getResources().openRawResource(R.raw.dot1x));
				xml = new XmlParser(getResources(),fileName,is);
			}
			
		
			fillInterface();
			
			
	}
	
	private void fillInterface() {
		
		tvConnectionName.setText(parse.getPayloadDisplayName(xml));
		tvOrganization.setText(parse.getPayloadOrganization(xml));
		tvDescription.setText(parse.getPayloadDescription(xml));
		Date d = new Date(configurationFile.lastModified());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		tvReceived.setText(dateFormat.format(d) );
		
		
		String conType = parse.getEncryptionType(xml);
		String eapType = options.getAcceptEAPTypeDefinition( parse.getAcceptEAPTypes(xml));
		if (conType.equalsIgnoreCase("WEP") ||conType.equalsIgnoreCase("WPA") ) {
			tvContains.setText("Wifi Network");
		}else {
			tvContains.setText("Unknown");
		}
		
		tvNetwork.setText(parse.getPayloadContentSSID_STR(xml));
		tvEncryption.setText(conType + " "+eapType);
		
		
		
		
		
		
	}

	private void configure() {
		
		
		
		String conType = parse.getEncryptionType(xml);
		String eapType = options.getAcceptEAPTypeDefinition( parse.getAcceptEAPTypes(xml));
		
		if (eapType.equalsIgnoreCase("PEAP")) {
			
			try {
				String ssid = parse.getPayloadContentSSID_STR(xml);
				String hiddenSSID = parse.getHiddenNetwork(xml);
				String userName = parse.getUsernameField(xml);
				String userPassword = parse.getUserPassword(xml);
				
				
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

	public void onClick(View v) {
		if (v.getId() == R.id.btnInstall) {
			configure();
		}
		
	}

}
