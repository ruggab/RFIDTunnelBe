package net.smart.rfid.tunnel.db.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "conf_porta")
public class ConfPorta {
		
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
    private Long id;
	
	private Long idConfReader;
	
	private Long numPorta;
	
	private Long idPortMode;
	
	private Integer pulsedDurMls;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getNumPorta() {
		return numPorta;
	}

	public void setNumPorta(Long numPorta) {
		this.numPorta = numPorta;
	}

	public Long getIdPortMode() {
		return idPortMode;
	}

	public void setIdPortMode(Long idPortMode) {
		this.idPortMode = idPortMode;
	}

	public Integer getPulsedDurMls() {
		return pulsedDurMls;
	}

	public void setPulsedDurMls(Integer pulsedDurMls) {
		this.pulsedDurMls = pulsedDurMls;
	}

	public Long getIdConfReader() {
		return idConfReader;
	}

	public void setIdConfReader(Long idConfReader) {
		this.idConfReader = idConfReader;
	}
	
	


	
}
