package net.mcsistemi.rfidtunnel.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import net.mcsistemi.rfidtunnel.form.ReaderForm;

@Entity
@Table(name = "reader_rfid_inpinj")
public class ReaderRfidInpinj extends Reader {

	//
	private Integer readerMode;

	// --------- LOG FILE PARAMETERS -----------
	private boolean createOutFile; // S/N
	@Column(length = 10)
	private String nameOutLog; // Z-OUT_
	@Column(length = 10)
	private String extentionOutLog; // .log_

	private boolean createErrFile; // S/N
	@Column(length = 10)
	private String nameErrLog; // Z-ERR_
	@Column(length = 10)
	private String extentionErrLog; // .log_

	// ---------- GPIO PORT CONFIGURATION --

	private boolean portaIn1;
	private boolean portaIn2;
	private boolean portaIn3;
	private boolean portaIn4;

	private boolean portaOut1;
	private boolean portaOut2;
	private boolean portaOut3;
	private boolean portaOut4;

	private boolean autoStartActive;
	private Integer numPortaAutostart;
	private Integer autoStartMode;
	private Integer autoStopMode;

	public ReaderRfidInpinj() {
	}

	public boolean isAutoStartActive() {
		return autoStartActive;
	}

	public void setAutoStartActive(boolean autoStartActive) {
		this.autoStartActive = autoStartActive;
	}

	public Integer getNumPortaAutostart() {
		return numPortaAutostart;
	}

	public void setNumPortaAutostart(Integer numPortaAutostart) {
		this.numPortaAutostart = numPortaAutostart;
	}

	public Integer getAutoStartMode() {
		return autoStartMode;
	}

	public void setAutoStartMode(Integer autoStartMode) {
		this.autoStartMode = autoStartMode;
	}

	public Integer getAutoStopMode() {
		return autoStopMode;
	}

	public void setAutoStopMode(Integer autoStopMode) {
		this.autoStopMode = autoStopMode;
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

	public Integer getReaderMode() {
		return readerMode;
	}

	public void setReaderMode(Integer readerMode) {
		this.readerMode = readerMode;
	}

	public boolean isPortaIn1() {
		return portaIn1;
	}

	public void setPortaIn1(boolean portaIn1) {
		this.portaIn1 = portaIn1;
	}

	public boolean isPortaIn2() {
		return portaIn2;
	}

	public void setPortaIn2(boolean portaIn2) {
		this.portaIn2 = portaIn2;
	}

	public boolean isPortaIn3() {
		return portaIn3;
	}

	public void setPortaIn3(boolean portaIn3) {
		this.portaIn3 = portaIn3;
	}

	public boolean isPortaIn4() {
		return portaIn4;
	}

	public void setPortaIn4(boolean portaIn4) {
		this.portaIn4 = portaIn4;
	}

	public boolean isPortaOut1() {
		return portaOut1;
	}

	public void setPortaOut1(boolean portaOut1) {
		this.portaOut1 = portaOut1;
	}

	public boolean isPortaOut2() {
		return portaOut2;
	}

	public void setPortaOut2(boolean portaOut2) {
		this.portaOut2 = portaOut2;
	}

	public boolean isPortaOut3() {
		return portaOut3;
	}

	public void setPortaOut3(boolean portaOut3) {
		this.portaOut3 = portaOut3;
	}

	public boolean isPortaOut4() {
		return portaOut4;
	}

	public void setPortaOut4(boolean portaOut4) {
		this.portaOut4 = portaOut4;
	}

}
