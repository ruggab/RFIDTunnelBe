package net.mcsistemi.rfidtunnel.listneroctane;

import com.impinj.octane.ConnectionLostListener;
import com.impinj.octane.ImpinjReader;

import net.mcsistemi.rfidtunnel.db.entity.ConfReader;
import net.mcsistemi.rfidtunnel.db.services.TunnelService;
import net.mcsistemi.rfidtunnel.util.DateFunction;

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
