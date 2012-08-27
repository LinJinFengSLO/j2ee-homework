package com.asafandben.server.frontend.services;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.asafandben.bl.core_entities.User;
import com.asafandben.server.backend.core_entities_managers.UsersManager;
import com.asafandben.server.frontend.filters.IsLoggedInFilter;
import com.asafandben.utilities.FrontEndToBackEndConsts;
import com.asafandben.utilities.HttpConsts;
import com.asafandben.utilities.XmlNamingConventions;

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
		User loggedInUser = null;
		PrintWriter out = response.getWriter();
		String loggedInAsUser = (String)request.getAttribute(FrontEndToBackEndConsts.LOGGED_IN_AS_NAME_PARAMETER);
		response.setContentType(HttpConsts.XML_CONTENT_TYPE);
		
		if (loggedInAsUser!=null)
			loggedInUser = usersManager.getUsers(loggedInAsUser, new String[0]).get(0);
		
		out.write(createWhoAmiResponse(loggedInUser));
	}

	private String createWhoAmiResponse(User loggedInUser) {
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		sb.append("<" + XmlNamingConventions.WHO_AM_I_TAG + ">");
		sb.append("<" + XmlNamingConventions.LOGGED_IN_AS_TAG + ">");
		sb.append(loggedInUser != null ? loggedInUser.getID() : "NOT_LOGGED_IN");
		sb.append("</" + XmlNamingConventions.LOGGED_IN_AS_TAG + ">\n");
		sb.append("<" + XmlNamingConventions.ROLE_AS_TAG + ">");
		sb.append(loggedInUser != null ? loggedInUser.getPermission() : "NOT_LOGGED_IN");
		sb.append("</" + XmlNamingConventions.ROLE_AS_TAG + ">\n");
		sb.append("<" + XmlNamingConventions.USER_NICKNAME_ELEMENT + ">");
		sb.append(loggedInUser != null ? loggedInUser.getNickname() : "NOT_LOGGED_IN");
		sb.append("</" + XmlNamingConventions.USER_NICKNAME_ELEMENT + ">");
		sb.append("</" + XmlNamingConventions.WHO_AM_I_TAG + ">");
		return sb.toString();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if (request.getAttribute(FrontEndToBackEndConsts.IS_LOGGED_IN_PARAM) == "true") {
			((HttpServletResponse)response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Cannot register while logged in.");
			return;
		}
		String email = request.getParameter(HttpConsts.USERNAME_PARAMETER_EMAIL);
		String password1 = request.getParameter(HttpConsts.USERNAME_PARAMETER_FIRSTPASSWORD);
		String password2 = request.getParameter(HttpConsts.USERNAME_PARAMETER_SECONDPASSWORD);
		String firstName = request.getParameter(HttpConsts.USERNAME_PARAMETER_FIRSTNAME);
		String lastName = request.getParameter(HttpConsts.USERNAME_PARAMETER_LASTNAME);
		String nickName = request.getParameter(HttpConsts.USERNAME_PARAMETER_NICKNAME);
		
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
				Cookie loginCookie = new Cookie(HttpConsts.LOGIN_COOKIE_NAME, email + HttpConsts.COOKIE_SEPERATOR + sessionID);
				loginCookie.setMaxAge(HttpConsts.LOGIN_COOKIE_AGE);
				response.addCookie(loginCookie);
				((HttpServletResponse)response).sendRedirect(HttpConsts.SUCCESSFUL_LOGIN_REDIRECT_URL);
			}
			
		}
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String loggedInAsUser = (String)request.getAttribute(FrontEndToBackEndConsts.LOGGED_IN_AS_NAME_PARAMETER);
		Cookie loginCookie = null;
		
		if ((request.getAttribute(FrontEndToBackEndConsts.IS_LOGGED_IN_PARAM) == "false")||(loggedInAsUser==null)) {
			((HttpServletResponse)response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Cannot logout while not logged in.");
			return;
		}
		
		IsLoggedInFilter.removeLoggedInUser(loggedInAsUser);
		
		loginCookie = IsLoggedInFilter.findLoginCookie(request.getCookies(), loginCookie);
		
		if (loginCookie != null) {
			loginCookie.setMaxAge(0);
		
			response.addCookie(loginCookie);
		}
	}

}
