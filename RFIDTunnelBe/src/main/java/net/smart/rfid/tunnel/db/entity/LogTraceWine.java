package net.smart.rfid.tunnel.db.entity;

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

/**
 * @author Gabriele
 *
 */
@Entity
@Table(name = "log_trace_wine")
public class LogTraceWine {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	private Long idSend;

	private Date dataInvio;

	private String esitoInvio;

	private String descError;
	
	private String dataInvioForm;

	public LogTraceWine() {
	}

	public Long getIdSend() {
		return idSend;
	}

	public void setIdSend(Long idSend) {
		this.idSend = idSend;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getDataInvioForm() {
		Date date = new Date();
		date.setTime(this.dataInvio.getTime());
		String formattedDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(date);
		return formattedDate;
	}

	public void setDataInvioForm(String dataForm) {
		this.dataInvioForm = dataForm;
	}

	

	public String getDescError() {
		return descError;
	}

	public void setDescError(String descError) {
		this.descError = descError;
	}

	public Date getDataInvio() {
		return dataInvio;
	}

	public void setDataInvio(Date dataInvio) {
		this.dataInvio = dataInvio;
	}

	public String getEsitoInvio() {
		return esitoInvio;
	}

	public void setEsitoInvio(String esitoInvio) {
		this.esitoInvio = esitoInvio;
	}

}
