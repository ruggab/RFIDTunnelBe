package net.mcsistemi.rfidtunnel.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class Item {
	
	private String epc;
	private String sku;
	
	public Item() {}
	
	public Item(String epc, String sku) {
		this.epc=epc;
		this.sku=sku;
	}

	public String getEpc() {
		return epc;
	}

	public void setEpc(String epc) {
		this.epc = epc;
	}
	
	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}
}
