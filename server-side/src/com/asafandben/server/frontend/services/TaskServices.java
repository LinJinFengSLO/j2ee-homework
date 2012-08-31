package com.asafandben.server.frontend.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.ArrayList;
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
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.FilenameUtils;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import com.asafandben.bl.core_entities.Task;
import com.asafandben.bl.core_entities.Tasks;
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
		
		if (request.getAttribute(FrontEndToBackEndConsts.IS_LOGGED_IN_PARAM) == "false") {
			((HttpServletResponse) response).sendError(
					HttpServletResponse.SC_UNAUTHORIZED,
					"Please login to get user's information");
			return;
		}

		boolean actionNotFound = true;
		String actionName = request.getParameter(HttpConsts.ACTION_PARAMETER_NAME);

		if (ServletFileUpload.isMultipartContent(request)) {
			actionNotFound = false;
			try {
				uploadMultipleFile(request, response);
			} catch (Exception e) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST,
						"Could not parse Xml File." + e.getMessage());
			}
			
		}
		
		if (actionName != null) {
			if (actionName.equals(HttpConsts.ADD_EDIT_TASK_ACTION_NAME)) {
				actionNotFound = false;
				try {
					createSingleTask(request, response);
				} catch (ParseException e) {
					response
					.sendError(HttpServletResponse.SC_BAD_REQUEST,
							"Could not parse dates.");
				}
			}
		}

		if (actionNotFound) {
			((HttpServletResponse) response)
					.sendError(HttpServletResponse.SC_BAD_REQUEST,
							"Action not found. A valid action is required for task doPost.");
			return;
		}
	}

	private void uploadMultipleFile(HttpServletRequest request,
			HttpServletResponse response) throws IOException, FileUploadException, JAXBException {
		String test = request.getParameter(HttpConsts.FILE_UPLOAD_PARAMETER);

        List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
        
        InputStream filecontent=null;
        
        for (FileItem item : items) {
            if (!item.isFormField()) {
                String fieldname = item.getFieldName();
                String filename = FilenameUtils.getName(item.getName());
                filecontent = item.getInputStream();
            }
        }

        if (filecontent!=null) {
        	JAXBContext context = JAXBContext.newInstance(Tasks.class, Task.class);
        	Unmarshaller unmarshaller = context.createUnmarshaller();
        
        	Tasks tasksToSave = (Tasks)unmarshaller.unmarshal(filecontent);
        	for (Task taskToSave : tasksToSave.getTasks()) {
        		tasksManager.saveTaskNoCheck(taskToSave);
        	}
        }

		
		
	}

	private void createSingleTask(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ParseException {
		
		if (request.getAttribute(FrontEndToBackEndConsts.IS_LOGGED_IN_PARAM) == "false") {
			response.sendError(
					HttpServletResponse.SC_UNAUTHORIZED,
					"Please login to get create/edit information");
			return;
		}
		
		String taskID = request.getParameter(HttpConsts.TASK_PARAMETER_NAME_ID);
		String taskName = request.getParameter(HttpConsts.TASK_PARAMETER_NAME_NAME);
		String taskDescription = request.getParameter(HttpConsts.TASK_PARAMETER_NAME_DESCRIPTION);
		String taskCreationDate = request.getParameter(HttpConsts.TASK_PARAMETER_NAME_CREATIONDATE);
		String taskDueDate = request.getParameter(HttpConsts.TASK_PARAMETER_NAME_DUEDATE);
		String taskPriorTasks = request.getParameter(HttpConsts.TASK_PARAMETER_NAME_PRIORTASKS);
		String taskUsersAssigned = request.getParameter(HttpConsts.TASK_PARAMETER_NAME_USERSASSIGNED);
		String taskStatus = request.getParameter(HttpConsts.TASK_PARAMETER_NAME_STATUS);
		
		List<Long> priorTasks = new ArrayList<Long>();
		List<String> assignedUsers = new ArrayList<String>();
		
		if (taskPriorTasks!=null) {
			String[] splittedPriorTasks = taskPriorTasks.split(HttpConsts.GET_URL_SEPEARTOR);
			for (String s : splittedPriorTasks) {
				priorTasks.add(Long.parseLong(s));
			}
		}
		
		if (taskUsersAssigned!=null) {
			String[] splittedAssignedUsers = taskUsersAssigned.split(HttpConsts.GET_URL_SEPEARTOR);
			for (String s : splittedAssignedUsers) {
				assignedUsers.add(s);
			}
		}
		
		tasksManager.createOrEditTask(taskID, taskName, taskDescription, taskCreationDate, taskDueDate, priorTasks, assignedUsers, taskStatus);
		
		
		
		
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		tasksManager.createDummyInformation();		//TODO: remove this line.


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
