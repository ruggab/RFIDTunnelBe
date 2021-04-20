package net.mcsistemi.rfidtunnel.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author Gabriele
 *
 */
@Entity
@Table(name = "conf_reader")
public class ConfReader {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	//
	@ManyToMany(mappedBy = "confReaders")
	private Set<Tunnel> tunnels;
	
	@Transient
	private List<ConfAntenna> antennas = new ArrayList();

	private Long idDispositivo;

	private Integer readerMode;
	private Integer searchMode;
	private Boolean keepAlive;
	private Boolean stato;
	private boolean autoStartActive;
	private Integer numPortaAutostart;
	private Integer autoStartMode;
	private Integer autoStopMode;
	private boolean enableUser;
	private boolean enableTid;
	private boolean enableEpc;

	

	public Integer getSearchMode() {
		return searchMode;
	}

	public void setSearchMode(Integer searchMode) {
		this.searchMode = searchMode;
	}

	public ConfReader() {
	}

	public boolean isAutoStartActive() {
		return autoStartActive;
	}

	public void setAutoStartActive(boolean autoStartActive) {
		this.autoStartActive = autoStartActive;
	}

	public Integer getNumPortaAutostart() {
		return numPortaAutostart;
	}

	public void setNumPortaAutostart(Integer numPortaAutostart) {
		this.numPortaAutostart = numPortaAutostart;
	}

	public Integer getAutoStartMode() {
		return autoStartMode;
	}

	public void setAutoStartMode(Integer autoStartMode) {
		this.autoStartMode = autoStartMode;
	}

	public Integer getAutoStopMode() {
		return autoStopMode;
	}

	public void setAutoStopMode(Integer autoStopMode) {
		this.autoStopMode = autoStopMode;
	}

	public Integer getReaderMode() {
		return readerMode;
	}

	public void setReaderMode(Integer readerMode) {
		this.readerMode = readerMode;
	}

	public boolean isEnableUser() {
		return enableUser;
	}

	public void setEnableUser(boolean enableUser) {
		this.enableUser = enableUser;
	}

	public boolean isEnableTid() {
		return enableTid;
	}

	public void setEnableTid(boolean enableTid) {
		this.enableTid = enableTid;
	}

	public boolean isEnableEpc() {
		return enableEpc;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getKeepAlive() {
		return keepAlive;
	}

	public void setKeepAlive(Boolean keepAlive) {
		this.keepAlive = keepAlive;
	}

	public Boolean getStato() {
		return stato;
	}

	public void setStato(Boolean stato) {
		this.stato = stato;
	}

	public void setEnableEpc(boolean enableEpc) {
		this.enableEpc = enableEpc;
	}


	public List<ConfAntenna> getAntennas() {
		return antennas;
	}

	public void setAntennas(List<ConfAntenna> antennas) {
		this.antennas = antennas;
	}

	public Set<Tunnel> getTunnels() {
		return tunnels;
	}

	public void setTunnels(Set<Tunnel> tunnels) {
		this.tunnels = tunnels;
	}

	public Long getIdDispositivo() {
		return idDispositivo;
	}

	public void setIdDispositivo(Long idDispositivo) {
		this.idDispositivo = idDispositivo;
	}
	
	
	

}
