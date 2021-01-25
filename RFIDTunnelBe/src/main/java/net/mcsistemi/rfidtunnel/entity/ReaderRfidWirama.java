package net.mcsistemi.rfidtunnel.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "reader_rfid_wirama")
public class ReaderRfidWirama extends Reader {

	@Column(length = 30)
	private String antenna1;
	@Column(length = 30)
	private String antenna2;
	
	public ReaderRfidWirama() {
	}

	public String getAntenna1() {
		return antenna1;
	}

	public void setAntenna1(String antenna1) {
		this.antenna1 = antenna1;
	}

	public String getAntenna2() {
		return antenna2;
	}

	public void setAntenna2(String antenna2) {
		this.antenna2 = antenna2;
	}
	
	

}
