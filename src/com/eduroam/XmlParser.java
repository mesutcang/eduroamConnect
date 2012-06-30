package com.eduroam;



import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.Stack;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;


import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.Toast;

public class XmlParser {
	String xmlResource="wireless_profile";
	private HashMap<String, Object> xmlConfiguration = new HashMap<String, Object>();
	Resources applicationResources;
	
	
	public XmlParser(Resources resources) {

		//getResources().openRawResource(R.raw.wireless_profile)
	
		
		
		
		
		this.applicationResources = resources;
		 if (checkFileExists()) {
			 //	File exists. So we can parse it.
			 
			 parseXml();
			
			 
			 
			 
			 //Toast.makeText(this,"EAPFASTProvisionPAC "+getConfigurationObject("PayloadDisplayName"), Toast.LENGTH_LONG).show();
			 
			
		}else {
			// File is not found. Exit.
			return;
		}
	}
	
	

	
	
  
private void parseXml() {
	

	try {
		
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		XmlPullParser parser = factory.newPullParser();
		
	    parser.setInput(applicationResources.openRawResource(R.raw.wireless_profile), "utf-8");

	    int eventType = parser.getEventType();
	    int arrayDepth = 0;

	    boolean done = false;
	    boolean parsingArray = false;

	    String name = null;
	    String key = null;

	    Stack<HashMap<String, Object>> stack = new Stack<HashMap<String, Object>>();
	    HashMap<String, Object> dict = null;
	    ArrayList<Object> array = null;

	    while (eventType != XmlPullParser.END_DOCUMENT && !done) {
	        switch(eventType) {
	            case XmlPullParser.START_DOCUMENT:
	                
	                break;
	            case XmlPullParser.START_TAG:
	                name = parser.getName();

	                if(name.equalsIgnoreCase("dict")) {
	                    
	                    if(key == null) {
	                        xmlConfiguration.clear();
	                        dict = xmlConfiguration;
	                    }
	                    else if(parsingArray) {
	                       
	                        HashMap<String, Object> childDict = new HashMap<String, Object>();
	                        array.add(childDict);
	                        stack.push(dict);
	                        dict = childDict;
	                    }
	                    else {
	                        
	                        HashMap<String, Object> childDict = new HashMap<String, Object>();
	                        dict.put(key, childDict);
	                        stack.push(dict);
	                        dict = childDict;
	                    }
	                } 
	                else if(name.equalsIgnoreCase("key")) {
	                   
	                	key = safeNextText(parser);
	                } 
	                else if(name.equalsIgnoreCase("integer")) {
	                   
	                	dict.put(key, new Integer(safeNextText(parser)));
	                }else if (name.equalsIgnoreCase("true") || name.equalsIgnoreCase("false") ) {
						dict.put(key, new Boolean(name));
					}
	                else if(name.equalsIgnoreCase("string")) {
	                    if(parsingArray && (parser.getDepth() == (arrayDepth + 1))) {
	                       
	                    	array.add(safeNextText(parser));
	                    } else {
	                       
	                    	dict.put(key, safeNextText(parser));
	                    }
	                }
	                else if(name.equalsIgnoreCase("array")) {
	                    parsingArray = true;
	                    array = new ArrayList<Object>();
	                    dict.put(key, array);
	                    arrayDepth = parser.getDepth();
	                }

	                break;
	            case XmlPullParser.END_TAG:
	                name = parser.getName();

	                if(name.equalsIgnoreCase("dict")) {
	                    
	                    if(!stack.empty()) {
	                        dict = stack.pop();
	                    }
	                } 
	                else if(name.equalsIgnoreCase("array")) {
	                    parsingArray = false;
	                    array = null;
	                }
	                else if(name.equalsIgnoreCase("plist")) {
	                    done = true;
	                }

	                break;
	            case XmlPullParser.END_DOCUMENT:
	               
	                break;

	        }
	        eventType = parser.next();
	    }
	}catch (Exception ex){
	    ex.printStackTrace();
	}       

}





	private boolean checkFileExists() {
		
		int xmlFile = 0; 
		/*
		 *	This try/catch block checks if wireless configuration profile is exists. 
		 */
		 
		try {
			
			xmlFile = applicationResources.getIdentifier(xmlResource,"raw",this.getClass().getPackage().getName());
			
			if (xmlFile == 0 ) {
				 //Toast.makeText(this,"Wireless configuration file cannot be found", Toast.LENGTH_LONG).show();
				 return false;
			}
			
		
			
		} catch (Resources.NotFoundException e) {
			
			 //Toast.makeText(this,"Wireless configuration file cannot be found", Toast.LENGTH_LONG).show();
			 return false;
			
		}catch (Exception e) {
			 //Toast.makeText(this,"Error occured!", Toast.LENGTH_LONG).show();
			 return false;
		}
		
		
		
		
		
		
		return true;
	}
	
	
	
	protected Object getConfigurationObject(String keyName) {
		
		String[] tokens = keyName.split("\\.");
		
		if(tokens.length >= 1) {
		    HashMap<String,Object> dict = xmlConfiguration;
		    Object obj;
		    for(int i = 0; i < tokens.length; i++) {
		        obj = dict.get(tokens[i]);
		        if(obj instanceof HashMap<?, ?>) {
		            dict = (HashMap<String, Object>) obj;
		            continue;
		        }
		        return obj;
		    }
		}

		return xmlConfiguration.get(keyName);
		
		}
	
	private String safeNextText(XmlPullParser parser)
	          throws XmlPullParserException, IOException {
	      String result = parser.nextText();
	      if (parser.getEventType() != XmlPullParser.END_TAG) {
	          parser.nextTag();
	      }
	      return result;
	  }


}
