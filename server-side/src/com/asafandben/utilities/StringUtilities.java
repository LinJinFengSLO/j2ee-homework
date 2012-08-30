package com.asafandben.utilities;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringUtilities {

	// Do not start this.
	private StringUtilities() {
	}

	public static String getMD5StringfromString(String input) throws NoSuchAlgorithmException {

		String res = "";
		MessageDigest algorithm = MessageDigest.getInstance("MD5");
		algorithm.reset();
		algorithm.update(input.getBytes());
		byte[] md5 = algorithm.digest();
		String tmp = "";
		for (int i = 0; i < md5.length; i++) {
			tmp = (Integer.toHexString(0xFF & md5[i]));
			if (tmp.length() == 1) {
				res += "0" + tmp;
			} else {
				res += tmp;
			}
		}
		return res;

	}

	public static boolean isStringEmptyOrNull(String strName) {
		if (strName == null) 
			return true;
		
		return strName.isEmpty();
	}
	
	public static boolean allStringsAreNotEmpty(String ... args) {
		for (String currentString : args) {
			if (isStringEmptyOrNull(currentString))
				return false;
		}
		
		return true;
			
	}
	
	public static boolean isEmailValid(String email) {
		Pattern pattern;
		Matcher matcher;

		String EMAIL_PATTERN = "[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})";

		pattern = Pattern.compile(EMAIL_PATTERN);

		matcher = pattern.matcher(email);
		return matcher.matches();

	}

	public static final String UTF_8 = "UTF-8";

}
