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
import com.asafandben.bl.core_entities.User;
import com.asafandben.server.backend.core_entities_managers.TasksManager;
import com.asafandben.utilities.FrontEndToBackEndConsts;
import com.asafandben.utilities.HttpConsts;
import com.asafandben.utilities.XmlNamingConventions;

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
		 * lets identify which TASKS and send the request to manager. */
		
		String requestTasksParam = request.getParameter(HttpConsts.TASK_PARAMETER_NAME);
		String requestTasksStrings[]  = requestTasksParam.split(HttpConsts.GET_URL_SEPEARTOR);
		Long requestTasksIds[] = new Long[requestTasksStrings.length];
		
		// Check if all tasks are requested
		if (requestTasksStrings[0].equals(FrontEndToBackEndConsts.ALL_ENTITIES_REQUESTED)) {
			requestTasksIds[0] = FrontEndToBackEndConsts.ALL_TASKS_REQUESTED;
		} else {
			// Convert PK from String to Long
			for(int i = 0; i < requestTasksStrings.length; ++i) {
				try {
					requestTasksIds[i] = Long.parseLong(requestTasksStrings[i]);
				} catch(NumberFormatException e) {
					e.printStackTrace();
					((HttpServletResponse)response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid tasks ids");
					return;
				}
			}
		}
		
		List<Task> returnedTasks = tasksManager.getTasks((String)request.getAttribute(FrontEndToBackEndConsts.LOGGED_IN_AS_NAME_PARAMETER), requestTasksIds);
		
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
		marsheller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marsheller.setProperty(Marshaller.JAXB_FRAGMENT, true);
		return marsheller;
	}

	
	private String tasksToXml(List<Task> listToReturnAsXML, Marshaller myMarshaller) throws JAXBException {
		
		StringBuffer results = new StringBuffer();
		StringWriter tempResponse = new StringWriter();
		results.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		results.append("<" + XmlNamingConventions.TASKS_TAG + ">");
		
		if (listToReturnAsXML == null) {
			results.append("</" + XmlNamingConventions.TASKS_TAG + ">");
			return results.toString();
		}

		for (Task currentTask : listToReturnAsXML) {
			if (currentTask != null) {
				myMarshaller.marshal(currentTask, tempResponse);
			}
		}
		
		results.append(tempResponse.toString());
		results.append("</" + XmlNamingConventions.TASKS_TAG + ">");
		return results.toString();
	}
	
	
}
