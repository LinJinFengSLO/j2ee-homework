package com.asafandben.server.frontend.services;

import java.io.IOException;
import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.asafandben.bl.core_entities.Task;
import com.asafandben.bl.core_entities.User;
import com.asafandben.bl.core_entities.User.Permission;

/**
 * Servlet implementation class UserServices
 */
@WebServlet("/user")
public class UserServices extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	
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
		// TODO Auto-generated method stub
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().println("you");
		
		
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("TaskManagement");
        EntityManager em = emf.createEntityManager();
        
        em.getTransaction().begin();
		User ben = new User("benbenedek@gmail.com");
		User asaf = new User("asaf.ratzon@gmail.com");
		
		ArrayList<Task> bensTasks = new ArrayList<Task>();
		Task bensTask = new Task();
		//bensTask.setTaskID(3L);
		bensTasks.add(bensTask);
		
//		ben.setUserID(1L);
		ben.setFirstName("Ben");
		ben.setLastName("Benedek");
		ben.setNickname("Chicky");
		ben.setPassword("Blah");
		ben.setTasks(bensTasks);
		ben.setPermission(Permission.ADMIN);
		
		
//		asaf.setUserID(2L);
		asaf.setFirstName("Asaf");
		asaf.setLastName("Ratzon");
		asaf.setNickname("Chucky");
		asaf.setPassword("Blah2");
		
		ArrayList<User> usersBenManages = new ArrayList<User>();
		usersBenManages.add(asaf);
		ben.setUsersIManage(usersBenManages);
		
		

		em.persist(ben);
		em.getTransaction().commit();
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("TaskManagement");
		EntityManager em = emf.createEntityManager();
		User ben = em.find(User.class, "benbenede");
		
		em.close();
		
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

}
