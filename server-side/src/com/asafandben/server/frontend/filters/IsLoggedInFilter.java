package com.asafandben.server.frontend.filters;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;

import com.asafandben.server.frontend.filters.FiltersManager.FilterArguments;
import com.asafandben.server.frontend.filters.FiltersManager.FilterState;
import com.asafandben.utilities.FrontEndToBackEndConsts;
import com.asafandben.utilities.HttpConsts;
import com.asafandben.utilities.StringUtilities;

public class IsLoggedInFilter implements Filter {
	private String[] filterPaths = { "" };
	private static Map<String, String> loggedInUsers = new LinkedHashMap<String, String>();
	
	@Override
	public void doInbound(FilterArguments args) throws IOException,
			ServletException {
		
		Cookie[] userCookies = args.request.getCookies();
		Cookie loginCookie = null;
		String loginCookieValue = null;
		
		loginCookie = findLoginCookie(userCookies, loginCookie);
		
		// Check if user has the needed cookie. 
		if (loginCookie != null) {
			loginCookieValue = loginCookie.getValue();
			String[] sessionInfo = null;

			if (loginCookieValue!=null) {
				sessionInfo = loginCookieValue.split(HttpConsts.COOKIE_SEPERATOR);
			}
			
			boolean hasAllLoginValues = ((sessionInfo[0]!=null)&&(sessionInfo[1]!=null));
			// sessionInfo[0] should contain email and sessionInfo[1] should contain Session ID.
			
			if (hasAllLoginValues) {
				// Check login values validity
				if (loggedInUsers.get(sessionInfo[0]).equals(sessionInfo[1])) {
					// User is logged in.
					setIsLoggedIn(args, true, sessionInfo[0]);	
				}
				else {
					setIsLoggedIn(args, false, null);	
					removeUserCookie(loginCookie, args);
				}
			}
			else {
				setIsLoggedIn(args, false, null);
				removeUserCookie(loginCookie, args);
			}
		}
		else {
			// User doesn't have the cookie!! Send him off.
			setIsLoggedIn(args, false, null);
		}
	}

	private Cookie findLoginCookie(Cookie[] userCookies, Cookie loginCookie) {
		if (userCookies == null)
			return null;
		for (Cookie currentCookie : userCookies) {
			if (currentCookie.getName().equals(HttpConsts.LOGIN_COOKIE_NAME)) {
				loginCookie = currentCookie;
				
			}
		}
		return loginCookie;
	}

	private void setIsLoggedIn(FilterArguments args, boolean isLoggedIn, String loggedInUser) {
		args.stash.put(FrontEndToBackEndConsts.IS_LOGGED_IN_PARAM, isLoggedIn);
		args.request.setAttribute(FrontEndToBackEndConsts.IS_LOGGED_IN_PARAM, isLoggedIn);
		args.request.setAttribute(FrontEndToBackEndConsts.LOGGED_IN_AS_NAME_PARAMETER, loggedInUser);
		args.state = isLoggedIn ? FilterState.PROCEED : FilterState.FAIL_FILTERS_RUN_REQUEST;
		
	}

	private void removeUserCookie(Cookie cookieToRemove, FilterArguments args) {
		// User does have a cookie, but he is not not logged in (or atleast isn't in our logged in users map), 
		// reset his cookie and send him off.
		cookieToRemove.setMaxAge(0);
		args.response.addCookie(cookieToRemove);
	}

	@Override
	public void doOutbound(FilterArguments args) throws IOException,
			ServletException {

	}

	@Override
	public String[] getPath() {
		return filterPaths;
	}
	
	public static String addLoggedInUserToMapAndGetSessionID(String email) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		java.util.Date date= new java.util.Date();
		String digestString = email +  new Timestamp(date.getTime());
		String thedigest = StringUtilities.getMD5StringfromString(digestString);
		
		loggedInUsers.put(email, thedigest);
		return thedigest;
		
	}



}
