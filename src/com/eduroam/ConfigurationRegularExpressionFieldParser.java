package com.eduroam;

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
		
		
		
		
		
		 /*String foundValue="";
		 
		 Pattern p = Pattern.compile(field + "=\\w+");
		 Matcher m = p.matcher(search);
		 if (m.find()) {
			String found = m.group();
			foundValue = found.substring(field.length()+1);
		}
		
		return foundValue;*/
		
		
		
	}

}
