package net.mcsistemi.rfidtunnel.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "reader_rfid_inpinj")
public class ReaderRfidInpinj extends Reader {

	@Column(length = 30)
	private String antenna3;
	@Column(length = 30)
	private String antenna4;
	@Column(length = 30)
	private String antenna5;

	public ReaderRfidInpinj() {
	}

	public String getAntenna3() {
		return antenna3;
	}

	public void setAntenna3(String antenna3) {
		this.antenna3 = antenna3;
	}

	public String getAntenna4() {
		return antenna4;
	}

	public void setAntenna4(String antenna4) {
		this.antenna4 = antenna4;
	}

	public String getAntenna5() {
		return antenna5;
	}

	public void setAntenna5(String antenna5) {
		this.antenna5 = antenna5;
	}
	

}
