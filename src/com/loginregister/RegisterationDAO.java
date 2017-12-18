package com.loginregister;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class RegisterationDAO {

	
	public static boolean registerUser(String firstname, String lastname,
			String address, String phonenumber, String email, String username,
			String password, String role, Float balance, Float fee) {

		Connection con = null;
		PreparedStatement ps = null;
		
		  float balnc = 0F;
		  
		  String approve_status="NO";

		try {
			con = DataConnect.getConnection();
			RegisterBean rb = new RegisterBean();
			

	
			
			if(role.equals("user")){
			balnc = 100000.00F;
			}
			
			if(role.equals("manager")){
				approve_status = "NO";
				}
				
			String sql = "INSERT INTO userDetails(firstname, lastname, address, phonenumber, email, username, password, role, balance, fee,approved) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
			ps = con.prepareStatement(sql);
			ps.setString(1, firstname);
			ps.setString(2, lastname);
			ps.setString(3, address);
			ps.setString(4, phonenumber);
			ps.setString(5, email);
			ps.setString(6, username);
			ps.setString(7, password);
			ps.setString(8, role);			
			ps.setFloat(9, balnc);
			ps.setFloat(10, fee);
			ps.setString(11, approve_status);
			ps.executeUpdate();
			System.out.println("Data Added Successfully");
			return true;

		} catch (SQLException ex) {
			System.out.println("Registeration error -->" + ex.getMessage());
			return false;
		} finally {
			DataConnect.close(con);
		}
	}
	
	
	public static boolean isUserExist(String username) {
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = DataConnect.getConnection();
			ps = con.prepareStatement("select firstname from userDetails where username = ?");
			ps.setString(1, username);
			

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				return true;
			}

		} catch (SQLException ex) {
			return false;
		} finally {

		}
		return false;
	}
	
	
	public static boolean updateUser(String firstname, String lastname,
			String address, String phonenumber, String email, String username,
			String password, String role) {
		
		RegisterBean rb = new RegisterBean();
		String usrname =  FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_name").toString();
		//String usrname = rb.getUsername();
	
		

		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = DataConnect.getConnection();
			String sql = "UPDATE userDetails SET username = ?, address = ?, phonenumber = ?, email = ?, password = ?, firstname = ?, lastname = ? WHERE username = ?";
			//String sql = "INSERT INTO userDetails(firstname, lastname, address, phonenumber, email, username, password, role) VALUES(?,?,?,?,?,?,?,?)";
			ps = con.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, address);
			ps.setString(3, phonenumber);
			ps.setString(4, email);
			ps.setString(5, password);
			ps.setString(6, firstname);
			ps.setString(7, lastname);
			ps.setString(8, usrname);					
			ps.executeUpdate();
			System.out.println("Data updated Successfully");
			return true;

		} catch (SQLException ex) {
			System.out.println("Registeration error -->" + ex.getMessage());
			return false;
		} finally {
			DataConnect.close(con);
		}
	}

}
