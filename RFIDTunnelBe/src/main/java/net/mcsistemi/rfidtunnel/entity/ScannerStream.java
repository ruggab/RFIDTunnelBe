package net.mcsistemi.rfidtunnel.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "scanner_stream")
public class ScannerStream {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	private Long idTunnel;

	
	private String packageData;
	
	
	private String esito;

	// @NotBlank
	private boolean dettaglio;

	@Temporal(TemporalType.TIMESTAMP)
	private Date timeStamp;

	
	private String quantita;
	
	
	private String lastEvent;
	
	@Transient
	private String dataForm;
	

	
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
	
	public String getDataForm() {
		Date date = new Date();
		date.setTime(this.timeStamp.getTime());
		String formattedDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(date);
		return formattedDate;
	}

	public void setDataForm(String dataForm) {
		this.dataForm = dataForm;
	}

	public String getQuantita() {
		return quantita;
	}

	public void setQuantita(String quantita) {
		this.quantita = quantita;
	}

	public String getLastEvent() {
		return lastEvent;
	}

	public void setLastEvent(String lastEvent) {
		this.lastEvent = lastEvent;
	}

	

}
