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
import net.mcsistemi.rfidtunnel.entity.ReaderRfidInpinj;
import net.mcsistemi.rfidtunnel.services.DispositivoService;
import net.mcsistemi.rfidtunnel.services.ReaderService;

public class KeepAliveListenerImplementation implements KeepaliveListener {

	private static final Logger LOGGER = LogManager.getLogger(RfidtunnelApplication.class);
	static net.mcsistemi.rfidtunnel.util.DateFunction myDate;
	private ReaderRfidInpinj readerRfidInpinj = null;
	private ReaderService readerService;

	public KeepAliveListenerImplementation(ReaderRfidInpinj readerRfidInpinj, ReaderService readerService) {
		this.readerRfidInpinj = readerRfidInpinj;
		this.readerService = readerService;
	}

	@Override
	public void onKeepalive(ImpinjReader reader, KeepaliveEvent e) {
		try {
			if (!reader.isConnected()) {
				LOGGER.info("READER NON CONNESSO");

				LOGGER.info(myDate.getFullDate() + " Reader try to Re-Connecting...........");
				// Connessione ed Attivazione LED su Tunnel
				reader.connect(readerRfidInpinj.getIpAdress());
				if (reader.isConnected()) {
					reader.setGpo(1, true);
					readerRfidInpinj.setStato(true);
					readerService.save(readerRfidInpinj);

					Status status = reader.queryStatus();

					LOGGER.info("Reader Temperature: " + status.getTemperatureCelsius());
					LOGGER.info("Singulating: " + status.getIsSingulating());
					LOGGER.info("Connected:" + status.getIsConnected());

					LOGGER.info("Antenna Status");
					for (AntennaStatus as : status.getAntennaStatusGroup().getAntennaList()) {
						LOGGER.info("  Antenna " + as.getPortNumber() + " status " + as.isConnected());
					}

					LOGGER.info("GPI Status");
					for (GpiStatus gs : status.getGpiStatusGroup().getGpiList()) {
						LOGGER.info("  GPI " + gs.getPortNumber() + " status " + gs.isState());
					}

					System.out.println("Antenna Hub Status");
					for (AntennaHubStatus ahs : status.getAntennaHubStatusGroup().getAntennaHubList()) {
						LOGGER.info("  Hub " + ahs.getHubId() + " connected " + ahs.getConnected() + " fault " + ahs.getFault());
					}

					if (status.getTiltSensorValue() != null) {
						LOGGER.info("Tilt:  x-" + status.getTiltSensorValue().getxAxis() + " y-" + status.getTiltSensorValue().getyAxis());
					}
					LOGGER.info(myDate.getFullDate() + " Reader Re-Connected");
				} else {
					LOGGER.info(myDate.getFullDate() + " Reader Re-Connection Failed");
				}

			} else {
				LOGGER.info("READER CONNESSO");
				ReaderRfidInpinj impinjConf = (ReaderRfidInpinj) readerService.getReaderById(readerRfidInpinj.getId());
				if (!impinjConf.getStato()) {
					impinjConf.setStato(true);
					readerService.save(impinjConf);
				}
				
				
				Status status = reader.queryStatus();

				LOGGER.info("Reader Temperature: " + status.getTemperatureCelsius());
				LOGGER.info("Singulating: " + status.getIsSingulating());
				LOGGER.info("Connected:" + status.getIsConnected());

				LOGGER.info("Antenna Status");
				for (AntennaStatus as : status.getAntennaStatusGroup().getAntennaList()) {
					LOGGER.info("  Antenna " + as.getPortNumber() + " status " + as.isConnected());
				}

				LOGGER.info("GPI Status");
				for (GpiStatus gs : status.getGpiStatusGroup().getGpiList()) {
					LOGGER.info("  GPI " + gs.getPortNumber() + " status " + gs.isState());
				}

				LOGGER.info("Antenna Hub Status");
				for (AntennaHubStatus ahs : status.getAntennaHubStatusGroup().getAntennaHubList()) {
					LOGGER.info("  Hub " + ahs.getHubId() + " connected " + ahs.getConnected() + " fault " + ahs.getFault());
				}

				if (status.getTiltSensorValue() != null) {
					LOGGER.info("Tilt:  x-" + status.getTiltSensorValue().getxAxis() + " y-" + status.getTiltSensorValue().getyAxis());
				}


	

			}
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
			ex.printStackTrace();
		}

	}
}
