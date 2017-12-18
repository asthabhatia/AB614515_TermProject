package com.loginregister;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;

@ManagedBean(name = "stockController", eager = true)
@RequestScoped
public class StockController {

	@ManagedProperty("#{stockDao}")
	private StockDao stockDao;

	/**
	 * @return the stockDao
	 */
	public StockDao getStockDao() {
		return stockDao;
	}

	/**
	 * @param stockDao
	 *            the stockDao to set
	 */
	public void setStockDao(StockDao stockDao) {
		this.stockDao = stockDao;
	}

	public List<SelectItem> getAvailableStocks() {

		List<SelectItem> availableStocks = stockDao.getAvailableStocks();

		return availableStocks;
	}

	public boolean addToWatchList(int stock_no) {

		boolean isInserted = stockDao.addToWatchList(stock_no);

		return true;
	}

	public List<StockDetails> getMyWatchList(String userName) {

		List<StockDetails> myWatchList = stockDao.getMyWatchList(userName);
		return myWatchList;
	}
	
	public boolean buy(String stock_symbol, double stock_price, int stock_qty, double amount) {
		boolean valid = stockDao.buy(stock_symbol, stock_price, stock_qty, amount);
		return valid;
		
			}
	
	public List<SelectItem> getStockSymbols() {
		
		List<SelectItem> availableStocks = stockDao.getStockSymbols();
			
			return availableStocks;
		}
	
	public List<StockOrders> getStockOrders(int userid){
		List<StockOrders> stockorders = stockDao.getStockOrders(userid);
		return stockorders;
	}




}
