package net.smart.rfid.tunnel.listneroctane;

import com.impinj.octane.ConnectionLostListener;
import com.impinj.octane.ImpinjReader;

import net.smart.rfid.tunnel.db.entity.ConfReader;
import net.smart.rfid.tunnel.db.services.TunnelService;
import net.smart.rfid.tunnel.util.DateFunction;

public class ConnectionLostListenerImplement implements ConnectionLostListener {
	static DateFunction myDate;
	
	private ConfReader confReader;
	private TunnelService tunnelService;

	
	
	public ConnectionLostListenerImplement(ConfReader confReader,TunnelService tunnelService) {
		this.confReader = confReader;
		this.tunnelService = tunnelService;
	}

	@Override
	public void onConnectionLost(ImpinjReader reader) {
		System.out.println("----Entrata ConnectionLostListenerImplement");
		try {
			confReader.getDispositivo().setStato(false);
			tunnelService.aggiornaDispositivo(this.confReader.getDispositivo());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("-Uscita ConnectionLostListenerImplement------->");
	}
}
