package com.loginregister;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class RegisterBean {

	private String firstname;
	private String lastname;
	private String address;
	private String phonenumber;
	private String email;
	private String username;
	private String password;
	private String role;
	private Float balance = 0F;
	public Float getBalance() {
		return balance;
	}

	public void setBalance(Float balance) {
		this.balance = balance;
	}

	public Float getFee() {
		return fee;
	}

	public void setFee(Float fee) {
		this.fee = fee;
	}

	private Float fee = 0F;
	

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String register() {

		return "registersuccess";

	}
	
	

	public String registerUserDetails() {
		boolean valid = RegisterationDAO.registerUser(firstname, lastname,
				address, phonenumber, email, username, password, role, balance, fee);
		if (valid == true) {
			return "registersuccess";
		} else {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_WARN,
							"Incorrect Username and Passowrd",
							"Please enter correct username and Password"));
			return "failure";
		}
	}

	public String updateUserDetails() {
		boolean valid = RegisterationDAO.updateUser(firstname, lastname,
				address, phonenumber, email, username, password, role);
		if (valid == true) {
			return "update-profile-success";
		} else {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_WARN,
							"Incorrect Username and Passowrd",
							"Please enter correct username and Password"));
			return "failure";
		}
	}

	public void navigateToDashBoard() throws IOException {
		ExternalContext externalContext = FacesContext.getCurrentInstance()
				.getExternalContext();
		String role = FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get("role").toString();
		if (role.equals("user")) {
			externalContext.redirect("user-success.xhtml");
		} else {
			externalContext.redirect("manager-success.xhtml");
		}
	}

}
