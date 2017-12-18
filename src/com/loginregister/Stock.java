package com.loginregister;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;





@ManagedBean
@RequestScoped
public class Stock {

    private static final long serialVersionUID = 1L;
    static final String API_KEY = "54DFGLR2VHRBD7T5";

    
    private List<StockDetails> myStockWatchList;
    
    @ManagedProperty("#{stockController}")
	private StockController stockController;
    private List<StockOrders> stockOrderList;
    
    
    private int selectedStockId;
    
    private String symbol;
    private double price;
    private int qty;
    private double amt;
    private String table1Markup;
    private String table2Markup;

    private String selectedSymbol;
    private List<SelectItem> availableSymbols;
    
    
    public List<SelectItem> getStockSymbols() {
		return stockSymbols;
	}

	public void setStockSymbols(List<SelectItem> stockSymbols) {
		this.stockSymbols = stockSymbols;
	}

	private List<SelectItem> stockSymbols;

    public String getPurchaseSymbol() {
        if (getRequestParameter("symbol") != null) {
            symbol = getRequestParameter("symbol");
        }
        return symbol;
    }
    
    public void setPurchaseSymbol(String purchaseSymbol) {
    	this.symbol = purchaseSymbol;
        System.out.println("func setPurchaseSymbol()"); 
    }

    public double getPurchasePrice() {
        if (getRequestParameter("price") != null) {
            price = Double.parseDouble(getRequestParameter("price"));
            System.out.println("price: " + price);
        }
        return price;
    }

    public void setPurchasePrice(double purchasePrice) {
    	this.price = purchasePrice;
        System.out.println("func setPurchasePrice()");  //check
    }
    
    private String getRequestParameter(String name) {
        return ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getParameter(name);
    }

    @PostConstruct
    public void init() {
        //initially populate stock list
        
    	availableSymbols = stockController.getAvailableStocks();

        //initially populate intervals for stock api
        availableIntervals = new ArrayList<SelectItem>();
        availableIntervals.add(new SelectItem("1min", "1min"));
        availableIntervals.add(new SelectItem("5min", "5min"));
        availableIntervals.add(new SelectItem("15min", "15min"));
        availableIntervals.add(new SelectItem("30min", "30min"));
        availableIntervals.add(new SelectItem("60min", "60min"));
        stockSymbols = stockController.getStockSymbols();
    }

    private String selectedInterval;
    private List<SelectItem> availableIntervals;

    public String getSelectedInterval() {
        return selectedInterval;
    }

    public void setSelectedInterval(String selectedInterval) {
        this.selectedInterval = selectedInterval;
    }

    public List<SelectItem> getAvailableIntervals() {
        return availableIntervals;
    }

    public void setAvailableIntervals(List<SelectItem> availableIntervals) {
        this.availableIntervals = availableIntervals;
    }

    public String getSelectedSymbol() {
        return selectedSymbol;
    }

    public void setSelectedSymbol(String selectedSymbol) {
        this.selectedSymbol = selectedSymbol;
    }

    public List<SelectItem> getAvailableSymbols() {
        return availableSymbols;
    }

    public void setAvailableSymbols(List<SelectItem> availableSymbols) {
        this.availableSymbols = availableSymbols;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getAmt() {
        return amt;
    }

    public void setAmt(double amt) {
        this.amt = amt;
    }

    public String getTable1Markup() {
        return table1Markup;
    }

    public void setTable1Markup(String table1Markup) {
        this.table1Markup = table1Markup;
    }

    public String getTable2Markup() {
        return table2Markup;
    }

    public void setTable2Markup(String table2Markup) {
        this.table2Markup = table2Markup;
    }

    public String createDbRecord(String symbol, double price, int qty, double amt) {
        try {
            //System.out.println("symbol: " + this.symbol + ", price: " + this.price + "\n");
            //System.out.println("qty: " + this.qty + ", amt: " + this.amt + "\n");

            Connection conn = DataConnect.getConnection();
            Statement statement = conn.createStatement();
            
            //get userid
            Integer uid = Integer.parseInt((String) FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getSessionMap().get("uid"));
            
            System.out.println(uid);
            System.out.println("symbol:" + symbol);
            System.out.println("price:" + price);
            System.out.println("qty:" + qty);
            System.out.println("amt:" + amt);
            statement.executeUpdate("INSERT INTO `purchase` (`id`, `uid`, `stock_symbol`, `qty`, `price`, `amt`) "
                    + "VALUES (NULL,'" + uid + "','" + symbol + "','" + qty + "','" + price + "','" + amt +"')");
            
            statement.close();
            conn.close();
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO, "Stock successfully purchased",""));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "buy-stock";
    }

    public void installAllTrustingManager() {
        TrustManager[] trustAllCerts;
        trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            System.out.println("Exception :" + e);
        }
        return;
    }

    public void timeseries() throws MalformedURLException, IOException {

        installAllTrustingManager();

        //System.out.println("selectedItem: " + this.selectedSymbol);
        //System.out.println("selectedInterval: " + this.selectedInterval);
        String symbol = this.selectedSymbol;
        String interval = this.selectedInterval;
        String url = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=" + symbol + "&interval=" + interval + "&apikey=" + API_KEY;

        this.table1Markup += "URL::: <a href='" + url + "'>Data Link</a><br>";
        InputStream inputStream = new URL(url).openStream();

        // convert the json string back to object
        JsonReader jsonReader = Json.createReader(inputStream);
        JsonObject mainJsonObj = jsonReader.readObject();
        for (String key : mainJsonObj.keySet()) {/*
            if (key.equals("Meta Data")) {
                this.table1Markup = null; // reset table 1 markup before repopulating
                JsonObject jsob = (JsonObject) mainJsonObj.get(key);
                this.table1Markup += "<style>#detail >tbody > tr > td{ text-align:center;}</style><b>Stock Details</b>:<br>";
                this.table1Markup += "<table>";
                this.table1Markup += "<tr><td>Information</td><td>" + jsob.getString("1. Information") + "</td></tr>";
                this.table1Markup += "<tr><td>Symbol</td><td>" + jsob.getString("2. Symbol") + "</td></tr>";
                this.table1Markup += "<tr><td>Last Refreshed</td><td>" + jsob.getString("3. Last Refreshed") + "</td></tr>";
                this.table1Markup += "<tr><td>Interval</td><td>" + jsob.getString("4. Interval") + "</td></tr>";
                this.table1Markup += "<tr><td>Output Size</td><td>" + jsob.getString("5. Output Size") + "</td></tr>";
                this.table1Markup += "<tr><td>Time Zone</td><td>" + jsob.getString("6. Time Zone") + "</td></tr>";
                this.table1Markup += "</table>";
            } else {
                this.table2Markup = null; // reset table 2 markup before repopulating
                JsonObject dataJsonObj = mainJsonObj.getJsonObject(key);
                this.table2Markup += "<table class='table table-hover'>";
                this.table2Markup += "<thead><tr><th>Timestamp</th><th>Open</th><th>High</th><th>Low</th><th>Close</th><th>Volume</th></tr></thead>";
                this.table2Markup += "<tbody>";
                int i = 0;
                for (String subKey : dataJsonObj.keySet()) {
                    JsonObject subJsonObj = dataJsonObj.getJsonObject(subKey);
                    this.table2Markup
                            += "<tr>"
                            + "<td>" + subKey + "</td>"
                            + "<td>" + subJsonObj.getString("1. open") + "</td>"
                            + "<td>" + subJsonObj.getString("2. high") + "</td>"
                            + "<td>" + subJsonObj.getString("3. low") + "</td>"
                            + "<td>" + subJsonObj.getString("4. close") + "</td>"
                            + "<td>" + subJsonObj.getString("5. volume") + "</td>";
                    if (i == 0) {
                        String path = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
                        this.table2Markup += "<td><a class='btn btn-success' href='" + path + "/faces/buy-stock.xhtml?symbol=" + symbol + "&price=" + subJsonObj.getString("4. close") + "'>Buy Stock</a></td>";
                    }
                    this.table2Markup += "</tr>";
                    i++;
                }
                this.table2Markup += "</tbody></table>";
            }
        */}
        return;
    }

    public void purchaseStock() {
        System.out.println("Calling function purchaseStock()");
        System.out.println("stockSymbol: " + FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("stockSymbol"));
        System.out.println("stockPrice" + FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("stockPrice"));
        return;
    }

	/**
	 * @return the stockController
	 */
	public StockController getStockController() {
		return stockController;
	}

	/**
	 * @param stockController the stockController to set
	 */
	public void setStockController(StockController stockController) {
		this.stockController = stockController;
	}
	
	
	
	/**
	 * @return the selectedStockId
	 */
	public int getSelectedStockId() {
		return selectedStockId;
	}

	/**
	 * @param selectedStockId the selectedStockId to set
	 */
	public void setSelectedStockId(int selectedStockId) {
		this.selectedStockId = selectedStockId;
	}

	/**
	 * @return the apiKey
	 */
	public static String getApiKey() {
		return API_KEY;
	}
	
	
	
public void addToWatchList() {
		
	 installAllTrustingManager();

     int selectedStockId = this.selectedStockId;
 	
		boolean flag = 	stockController.addToWatchList(selectedStockId);
			
		if(flag == true) {
			ExternalContext externalContext =  FacesContext.getCurrentInstance().getExternalContext();
			try {
				externalContext.redirect("addtowatchlist-success.xhtml");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if(flag == false) {
			ExternalContext externalContext =  FacesContext.getCurrentInstance().getExternalContext();
			try {
				externalContext.redirect("addtowatchlist-failure.xhtml");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	}

	
public void myWatchList() throws IOException {
	
	ExternalContext externalContext =  FacesContext.getCurrentInstance().getExternalContext();	
	String userName = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username");	
	myStockWatchList = stockController.getMyWatchList(userName);	
	FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("myStockWatchList", myStockWatchList);	
	externalContext.redirect("mywatchlist.xhtml");
}


public void viewStocksHistory() throws IOException {
			 
	 String stock_symbol = this.selectedSymbol;
    String interval = this.selectedInterval;
    installAllTrustingManager();
    String url = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=" + stock_symbol + "&interval=" + interval + "&apikey=" + API_KEY;
    this.table1Markup += "URL::: <a href='" + url + "'>Data Link</a><br>";
    InputStream inputStream = new URL(url).openStream();

    // convert the json string back to object
    JsonReader jsonReader = Json.createReader(inputStream);
    JsonObject mainJsonObj = jsonReader.readObject();
    for (String key : mainJsonObj.keySet()) {
        if (key.equals("Meta Data")) {
            this.table1Markup = null; // reset table 1 markup before repopulating
            JsonObject jsob = (JsonObject) mainJsonObj.get(key);
            this.table1Markup += "<style>#detail >tbody > tr > td{ text-align:center;}</style><b>Stock Details</b>:<br>";
            this.table1Markup += "<table>";
            this.table1Markup += "<tr><td>Information</td><td>" + jsob.getString("1. Information") + "</td></tr>";
            this.table1Markup += "<tr><td>Symbol</td><td>" + jsob.getString("2. Symbol") + "</td></tr>";
            this.table1Markup += "<tr><td>Last Refreshed</td><td>" + jsob.getString("3. Last Refreshed") + "</td></tr>";
            this.table1Markup += "<tr><td>Interval</td><td>" + jsob.getString("4. Interval") + "</td></tr>";
            this.table1Markup += "<tr><td>Output Size</td><td>" + jsob.getString("5. Output Size") + "</td></tr>";
            this.table1Markup += "<tr><td>Time Zone</td><td>" + jsob.getString("6. Time Zone") + "</td></tr>";
            this.table1Markup += "</table>";
        } else {
            this.table2Markup = null; // reset table 2 markup before repopulating
            JsonObject dataJsonObj = mainJsonObj.getJsonObject(key);
            this.table2Markup += "<table class='table table-hover'>";
            this.table2Markup += "<thead><tr><th>Timestamp</th><th>Open</th><th>High</th><th>Low</th><th>Close</th><th>Volume</th></tr></thead>";
            this.table2Markup += "<tbody>";
            int i = 0;
            for (String subKey : dataJsonObj.keySet()) {
                JsonObject subJsonObj = dataJsonObj.getJsonObject(subKey);
                this.table2Markup
                        += "<tr>"
                        + "<td>" + subKey + "</td>"
                        + "<td>" + subJsonObj.getString("1. open") + "</td>"
                        + "<td>" + subJsonObj.getString("2. high") + "</td>"
                        + "<td>" + subJsonObj.getString("3. low") + "</td>"
                        + "<td>" + subJsonObj.getString("4. close") + "</td>"
                        + "<td>" + subJsonObj.getString("5. volume") + "</td>";
                if (i == 0) {
                   
                    this.table2Markup += "<td><a class='btn btn-success' href='" + "buy-stock.xhtml?symbol=" + stock_symbol + "&price=" + subJsonObj.getString("4. close") + "'>Buy Stock</a></td>";
                }
                this.table2Markup += "</tr>";
                i++;
            }
            this.table2Markup += "</tbody></table>";
        }
    }
    return;
 
			
}

public String buyStock(String stock_symbol, double stock_price, int stock_qty, double amount) {
	   
	boolean valid = stockController.buy(stock_symbol, stock_price, stock_qty, amount);
	if(valid == true)
		 FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO, "Stocks purchased successfully",""));
	else
		 FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO, "Error encountered while buying the Stock. Please try after sometime.",""));
	
	return "buy-stock";
}

public void viewAccountHistory() throws IOException {
	ExternalContext externalContext =  FacesContext.getCurrentInstance().getExternalContext();
	int userid = Integer.valueOf((String)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userid"));
	stockOrderList = stockController.getStockOrders(userid);
	FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("my_orders", stockOrderList);
	externalContext.redirect("view-account-history.xhtml");

}


}