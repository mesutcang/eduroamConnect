package com.eduroam;

import java.util.HashMap;
import java.util.Map;

public class Options {
	Map<String,String> eapTypes ;
	
	public Options() {
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

}
