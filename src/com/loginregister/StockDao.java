package com.loginregister;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.jdbc.PreparedStatement;

@ManagedBean(name = "stockDao", eager = true)
@RequestScoped
public class StockDao implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<SelectItem> availableSymbols;
	private List<StockDetails> stocksInWatchList;
	private List<StockOrders> stockOrdersList;
	public List<SelectItem> getAvailableStocks() {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = DataConnect.getConnection();

			String query = "select * from stock_details";

			availableSymbols = new ArrayList<SelectItem>();
			preparedStatement = (PreparedStatement) connection
					.prepareStatement(query);

			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				availableSymbols.add(new SelectItem(rs.getString("stock_id"),
						rs.getString("stock_symbol")));

			}
			connection.close();

			return availableSymbols;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			System.out.println(e.getMessage());
		}

		return null;
	}

	public boolean addToWatchList(int stock_no) {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		int userid = Integer
				.valueOf(FacesContext.getCurrentInstance().getExternalContext()
						.getSessionMap().get("userid").toString());

		try {
			connection = DataConnect.getConnection();

			String query = "insert into watchlist (stock_no, userid ) values(?,?)";

			preparedStatement = (PreparedStatement) connection
					.prepareStatement(query);

			preparedStatement.setInt(1, stock_no);
			preparedStatement.setInt(2, userid);

			preparedStatement.execute();

			connection.close();

			return true;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			System.out.println(e.getMessage());
		}

		return false;
	}

	public List<StockDetails> getMyWatchList(String userName) {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement2 = null;
		int userid = Integer
				.valueOf(FacesContext.getCurrentInstance().getExternalContext()
						.getSessionMap().get("userid").toString());

		try {
			connection = DataConnect.getConnection();

			String query = "select * from watchlist where userid = ?";

			stocksInWatchList = new ArrayList<StockDetails>();
			preparedStatement = (PreparedStatement) connection
					.prepareStatement(query);
			preparedStatement.setInt(1, userid);

			ResultSet rs = preparedStatement.executeQuery();

			// preparedStatement.execute();
			int count = 0;

			while (rs.next()) {
				count++;
				int stock_no = rs.getInt("stock_no");

				String query2 = "select * from stock_details where stock_id = ?";

				// stocksInWatchList = new ArrayList<SelectItem>();
				preparedStatement2 = (PreparedStatement) connection
						.prepareStatement(query2);
				preparedStatement2.setInt(1, stock_no);

				ResultSet rs2 = preparedStatement2.executeQuery();
				String stockSymbol = "";
				String stockName = "";
				String currentPrice = "";

				while (rs2.next()) {

					stockSymbol = rs2.getString("stock_symbol");
					stockName = rs2.getString("stock_name");

					System.out.println("Calling AlphaVantage API...");
					Client client = ClientBuilder.newClient();

					// Core settings are here, put what ever API parameter you
					// want to use
					WebTarget target = client
							.target("https://www.alphavantage.co/query")
							.queryParam("function", "TIME_SERIES_INTRADAY")
							.queryParam("symbol", stockSymbol)
							.queryParam("interval", "15min")
							.queryParam("outputsize", "1")
							.queryParam("apikey", "54DFGLR2VHRBD7T5");
					// Actually calling API here, Use HTTP GET method
					// data is the response JSON string
					String data = target.request(MediaType.APPLICATION_JSON)
							.get(String.class);

					try {
						// Use Jackson to read the JSON into a tree like
						// structure
						ObjectMapper mapper = new ObjectMapper();
						JsonNode root = mapper.readTree(data);

						// Make sure the JSON is an object, as said in their
						// documents
						assert root.isObject();
						// Read the "Meta Data" property of JSON object
						JsonNode metadata = root.get("Meta Data");
						assert metadata.isObject();
						// Read "2. Symbol" property of "Meta Data" property
						if (metadata.get("2. Symbol").isValueNode()) {
							System.out.println(metadata.get("2. Symbol")
									.asText());
						}
						// Print "4. Time Zone" property of "Meta Data" property
						// of root JSON object
						System.out.println(root.at("/Meta Data/4. Time Zone")
								.asText());
						// Read "Weekly Time Series" property of root JSON
						// object
						Iterator<String> dates = root
								.get("Time Series (15min)").fieldNames();
						while (dates.hasNext()) {
							// Read the first date's open price
							currentPrice = root.at(
									"/Time Series (15min)/" + dates.next()
											+ "/1. open").asText();
							System.out
									.println(Double.parseDouble(currentPrice));
							// remove break if you wan't to print all the open
							// prices.
							break;
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				StockDetails sd = new StockDetails();
				sd.setStockNumber(count);
				sd.setStockName(stockSymbol);
				sd.setProductName(stockName);
				sd.setCurrentPrice(currentPrice);

				stocksInWatchList.add(sd);

			}
			connection.close();

			return stocksInWatchList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			System.out.println(e.getMessage());
		}

		return null;
	}
	
	
public boolean buy(String stock_symbol, double stock_price, int stock_qty, double amount) {
		
		

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		int userid = Integer.valueOf(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userid").toString());
		
		try {
			connection = DataConnect.getConnection();
			
			String query = "insert into order_details (stock_symbol, stock_price, stock_qty, amount, ordertype, userid ) values(?,?,?,?,?,?)";
			
			preparedStatement = (PreparedStatement) connection.prepareStatement(query);
			
			preparedStatement.setString(1,stock_symbol);
			preparedStatement.setFloat(2,(float)stock_price);
			preparedStatement.setInt(3,stock_qty);
			preparedStatement.setFloat(4,(float)amount);
			preparedStatement.setString(5,"BUY");
			preparedStatement.setInt(6,userid);			
			preparedStatement.execute();
			
			
			String sqlQuery = "select balance from userdetails where userid = ?";
			
			
			preparedStatement = (PreparedStatement) connection.prepareStatement(sqlQuery);		
			preparedStatement.setInt(1,userid);
			
			
			ResultSet rs = preparedStatement.executeQuery();
			
			if(rs.next()) {
			
			float balance = rs.getFloat("balance");
				balance = balance - (float)amount;
				
				
				String balanceUpdate = "update userdetails set balance = ? where userid = ?";
				
				preparedStatement = (PreparedStatement) connection.prepareStatement(balanceUpdate);
				
				preparedStatement.setFloat(1,balance);
				preparedStatement.setInt(2,userid);


				preparedStatement.executeUpdate();
				
				
				FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("balance", balance);
				
				connection.close();
			
			}
			return true;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			System.out.println(e.getMessage());
		}
		
				
		return false;
	}
	
	
	
	
	
	public List<SelectItem> getStockSymbols() {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = DataConnect.getConnection();

			String query = "select * from stock_details";
			availableSymbols = new ArrayList<SelectItem>();
			preparedStatement = (PreparedStatement) connection
					.prepareStatement(query);

			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				availableSymbols.add(new SelectItem(rs
						.getString("stock_symbol"), rs
						.getString("stock_symbol")));
			}
			connection.close();

			return availableSymbols;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			System.out.println(e.getMessage());
		}

		return null;
	}
	
	
public List<StockOrders> getStockOrders(int userid) {
Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement2 = null;
		
		try {
			connection = DataConnect.getConnection();
			
			String query = "select * from order_details where userid = ?";
			
			stockOrdersList = new ArrayList<StockOrders>();
			preparedStatement = (PreparedStatement) connection.prepareStatement(query);
			preparedStatement.setInt(1,userid);			
			ResultSet rs = preparedStatement.executeQuery();		
			StockOrders myOrders = null;
			while(rs.next()) {
						
					myOrders = new StockOrders();
					
					
					myOrders.setOrder_id(rs.getInt("order_id"));
					myOrders.setStock_symbol(rs.getString("stock_symbol"));
					myOrders.setStock_price(rs.getFloat("stock_price"));
					myOrders.setAmount(rs.getFloat("amount"));
					myOrders.setStock_qty(rs.getInt("stock_qty"));
					myOrders.setOrderType(rs.getString("ordertype"));					
					stockOrdersList.add(myOrders);  
					}
				
			connection.close();
			return stockOrdersList;                                                                   
				
			}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
		
		return null;
	}
	
	
}
