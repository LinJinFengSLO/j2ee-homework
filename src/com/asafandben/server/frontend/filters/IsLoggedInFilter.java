package com.asafandben.server.frontend.filters;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import com.asafandben.server.frontend.filters.FiltersManager.FilterArguments;
import com.asafandben.utilities.HttpConsts;

public class IsLoggedInFilter implements Filter {

	private String[] path = { "/" };
	
	@Override
	public void doInbound(FilterArguments args) throws IOException,
			ServletException {
		
		// Assume user is not logged in.
		args.stash.put("isLoggedIn", (boolean) false);
		
		// Get all cookies from the request
		Cookie[] allCookies = args.request.getCookies();
		
		Cookie loginCookie = null;
		
		// Iterate over all cookies untill we find the correct Login Cookie.
		for (Cookie currentCookie : allCookies) {
			if (currentCookie.getName().equals(HttpConsts.LOGIN_COOKIE_NAME)) {
				loginCookie = currentCookie; 
				break;
			}
		}
		
		if (loginCookie != null) {
			if (isValidLoginCookie(loginCookie)) {
				args.stash.put("isLoggedIn", (boolean) true);
			}
			else {
				removeCookieFromRequest(loginCookie, args.response);
			}
		}

	}

	private boolean isValidLoginCookie(Cookie loginCookie) {
		boolean isValid = false;
		
		String cookieValue = loginCookie.getValue();
		//getValueFromContext
		
		
		
		return isValid;
	}

	private void removeCookieFromRequest(Cookie deletedLoginCookie, HttpServletResponse response) {
		deletedLoginCookie.setMaxAge(0);
		response.addCookie(deletedLoginCookie);
	}

	@Override
	public void doOutbound(FilterArguments args) throws IOException,
			ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public String[] getPath() {
		return path;
	}

}
