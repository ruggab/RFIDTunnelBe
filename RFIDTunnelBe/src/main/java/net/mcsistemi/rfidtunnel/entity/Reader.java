package net.mcsistemi.rfidtunnel.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "reader")
public class Reader {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
    protected Long id;
	
	protected Long idTipoReader;
	
	@Column(length = 15)
	protected String ipadress;
	
	@Column(length = 4)
	protected String port;
	
	
	
	public Reader(){}
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public Long getIdTipoReader() {
		return idTipoReader;
	}


	public void setIdTipoReader(Long idTipoReader) {
		this.idTipoReader = idTipoReader;
	}


	public String getIpadress() {
		return ipadress;
	}


	public void setIpadress(String ipadress) {
		this.ipadress = ipadress;
	}


	public String getPort() {
		return port;
	}


	public void setPort(String port) {
		this.port = port;
	}

	
}
