package com.asafandben.bl.core_entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.IndexColumn;

import com.asafandben.dal.cache.ICacheable;
import com.asafandben.utilities.XmlNamingConventions;

@Entity
@XmlRootElement(name = XmlNamingConventions.USER_ROOT_ELEMENT) // TODO: use naming convention for the whole class!!!
public class User implements Serializable, ICacheable<String> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1098661620899282204L;
	
	
	public enum Permission {
		USER, ADMIN;
	}
	
	// Members

	@Id
	@IndexColumn(name = "useremail")
	private String email;
	@IndexColumn(name = "nickname")
	private String nickname;
	@IndexColumn(name = "firstname")
	private String firstName;
	@IndexColumn(name = "lastname")
	private String lastName;
	@IndexColumn(name = "password")
	private String password;
	@IndexColumn(name = "permission")
	private Permission permission;
	@ManyToMany(cascade={CascadeType.ALL})
	private List<Task> tasks = new ArrayList<Task>();
	@ManyToMany(cascade={CascadeType.ALL})
	private List<User> usersIManage = new ArrayList<User>();
	
	// Constructors
	public User() {
	}	
	public User(String email) {
		this.email = email;
	}
	
	// Setters/Getters
	@XmlElement(name = "Email")
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@XmlElement(name = "Nickname")
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	@XmlElement(name = "FirstName")
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	@XmlElement(name = "LastName")
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	@XmlTransient
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@XmlElement(name = "Tasks")
	@XmlIDREF
	public List<Task> getTasks() {
		return tasks;
	}
	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
	@XmlElement(name = "UsersIManage")
	@XmlIDREF
	public List<User> getUsersIManage() {
		return usersIManage;
	}
	public void setUsersIManage(List<User> usersIManage) {
		this.usersIManage = usersIManage;
	}
	@XmlElement(name = "Id")
	public String getID() {
		return email;
	}
	public void setID(String email) {
		this.email = email;
	}
	@XmlElement(name = "Permission")
	public Permission getPermission() {
		return permission;
	}
	public void setPermission(Permission permission) {
		this.permission = permission;
	}
}
