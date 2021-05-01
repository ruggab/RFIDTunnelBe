package net.mcsistemi.rfidtunnel.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "reader_stream_atteso")
public class ReaderStreamAtteso {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	private String packId;

	private String epc;

	private String tid;
	
	private String userData;
	
	private String barcode;


	public ReaderStreamAtteso() {
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getPackId() {
		return packId;
	}


	public void setPackId(String packId) {
		this.packId = packId;
	}


	public String getEpc() {
		return epc;
	}


	public void setEpc(String epc) {
		this.epc = epc;
	}


	public String getTid() {
		return tid;
	}


	public void setTid(String tid) {
		this.tid = tid;
	}


	public String getUserData() {
		return userData;
	}


	public void setUserData(String userData) {
		this.userData = userData;
	}


	public String getBarcode() {
		return barcode;
	}


	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	

}
