package com.loginregister;


public class StockOrders {

	private int order_id;
	private String stock_symbol;
	private float stock_price;
	private int stock_qty;
	private float amount;
	private String orderType;
	private int userid;
	
	public int getOrder_id() {
		return order_id;
	}
	public void setOrder_id(int order_id) {
		this.order_id = order_id;
	}
	public String getStock_symbol() {
		return stock_symbol;
	}
	public void setStock_symbol(String stock_symbol) {
		this.stock_symbol = stock_symbol;
	}
	public float getStock_price() {
		return stock_price;
	}
	public void setStock_price(float stock_price) {
		this.stock_price = stock_price;
	}
	public int getStock_qty() {
		return stock_qty;
	}
	public void setStock_qty(int stock_qty) {
		this.stock_qty = stock_qty;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	
		
	
}
