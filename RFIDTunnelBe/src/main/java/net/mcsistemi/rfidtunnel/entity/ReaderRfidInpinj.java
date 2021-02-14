package net.mcsistemi.rfidtunnel.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import net.mcsistemi.rfidtunnel.form.ReaderForm;

@Entity
@Table(name = "reader_rfid_inpinj")
public class ReaderRfidInpinj extends Reader {

	
	
	
	
// ---------- GPIO PORT CONFIGURATION --
	@Column(length = 2)
	private String activatePort;
	
	private Integer onlinePort;

	private Integer greenPort;
	
	private Integer redPort;
	
	private Integer yellowPort;	
		
	
//	--------- LOG FILE PARAMETERS -----------
	private boolean createOutFile;	//S/N	
	@Column(length = 10)
	private String nameOutLog;	//Z-OUT_	
	@Column(length = 10)
	private String extentionOutLog;	//.log_	
	
	
	private boolean createErrFile;	//S/N	
	@Column(length = 10)
	private String nameErrLog;	//Z-ERR_	
	@Column(length = 10)
	private String extentionErrLog;	//.log_	
	
/*  # --------- PRINT PARAMETERS ------------*/
	
	@Column(length = 1)
	private String printDetails; //S/N
	
	
	public ReaderRfidInpinj() {
	}


	public String getActivatePort() {
		return activatePort;
	}

	public void setActivatePort(String activatePort) {
		this.activatePort = activatePort;
	}

	public Integer getGreenPort() {
		return greenPort;
	}

	public void setGreenPort(Integer greenPort) {
		this.greenPort = greenPort;
	}

	

	
	public String getNameOutLog() {
		return nameOutLog;
	}


	public void setNameOutLog(String nameOutLog) {
		this.nameOutLog = nameOutLog;
	}


	public String getExtentionOutLog() {
		return extentionOutLog;
	}


	public void setExtentionOutLog(String extentionOutLog) {
		this.extentionOutLog = extentionOutLog;
	}


	public String getNameErrLog() {
		return nameErrLog;
	}


	public void setNameErrLog(String nameErrLog) {
		this.nameErrLog = nameErrLog;
	}


	public String getExtentionErrLog() {
		return extentionErrLog;
	}


	public void setExtentionErrLog(String extentionErrLog) {
		this.extentionErrLog = extentionErrLog;
	}


	public String getPrintDetails() {
		return printDetails;
	}


	public void setPrintDetails(String printDetails) {
		this.printDetails = printDetails;
	}


	public boolean isCreateOutFile() {
		return createOutFile;
	}


	public void setCreateOutFile(boolean createOutFile) {
		this.createOutFile = createOutFile;
	}


	public boolean isCreateErrFile() {
		return createErrFile;
	}


	public void setCreateErrFile(boolean createErrFile) {
		this.createErrFile = createErrFile;
	}


	public Integer getOnlinePort() {
		return onlinePort;
	}


	public void setOnlinePort(Integer onlinePort) {
		this.onlinePort = onlinePort;
	}


	public Integer getRedPort() {
		return redPort;
	}


	public void setRedPort(Integer redPort) {
		this.redPort = redPort;
	}


	public Integer getYellowPort() {
		return yellowPort;
	}


	public void setYellowPort(Integer yellowPort) {
		this.yellowPort = yellowPort;
	}
	
	

		
}
