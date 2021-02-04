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
	private Integer activatePort;
	@Column(length = 2)
	private Integer onlinePort;
	@Column(length = 2)
	private Integer greenPort;
	@Column(length = 2)
	private Integer redPort;
	@Column(length = 2)
	private Integer yellowPort;
	
//# --------- INPUT / OUTPUT FILE PARAMETERS --	
	@Column(length = 2)
	private Integer inputMode;
	@Column(length = 100)
	private String inPathFile;
	@Column(length = 10)
	private String extensionInFile;
			
	@Column(length = 2)
	private Integer outputMode;
	@Column(length = 100)
	private String outPathFile;
	@Column(length = 10)
	private String extensionOutFile;
	
	@Column(length = 10)
	private String prefixPackage;		
	@Column(length = 10)
	private String postfixPackage;		
	
	private Integer lineLenght;		
	
	private Integer fieldsNumber;
		
//# --------- TAG ID PARAMETERS --------------------------------------------
	@Column(length = 10)
	private String tagPackageID;		
	@Column(length = 10)
	private Integer tagItemID;		
		
	
//	--------- LOG FILE PARAMETERS -----------

	@Column(length = 10)
	private String createOutFile;	//S/N	
	@Column(length = 10)
	private String nameOutLog;	//Z-OUT_	
	@Column(length = 10)
	private String extentionOutLog;	//.log_	
	
	@Column(length = 1)
	private String createErrFile;	//S/N	
	@Column(length = 10)
	private String nameErrLog;	//Z-ERR_	
	@Column(length = 10)
	private String extentionErrLog;	//.log_	
	
/*  # --------- PRINT PARAMETERS ------------*/
	
	@Column(length = 1)
	private String printDetails; //S/N
	
	
	public ReaderRfidInpinj() {
	}
	
	public ReaderRfidInpinj(ReaderForm form) {
		super(form);
		//this.activatePort = form.get;
		//this.antenna2 = form.getAntenna2();
	}

	public Integer getActivatePort() {
		return activatePort;
	}

	public void setActivatePort(Integer activatePort) {
		this.activatePort = activatePort;
	}

	public Integer getOnlinePort() {
		return onlinePort;
	}

	public void setOnlinePort(Integer onlinePort) {
		this.onlinePort = onlinePort;
	}

	public Integer getGreenPort() {
		return greenPort;
	}

	public void setGreenPort(Integer greenPort) {
		this.greenPort = greenPort;
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

	public Integer getInputMode() {
		return inputMode;
	}

	public void setInputMode(Integer inputMode) {
		this.inputMode = inputMode;
	}

	public String getInPathFile() {
		return inPathFile;
	}

	public void setInPathFile(String inPathFile) {
		this.inPathFile = inPathFile;
	}

	public String getExtensionInFile() {
		return extensionInFile;
	}

	public void setExtensionInFile(String extensionInFile) {
		this.extensionInFile = extensionInFile;
	}

	public Integer getOutputMode() {
		return outputMode;
	}

	public void setOutputMode(Integer outputMode) {
		this.outputMode = outputMode;
	}

	public String getOutPathFile() {
		return outPathFile;
	}

	public void setOutPathFile(String outPathFile) {
		this.outPathFile = outPathFile;
	}

	public String getExtensionOutFile() {
		return extensionOutFile;
	}

	public void setExtensionOutFile(String extensionOutFile) {
		this.extensionOutFile = extensionOutFile;
	}

	public String getPrefixPackage() {
		return prefixPackage;
	}

	public void setPrefixPackage(String prefixPackage) {
		this.prefixPackage = prefixPackage;
	}

	public String getPostfixPackage() {
		return postfixPackage;
	}

	public void setPostfixPackage(String postfixPackage) {
		this.postfixPackage = postfixPackage;
	}

	public Integer getLineLenght() {
		return lineLenght;
	}

	public void setLineLenght(Integer lineLenght) {
		this.lineLenght = lineLenght;
	}

	public Integer getFieldsNumber() {
		return fieldsNumber;
	}

	public void setFieldsNumber(Integer fieldsNumber) {
		this.fieldsNumber = fieldsNumber;
	}

	public String getTagPackageID() {
		return tagPackageID;
	}

	public void setTagPackageID(String tagPackageID) {
		this.tagPackageID = tagPackageID;
	}

	public Integer getTagItemID() {
		return tagItemID;
	}

	public void setTagItemID(Integer tagItemID) {
		this.tagItemID = tagItemID;
	}

	public String getCreateOutFile() {
		return createOutFile;
	}

	public void setCreateOutFile(String createOutFile) {
		this.createOutFile = createOutFile;
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

	public String getCreateErrFile() {
		return createErrFile;
	}

	public void setCreateErrFile(String createErrFile) {
		this.createErrFile = createErrFile;
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
			

		
}
