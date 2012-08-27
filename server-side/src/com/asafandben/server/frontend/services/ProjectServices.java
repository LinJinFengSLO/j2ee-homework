package com.asafandben.server.frontend.services;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.asafandben.bl.core_entities.Project;
import com.asafandben.bl.core_entities.Task;
import com.asafandben.server.backend.core_entities_managers.ProjectsManager;
import com.asafandben.utilities.FrontEndToBackEndConsts;
import com.asafandben.utilities.HttpConsts;
import com.asafandben.utilities.XmlNamingConventions;

/**
 * Servlet implementation class ProjectServices
 */
@WebServlet(value="/project", loadOnStartup=1)
public class ProjectServices extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	public static ProjectsManager projectsManager;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProjectServices() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
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
		if (request.getAttribute(FrontEndToBackEndConsts.IS_LOGGED_IN_PARAM) == "false") {
			((HttpServletResponse)response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Please login to get user's information");
			return;
		}
		
		/* User wants to receive information about projects/s, 
		 * lets identify which projects and send the request to manager. */
		
		String requestProjectsParam = request.getParameter(HttpConsts.PROJECT_PARAMETER_NAME);
		String requestProjectIdsStrings[]  = requestProjectsParam.split(HttpConsts.GET_URL_SEPEARTOR);
		Long requestProjectsIds[] = new Long[requestProjectIdsStrings.length];
		
		// Check if all tasks are requested
		if (requestProjectIdsStrings[0].equals(FrontEndToBackEndConsts.ALL_ENTITIES_REQUESTED)) {
			requestProjectsIds[0] = FrontEndToBackEndConsts.ALL_PROJECTS_REQUESTED;
		} else {
			// Convert PK from String to Long
			for(int i = 0; i < requestProjectIdsStrings.length; ++i) {
				try {
					requestProjectsIds[i] = Long.parseLong(requestProjectIdsStrings[i]);
				} catch(NumberFormatException e) {
					e.printStackTrace();
					((HttpServletResponse)response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid projects ids");
					return;
				}
			}
		}
		
		List<Project> returnedProjects = projectsManager.getProjects((String)request.getAttribute(FrontEndToBackEndConsts.LOGGED_IN_AS_NAME_PARAMETER), requestProjectsIds);
		
		String finalResults = null;
		try {
			Marshaller myMarshaller = getProjectMarsheller();
			finalResults = projectsToXml(returnedProjects, myMarshaller);
		} catch (JAXBException e) {
			((HttpServletResponse)response).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage() + " Error returning projects information.");
			e.printStackTrace();
		}
		
		response.getWriter().write(finalResults);
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

	private Marshaller getProjectMarsheller() throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(Project.class);
		Marshaller marsheller = context.createMarshaller();
		marsheller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marsheller.setProperty(Marshaller.JAXB_FRAGMENT, true);
		return marsheller;
	}

	
	private String projectsToXml(List<Project> listToReturnAsXML, Marshaller myMarshaller) throws JAXBException {
		
		StringBuffer results = new StringBuffer();
		StringWriter tempResponse = new StringWriter();
		results.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		results.append("<" + XmlNamingConventions.PROJECTS_TAG + ">");
		
		if (listToReturnAsXML == null) {
			results.append("</" + XmlNamingConventions.PROJECTS_TAG + ">");
			return results.toString();
		}

		for (Project currentProject : listToReturnAsXML) {
			if (currentProject != null) {
				myMarshaller.marshal(currentProject, tempResponse);
			}
		}
		
		results.append(tempResponse.toString());
		results.append("</" + XmlNamingConventions.PROJECTS_TAG + ">");
		return results.toString();
	}
	
}
