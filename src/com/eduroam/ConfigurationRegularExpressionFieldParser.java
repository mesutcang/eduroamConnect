package com.eduroam;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigurationRegularExpressionFieldParser {
	
	public String parseField(String search, String field){
		
		String foundValue="";
		
		int fieldIndex = search.indexOf(field);
		
		if (fieldIndex != -1) {
			// field is found.
			
			int endIndex = search.indexOf(",", fieldIndex);
			if (endIndex == -1) {
				//if it is not ending in "," it must be a "}" at the end.
				endIndex = search.indexOf("}", fieldIndex);
				
			}
			
			foundValue = search.substring(fieldIndex + field.length()+1 , endIndex);
					
		}
		
		
		return foundValue;
		
		
		
	}

	public String getUsernameField(XmlParser xml) {
		String field = null;
		
		 ArrayList<Object>  configuration = (ArrayList<Object>) xml.getConfigurationObject("PayloadContent");
		 
		 field = this.parseField( configuration.get(1).toString(), "UserName");
		 
		 field = field.replace("[% ", "");
		 field = field.replace(" %]", "");
		 
		return field; 
				
	}

	public String getPayloadDescription(XmlParser xml) {
		String field = null;
		
		 field = (String) xml.getConfigurationObject("PayloadDescription");
		 
		
		 return field;
	}
	
	public String getPayloadDisplayName(XmlParser xml) {
		String field = null;
		
		 field = (String) xml.getConfigurationObject("PayloadDisplayName");
		 
		
		 return field;
	}
	
	public String getPayloadIdentifier(XmlParser xml) {
		String field = null;
		
		 field = (String) xml.getConfigurationObject("PayloadIdentifier");
		 
		
		 return field;
	}
	
	public String getPayloadOrganization(XmlParser xml) {
		String field = null;
		
		 field = (String) xml.getConfigurationObject("PayloadOrganization");
		 
		
		 return field;
	}
	
	public Boolean getPayloadRemovalDisallowed(XmlParser xml) {
		Boolean field = null;
		
		 field = (Boolean) xml.getConfigurationObject("PayloadRemovalDisallowed");
		 
		
		 return field;
	}
	
	public String getPayloadType(XmlParser xml) {
		String field = null;
		
		 field = (String) xml.getConfigurationObject("PayloadType");
		 
		
		 return field;
	}
	
	public String getPayloadUUID(XmlParser xml) {
		String field = null;
		
		 field = (String) xml.getConfigurationObject("PayloadUUID");
		 
		
		 return field;
	}
	
	public Integer getPayloadVersion(XmlParser xml) {
		Integer field = null;
		
		 field = (Integer) xml.getConfigurationObject("PayloadVersion");
		 
		
		 return field;
	}
	
	public String getAcceptEAPTypes(XmlParser xml) {
		String field = null;
		
		 ArrayList<Object>  configuration = (ArrayList<Object>) xml.getConfigurationObject("PayloadContent");
		 
		 field = this.parseField( configuration.get(1).toString(), "AcceptEAPTypes");
		 
		 
		return field; 
				
	}
	
	public String getEAPFASTProvisionPAC(XmlParser xml) {
		String field = null;
		
		 ArrayList<Object>  configuration = (ArrayList<Object>) xml.getConfigurationObject("PayloadContent");
		 
		 field = this.parseField( configuration.get(1).toString(), "EAPFASTProvisionPAC");
		 
		 
		return field; 
				
	}
	
	public String getEAPFASTProvisionPACAnonymously(XmlParser xml) {
		String field = null;
		
		 ArrayList<Object>  configuration = (ArrayList<Object>) xml.getConfigurationObject("PayloadContent");
		 
		 field = this.parseField( configuration.get(1).toString(), "EAPFASTProvisionPACAnonymously");
		 
		 
		return field; 
				
	}
	
	public String getEAPFASTUsePAC(XmlParser xml) {
		String field = null;
		
		 ArrayList<Object>  configuration = (ArrayList<Object>) xml.getConfigurationObject("PayloadContent");
		 
		 field = this.parseField( configuration.get(1).toString(), "EAPFASTUsePAC");
		 
		 
		return field; 
				
	}
	
	public String getTLSAllowTrustExceptions(XmlParser xml) {
		String field = null;
		
		 ArrayList<Object>  configuration = (ArrayList<Object>) xml.getConfigurationObject("PayloadContent");
		 
		 field = this.parseField( configuration.get(1).toString(), "TLSAllowTrustExceptions");
		 
		 
		return field; 
				
	}
	
	public String getUserPassword(XmlParser xml) {
		String field = null;
		
		 ArrayList<Object>  configuration = (ArrayList<Object>) xml.getConfigurationObject("PayloadContent");
		 
		 field = this.parseField( configuration.get(1).toString(), "UserPassword");
		 
		 
		return field; 
				
	}
	
	public String getEncryptionType(XmlParser xml) {
		String field = null;
		
		 ArrayList<Object>  configuration = (ArrayList<Object>) xml.getConfigurationObject("PayloadContent");
		 
		 field = this.parseField( configuration.get(0).toString(), "EncryptionType");
		 
		 
		return field; 
				
	}
	
	public String getHiddenNetwork(XmlParser xml) {
		String field = null;
		
		 ArrayList<Object>  configuration = (ArrayList<Object>) xml.getConfigurationObject("PayloadContent");
		 
		 field = this.parseField( configuration.get(0).toString(), "HIDDEN_NETWORK");
		 
		 
		return field; 
				
	}
	
	public String getPayloadContentPayloadDescription(XmlParser xml) {
		String field = null;
		
		 ArrayList<Object>  configuration = (ArrayList<Object>) xml.getConfigurationObject("PayloadContent");
		 
		 field = this.parseField( configuration.get(0).toString(), "PayloadDescription");
		 
		 
		return field; 
				
	}
	
	public String getPayloadContentPayloadDisplayName(XmlParser xml) {
		String field = null;
		
		 ArrayList<Object>  configuration = (ArrayList<Object>) xml.getConfigurationObject("PayloadContent");
		 
		 field = this.parseField( configuration.get(0).toString(), "PayloadDisplayName");
		 field = field.replace("Wi-Fi ([% ", "");
		 field = field.replace(" %])", "");
		 
		return field; 
				
	}
	
	public String getPayloadContentPayloadIdentifier(XmlParser xml) {
		String field = null;
		
		 ArrayList<Object>  configuration = (ArrayList<Object>) xml.getConfigurationObject("PayloadContent");
		 
		 field = this.parseField( configuration.get(0).toString(), "PayloadIdentifier");
		 
		 
		return field; 
				
	}
	
	public String getPayloadContentPayloadOrganization(XmlParser xml) {
		String field = null;
		
		 ArrayList<Object>  configuration = (ArrayList<Object>) xml.getConfigurationObject("PayloadContent");
		 
		 field = this.parseField( configuration.get(0).toString(), "PayloadOrganization");
		 
		 
		return field; 
				
	}
	
	public String getPayloadContentPayloadType(XmlParser xml) {
		String field = null;
		
		 ArrayList<Object>  configuration = (ArrayList<Object>) xml.getConfigurationObject("PayloadContent");
		 
		 field = this.parseField( configuration.get(0).toString(), "PayloadType");
		 
		 
		return field; 
				
	}
	
	public String getPayloadContentPayloadUUID(XmlParser xml) {
		String field = null;
		
		 ArrayList<Object>  configuration = (ArrayList<Object>) xml.getConfigurationObject("PayloadContent");
		 
		 field = this.parseField( configuration.get(0).toString(), "PayloadUUID");
		 
		 
		return field; 
				
	}
	
	public String getPayloadContentPayloadVersion(XmlParser xml) {
		String field = null;
		
		 ArrayList<Object>  configuration = (ArrayList<Object>) xml.getConfigurationObject("PayloadContent");
		 
		 field = this.parseField( configuration.get(0).toString(), "PayloadVersion");
		 
		 
		return field; 
				
	}
	
	public String getPayloadContentSSID_STR(XmlParser xml) {
		String field = null;
		
		 ArrayList<Object>  configuration = (ArrayList<Object>) xml.getConfigurationObject("PayloadContent");
		 
		 field = this.parseField( configuration.get(0).toString(), "SSID_STR");
		 
		 field = field.replace("[% ", "");
		 field = field.replace(" %]", "");
		return field; 
				
	}

}
