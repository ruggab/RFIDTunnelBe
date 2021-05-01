package net.mcsistemi.rfidtunnel.job2;

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.impinj.octane.AntennaConfigGroup;
import com.impinj.octane.AutoStartMode;
import com.impinj.octane.AutoStopMode;
import com.impinj.octane.GpoConfigGroup;
import com.impinj.octane.GpoMode;
import com.impinj.octane.ImpinjReader;
import com.impinj.octane.MemoryBank;
import com.impinj.octane.OctaneSdkException;
import com.impinj.octane.ReaderMode;
import com.impinj.octane.ReportConfig;
import com.impinj.octane.ReportMode;
import com.impinj.octane.SearchMode;
import com.impinj.octane.Settings;
import com.impinj.octane.TagReadOp;

import net.mcsistemi.rfidtunnel.entity.ConfAntenna;
import net.mcsistemi.rfidtunnel.entity.ConfPorta;
import net.mcsistemi.rfidtunnel.entity.ConfReader;
import net.mcsistemi.rfidtunnel.entity.Dispositivo;
import net.mcsistemi.rfidtunnel.entity.Tunnel;
import net.mcsistemi.rfidtunnel.listneroctane.ConnectionLostListenerImplement;
import net.mcsistemi.rfidtunnel.listneroctane.KeepAliveListenerImplementation;
import net.mcsistemi.rfidtunnel.listneroctane.TagOpCompleteListenerImplementation;
import net.mcsistemi.rfidtunnel.listneroctane.TagReportListenerImplementation;
import net.mcsistemi.rfidtunnel.services.TunnelService;
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

public class JobRfidImpinj extends Job implements JobImpinjInterface {
	Logger logger = LoggerFactory.getLogger(JobRfidImpinj.class);
	
	static DateFunction myDate = new DateFunction();
	private ImpinjReader reader = null;
	private Settings settings = null;
	
	private ConfReader confReader;
	
	public JobRfidImpinj(TunnelJob tunnelJob,  ConfReader confReader) throws Exception {

		// Istanzia l'oggetto Reader
		this.reader = new ImpinjReader();
		this.confReader = confReader;
		

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
			confReader.getDispositivo().getNumPortInput();
			// Start Port
			if (!StringUtils.isEmpty(confReader.getGpiPortStart())) {
				settings.getGpis().get(confReader.getGpiPortStart()).setIsEnabled(true);
				settings.getGpis().get(confReader.getGpiPortStart()).setPortNumber(confReader.getGpiPortStart());
				settings.getGpis().get(confReader.getGpiPortStart()).setDebounceInMs(confReader.getDebGpiPortStart());
			}
			settings.getAutoStart().setMode(Utils.getAutoStartMode(confReader.getAutoStartMode()));
			settings.getAutoStart().setGpiPortNumber(confReader.getGpiPortStart());
			settings.getAutoStart().setGpiLevel(confReader.isStateGpiPortStart());

			// Stop Port
			if (!StringUtils.isEmpty(confReader.getGpiPortStop())) {
				settings.getGpis().get(confReader.getGpiPortStop()).setIsEnabled(true);
				settings.getGpis().get(confReader.getGpiPortStop()).setPortNumber(confReader.getGpiPortStop());
				settings.getGpis().get(confReader.getGpiPortStop()).setDebounceInMs(confReader.getDebGpiPortStop());
			}
			// if you set start, you have to set stop
			settings.getAutoStop().setGpiPortNumber(confReader.getGpiPortStop());
			settings.getAutoStop().setGpiLevel(confReader.isStateGpiPortStop());
			settings.getAutoStop().setMode(Utils.getAutoStopMode(confReader.getAutoStopMode()));
			// settings.getAutoStop().setTimeout(60000);

			// Maitenance port
			if (!StringUtils.isEmpty(confReader.getGpiPortMaintenance())) {
				settings.getGpis().get(confReader.getGpiPortMaintenance()).setIsEnabled(true);
				settings.getGpis().get(confReader.getGpiPortMaintenance()).setPortNumber(confReader.getGpiPortMaintenance());
				settings.getGpis().get(confReader.getGpiPortMaintenance()).setDebounceInMs(confReader.getDebGpiPortMaintenance());
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
				reader.setTagOpCompleteListener(new TagOpCompleteListenerImplementation(confReader, tunnelJob.getTunnelService()));
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

				reader.setTagReportListener(new TagReportListenerImplementation(tunnelJob));

			} 

			if (confReader.getKeepAlive()) {
				// Configurazione controllo KEEP ALIVE
				settings.getKeepalives().setPeriodInMs(5000); // Tempo di Attesa prima di attivare evento di
				settings.getKeepalives().setEnabled(true); // Abilita il Controllo Disconnessione
				reader.setKeepaliveListener(new KeepAliveListenerImplementation(confReader,tunnelJob.getTunnelService()));
			}
			reader.setConnectionLostListener(new ConnectionLostListenerImplement(confReader,tunnelJob.getTunnelService()));

			myDate.RefreshDate();
			logger.info(myDate.getFullDate() + " READER STARTING ........");

		} catch (Exception e) {
			this.reader.stop();
			reader.disconnect();
			throw e;
		}

	}

	public void start() throws OctaneSdkException {
		if (!reader.isConnected()) {
			this.reader.connect(this.confReader.getDispositivo().getIpAdress());
			logger.info(" Reader Already RE - Connected");
		} else {
			System.out.println(" Reader Already Connected");
		}
		if (reader.isConnected()) {
			this.reader.applySettings(settings);
			// Switch all led 0
		
			reader.start();
			logger.info(myDate.getFullDate() + " Reader Start Success");
		} else {
			logger.info(myDate.getFullDate() + " Reader Start Failed");
		}

	}

	public void stop() throws OctaneSdkException {
		if (reader!= null) {
			reader.stop();
			reader.disconnect();
			logger.info("TUNNEL DISCONNECTED, NO OPERATION AVAILABLE !");
		}
		
	}

	


}