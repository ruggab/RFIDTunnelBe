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
@Table(name = "scanner_stream")
public class ScannerStream {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	private Long idTunnel;

	// @NotBlank
	private String packageData;
	
	// @NotBlank
	@Column(nullable = true)
	private String esito;

	// @NotBlank
	private boolean dettaglio;

	@Temporal(TemporalType.TIMESTAMP)
	private Date timeStamp;

	@Column(nullable = true)
	private long quantita;

	public ScannerStream() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getEsito() {
		return esito;
	}

	public void setEsito(String esito) {
		this.esito = esito;
	}

	public boolean isDettaglio() {
		return dettaglio;
	}

	public void setDettaglio(boolean dettaglio) {
		this.dettaglio = dettaglio;
	}

	public long getQuantita() {
		return quantita;
	}

	public void setQuantita(long quantita) {
		this.quantita = quantita;
	}

	public Long getIdTunnel() {
		return idTunnel;
	}

	public void setIdTunnel(Long idTunnel) {
		this.idTunnel = idTunnel;
	}

	public String getPackageData() {
		return packageData;
	}

	public void setPackageData(String packageData) {
		this.packageData = packageData;
	}
	
	

}
