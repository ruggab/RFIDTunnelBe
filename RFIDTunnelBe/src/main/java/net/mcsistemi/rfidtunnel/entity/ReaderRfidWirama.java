package net.mcsistemi.rfidtunnel.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "reader_rfid_wirama")
public class ReaderRfidWirama extends Reader {

	
	public ReaderRfidWirama() {
	}
	
	@Column(length = 4)
	protected String portaComandi;

	public String getPortaComandi() {
		return portaComandi;
	}

	public void setPortaComandi(String portaComandi) {
		this.portaComandi = portaComandi;
	}
	

}
