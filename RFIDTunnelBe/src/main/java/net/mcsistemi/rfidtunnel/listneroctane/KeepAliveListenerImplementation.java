package net.mcsistemi.rfidtunnel.listneroctane;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.impinj.octane.AntennaHubStatus;
import com.impinj.octane.AntennaStatus;
import com.impinj.octane.GpiStatus;
import com.impinj.octane.ImpinjReader;
import com.impinj.octane.KeepaliveEvent;
import com.impinj.octane.KeepaliveListener;
import com.impinj.octane.Status;

import net.mcsistemi.rfidtunnel.RfidtunnelApplication;
import net.mcsistemi.rfidtunnel.entity.ConfReader;
import net.mcsistemi.rfidtunnel.entity.Dispositivo;
import net.mcsistemi.rfidtunnel.entity.ReaderRfidInpinj;
import net.mcsistemi.rfidtunnel.services.ConfReaderService;
import net.mcsistemi.rfidtunnel.services.DispositivoService;
import net.mcsistemi.rfidtunnel.services.ReaderService;
import net.mcsistemi.rfidtunnel.services.TunnelService;

public class KeepAliveListenerImplementation implements KeepaliveListener {

	private static final Logger LOGGER = LogManager.getLogger(KeepAliveListenerImplementation.class);
	static net.mcsistemi.rfidtunnel.util.DateFunction myDate;
	private ReaderRfidInpinj readerRfidInpinj = null;
	private ReaderService readerService;
	
	private ConfReader confReader = null;
	private TunnelService tunnelService;
	


	public KeepAliveListenerImplementation(ReaderRfidInpinj readerRfidInpinj, ReaderService readerService) {
		this.readerRfidInpinj = readerRfidInpinj;
		this.readerService = readerService;
	}
	
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
				if (!this.confReader.getDispositivo().getStato()) {
					this.confReader.getDispositivo().setStato(true);
					tunnelService.aggiornaDispositivo(this.confReader.getDispositivo());
				}
			}
		} catch (Exception ex) {
			LOGGER.error(ex.toString() + " - " + ex.getMessage());
		}

	}
	
	
	
	
}
