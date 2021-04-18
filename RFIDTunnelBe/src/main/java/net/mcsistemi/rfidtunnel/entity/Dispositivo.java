package net.mcsistemi.rfidtunnel.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

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

	private String nome;

	private Long idTipoDispositivo;

	private String descTipoDispositivo;
	
	private Long idModelloReader;
	
	private String descModelloReader;

	@Column(length = 15)
	private String ipAdress;

	@Column(length = 4)
	private String porta;

	private Boolean monitorEnable;

	private Boolean logEnable;

	private String freqLogMs;

	private String numAntenne;

	private String numPortOut;

	private String numPortInput;
	
	@ManyToMany
	private Set<Tunnel> tunnels;


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

	public String getPorta() {
		return porta;
	}

	public void setPorta(String porta) {
		this.porta = porta;
	}

	public Boolean getMonitorEnable() {
		return monitorEnable;
	}

	public void setMonitorEnable(Boolean monitorEnable) {
		this.monitorEnable = monitorEnable;
	}

	public Boolean getLogEnable() {
		return logEnable;
	}

	public void setLogEnable(Boolean logEnable) {
		this.logEnable = logEnable;
	}

	public String getFreqLogMs() {
		return freqLogMs;
	}

	public void setFreqLogMs(String freqLogMs) {
		this.freqLogMs = freqLogMs;
	}

	public String getNumAntenne() {
		return numAntenne;
	}

	public void setNumAntenne(String numAntenne) {
		this.numAntenne = numAntenne;
	}

	public String getNumPortOut() {
		return numPortOut;
	}

	public void setNumPortOut(String numPortOut) {
		this.numPortOut = numPortOut;
	}

	public String getNumPortInput() {
		return numPortInput;
	}

	public void setNumPortInput(String numPortInput) {
		this.numPortInput = numPortInput;
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

	public Set<Tunnel> getTunnels() {
		return tunnels;
	}

	public void setTunnels(Set<Tunnel> tunnels) {
		this.tunnels = tunnels;
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

	
	

}
