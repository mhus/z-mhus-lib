/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
	
	// http://stackoverflow.com/questions/2385701/regular-expression-for-first-and-last-name
	/**
	 * International name. This is a simple test and should be extended ...
	 * 
	 * What about:
	 * - names with only upper chars.
	 * - Ann-Sophie
	 *
	 * 
	 * @param in
	 * @return true if it's a first name
	 */
	public static boolean isFirstName(String in) {
		if (in == null) return false;
		if (in.length() < 2) return false;
		return in.matches( "[a-zA-ZàáâäãåąčćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçčšžÀÁÂÄÃÅĄĆČĖĘÈÉÊËÌÍÎÏĮŁŃÒÓÔÖÕØÙÚÛÜŲŪŸÝŻŹÑßÇŒÆČŠŽ∂ð '-.,0-9].*" );
	}
	
	/**
	 * International name. This is a simple test and should be extended ...
	 * What about:
	 * - DIETER-FILSINGER (should fail, only upper chars)
	 * - "King, Jr." (Allowed in a strict matter?, "Jr" is not part of the name.)
	 * - Dieter-Filsinger (should pass, Not only upper chars but more then the first one)
	 * - DieTer-Filsinger (Should fail)
	 * - Van Gerben (Space in the name)
	 *
	 * @param in
	 * @return true if it's a last name
	 */
	public static boolean isLastName(String in) {
		if (in == null) return false;
		if (in.length() < 2) return false;
		// \u00e0\u00e1\u00e2\u00e4\u00e3\u00e5\u0105\u010d\u0107\u0119\u00e8\u00e9\u00ea\u00eb\u0117\u012f\u00ec\u00ed\u00ee\u00ef\u0142\u0144\u00f2\u00f3\u00f4\u00f6\u00f5\u00f8\u00f9\u00fa\u00fb\u00fc\u0173\u016b\u00ff\u00fd\u017c\u017a\u00f1\u00e7\u010d\u0161\u017e\u00c0\u00c1\u00c2\u00c4\u00c3\u00c5\u0104\u0106\u010c\u0116\u0118\u00c8\u00c9\u00ca\u00cb\u00cc\u00cd\u00ce\u00cf\u012e\u0141\u0143\u00d2\u00d3\u00d4\u00d6\u00d5\u00d8\u00d9\u00da\u00db\u00dc\u0172\u016a\u0178\u00dd\u017b\u0179\u00d1\u00df\u00c7\u0152\u00c6\u010c\u0160\u017d\u2202\u00f0
		return in.matches( "[a-zA-ZàáâäãåąčćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçčšžÀÁÂÄÃÅĄĆČĖĘÈÉÊËÌÍÎÏĮŁŃÒÓÔÖÕØÙÚÛÜŲŪŸÝŻŹÑßÇŒÆČŠŽ∂ð '-].*" );
	}
	
	public static boolean isAddress( String in ) {
		if (in == null) return false;
		if (in.length() < 2) return false;
	      return in.matches( "\\d+\\s+([a-zA-ZàáâäãåąčćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçčšžÀÁÂÄÃÅĄĆČĖĘÈÉÊËÌÍÎÏĮŁŃÒÓÔÖÕØÙÚÛÜŲŪŸÝŻŹÑßÇŒÆČŠŽ∂ð]+|[a-zA-ZàáâäãåąčćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçčšžÀÁÂÄÃÅĄĆČĖĘÈÉÊËÌÍÎÏĮŁŃÒÓÔÖÕØÙÚÛÜŲŪŸÝŻŹÑßÇŒÆČŠŽ∂ð]+\\s[a-zA-ZàáâäãåąčćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçčšžÀÁÂÄÃÅĄĆČĖĘÈÉÊËÌÍÎÏĮŁŃÒÓÔÖÕØÙÚÛÜŲŪŸÝŻŹÑßÇŒÆČŠŽ∂ð]+)" );
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
	
	public static boolean isPhoneNumber(String phone) {
		return isPhoneNumber(phone, null);
	}
	
	public static boolean isPhoneNumber(String phone, Locale locale) {
		if (MString.isEmpty(phone)) return false;
		//validate phone numbers of format "+123-456 7890"
		// 49 1234 123456 1234 => 19 Digits (max to 20)
		if (phone.startsWith("+")) phone = phone.substring(1);
		if (phone.length() < 4 || phone.length() > 20) return false;
		if (locale != null) {
			if ("US".equals(locale.getCountry())) {
		        return phone.matches("^(\\([0-9]{3}\\) |[0-9]{3}-)[0-9]{3}-[0-9]{4}$");
			}
		}
        return phone.matches("^[0-9| |\\-|/]*$");
    }

	public static boolean isNumber(String nr) {
		if (nr == null) return false;
		// http://www.regular-expressions.info/floatingpoint.html
		return nr.matches("^[-+]?[0-9]*\\.?[0-9]+$");
	}
	
	public static boolean isInteger(String nr) {
		if (nr == null) return false;
		return nr.matches("^[-+]?[0-9]*$");
	}
	
	public static boolean isIPv4(String ip) {
		if (ip == null) return false;
		return ip.matches("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
	}
	
}
