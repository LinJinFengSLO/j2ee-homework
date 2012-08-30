package utilities;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
		
		String responseString;
		Cookie[] cookies;
	}
		
	public static HttpResponseInfo SendGetToSecurityServlet(Cookie[] cookies) {
		try {

			HttpURLConnection connection = null;	
			URL url = new URL(HttpConsts.SECURITY_SERVLET_ADDRESS);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			// Passing current cookies
			connection.setRequestProperty("Cookie", cookiesArrayToCookiesString(cookies) );
			connection.connect();
			connection.getInputStream();
			// Retrieving response body
			String response = new java.util.Scanner(connection.getInputStream()).useDelimiter("\\A").next();
			// Retrieving new cookies
			Cookie[] retCookies = CookieStringToCookieArray(connection.getHeaderField("Set-Cookie"));
			HttpResponseInfo responseInfo = new HttpResponseInfo(response, retCookies);
			
			return responseInfo;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	public static HttpResponseInfo SendPostToSecurityServlet(String body, Cookie[] cookies) {
		try {
			// Open connection
			HttpURLConnection connection = null;	
	        URL url = new URL("http://localhost:8080/TaskManagement/security");
	        connection = (HttpURLConnection) url.openConnection();
	        connection.setRequestMethod("POST");
	        connection.setDoInput(true);
	        connection.setDoOutput(true);
	        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	        DataOutputStream outStream = null;
	        
	        // Create I/O streams
	        outStream = new DataOutputStream(connection.getOutputStream());
	        connection.connect();
		    outStream.writeBytes(body);
		    outStream.flush();
		    outStream.close();
		    //DataInputStream inStream = new DataInputStream(connection.getInputStream());
	        
		    // Retrieve and Parse cookie
	        String cookieString = connection.getHeaderField("Set-Cookie");
	        Cookie[] retCookies = CookieStringToCookieArray(cookieString);
			
	        HttpResponseInfo responseInfo = new HttpResponseInfo(retCookies);
	        return responseInfo;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static Cookie[] CookieStringToCookieArray(String cookieString) {

        List<Cookie> cookiesList = new ArrayList<Cookie>();
        
		if (cookieString != null) {
        	String delimiter = "=";
        	// Parse cookie name
        	String cookieName = cookieString.substring(0, cookieString.indexOf(';'));
        	String[] nameTemp;
        	nameTemp = cookieName.split(delimiter);
        	
        	// Parse cookie max age
        	String cookieMaxAge = cookieString.substring(2, cookieString.indexOf(';'));
        	String[] maxAgeTemp;
        	maxAgeTemp = cookieMaxAge.split(delimiter);
        	
        	// Creating the cookie
        	Cookie temp = new Cookie(nameTemp[0],nameTemp[1]);
        	temp.setMaxAge(Integer.parseInt(maxAgeTemp[1]));
        	temp.setPath("/");
        	cookiesList.add(temp);
        }
		
	    Cookie[] cookies = new Cookie[cookiesList.size()];
		cookiesList.toArray(cookies);
		
        return cookies;
	}

	private static String cookiesArrayToCookiesString(Cookie[] cookies) {
		String cookiesString = null;
		if (cookies != null) {
			cookiesString = cookies[0].getName() + "=" + cookies[0].getValue();
			for (int i=1; i<cookies.length; ++i) {
				cookiesString += "; " + cookies[i].getName() + "=" + cookies[i].getValue();
			}
		}
		return cookiesString;
	}
}
