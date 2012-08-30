package com.asafandben.server.frontend.services;

import java.io.IOException;
import java.io.StringWriter;
import java.security.NoSuchAlgorithmException;
import java.util.List;

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

import com.asafandben.bl.core_entities.Task;
import com.asafandben.bl.core_entities.User;
import com.asafandben.server.backend.core_entities_managers.UsersManager;
import com.asafandben.utilities.FrontEndToBackEndConsts;
import com.asafandben.utilities.HttpConsts;
import com.asafandben.utilities.StringUtilities;
import com.asafandben.utilities.XmlNamingConventions;

/**
 * Servlet implementation class UserServices
 */
@WebServlet(value = "/user", loadOnStartup = 1)
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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		/*
		 * User wants to receive information about other users (or himself),
		 * lets identify which users and send the request to manager.
		 */
		if (request.getAttribute(FrontEndToBackEndConsts.IS_LOGGED_IN_PARAM) == "false") {
			((HttpServletResponse) response).sendError(
					HttpServletResponse.SC_UNAUTHORIZED, "Please login to get user's information");
			return;
		}

		String requestUsersParam = request
				.getParameter(HttpConsts.USER_PARAMETER_NAME);
		String requestUsersIds[] = requestUsersParam
				.split(HttpConsts.GET_URL_SEPEARTOR);

		List<User> returnedUsers = usersManager.getUsers(
						(String) request.getAttribute(FrontEndToBackEndConsts.LOGGED_IN_AS_NAME_PARAMETER),
						requestUsersIds);

		String finalResults = null;
		try {
			Marshaller myMarshaller = getUserMarsheller();
			finalResults = usersToXml(returnedUsers, myMarshaller);
		} catch (JAXBException e) {
			((HttpServletResponse) response).sendError(
					HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					e.getLocalizedMessage()
							+ " Error returning users information.");
			e.printStackTrace();
		}

		response.getWriter().write(finalResults);

	}

	private String usersToXml(List<User> listToReturnAsXML,
			Marshaller myMarshaller) throws JAXBException {

		StringBuffer results = new StringBuffer();
		StringWriter tempResponse = new StringWriter();
		results.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		results.append("<" + XmlNamingConventions.USERS_TAG + ">");

		if (listToReturnAsXML == null) {
			results.append("</" + XmlNamingConventions.USERS_TAG + ">");
			return results.toString();
		}

		for (User currentUser : listToReturnAsXML) {
			if (currentUser != null) {
				myMarshaller.marshal(currentUser, tempResponse);
			}
		}

		results.append(tempResponse.toString());
		results.append("</" + XmlNamingConventions.USERS_TAG + ">");
		return results.toString();
	}

	private Marshaller getUserMarsheller() throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(User.class);
		Marshaller marsheller = context.createMarshaller();
		marsheller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marsheller.setProperty(Marshaller.JAXB_FRAGMENT, true);
		return marsheller;
	}

	/**
	 * 	 * The doPost method will control modifying/creating new user: This scenario
	 * should support the following: 
	 * 
	 * 		1. Edit user (his/her profile) (parameters accepted are: email, first and last name, and nickname). 
	 * 		ACTION NAME: "EditProfile" (HttpConst.EDIT_USER_PROFILE_ACTION_NAME)
	 *  
	 * 		2. Edit password, in which case it will accept old password, and new password (twice).
	 * 		ACTION NAME: "EditPassword". (HttpConst.EDIT_USER_PASSWORD_ACTION_NAME)
	 * 
	 * 		3. Assign a task for user, in which case it will accept two valid emails
	 * 		   (assigner userID and assigne userID) and a valid Long (TaskID). 
	 * 		ACTION NAME: "AssignTask". (HttpConst.ASSIGN_TASK_TO_USER_ACTION_NAME)
	 * 
	 * For all of the above tasks, the user must be logged in.
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		if (request.getAttribute(FrontEndToBackEndConsts.IS_LOGGED_IN_PARAM) == "false") {
			((HttpServletResponse) response).sendError(
					HttpServletResponse.SC_UNAUTHORIZED,
					"Please login to get user's information");
			return;
		}

		boolean actionNotFound = true;
		String actionName = request.getParameter(HttpConsts.ACTION_PARAMETER_NAME);

		if (actionName != null) {
			if (actionName.equals(HttpConsts.EDIT_USER_PROFILE_ACTION_NAME)) {
				actionNotFound = false;
				editUserProfile(request, response);
			}

			if (actionName.equals(HttpConsts.EDIT_USER_PASSWORD_ACTION_NAME)) {
				actionNotFound = false;
				try {
					editUserPassword(request, response);
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				}
			}

			if (actionName.equals(HttpConsts.ASSIGN_TASK_TO_USER_ACTION_NAME)) {
				actionNotFound = false;
				assignTaskToUser(request, response);
			}
		}

		if (actionNotFound) {
			((HttpServletResponse) response)
					.sendError(HttpServletResponse.SC_BAD_REQUEST,
							"Action not found. A valid action is required for user doPut.");
			return;
		}
		
	}

	/**
	 * 
	 * @see HttpServlet#doPut
	 **/
	protected void doPut(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		usersManager.createDummyInformation(); // TODO: remove this line.

	}

	private void assignTaskToUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String email = (String) request.getAttribute(FrontEndToBackEndConsts.LOGGED_IN_AS_NAME_PARAMETER);
		String employeeEmail = request.getParameter(HttpConsts.USERNAME_PARAMETER_EMPLOYEE_EMAIL);
		Long taskID = Long.parseLong(request.getParameter(HttpConsts.TASK_PARAMETER_NAME_ID));
		
		boolean haveAllFields = StringUtilities.allStringsAreNotEmpty(email, employeeEmail) && (taskID!=null);
		
		if (haveAllFields) {
			usersManager.assignTaskToUser(email, employeeEmail, taskID);
		}
		else {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing information.");
		}
		
	}

	private void editUserPassword(HttpServletRequest request,
			HttpServletResponse response) throws NoSuchAlgorithmException,
			IOException {
		String email = (String) request.getAttribute(FrontEndToBackEndConsts.LOGGED_IN_AS_NAME_PARAMETER);
		String password1 = request.getParameter(HttpConsts.USERNAME_PARAMETER_FIRSTPASSWORD);
		String password2 = request.getParameter(HttpConsts.USERNAME_PARAMETER_SECONDPASSWORD);
		String oldPassword = request.getParameter(HttpConsts.USERNAME_PARAMETER_OLDPASSWORD);

		boolean haveAllFields = StringUtilities.allStringsAreNotEmpty(email,
				password1, password2, oldPassword);

		if (haveAllFields) {
			usersManager.changeUserPassword(email, password1, password2,
					oldPassword);
		} else {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing information.");
		}

	}

	private void editUserProfile(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		String email = (String) request.getAttribute(FrontEndToBackEndConsts.LOGGED_IN_AS_NAME_PARAMETER);
		String firstName = request.getParameter(HttpConsts.USERNAME_PARAMETER_FIRSTNAME);
		String lastName = request.getParameter(HttpConsts.USERNAME_PARAMETER_LASTNAME);
		String nickName = request.getParameter(HttpConsts.USERNAME_PARAMETER_NICKNAME);

		boolean haveAllFields = StringUtilities.allStringsAreNotEmpty(email, firstName, lastName, nickName);

		if (haveAllFields) {
			usersManager.editUserProfile(email, firstName, lastName, nickName);
		} else {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing information.");
		}

	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
