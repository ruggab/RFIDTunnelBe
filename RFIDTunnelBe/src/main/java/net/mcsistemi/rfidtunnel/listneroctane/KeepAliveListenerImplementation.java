package net.mcsistemi.rfidtunnel.listneroctane;



import org.apache.log4j.Logger;

import com.impinj.octane.ImpinjReader;
import com.impinj.octane.KeepaliveEvent;
import com.impinj.octane.KeepaliveListener;

import net.mcsistemi.rfidtunnel.entity.ConfReader;
import net.mcsistemi.rfidtunnel.services.TunnelService;

public class KeepAliveListenerImplementation implements KeepaliveListener {

	private static final Logger LOGGER = Logger.getLogger(KeepAliveListenerImplementation.class);
	static net.mcsistemi.rfidtunnel.util.DateFunction myDate;

	
	private ConfReader confReader = null;
	private TunnelService tunnelService;
	


	
	
	public KeepAliveListenerImplementation(ConfReader confReader, TunnelService tunnelService) {
		this.confReader = confReader;
		this.tunnelService = tunnelService;
		
	}

	
	public void onKeepalive(ImpinjReader reader, KeepaliveEvent e) {
		try {
			if (!reader.isConnected()) {
				LOGGER.info("READER NON CONNESSO");

				LOGGER.info(myDate.getFullDate() + " Reader try to Re-Connecting...........");
				// Connessione ed Attivazione LED su Tunnel
				reader.connect(this.confReader.getDispositivo().getIpAdress());
				if (reader.isConnected()) {
					reader.setGpo(1, true);
					this.confReader.getDispositivo().setStato(true);
					tunnelService.aggiornaDispositivo(this.confReader.getDispositivo());
				} else {
					LOGGER.info(myDate.getFullDate() + " Reader Re-Connection Failed");
				}

			} else {
				LOGGER.info("READER CONNESSO");
				if (!this.confReader.getDispositivo().isStato()) {
					this.confReader.getDispositivo().setStato(true);
					tunnelService.aggiornaDispositivo(this.confReader.getDispositivo());
				}
			}
		} catch (Exception ex) {
			LOGGER.error(ex.toString() + " - " + ex.getMessage());
		}

	}
	
	
	
	
}
