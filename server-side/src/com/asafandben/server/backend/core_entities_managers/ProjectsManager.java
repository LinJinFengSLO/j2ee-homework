package com.asafandben.server.backend.core_entities_managers;

import java.util.ArrayList;
import java.util.List;

import com.asafandben.bl.core_entities.Project;
import com.asafandben.bl.core_entities.User;
import com.asafandben.dal.cache.GenericCache;
import com.asafandben.utilities.FrontEndToBackEndConsts;

public class ProjectsManager {

	private ProjectsManager() {}
	private static ProjectsManager _instance;

	private static GenericCache<Project, Long> projectsCache = new GenericCache<Project, Long>(Project.class);
	
	public static ProjectsManager getInstance() {
		if (_instance == null)
			_instance = new ProjectsManager();
		return _instance;
	}

	public List<Project> getProjects(String currentUser, Long[] requestProjectsIds) {
		
		List<Project> returnProjects= new ArrayList<Project>();

		User requestingUser = UsersManager.getInstance().getUserById(currentUser);
		
		if (requestingUser != null && requestProjectsIds.length >= 1) {
			boolean isRequestingUserAdmin = UsersManager.getInstance().isUserAdmin(requestingUser);
			
			// If all projects are requested
			if (requestProjectsIds[0].equals(FrontEndToBackEndConsts.ALL_PROJECTS_REQUESTED)) {
				returnProjects = isRequestingUserAdmin ? projectsCache.getAll() : null;			
			} else {
			// If specific projects are requested
				for (int i = 0; i < requestProjectsIds.length; i++) {
					Project requestedProject = projectsCache.find(requestProjectsIds[i]);
					if (requestedProject != null) {
						// Check if user is allowed to get this information:
						boolean isUserAllowedtoGetInformation = (isRequestingUserAdmin || requestingUser.getProjects().contains(requestedProject));
	
						if (isUserAllowedtoGetInformation)
							returnProjects.add(requestedProject);
					}
				}
			}
		} else {
			returnProjects = null;
		}

		return returnProjects;
	}
	
	public void createDummyInformation() {
		// TODO: IMPLEMENT FOR TESTING
	}
}
