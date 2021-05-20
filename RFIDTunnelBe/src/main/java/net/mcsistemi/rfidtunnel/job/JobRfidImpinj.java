package net.mcsistemi.rfidtunnel.job;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.impinj.octane.AntennaConfigGroup;
import com.impinj.octane.GpoConfigGroup;
import com.impinj.octane.GpoMode;
import com.impinj.octane.ImpinjReader;
import com.impinj.octane.MemoryBank;
import com.impinj.octane.OctaneSdkException;
import com.impinj.octane.ReportConfig;
import com.impinj.octane.ReportMode;
import com.impinj.octane.Settings;
import com.impinj.octane.TagReadOp;

import net.mcsistemi.rfidtunnel.db.entity.ConfAntenna;
import net.mcsistemi.rfidtunnel.db.entity.ConfPorta;
import net.mcsistemi.rfidtunnel.db.entity.ConfReader;
import net.mcsistemi.rfidtunnel.db.entity.Tunnel;
import net.mcsistemi.rfidtunnel.db.services.TunnelService;
import net.mcsistemi.rfidtunnel.listneroctane.ConnectionLostListenerImplement;
import net.mcsistemi.rfidtunnel.listneroctane.KeepAliveListenerImplementation;
import net.mcsistemi.rfidtunnel.listneroctane.TagOpCompleteListenerImplementation;
import net.mcsistemi.rfidtunnel.listneroctane.TagReportListenerImplementation;
import net.mcsistemi.rfidtunnel.util.DateFunction;
import net.mcsistemi.rfidtunnel.util.Utils;

/**
 * 
 * La Classe gestisce il Tunnel per il controllo dei pacchi
 * 
 * @version 1.0
 * @author Pietro Borrelli / Daniele Perrella
 * 
 */

public class JobRfidImpinj  implements JobInterface {
	Logger logger = Logger.getLogger(JobRfidImpinj.class);
	
	
	static DateFunction myDate = new DateFunction();
	private ImpinjReader reader = null;
	private Settings settings = null;
	
	private ConfReader confReader;
	private TunnelService tunnelService;
	
	
	public JobRfidImpinj(TunnelService tunnelService,  ConfReader confReader) throws Exception {

		// Istanzia l'oggetto Reader
		this.reader = new ImpinjReader();
		this.confReader = confReader;
		this.tunnelService = tunnelService;
		
		

		try {

			// Connessione  (Attivazione LED su Tunnel da stabilire)
			this.reader.connect(this.confReader.getDispositivo().getIpAdress());
			// Caricamento della Configurazione del Reader
			this.settings = reader.queryDefaultSettings();

			// Configurazione Reportistica TAG
			ReportConfig report = settings.getReport();
			report.setIncludeAntennaPortNumber(false); // Lista per Antenna=true, Lista Cumulata=false
			report.setMode(ReportMode.BatchAfterStop); // Modalita' di presentare l'inventario TAG
			// report.setMode(ReportMode.Individual); // Modalita' di presentare l'inventario TAG

			// The reader can be set into various modes in which reader
			// dynamics are optimized for specific regions and environments.
			// The following mode, AutoSetDenseReader, monitors RF noise and interference and then automatically
			// and continuously optimizes the reader configuration
			
			settings.setReaderMode(Utils.getReaderMode(confReader.getReaderMode()));
			settings.setSearchMode(Utils.getSearchMode(confReader.getSearchMode()));
			settings.setSession(confReader.getSession());
			// set some special settings for antenna 1

			AntennaConfigGroup antennas = settings.getAntennas();
			antennas.disableAll();
			List<ConfAntenna> listaAntenna = confReader.getAntennas();
			for (Iterator iterator = listaAntenna.iterator(); iterator.hasNext();) {
				ConfAntenna antenna = (ConfAntenna) iterator.next();
				if (antenna.isEnable()) {
					antennas.enableById(new short[] { antenna.getPosition().shortValue() });
					antennas.getAntenna(antenna.getPosition()).setIsMaxRxSensitivity(Boolean.valueOf(antenna.isMaxRxSensitivity()));
					antennas.getAntenna(antenna.getPosition()).setIsMaxTxPower(Boolean.valueOf(antenna.isMaxTxPower()));
					antennas.getAntenna(antenna.getPosition()).setTxPowerinDbm(Double.valueOf(antenna.getPowerinDbm()));
					antennas.getAntenna(antenna.getPosition()).setRxSensitivityinDbm(Double.valueOf(antenna.getSensitivityinDbm()));
				}
			}

			// Configurazione Listener Reader
			// reader.setReaderStartListener(new ReaderStartListenerImplementation());
			// reader.setReaderStopListener(new ReaderStopListenerImplementation());

			// Configurazione GPIO-INPUT
			// turn on these listeners to see how the GPI triggers work.
			// reader.setGpiChangeListener(new GpiChangeListenerImplementation());

			// enable this GPI and set some debounce
			
			// Start Port
			
			for (int i = 0; i < confReader.getDispositivo().getNumPortInput(); i++) {
				settings.getGpis().get(i).setIsEnabled(true);
				settings.getGpis().get(i).setDebounceInMs(confReader.getDebGpiPortStart());
				settings.getGpis().get(i).setPortNumber(i+1);
			}
			 
	        
	         
			
			if (confReader.getGpiPortStart() != null) {
				settings.getAutoStart().setMode(Utils.getAutoStartMode(confReader.getAutoStartMode()));
				settings.getAutoStart().setGpiPortNumber(confReader.getGpiPortStart());
				settings.getAutoStart().setGpiLevel(confReader.isStateGpiPortStart());
				
			}
			

			// Stop Port
			if (confReader.getGpiPortStop() != null) {
				settings.getAutoStop().setGpiPortNumber(confReader.getGpiPortStop());
				settings.getAutoStop().setGpiLevel(confReader.isStateGpiPortStop());
				settings.getAutoStop().setMode(Utils.getAutoStopMode(confReader.getAutoStopMode()));
				
			}

			// Maitenance port
			if (confReader.getGpiPortMaintenance() != null) {
			
			}
			
			GpoConfigGroup gpos = settings.getGpos();

			List<ConfPorta>  listConfPort = confReader.getPorts();
			//
			for (Iterator iterator = listConfPort.iterator(); iterator.hasNext();) {
				ConfPorta confPorta = (ConfPorta) iterator.next();
				gpos.getGpo(confPorta.getNumPorta().shortValue()).setMode(Utils.getGpoMode(confPorta.getIdPortMode()));
				if (Utils.getGpoMode(confPorta.getIdPortMode()) == GpoMode.Pulsed) {
					 gpos.getGpo(confPorta.getNumPorta().shortValue()).setGpoPulseDurationMsec(confPorta.getPulsedDurMls());
				}
			}
			
			

			if (confReader.isEnableUser()) {

				TagReadOp readUser = new TagReadOp();
				readUser.setMemoryBank(MemoryBank.User);
				readUser.setWordCount((short) 2);
				readUser.setWordPointer((short) 0);
				readUser.Id = new Short(111 + "");

				// reader the non-serialzed part of the TID (first 2 words)
				TagReadOp readTid = new TagReadOp();
				readTid.setMemoryBank(MemoryBank.Tid);
				readTid.setWordPointer((short) 0);
				readTid.setWordCount((short) 6);
				readTid.Id = new Short(222 + "");

				// add to the optimized read operations
				settings.getReport().getOptimizedReadOps().add(readUser);
				settings.getReport().getOptimizedReadOps().add(readTid);
				// set up listeners for user and TID also
				reader.setTagOpCompleteListener(new TagOpCompleteListenerImplementation(confReader, tunnelService));
			} else {
				// set up listeners just for EPC
				// reader.setAntennaChangeListener( new AntennaChangeListenerImplementation());
				ReportConfig r = settings.getReport();
				// tell the reader to include the antenna port number in the report
				r.setIncludeAntennaPortNumber(true);
				r.setIncludeFirstSeenTime(true);
				r.setIncludeChannel(true);
				r.setIncludeCrc(true);
				r.setIncludeDopplerFrequency(true);
				r.setIncludeFastId(true);
				r.setIncludeLastSeenTime(true);
				r.setIncludeLastSeenTime(true);
				r.setIncludePeakRssi(true);
				r.setIncludePhaseAngle(true);
				r.setIncludeSeenCount(true);
				settings.setReport(r);

				reader.setTagReportListener(new TagReportListenerImplementation(tunnelService, confReader));

			} 

			if (confReader.getKeepAlive()) {
				// Configurazione controllo KEEP ALIVE
				settings.getKeepalives().setPeriodInMs(5000); // Tempo di Attesa prima di attivare evento di
				settings.getKeepalives().setEnabled(true); // Abilita il Controllo Disconnessione
				reader.setKeepaliveListener(new KeepAliveListenerImplementation(confReader,tunnelService));
			}
			reader.setConnectionLostListener(new ConnectionLostListenerImplement(confReader,tunnelService));

			myDate.RefreshDate();
			

		} catch (Exception e) {
			this.stop();
			logger.error("READER IMPINJ FAILED CONFIGURATION !");
			throw new Exception("Reader IMPINJ FAILED CONFIGURATION - " + " - CAUSE: " + e.getCause() + " - MESSAGE: " + e.getMessage());
		}

	}

	

	public void stop() throws Exception {
		if (reader!= null) {
			//reader.stop();
			reader.disconnect();
			this.confReader.getDispositivo().setStato(false);
			this.tunnelService.aggiornaDispositivo(this.confReader.getDispositivo());
			logger.info("TUNNEL DISCONNECTED, NO OPERATION AVAILABLE !");
		}
		
	}

	@Override
	public void run() throws Exception {
		try {
			if (!reader.isConnected()) {
				this.reader.connect(this.confReader.getDispositivo().getIpAdress());
				logger.info(myDate.getFullDate() + " READER STARTING ........");
			} else {
				logger.info(" Reader Already Connected");
			}
			if (reader.isConnected()) {
				this.reader.applySettings(settings);
				
				logger.info(myDate.getFullDate() + " READER START SUCCESS");
				this.confReader.getDispositivo().setStato(true);
				this.tunnelService.aggiornaDispositivo(this.confReader.getDispositivo());
			} else {
				logger.info(myDate.getFullDate() + " READER START FAILDED");
				this.confReader.getDispositivo().setStato(false);
				this.tunnelService.aggiornaDispositivo(this.confReader.getDispositivo());
			}
		} catch (Exception e) {
			this.stop();
			logger.error("READER IMPINJ FAILED START !");
			throw new Exception("Reader IMPINJ FAILED START - " + " - CAUSE: " + e.getCause() + " - MESSAGE: " + e.getMessage());
		}
		
	}

	


}