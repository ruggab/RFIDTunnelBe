package net.mcsistemi.rfidtunnel.db.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Gabriele
 *
 */
@Entity
@Table(name = "dispositivo")
@JsonIgnoreProperties
public class Dispositivo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	private Long idTunnel;
	
	public Long getIdTunnel() {
		return idTunnel;
	}

	public void setIdTunnel(Long idTunnel) {
		this.idTunnel = idTunnel;
	}

	private String nome;

	private Long idTipoDispositivo;

	private String descTipoDispositivo;
	
	private Long idModelloReader;
	
	private String descModelloReader;

	@Column(length = 15)
	private String ipAdress;

	@Column(length = 4)
	private Long porta;

	private boolean monitorEnable;

	private boolean logEnable;

	private Integer freqLogMs;

	private Integer numAntenne;

	private Integer numPortOut;

	private Integer numPortInput;
	
	private boolean stato;

	
	public Dispositivo() {
	}

	// @Transient
	// protected List<Antenna> listAntenna = new ArrayList<Antenna>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdTipoDispositivo() {
		return idTipoDispositivo;
	}

	public void setIdTipoDispositivo(Long idTipoDispositivo) {
		this.idTipoDispositivo = idTipoDispositivo;
	}

	public String getIpAdress() {
		return ipAdress;
	}

	public void setIpAdress(String ipAdress) {
		this.ipAdress = ipAdress;
	}
	
	public String getDescTipoDispositivo() {
		return descTipoDispositivo;
	}

	public void setDescTipoDispositivo(String descTipoDispositivo) {
		this.descTipoDispositivo = descTipoDispositivo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	

	public Long getIdModelloReader() {
		return idModelloReader;
	}

	public void setIdModelloReader(Long idModelloReader) {
		this.idModelloReader = idModelloReader;
	}

	public String getDescModelloReader() {
		return descModelloReader;
	}

	public void setDescModelloReader(String descModelloReader) {
		this.descModelloReader = descModelloReader;
	}

	public Long getPorta() {
		return porta;
	}

	public void setPorta(Long porta) {
		this.porta = porta;
	}

	public Integer getFreqLogMs() {
		return freqLogMs;
	}

	public void setFreqLogMs(Integer freqLogMs) {
		this.freqLogMs = freqLogMs;
	}

	public Integer getNumAntenne() {
		return numAntenne;
	}

	public void setNumAntenne(Integer numAntenne) {
		this.numAntenne = numAntenne;
	}

	public Integer getNumPortOut() {
		return numPortOut;
	}

	public void setNumPortOut(Integer numPortOut) {
		this.numPortOut = numPortOut;
	}

	public Integer getNumPortInput() {
		return numPortInput;
	}

	public void setNumPortInput(Integer numPortInput) {
		this.numPortInput = numPortInput;
	}

	public boolean isMonitorEnable() {
		return monitorEnable;
	}

	public void setMonitorEnable(boolean monitorEnable) {
		this.monitorEnable = monitorEnable;
	}

	public boolean isLogEnable() {
		return logEnable;
	}

	public void setLogEnable(boolean logEnable) {
		this.logEnable = logEnable;
	}

	public boolean isStato() {
		return stato;
	}

	public void setStato(boolean stato) {
		this.stato = stato;
	}


	
	
	
}
