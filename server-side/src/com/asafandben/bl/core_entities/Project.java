package com.asafandben.bl.core_entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hibernate.annotations.IndexColumn;
import com.asafandben.dal.cache.ICacheable;
import com.asafandben.utilities.XmlNamingConventions;

@Entity
@XmlRootElement(name = XmlNamingConventions.PROJECT_ID_ELEMENT) // TODO: use naming convention for the whole class!!!
public class Project implements ICacheable<Long> {
	
	// Members
	@Id
	@GeneratedValue
	@IndexColumn(name = "projectid")
	private Long projectID;
	@IndexColumn(name = "projectname")
	private String projectName;
	@IndexColumn(name = "description")
	private String projectDescription;
	@ManyToMany(cascade={CascadeType.ALL})
	private List<Task> tasks = new ArrayList<Task>();
	@ManyToMany(cascade={CascadeType.ALL})
	private List<User> users = new ArrayList<User>();
	
	// Setters/Getters
	@XmlElement(name = XmlNamingConventions.PROJECT_ID_ELEMENT)
	@XmlID
	@XmlJavaTypeAdapter(LongAdapter.class)
	public Long getID() {
		return projectID;
	}
	public void setID(Long projectID) {
		this.projectID = projectID;
	}
	@XmlElement(name = XmlNamingConventions.PROJECT_NAME_ELEMENT)
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	@XmlElement(name = XmlNamingConventions.PROJECT_DESCRIPTION_ELEMENT)
	public String getProjectDescription() {
		return projectDescription;
	}
	public void setProjectDescription(String projectDescription) {
		this.projectDescription = projectDescription;
	}
	@XmlElement(name = XmlNamingConventions.PROJECT_TASKSASSIGNED_ELEMENT)
	public List<Task> getTasks() {
		return tasks;
	}
	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
	@XmlElement(name = XmlNamingConventions.PROJECT_USERASSIGNED_ELEMENT)
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}
	
    public boolean equals(Object obj) {
        if (obj instanceof Project)
            return getID().equals(((Project)obj).getID()); 
        else
            return false;
    }
}
