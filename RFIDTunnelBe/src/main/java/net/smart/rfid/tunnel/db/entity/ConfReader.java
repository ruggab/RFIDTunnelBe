package net.mcsistemi.rfidtunnel.db.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

	@Transient
	private List<ConfAntenna> antennas = new ArrayList<ConfAntenna>();
	@Transient
	private List<ConfPorta> ports = new ArrayList<ConfPorta>();
	@Transient
	private Dispositivo dispositivo;
	@Transient
	private Tunnel tunnel;
	
	private Long idDispositivo;
	private Long idTunnel;
	private Boolean keepAlive;
	private Integer readerMode;
	private Integer searchMode;
	private Integer session;

	// Ports
	private Integer autoStartMode;
	private Integer autoStopMode;

	// Start
	private Integer gpiPortStart;
	private boolean stateGpiPortStart;
	private Integer debGpiPortStart;
	// Stop
	private Integer gpiPortStop;
	private boolean stateGpiPortStop;
	private Integer debGpiPortStop;

	// Maintenance
	private Integer gpiPortMaintenance;
	private boolean stateGpiPortMaintenance;
	private Integer debGpiPortMaintenance;

	//
	private boolean enableUser;
	private boolean enableTid;
	private boolean enableEpc;
	private boolean enableSku;
	

	public Integer getSearchMode() {
		return searchMode;
	}

	public void setSearchMode(Integer searchMode) {
		this.searchMode = searchMode;
	}

	public ConfReader() {
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

	public void setEnableEpc(boolean enableEpc) {
		this.enableEpc = enableEpc;
	}

	public List<ConfAntenna> getAntennas() {
		return antennas;
	}

	public void setAntennas(List<ConfAntenna> antennas) {
		this.antennas = antennas;
	}

	public Long getIdDispositivo() {
		return idDispositivo;
	}

	public void setIdDispositivo(Long idDispositivo) {
		this.idDispositivo = idDispositivo;
	}

	public Long getIdTunnel() {
		return idTunnel;
	}

	public void setIdTunnel(Long idTunnel) {
		this.idTunnel = idTunnel;
	}

	public Integer getGpiPortStart() {
		return gpiPortStart;
	}

	public void setGpiPortStart(Integer gpiPortStart) {
		this.gpiPortStart = gpiPortStart;
	}

	public Integer getGpiPortStop() {
		return gpiPortStop;
	}

	public void setGpiPortStop(Integer gpiPortStop) {
		this.gpiPortStop = gpiPortStop;
	}

	public boolean isStateGpiPortStart() {
		return stateGpiPortStart;
	}

	public void setStateGpiPortStart(boolean stateGpiPortStart) {
		this.stateGpiPortStart = stateGpiPortStart;
	}

	public Integer getDebGpiPortStart() {
		return debGpiPortStart;
	}

	public void setDebGpiPortStart(Integer debGpiPortStart) {
		this.debGpiPortStart = debGpiPortStart;
	}

	public boolean isStateGpiPortStop() {
		return stateGpiPortStop;
	}

	public void setStateGpiPortStop(boolean stateGpiPortStop) {
		this.stateGpiPortStop = stateGpiPortStop;
	}

	public Integer getDebGpiPortStop() {
		return debGpiPortStop;
	}

	public void setDebGpiPortStop(Integer debGpiPortStop) {
		this.debGpiPortStop = debGpiPortStop;
	}

	public List<ConfPorta> getPorts() {
		return ports;
	}

	public void setPorts(List<ConfPorta> ports) {
		this.ports = ports;
	}

	public Integer getGpiPortMaintenance() {
		return gpiPortMaintenance;
	}

	public void setGpiPortMaintenance(Integer gpiPortMaintenance) {
		this.gpiPortMaintenance = gpiPortMaintenance;
	}

	public boolean isStateGpiPortMaintenance() {
		return stateGpiPortMaintenance;
	}

	public void setStateGpiPortMaintenance(boolean stateGpiPortMaintenance) {
		this.stateGpiPortMaintenance = stateGpiPortMaintenance;
	}

	public Integer getDebGpiPortMaintenance() {
		return debGpiPortMaintenance;
	}

	public void setDebGpiPortMaintenance(Integer debGpiPortMaintenance) {
		this.debGpiPortMaintenance = debGpiPortMaintenance;
	}

	public Dispositivo getDispositivo() {
		return dispositivo;
	}

	public void setDispositivo(Dispositivo dispositivo) {
		this.dispositivo = dispositivo;
	}

	public Integer getSession() {
		return session;
	}

	public void setSession(Integer session) {
		this.session = session;
	}

	public Tunnel getTunnel() {
		return tunnel;
	}

	public void setTunnel(Tunnel tunnel) {
		this.tunnel = tunnel;
	}

	public boolean isEnableSku() {
		return enableSku;
	}

	public void setEnableSku(boolean enableSku) {
		this.enableSku = enableSku;
	}
	
	

}
