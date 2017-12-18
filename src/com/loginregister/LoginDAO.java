package com.loginregister;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.faces.context.FacesContext;

public class LoginDAO {
	

	public static boolean validate(String username, String password) {
		Connection con = null;
		PreparedStatement ps = null;
		RegisterBean registerBean = new RegisterBean();
		try {
			con = DataConnect.getConnection();
			ps = con.prepareStatement("select firstname,role,lastname,address,phonenumber,email,username,userid,balance,approved from userDetails where username = ? and password = ?");
			ps.setString(1, username);
			ps.setString(2, password);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				registerBean.setFirstname(rs.getString("firstname"));
				registerBean.setLastname(rs.getString("lastname"));
				registerBean.setAddress(rs.getString("address"));
				registerBean.setPhonenumber(rs.getString("phonenumber"));
				registerBean.setEmail(rs.getString("email"));
				registerBean.setUsername(rs.getString("username"));
				
				

				FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().put("approve_status", rs.getString("approved"));
				
				FacesContext.getCurrentInstance().getExternalContext()
						.getSessionMap().put("role", rs.getString("role"));
				
				FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().put("balance", rs.getString("balance"));
		
				
				FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().put("userid", rs.getString("userid"));

				FacesContext.getCurrentInstance().getExternalContext()
						.getSessionMap().put("registerBean", registerBean);

				FacesContext.getCurrentInstance().getExternalContext()
						.getSessionMap().put("user_name", username);
				DataConnect.close(con);

				return true;
			}

		} catch (SQLException ex) {
			System.out.println("Login error -->" + ex.getMessage());
			return false;
		} finally {

		}
		return false;
	}
	
public static List<SelectItem> showAllManagers() {
	List<SelectItem> managersList;
		
		
		Connection connection = null;
		PreparedStatement preparedStatement = null;		
		try {
			connection = DataConnect.getConnection();			
			String query = "select * from userdetails where role = ? and approved = ?";			
			managersList = new ArrayList<SelectItem>();
			preparedStatement = (PreparedStatement) connection.prepareStatement(query);			
			preparedStatement.setString(1,"manager");
			preparedStatement.setString(2,"NO");			
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()) {
				managersList.add(new SelectItem( rs.getString("userid") , rs.getString("firstname") + " "+ rs.getString("lastname")));			
			}
			connection.close();
			
			return managersList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		return null;
	}

public static boolean approveManager(int selectedManager) {
	

	Connection connection = null;
	PreparedStatement preparedStatement = null;
	//int userid = Integer.valueOf(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userid").toString());
	
	try {
		connection = DataConnect.getConnection();		
		String query = "update userdetails set approved = ? where userid = ?";		
		preparedStatement = (PreparedStatement) connection.prepareStatement(query);		
		preparedStatement.setString(1,"YES");
		preparedStatement.setInt(2,selectedManager);				
	     preparedStatement.executeUpdate();	
		connection.close();
		return true;			
		
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		
	}
	
	return false;
}

/*public static boolean viewAccountHistory(String username) {
	

	Connection connection = null;
	PreparedStatement preparedStatement = null;
	//int userid = Integer.valueOf(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userid").toString());
	
	try {
		connection = DataConnect.getConnection();		
		String query = "select userdetails set approved = ? where userid = ?";		
		preparedStatement = (PreparedStatement) connection.prepareStatement(query);		
		preparedStatement.setString(1,"YES");
		preparedStatement.setInt(2,selectedManager);				
	     preparedStatement.executeUpdate();	
		connection.close();
		return true;			
		
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		
	}
	
	return false;
}*/



	

}
