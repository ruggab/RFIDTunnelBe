package net.mcsistemi.rfidtunnel.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "antenna")
public class Antenna {
		
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
    private Long id;
	
	
    private Long idReader;
	
	private boolean maxRxSensitivity;
	
	private boolean maxTxPower;
	
	private float powerinDbm;
	
	private float sensitivityinDbm;
	
	
	public Antenna(){}
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public Long getIdReader() {
		return idReader;
	}


	public void setIdReader(Long idReader) {
		this.idReader = idReader;
	}


	public boolean isMaxRxSensitivity() {
		return maxRxSensitivity;
	}


	public void setMaxRxSensitivity(boolean maxRxSensitivity) {
		this.maxRxSensitivity = maxRxSensitivity;
	}


	public boolean isMaxTxPower() {
		return maxTxPower;
	}


	public void setMaxTxPower(boolean maxTxPower) {
		this.maxTxPower = maxTxPower;
	}


	public float getPowerinDbm() {
		return powerinDbm;
	}


	public void setPowerinDbm(float powerinDbm) {
		this.powerinDbm = powerinDbm;
	}


	public float getSensitivityinDbm() {
		return sensitivityinDbm;
	}


	public void setSensitivityinDbm(float sensitivityinDbm) {
		this.sensitivityinDbm = sensitivityinDbm;
	}

	
}
