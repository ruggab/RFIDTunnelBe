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
	@Column(length = 30)
	private String antenna3;

	public ReaderRfidWirama() {
	}

}
