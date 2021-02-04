package net.mcsistemi.rfidtunnel.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.mcsistemi.rfidtunnel.form.ReaderForm;

@Entity
@Table(name = "reader_rfid_wirama")
public class ReaderRfidWirama extends Reader {

	
	public ReaderRfidWirama() {
	}
	
	public ReaderRfidWirama(ReaderForm form) {
		super(form);
		
	}

	

	

}
