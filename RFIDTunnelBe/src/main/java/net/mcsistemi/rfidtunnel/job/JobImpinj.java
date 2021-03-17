package net.mcsistemi.rfidtunnel.job;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;

import org.springframework.util.StringUtils;

import com.impinj.octane.AntennaConfigGroup;
import com.impinj.octane.AutoStartMode;
import com.impinj.octane.AutoStopMode;
import com.impinj.octane.BitPointers;
import com.impinj.octane.ImpinjReader;
import com.impinj.octane.MemoryBank;
import com.impinj.octane.OctaneSdkException;
import com.impinj.octane.ReaderMode;
import com.impinj.octane.ReportConfig;
import com.impinj.octane.ReportMode;
import com.impinj.octane.Settings;
import com.impinj.octane.TagFilter;
import com.impinj.octane.TagFilterMode;
import com.impinj.octane.TagFilterOp;
import com.impinj.octane.TagReadOp;

import net.mcsistemi.rfidtunnel.entity.Antenna;
import net.mcsistemi.rfidtunnel.entity.ReaderRfidInpinj;
import net.mcsistemi.rfidtunnel.listneroctane.ConnectionLostListenerImplement;
import net.mcsistemi.rfidtunnel.listneroctane.KeepAliveListenerImplementation;
import net.mcsistemi.rfidtunnel.listneroctane.ReaderStartListenerImplementation;
import net.mcsistemi.rfidtunnel.listneroctane.ReaderStopListenerImplementation;
import net.mcsistemi.rfidtunnel.listneroctane.TagOpCompleteListenerImplementation;
import net.mcsistemi.rfidtunnel.listneroctane.TagReportListenerImplementation;
import net.mcsistemi.rfidtunnel.services.ReaderService;
import net.mcsistemi.rfidtunnel.util.DateFunction;

/**
 * 
 * La Classe gestisce il Tunnel per il controllo dei pacchi
 * 
 * @version 1.0
 * @author Pietro Borrelli / Daniele Perrella
 * 
 */

public class JobImpinj implements JobImpinjInterface {

	static DateFunction myDate = new DateFunction();
	public static String PACKAGE_BARCODE = "";
	private ImpinjReader reader = null;
	private Settings settings = null;
	private String hostname = "";
	private ReaderRfidInpinj readerRfidInpinj = null;
	private ReaderService readerService = null;

	public JobImpinj(ReaderRfidInpinj readerRfidInpinj, ReaderService readerService) throws Exception {

		// Istanzia l'oggetto Reader
		this.reader = new ImpinjReader();
		this.readerRfidInpinj = readerRfidInpinj;
		this.readerService = readerService;

		try {
			// Redirect Standard Out
			if (readerRfidInpinj.isCreateOutFile()) {

				// Attribuisco nome al File di OUT con Riferimento a Data e Ora
				myDate.RefreshDate();
				myDate.setDateSeparator("-");
				myDate.setTimeSeparator("-");
				myDate.setDttmSeparator("_");
				FileOutputStream fout = new FileOutputStream(readerRfidInpinj.getNameOutLog() + myDate.getFullDate() + readerRfidInpinj.getExtentionOutLog());
				PrintStream out = new PrintStream(fout);
				System.setOut(out);
				myDate.setDefaul();
			}

			// Redirect Standard Err
			if (readerRfidInpinj.isCreateErrFile()) {

				myDate.RefreshDate();
				myDate.setDateSeparator("-");
				myDate.setTimeSeparator("-");
				myDate.setDttmSeparator("_");
				FileOutputStream ferr = new FileOutputStream(readerRfidInpinj.getNameErrLog() + myDate.getFullDate() + readerRfidInpinj.getExtentionErrLog());
				PrintStream err = new PrintStream(ferr);
				System.setErr(err);
				myDate.setDefaul();
			}
			// int portActivate = Integer.parseInt(readerRfidInpinj.getActivatePort());

			// DA SOSTITUIRE CON OPPORTUNA PARAMETRIZZAZIONE IN VISTA DEI THREAD
			this.hostname = readerRfidInpinj.getIpAdress(); // Indirizzo IP Reader
			// Connessione ed Attivazione LED su Tunnel
			this.reader.connect(hostname);
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

			switch (readerRfidInpinj.getReaderMode()) {
			case 1:
				settings.setReaderMode(ReaderMode.AutoSetDenseReader);
				break;
			case 2:
				settings.setReaderMode(ReaderMode.AutoSetCustom);
				break;
			case 3:
				settings.setReaderMode(ReaderMode.AutoSetDenseReaderDeepScan);
				break;
			case 4:
				settings.setReaderMode(ReaderMode.AutoSetStaticDRM);
				break;
			case 5:
				settings.setReaderMode(ReaderMode.AutoSetStaticFast);
				break;
			case 6:
				settings.setReaderMode(ReaderMode.DenseReaderM4);
				break;
			case 7:
				settings.setReaderMode(ReaderMode.DenseReaderM4Two);
				break;
			case 8:
				settings.setReaderMode(ReaderMode.DenseReaderM8);
				break;
			case 9:
				settings.setReaderMode(ReaderMode.Hybrid);
				break;
			case 10:
				settings.setReaderMode(ReaderMode.MaxMiller);
				break;
			case 11:
				settings.setReaderMode(ReaderMode.MaxThroughput);
				break;
			default:
				settings.setReaderMode(ReaderMode.AutoSetDenseReader);
				break;
			}

			// set some special settings for antenna 1

			AntennaConfigGroup antennas = settings.getAntennas();
			antennas.disableAll();
			List<Antenna> listaAntenna = readerRfidInpinj.getListAntenna();
			for (Iterator iterator = listaAntenna.iterator(); iterator.hasNext();) {
				Antenna antenna = (Antenna) iterator.next();
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
			settings.getGpis().get(0).setIsEnabled(readerRfidInpinj.isPortaIn1());
			settings.getGpis().get(0).setPortNumber(1);
			settings.getGpis().get(0).setDebounceInMs(50); // Gestione debounce in millisecondi
			//
			settings.getGpis().get(1).setIsEnabled(readerRfidInpinj.isPortaIn2());
			settings.getGpis().get(1).setPortNumber(2);
			settings.getGpis().get(1).setDebounceInMs(50);
			//
			settings.getGpis().get(2).setIsEnabled(readerRfidInpinj.isPortaIn3());
			settings.getGpis().get(2).setPortNumber(3);
			settings.getGpis().get(2).setDebounceInMs(50);
			//
			settings.getGpis().get(3).setIsEnabled(readerRfidInpinj.isPortaIn4());
			settings.getGpis().get(3).setPortNumber(4);
			settings.getGpis().get(3).setDebounceInMs(50);
			//

			// set autostart to go on GPI level
			switch (readerRfidInpinj.getAutoStartMode()) {
			case 1:
				settings.getAutoStart().setMode(AutoStartMode.GpiTrigger);
				break;
			case 2:
				settings.getAutoStart().setMode(AutoStartMode.Immediate);
				break;
			case 3:
				settings.getAutoStart().setMode(AutoStartMode.Periodic);
				break;
			default:
				settings.getAutoStart().setMode(AutoStartMode.GpiTrigger);
				break;
			}

			settings.getAutoStart().setGpiPortNumber(readerRfidInpinj.getNumPortaAutostart());
			settings.getAutoStart().setGpiLevel(readerRfidInpinj.isAutoStartActive());

			switch (readerRfidInpinj.getAutoStopMode()) {
			case 1:
				settings.getAutoStop().setMode(AutoStopMode.GpiTrigger);
				break;
			case 2:
				settings.getAutoStop().setMode(AutoStopMode.Duration);
				break;
			case 3:
				settings.getAutoStop().setMode(AutoStopMode.None);
				break;
			default:
				settings.getAutoStop().setMode(AutoStopMode.GpiTrigger);
				break;
			}
			// if you set start, you have to set stop
			settings.getAutoStop().setGpiPortNumber(readerRfidInpinj.getNumPortaAutostart());
			settings.getAutoStop().setGpiLevel(!readerRfidInpinj.isAutoStartActive());
			//
			reader.setGpo(1, readerRfidInpinj.isPortaOut1());
			reader.setGpo(2, readerRfidInpinj.isPortaOut2());
			reader.setGpo(3, readerRfidInpinj.isPortaOut3());
			reader.setGpo(4, readerRfidInpinj.isPortaOut4());

			if (readerRfidInpinj.isEnableUserTid()) {

				TagReadOp readUser = new TagReadOp();
				readUser.setMemoryBank(MemoryBank.User);
				readUser.setWordCount((short) 2);
				readUser.setWordPointer((short) 0);
				readUser.Id = new Short(readerRfidInpinj.getIdUser() + "");

				// reader the non-serialzed part of the TID (first 2 words)
				TagReadOp readTid = new TagReadOp();
				readTid.setMemoryBank(MemoryBank.Tid);
				readTid.setWordPointer((short) 0);
				readTid.setWordCount((short) 6);
				readTid.Id = new Short(readerRfidInpinj.getIdTid() + "");

				// add to the optimized read operations
				settings.getReport().getOptimizedReadOps().add(readUser);
				settings.getReport().getOptimizedReadOps().add(readTid);
				// set up listeners for user and TID also
				reader.setTagOpCompleteListener(new TagOpCompleteListenerImplementation(readerRfidInpinj, this.readerService));
			} else {
				// set up listeners just for EPC
				reader.setTagReportListener(new TagReportListenerImplementation(this.readerService));
			}

			if (readerRfidInpinj.getKeepAlive()) {
				// Configurazione controllo KEEP ALIVE
				settings.getKeepalives().setPeriodInMs(5000); // Tempo di Attesa prima di attivare evento di
				settings.getKeepalives().setEnabled(true); // Abilita il Controllo Disconnessione
				reader.setKeepaliveListener(new KeepAliveListenerImplementation(readerRfidInpinj, this.readerService));
			}
			reader.setConnectionLostListener(new ConnectionLostListenerImplement(readerRfidInpinj, this.readerService));

			myDate.RefreshDate();
			System.out.println(myDate.getFullDate() + " READER STARTING ........");

		} catch (Exception e) {
			this.reader.stop();
			reader.disconnect();
			throw e;
		}

	}

	public void start() throws OctaneSdkException {
		if (!reader.isConnected()) {
			this.reader.connect(hostname);
			System.out.println(" Reader Already RE - Connected");
		} else {
			System.out.println(" Reader Already Connected");
		}
		if (reader.isConnected()) {
			this.reader.applySettings(settings);
			// Switch all led 0
			this.reader.setGpo(1, false);
			this.reader.setGpo(2, false);
			this.reader.setGpo(3, false);
			this.reader.setGpo(4, false);
			// Switch ON led 1
			if (readerRfidInpinj.isPortaOut1()) {
				reader.setGpo(1, true);
			}
			reader.start();
			System.out.println(myDate.getFullDate() + " Reader Start Success");
		} else {
			System.out.println(myDate.getFullDate() + " Reader Start Failed");
		}

	}

	public void stop() throws OctaneSdkException {
		reader.setGpo(1, false);
		reader.setGpo(2, false);
		reader.setGpo(3, false);
		reader.setGpo(4, false);
		reader.stop();
		reader.disconnect();
		System.out.println("TUNNEL DISCONNECTED, NO OPERATION AVAILABLE !");
	}

}