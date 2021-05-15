package net.mcsistemi.rfidtunnel.model;

import net.mcsistemi.rfidtunnel.entity.Dispositivo;
import net.mcsistemi.rfidtunnel.entity.Tunnel;

/**
 * @author Gabriele
 *
 */


public class TunnelDevice {

	public TunnelDevice() {
	}

	
	private Dispositivo dispositivo;
	
	private Tunnel tunnel;

	

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
