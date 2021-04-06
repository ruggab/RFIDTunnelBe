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
@Table(name = "reader_stream")
public class ReaderStream {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	private String packId;

	private String epc;

	private String userData;

	private String tid;

	private String ipAdress;

	private String port;

	private String antennaPortNumber;

	private String firstSeenTime;

	private String lastSeenTime;

	private String tagSeenCount;

	private String rfDopplerFrequency;

	private String peakRssiInDbm;

	private String channelInMhz;

	private String phaseAngleInRadians;

	private String modelName;
	
	

	@Temporal(TemporalType.TIMESTAMP)
	private Date timeStamp;

	public ReaderStream() {
	}

	public ReaderStream(String packId, String epc, String tid, Date timeStamp) {
		this.packId = packId;
		this.epc = epc;
		this.tid = tid;
		this.timeStamp = timeStamp;
	}

	public String getAntennaPortNumber() {
		return antennaPortNumber;
	}

	public void setAntennaPortNumber(String antennaPortNumber) {
		this.antennaPortNumber = antennaPortNumber;
	}

	public String getFirstSeenTime() {
		return firstSeenTime;
	}

	public void setFirstSeenTime(String firstSeenTime) {
		this.firstSeenTime = firstSeenTime;
	}

	public String getLastSeenTime() {
		return lastSeenTime;
	}

	public void setLastSeenTime(String lastSeenTime) {
		this.lastSeenTime = lastSeenTime;
	}

	public String getTagSeenCount() {
		return tagSeenCount;
	}

	public void setTagSeenCount(String tagSeenCount) {
		this.tagSeenCount = tagSeenCount;
	}

	public String getRfDopplerFrequency() {
		return rfDopplerFrequency;
	}

	public void setRfDopplerFrequency(String rfDopplerFrequency) {
		this.rfDopplerFrequency = rfDopplerFrequency;
	}

	public String getPeakRssiInDbm() {
		return peakRssiInDbm;
	}

	public void setPeakRssiInDbm(String peakRssiInDbm) {
		this.peakRssiInDbm = peakRssiInDbm;
	}

	public String getChannelInMhz() {
		return channelInMhz;
	}

	public void setChannelInMhz(String channelInMhz) {
		this.channelInMhz = channelInMhz;
	}

	public String getPhaseAngleInRadians() {
		return phaseAngleInRadians;
	}

	public void setPhaseAngleInRadians(String phaseAngleInRadians) {
		this.phaseAngleInRadians = phaseAngleInRadians;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
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

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getIpAdress() {
		return ipAdress;
	}

	public void setIpAdress(String ipAdress) {
		this.ipAdress = ipAdress;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getUserData() {
		return userData;
	}

	public void setUserData(String userData) {
		this.userData = userData;
	}

}
