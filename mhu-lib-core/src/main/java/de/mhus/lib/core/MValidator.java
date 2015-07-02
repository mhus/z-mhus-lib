package de.mhus.lib.core;

import java.util.Locale;

import de.mhus.lib.errors.NotSupportedException;

public class MValidator {

	public static boolean isEmailAddress(String email) {
		if (email == null) return false;
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
	}
	
	public static boolean isFirstName(String in) {
		if (in == null) return false;
		if (in.length() < 2) return false;
		return in.matches( "[A-Z][a-zA-Z]*" );
	}
	
	public static boolean isLastName(String in) {
		if (in == null) return false;
		if (in.length() < 2) return false;
		return in.matches( "[a-zA-z]+([ '-][a-zA-Z]+)*" );
	}
	
	public static boolean isAddress( String in ) {
		if (in == null) return false;
		if (in.length() < 2) return false;
	      return in.matches( "\\d+\\s+([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z]+)" );
	}
	
	public static boolean isPassword(String in, int maxLen, boolean needNumbers, boolean needSpecials) {
		if (in == null) return false;
		if (in.length() < maxLen) return false;
		if (in.length() == 0) return true;
		
		char c = in.charAt(0);
		if (!(c >='a' && c <= 'z' || c >='A' && c <= 'Z')) return false; // need to start with a letter
		if (!needNumbers && !needSpecials) return true;
		
		boolean hasNumber = false;
		boolean hasSpecial = false;
		for (int i = 0; i < in.length(); i++) {
			c = in.charAt(i);
			if (c >= '0' && c <= '9') hasNumber = true;
			else
			if (!(c >='a' && c <= 'z' || c >='A' && c <= 'Z')) hasSpecial = true;
		}
		
		if (needNumbers && !hasNumber) return false;
		if (needSpecials && !hasSpecial) return false;
		return true;
		
	}
	
	public static boolean isZipCode(Locale locale, String zip) {
		if (locale == null) {
		} else
		if (locale.getCountry().equals("DE")) {
			if (zip == null || zip.length() != 5) return false;
			int i = MCast.toint(zip, 0);
			return (i >= 1000 && i <= 99999);
		}
		throw new NotSupportedException("Country not supported",locale);
	}
	
	public static boolean isUUID(String id) {
		if (id == null || id.length() != 36) return false;
        String[] components = id.split("-");
        if (components.length != 5) return false;
        
        if (components[0].length() != 8 || components[1].length() != 4 || components[2].length() != 4 || components[3].length() != 4 || components[4].length() != 12)
        	return false;
        
        for (int i=0; i<5; i++) {
        	String part = components[i];
        	for (int j=0; j < part.length(); j++) {
        		char c = part.charAt(j);
        		if (c != '0' && c != '1' && c != '2' && c != '3' && c != '4' && c != '5' && c != '6' && c != '7' && c != '8' && c != '9' && c != 'a' && c != 'b' && c != 'c' && c != 'd' && c != 'e' && c != 'f' )
        			return false;
        	}
        }
        	
        return true;
	}
	
}
