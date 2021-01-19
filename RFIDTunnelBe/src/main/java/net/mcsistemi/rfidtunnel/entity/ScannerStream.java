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
@Table(name = "scanner")
public class ScannerStream {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	//@NotBlank
    private String packId;
	
	@Temporal(TemporalType.TIMESTAMP)
    private Date timeStamp;
	
	@Column(nullable = true)
	private long epcCount;
	
	public ScannerStream(){}
	
	public ScannerStream(String packId, Date timeStamp) {
		this.packId=packId;
		this.timeStamp=timeStamp;
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
	
	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public long getEpcCount() {
		return epcCount;
	}

	public void setEpcCount(long epcCount) {
		this.epcCount = epcCount;
	}
	
}
