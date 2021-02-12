package net.mcsistemi.rfidtunnel.job;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.springframework.util.StringUtils;

import com.impinj.octane.AntennaConfigGroup;
import com.impinj.octane.AutoStartMode;
import com.impinj.octane.AutoStopMode;
import com.impinj.octane.ImpinjReader;
import com.impinj.octane.ReaderMode;
import com.impinj.octane.ReportConfig;
import com.impinj.octane.ReportMode;
import com.impinj.octane.Settings;

import net.mcsistemi.rfidtunnel.entity.Antenna;
import net.mcsistemi.rfidtunnel.entity.ReaderRfidInpinj;
import net.mcsistemi.rfidtunnel.entity.ReaderRfidWirama;
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

public class ReaderImpinjJob {
	static DateFunction myDate;
	public static String PACKAGE_BARCODE = "";

	//public static Packages pack = null;
	//public static Impinj scan = null;
	private ImpinjReader reader = null;

	public ReaderImpinjJob(ReaderRfidInpinj readerRfidInpinj, ReaderService readerService) {
	
		// Istanzia l'oggetto Reader
		reader = new ImpinjReader();

		myDate = new DateFunction();

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
			int portActivate = Integer.parseInt(readerRfidInpinj.getActivatePort());

			// DA SOSTITUIRE CON OPPORTUNA PARAMETRIZZAZIONE IN VISTA DEI THREAD
			String hostname = readerRfidInpinj.getIpAdress(); // Indirizzo IP Reader

			
			// Connessione ed Attivazione LED su Tunnel
			reader.connect(hostname);

			// Caricamento della Configurazione del Reader
			Settings settings = reader.queryDefaultSettings();

			// Configurazione Reportistica TAG
			ReportConfig report = settings.getReport();
			report.setIncludeAntennaPortNumber(false); // Lista per Antenna=true, Lista Cumulata=false
			report.setMode(ReportMode.BatchAfterStop); // Modalita' di presentare l'inventario TAG
			// report.setMode(ReportMode.Individual); // Modalita' di presentare l'inventario TAG

			// The reader can be set into various modes in which reader
			// dynamics are optimized for specific regions and environments.
			// The following mode, AutoSetDenseReader, monitors RF noise and interference and then automatically
			// and continuously optimizes the reader configuration

			settings.setReaderMode(ReaderMode.AutoSetDenseReader);

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
			
			// Configurazione Listener Lettura TAG
			//reader.setTagReportListener(new TagReportListenerImplementation(prop));

			// Configurazione Listener Reader
			// reader.setReaderStartListener(new ReaderStartListenerImplementation());
			// reader.setReaderStopListener(new ReaderStopListenerImplementation());

			// Configurazione GPIO-INPUT
			// turn on these listeners to see how the GPI triggers work.
			//reader.setGpiChangeListener(new GpiChangeListenerImplementation());

			// enable this GPI and set some debounce
			settings.getGpis().get(1).setIsEnabled(true);
			settings.getGpis().get(1).setPortNumber(portActivate);
			settings.getGpis().get(1).setDebounceInMs(50); // Gestione debounce in millisecondi

			// set autostart to go on GPI level
			settings.getAutoStart().setGpiPortNumber(portActivate);
			settings.getAutoStart().setMode(AutoStartMode.GpiTrigger);
			settings.getAutoStart().setGpiLevel(true);

			// if you set start, you have to set stop
			settings.getAutoStop().setMode(AutoStopMode.GpiTrigger);
			settings.getAutoStop().setGpiPortNumber(portActivate);
			settings.getAutoStop().setGpiLevel(false);

			// settings.getAutoStop().setTimeout(6000);

			// Configurazione controllo KEEP ALIVE
			// settings.getKeepalives().setPeriodInMs(5000); // Tempo di Attesa prima di attivare evento di
			// disconnessione
			// settings.getKeepalives().setEnabled(true); // Abilita il Controllo Disconnessione

			// Configurazione controllo ON CONNECTION LOST
			// reader.setConnectionLostListener(new ConnectionLostListenerImplement(prop));
			// reader.setKeepaliveListener(new KeepAliveListenerImplementation(prop));

			reader.applySettings(settings);

			if (reader.isConnected()) {
				if (!StringUtils.isEmpty(readerRfidInpinj.getOnlinePort()))
					reader.setGpo(Integer.parseInt(readerRfidInpinj.getOnlinePort()), true);
			}

			myDate.RefreshDate();
			System.out.println(myDate.getFullDate() + " READER STARTING ........");
			// reader.start();

			// ====================================================
			// CONNECTION
			//conn = Utility.getConn();

			// ====================================================
			// PACKAGES & SCANNER
			//pack = new Packages();
			//scan = new Impinj();

			// ====================================================
			// BARCODE SERVER
			//barcodeServer = new Thread(new BarcodeServer());
			//barcodeServer.start();

			// wms = new Thread(new WMS());
			// wms.start();

		}
		
		public void start() {
				
		}
			
			String in = "";
			Scanner key = new Scanner(System.in);

			System.out.println("Avviato Servizio Tunnel .........");

			while (!in.equals("quit")) {
				System.out.println("Enter a command <h> for help.......");
				in = key.nextLine();
				switch (in) {
				case "h":
					System.out.println("Command List available for CheckBox Tunnel ver. 1.1");
					System.out.println(" h       for help");
					System.out.println(" stop    disconnect tunnel RFID");
					System.out.println(" start   connect tunnel RFID");
					System.out.println(" status  tunnel RFID status");
					System.out.println(" stats   extracts statistics data");
					System.out.println(" quit       exit program");
					break;
				case "start":
					if (!reader.isConnected()) {

						reader.connect(hostname);
						reader.applySettings(settings);

						if (reader.isConnected()) {
							// Switch Off all LED
							reader.setGpo(Integer.parseInt(prop.getKey("online_port")), false);
							reader.setGpo(Integer.parseInt(prop.getKey("green_port")), false);
							reader.setGpo(Integer.parseInt(prop.getKey("yellow_port")), false);
							reader.setGpo(Integer.parseInt(prop.getKey("red_port")), false);

							reader.setGpo(Integer.parseInt(prop.getKey("online_port")), true);
							System.out.println(myDate.getFullDate() + " Reader Re-Start Success..........");
						} else
							System.out.println(myDate.getFullDate() + " Reader Re-Start Failed...........");
					} else
						System.out.println(" Reader Already Started...........");
					break;
				case "stop":
					if (reader.isConnected()) {
						reader.setGpo(Integer.parseInt(prop.getKey("online_port")), false);
						reader.setGpo(Integer.parseInt(prop.getKey("green_port")), false);
						reader.setGpo(Integer.parseInt(prop.getKey("yellow_port")), false);
						reader.setGpo(Integer.parseInt(prop.getKey("red_port")), false);

						reader.disconnect();
						System.out.println("TUNNEL DISCONNECTED, NO OPERATION AVAILABLE !");
					}
					break;
				case "status":
					if (reader.isConnected())
						System.out.println("TUNNEL READY, OPERATIONS AVAILABLE !");
					else
						System.out.println("TUNNEL NOT READY, NO OPERATIONS AVAILABLE !");

					break;
				case "stats":
					int anno = 0;
					int mese = 0;
					boolean check = true;
					Scanner stat = new Scanner(System.in);
					try {
						System.out.println("Inserire Anno: ");
						anno = stat.nextInt();
					} catch (Exception ex) {
						check = false;
					}

					if (check) {
						try {
							System.out.println("Inserire Mese: ");
							mese = stat.nextInt();
						} catch (Exception ex) {
							check = false;
						}
					}

					if (check) {
						System.out.println("Anno: " + anno + " Mese: " + mese);
						// Estrazione dati Statistici
						System.out.println("Estrazione in esecuzione........");
						WriterDB scriviSA = new WriterDB();
						scriviSA.Statistics(anno, mese);
						System.out.println("Estrazione effettuata.");
					} else
						System.out.println("Errore nella richiesta..........");
					break;

				case "quit":
					break;

				default:
					System.out.println("comando <" + in + "> sconosciuto.....");
					break;
				}
			}

			key.close();

			myDate.RefreshDate();
			System.out.println(myDate.getFullDate() + " READER ENDING ........");

			// reader.stop();
			reader.disconnect();

		} catch (Exception ex) {
			System.err.println(ex.getMessage());
			conn.close();
		}
		System.exit(0);
	}
}