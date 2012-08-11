package com.asafandben.bl.core_entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.IndexColumn;

import com.asafandben.dal.cache.ICacheable;



@Entity
public class User implements Serializable, ICacheable<Long> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1098661620899282204L;
	
	public enum Permission {
		USER, ADMIN;
	}
	
	// Members
	@Id
	@GeneratedValue
	@IndexColumn(name = "userid")
	private Long userID;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<Task> getTasks() {
		return tasks;
	}
	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
	public List<User> getUsersIManage() {
		return usersIManage;
	}
	public void setUsersIManage(List<User> usersIManage) {
		this.usersIManage = usersIManage;
	}
	public Long getID() {
		return userID;
	}
	public void setID(Long userID) {
		this.userID = userID;
	}
	public Permission getPermission() {
		return permission;
	}
	public void setPermission(Permission permission) {
		this.permission = permission;
	}
}
