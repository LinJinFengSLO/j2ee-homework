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
import com.asafandben.server.backend.core_entities_managers.UsersManager;
import com.asafandben.utilities.FrontEndToBackEndConsts;

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
		
	}



	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
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
	
	private Marshaller getTasksMarsheller() throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(User.class);
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
