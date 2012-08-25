package com.asafandben.server.frontend.services;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.asafandben.bl.core_entities.User;
import com.asafandben.server.backend.core_entities_managers.UsersManager;
import com.asafandben.utilities.FrontEndToBackEndConsts;
import com.asafandben.utilities.HttpConsts;

/**
 * Servlet implementation class UserServices
 */
@WebServlet(value="/user", loadOnStartup=1)
public class UserServices extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	public static UsersManager usersManager;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserServices() {
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
	}

	/**
	 * @see Servlet#getServletConfig()
	 */
	public ServletConfig getServletConfig() {
		// TODO Auto-generated method stub
		return null;
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
		/* User wants to receive information about other users (or himself), 
		 * lets identify which users and send the request to manager.
		*/
		
		if (request.getAttribute(FrontEndToBackEndConsts.IS_LOGGED_IN_PARAM) == "false") {
			((HttpServletResponse)response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Please login to get user's information");
			return;
		}
		
		
		String requestUrl = request.getRequestURI();
		String urlSuffix = requestUrl.replaceFirst(HttpConsts.USER_PATH, "");
		String requestUsers[]  = urlSuffix.split(HttpConsts.GET_URL_SEPEARTOR);
		
		List<User> returnedUsers = usersManager.getUsers((String)request.getAttribute(FrontEndToBackEndConsts.LOGGED_IN_AS_NAME_PARAMETER), requestUsers);
		
		String finalResults = null;
		try {
			Marshaller myMarshaller = getUserMarsheller();
			finalResults = usersToXml(returnedUsers, myMarshaller);
		} catch (JAXBException e) {
			((HttpServletResponse)response).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage() + " Error returning users information.");
			e.printStackTrace();
		}
		
		response.getWriter().write(finalResults);
		
	}

	private String usersToXml(List<User> returnedUsers, Marshaller myMarshaller)
			throws JAXBException {
		if (returnedUsers == null)
			return "";
		StringBuffer results = new StringBuffer();
		StringWriter tempResponse = new StringWriter();
		for (User currentUser : returnedUsers) {
			if (currentUser!=null) {
				myMarshaller.marshal(currentUser, tempResponse);
				results.append(tempResponse.toString());
			}
		}
		return results.toString();
	}

	private Marshaller getUserMarsheller() throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(User.class);
		Marshaller marsheller = context.createMarshaller();
		return marsheller;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		usersManager.createDummyInformation();		//TODO: remove this line.
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
