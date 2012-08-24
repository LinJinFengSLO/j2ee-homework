package com.asafandben.server.frontend.services;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.el.MethodNotFoundException;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.asafandben.server.backend.core_entities_managers.UsersManager;
import com.asafandben.server.frontend.filters.IsLoggedInFilter;
import com.asafandben.utilities.HttpConsts;

/**
 * Servlet implementation class SecurityServices
 */
@WebServlet(value="/security", loadOnStartup=1)
public class SecurityServices extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	public static UsersManager usersManager;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SecurityServices() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		usersManager = UsersManager.getInstance();
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Servlet#getServletInfo()
	 */
	public String getServletInfo() {
		// TODO Auto-generated method stub
		return null; 
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		throw new MethodNotFoundException("Can't use GET method in the Security Service.");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String email = request.getHeader(HttpConsts.USERNAME_PARAMETER_EMAIL);
		String password1 = request.getHeader(HttpConsts.USERNAME_PARAMETER_FIRSTPASSWORD);
		String password2 = request.getHeader(HttpConsts.USERNAME_PARAMETER_SECONDPASSWORD);
		String firstName = request.getHeader(HttpConsts.USERNAME_PARAMETER_FIRSTNAME);
		String lastName = request.getHeader(HttpConsts.USERNAME_PARAMETER_LASTNAME);
		String nickName = request.getHeader(HttpConsts.USERNAME_PARAMETER_NICKNAME);
		
		boolean allFieldsAreNotNull = ((email!=null)&&(password1!=null)&&(password2!=null)&&(firstName!=null)&&(lastName!=null)&&(nickName!=null));
		
		if (allFieldsAreNotNull) {
			boolean registeredSuccessfully = usersManager.createNewUser(email, firstName, lastName, nickName, password1, password2);
		}
		else {
			((HttpServletResponse)response).sendError(HttpServletResponse.SC_EXPECTATION_FAILED, "Missing information.");
		}
		
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		String email = request.getHeader(HttpConsts.USERNAME_PARAMETER_EMAIL);
		String password = request.getHeader(HttpConsts.USERNAME_PARAMETER_FIRSTPASSWORD);
		
		if ((email==null)||(password==null)) {
			((HttpServletResponse)response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid username or password.");
		}
		else {
			boolean isLoginCredintialsValid = usersManager.checkCredentials(email, password);
			if (!isLoginCredintialsValid) {
				((HttpServletResponse)response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid username or password.");
			}
			else {
				String sessionID = null;
				try {
					sessionID = IsLoggedInFilter.addLoggedInUserToMapAndGetSessionID(email);
				} catch (NoSuchAlgorithmException e) {
					((HttpServletResponse)response).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage() + " Error adding user to user map.");
					
				}
				Cookie loginCookie = new Cookie(HttpConsts.LOGIN_COOKIE_NAME, sessionID);
				response.addCookie(loginCookie);
				((HttpServletResponse)response).sendRedirect("");
			}
			
		}
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		throw new MethodNotFoundException("Can't use DELETE method in the Security Service.");
	}

}
