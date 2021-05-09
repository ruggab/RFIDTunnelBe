package net.mcsistemi.rfidtunnel.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author Gabriele
 *
 */
@Entity
@Table(name = "tunnel_dispositivi")
public class TunnelDispositivi {

	public TunnelDispositivi() {
	}

	
	@Column(name = "tunnel_id")
	private Long tunnelId;
	@Id
	@Column(name = "dispositivi_id")
	private Long dispositiviId;
	
	@Transient
	private Dispositivo dispositivo;
	@Transient
	private Tunnel tunnel;

	public Long getTunnelId() {
		return tunnelId;
	}

	public void setTunnelId(Long tunnelId) {
		this.tunnelId = tunnelId;
	}

	public Long getDispositiviId() {
		return dispositiviId;
	}

	public void setDispositiviId(Long dispositiviId) {
		this.dispositiviId = dispositiviId;
	}

	public Dispositivo getDispositivo() {
		return dispositivo;
	}

	public void setDispositivo(Dispositivo dispositivo) {
		this.dispositivo = dispositivo;
	}

	public Tunnel getTunnel() {
		return tunnel;
	}

	public void setTunnel(Tunnel tunnel) {
		this.tunnel = tunnel;
	}

	
	

}
