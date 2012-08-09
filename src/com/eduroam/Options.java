package com.eduroam;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.os.Environment;
import android.widget.Toast;

public class Options {
	Map<String,String> eapTypes ;
	private String storageDirectory=".dot1x";
	public Options() {
		File dir = new File(Environment.getExternalStorageDirectory() + File.separator + storageDirectory);
		if(!dir.exists() || !dir.isDirectory()) {
		   try {
			   dir.mkdir();
		} catch (Exception e) {
				
		}
			
		    	
			
		}


		
		
		eapTypes = new HashMap();
		
		eapTypes.put("13", "TLS");
		eapTypes.put("17", "LEAP");
		eapTypes.put("21", "TTLS");
		eapTypes.put("25", "PEAP");
		eapTypes.put("43", "EAP-FAST");
	}
	
	
	public String getAcceptEAPTypeDefinition(String eapType) {
		
		
		return eapTypes.get(eapType);

	}


	public String getStorageDirectory() {
		return storageDirectory;
	}


	public void setStorageDirectory(String storageDirectory) {
		this.storageDirectory = storageDirectory;
	}
	
	

}
