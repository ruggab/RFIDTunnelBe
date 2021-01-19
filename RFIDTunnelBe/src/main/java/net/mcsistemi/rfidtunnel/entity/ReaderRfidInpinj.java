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

}
