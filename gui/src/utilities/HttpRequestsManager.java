package utilities;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;

import constants.HttpConsts;

public abstract class HttpRequestsManager {

	public static class HttpResponseInfo {
		public HttpResponseInfo(String responseString, Cookie[] cookies) {
			super();
			this.responseString = responseString;
			this.cookies = cookies;
		}
		
		public HttpResponseInfo(Cookie[] cookies) {
			super();
			this.responseString = null; 
			this.cookies = cookies;
		}
		
		public String responseString;
		public Cookie[] cookies;
	}
		
	public static HttpResponseInfo doGetToSecurityServlet(Cookie[] cookies) {
		try {

			HttpURLConnection connection = null;	
			URL url = new URL(HttpConsts.SECURITY_SERVLET_ADDRESS);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
			connection.setRequestMethod("GET");
			// Passing current cookies
			connection.setRequestProperty("Cookie", cookiesArrayToCookiesString(cookies) );
			connection.connect();
			connection.getInputStream();
			// Retrieving response body
			String response = new java.util.Scanner(connection.getInputStream()).useDelimiter("\\A").next();
			// Retrieving new cookies
			String retCookiesString = connection.getHeaderField("Set-Cookie");
			Cookie[] retCookies = CookieStringToCookieArray(retCookiesString);
			HttpResponseInfo responseInfo = new HttpResponseInfo(response, retCookies);
			
			return responseInfo;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	public static HttpResponseInfo doPostToSecurityServlet(String body, Cookie[] cookies) {
		try {
			// Open connection
			HttpURLConnection connection = null;	
	        URL url = new URL("http://localhost:8080/TaskManagement/security");
	        connection = (HttpURLConnection) url.openConnection();
	        connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
	        connection.setRequestMethod("POST");
			// Passing current cookies
			connection.setRequestProperty("Cookie", cookiesArrayToCookiesString(cookies));
	        connection.setDoInput(true);
	        connection.setDoOutput(true);
	        DataOutputStream outStream = null;
	        
	        // Create I/O streams
	        outStream = new DataOutputStream(connection.getOutputStream());
	        connection.connect();
		    outStream.writeBytes(body);
		    outStream.flush();
		    outStream.close();
		    DataInputStream inStream = null;
	    	inStream = new DataInputStream(connection.getInputStream());
	        
		    // Retrieve and Parse cookie
		    String retCookiesString = connection.getHeaderField("Set-Cookie");
	        Cookie[] retCookies = CookieStringToCookieArray(retCookiesString);
			
	        HttpResponseInfo responseInfo = new HttpResponseInfo(retCookies);
	        return responseInfo;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static HttpResponseInfo doGetToUserServlet(String params, Cookie[] cookies) {
		try {

			HttpURLConnection connection = null;	
			URL url = new URL(HttpConsts.USER_SERVLET_ADDRESS + "?" + params);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
			connection.setRequestMethod("GET");
			// Passing current cookies
			connection.setRequestProperty("Cookie", cookiesArrayToCookiesString(cookies) );
			connection.connect();
			connection.getInputStream();
			// Retrieving response body
			String response = new java.util.Scanner(connection.getInputStream()).useDelimiter("\\A").next();
			// Retrieving new cookies
			String retCookiesString = connection.getHeaderField("Set-Cookie");
			Cookie[] retCookies = CookieStringToCookieArray(retCookiesString);
			HttpResponseInfo responseInfo = new HttpResponseInfo(response, retCookies);
			
			return responseInfo;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static HttpResponseInfo doGetToTaskServlet(String params, Cookie[] cookies) {
		try {

			HttpURLConnection connection = null;	
			URL url = new URL(HttpConsts.TASK_SERVLET_ADDRESS + "?" + params);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
			connection.setRequestMethod("GET");
			// Passing current cookies
			connection.setRequestProperty("Cookie", cookiesArrayToCookiesString(cookies) );
			connection.connect();
			connection.getInputStream();
			// Retrieving response body
			String response = new java.util.Scanner(connection.getInputStream()).useDelimiter("\\A").next();
			// Retrieving new cookies
			String retCookiesString = connection.getHeaderField("Set-Cookie");
			Cookie[] retCookies = CookieStringToCookieArray(retCookiesString);
			HttpResponseInfo responseInfo = new HttpResponseInfo(response, retCookies);
			
			return responseInfo;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static Cookie[] CookieStringToCookieArray(String cookieString) throws UnsupportedEncodingException {

        List<Cookie> cookiesList = new ArrayList<Cookie>();
        
		if (cookieString != null) {
			cookieString = URLDecoder.decode(cookieString, "UTF-8");
	        int cookieStartIndex = cookieString.indexOf(HttpConsts.LOGIN_COOKIE_NAME);
	        String delimiter = "=";
	        
			while(cookieStartIndex != -1) {
	        	// Parse cookie name
	        	String cookieName = cookieString.substring(0, cookieString.indexOf(';'));
	        	String[] nameTemp = cookieName.split(delimiter);
	        	
	        	// Init the cookie
	        	Cookie temp = new Cookie(nameTemp[0],nameTemp[1]);
	        	
	        	// Handle Max-Age field
	        	int maxAgeIndex = cookieString.indexOf(HttpConsts.COOKIE_MAX_AGE_FIELD_NAME);
	        	if (maxAgeIndex != -1) {
	        		// Eat up cookie string
	        		cookieString = cookieString.substring(maxAgeIndex);
	        	
		        	// Parse cookie max age field
		        	String cookieMaxAge = cookieString.substring(0, cookieString.indexOf(';'));
		        	String[] maxAgeTemp;
		        	maxAgeTemp = cookieMaxAge.split(delimiter);
		        	temp.setMaxAge(Integer.parseInt(maxAgeTemp[1]));
	        	} else {
	        		// Eat up cookie string
	        		cookieString = cookieString.substring(cookieString.indexOf(';')+1);
	        	}
	        	
	        	temp.setPath("/");
	        	cookiesList.add(temp);
	        	
	        	// Eat up cookie string
	        	cookieStartIndex = cookieString.indexOf(HttpConsts.LOGIN_COOKIE_NAME);
	        	if (cookieStartIndex != -1) {
	        		cookieString = cookieString.substring(cookieStartIndex);
	        	}
			}
        }
		
	    Cookie[] cookies = new Cookie[cookiesList.size()];
		cookiesList.toArray(cookies);
		
        return cookies;
	}

	private static String cookiesArrayToCookiesString(Cookie[] cookies) throws UnsupportedEncodingException {
		String cookiesString = null;
		if (cookies != null) {
			String charset = "UTF-8";
			
			cookiesString = String.format(cookies[0].getName() + "=%s",
					URLEncoder.encode(cookies[0].getValue(), charset));
			
			for (int i=1; i<cookies.length; ++i) {
				cookiesString += "; " + String.format(cookies[i].getName() + "=%s",
						URLEncoder.encode(cookies[i].getValue(), charset));
			}
		}
		return cookiesString;
	}
}
