package net.mcsistemi.rfidtunnel.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "conf_antenna")
public class ConfAntenna {
		
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
    private Long id;
	
	private Long position;
    
	private boolean maxRxSensitivity;
	
	private boolean maxTxPower;
	
	private float powerinDbm;
	
	private float sensitivityinDbm;
	
	private boolean enable;
	
	@ManyToOne
    private ConfReader confReader;
	
	
	public ConfAntenna(){}
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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


	public Long getPosition() {
		return position;
	}


	public void setPosition(Long position) {
		this.position = position;
	}


	public boolean isEnable() {
		return enable;
	}


	public void setEnable(boolean enable) {
		this.enable = enable;
	}


	public ConfReader getConfReader() {
		return confReader;
	}


	public void setConfReader(ConfReader confReader) {
		this.confReader = confReader;
	}


	
	
	

	
}
