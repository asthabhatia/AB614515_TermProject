package com.loginregister;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class LoginBean {

	private String username;
	private String password;
	private int selectManager;
	private List <SelectItem> showAvailableManagers;

	public List<SelectItem> getShowAvailableManagers() {
		return showAvailableManagers;
	}

	public void setShowAvailableManagers(List<SelectItem> showAvailableManagers) {
		this.showAvailableManagers = showAvailableManagers;
	}

	public int getSelectManager() {
		return selectManager;
	}

	public void setSelectManager(int selectManager) {
		this.selectManager = selectManager;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/*
	 * public String login() { if (username.equalsIgnoreCase("admin") &&
	 * password.equalsIgnoreCase("admin")) { return "success"; } else return
	 * "failure"; }
	 */

	
	
	
	
	
	// validate login
	public void validateUsernamePassword() throws IOException {

		boolean valid = LoginDAO.validate(username, password);
		ExternalContext externalContext = FacesContext.getCurrentInstance()
				.getExternalContext();
		
		if(valid == true){
		String role = FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get("role").toString();
		
		String aprove_status = FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get("approve_status").toString();
		
		if (valid == true && role.equals("admin")) {
			externalContext.redirect("admin-dashboard.xhtml");
		}else if (valid == true && role.equals("user")) {
			externalContext.redirect("user-success.xhtml");
		} else if (valid == true && role.equals("manager")) {
			  if(aprove_status.equals("YES"))
			externalContext.redirect("manager-success.xhtml");
			  else
				  externalContext.redirect("login-failure.xhtml");
		}}else {
			externalContext.redirect("login-failure.xhtml");
		}
		
	}
	

	

	public void logout() throws IOException {
		ExternalContext externalContext = FacesContext.getCurrentInstance()
				.getExternalContext();
		externalContext.redirect("login.xhtml");
	}
	
	public String approve() {
	      int selectedManager  = this.selectManager;	
		   boolean valid = 	LoginDAO.approveManager(selectedManager);				
			if(valid == true)
				 FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO, "Manager approved",""));
			else
				 FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO, "Please try again. Error occured",""));			
			return "adminsuccess";					
		}
	
	
	/*public String viewAccountHistory() {
	     	
		   boolean valid = 	LoginDAO.viewAccountHistory(username);				
			if(valid == true)
				 FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO, "Manager approved",""));
			else
				 FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO, "Please try again. Error occured",""));			
			return "adminsuccess";					
		}
	*/


	@PostConstruct
	public void init() {
	showAvailableManagers = LoginDAO.showAllManagers();

		
	}


}
