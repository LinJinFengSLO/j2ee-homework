package com.asafandben.server.frontend.services;

import java.io.IOException;
import java.io.StringWriter;
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
import com.asafandben.server.backend.core_entities_managers.TasksManager;
import com.asafandben.utilities.FrontEndToBackEndConsts;
import com.asafandben.utilities.HttpConsts;

/**
 * Servlet implementation class TaskServices
 */
@WebServlet(value="/task", loadOnStartup=1)
public class TaskServices extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	public static TasksManager tasksManager;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TaskServices() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		tasksManager = TasksManager.getInstance();
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getAttribute(FrontEndToBackEndConsts.IS_LOGGED_IN_PARAM) == "false") {
			((HttpServletResponse)response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Please login to get user's information");
			return;
		}
		
		/* User wants to receive information about other tasks/s, 
		 * lets identify which users and send the request to manager.
		*/
			
		//String requestUrl = request.getRequestURI();
		//String urlSuffix = requestUrl.replaceFirst(HttpConsts.TASK_PATH, "");
		//String requestTasksStrings[]  = urlSuffix.split(HttpConsts.GET_URL_SEPEARTOR);
		
		String tasksIds = request.getParameter("tasksIds");
		String requestTasksStrings[]  = tasksIds.split(HttpConsts.GET_URL_SEPEARTOR);
		
		// Convert PK from String to Long
		Long requestTasks[] = new Long[requestTasksStrings.length];
		for(int i = 0; i < requestTasksStrings.length; ++i) {
			requestTasks[i] = Long.parseLong(requestTasksStrings[i]);
		}
		
		List<Task> returnedTasks = tasksManager.getTasks((String)request.getAttribute(FrontEndToBackEndConsts.LOGGED_IN_AS_NAME_PARAMETER), requestTasks);
		
		String finalResults = null;
		try {
			Marshaller myMarshaller = getTaskMarsheller();
			finalResults = tasksToXml(returnedTasks, myMarshaller);
		} catch (JAXBException e) {
			((HttpServletResponse)response).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage() + " Error returning tasks information.");
			e.printStackTrace();
		}
		
		response.getWriter().write(finalResults);
	}



	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		tasksManager.createDummyInformation();		//TODO: remove this line.
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
	
	private Marshaller getTaskMarsheller() throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(Task.class);
		Marshaller marsheller = context.createMarshaller();
		return marsheller;
	}

	
	private String tasksToXml(List<Task> listToReturnAsXML, Marshaller myMarshaller) throws JAXBException {
		if (listToReturnAsXML == null)
			return "";
		StringBuffer results = new StringBuffer();
		StringWriter tempResponse = new StringWriter();
		for (Task currentTask : listToReturnAsXML) {
			myMarshaller.marshal(currentTask, tempResponse);
			results.append(tempResponse.toString());
		}
		return results.toString();
	}
	
	
}
