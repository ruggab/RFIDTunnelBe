package net.mcsistemi.rfidtunnel.job;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import com.impinj.octane.AntennaConfigGroup;
import com.impinj.octane.AutoStartMode;
import com.impinj.octane.AutoStopMode;
import com.impinj.octane.ImpinjReader;
import com.impinj.octane.ReaderMode;
import com.impinj.octane.ReportConfig;
import com.impinj.octane.ReportMode;
import com.impinj.octane.Settings;

import net.mcsistemi.rfidtunnel.util.DateFunction;



/**
 * 
 * La Classe gestisce il Tunnel per il controllo dei pacchi
 * 
 * @version 1.0
 * @author Pietro Borrelli / Daniele Perrella
 * 
 */

public class CheckBox {
	static DateFunction myDate;
	private static Thread barcodeServer = null;
	private static Thread wms = null;
	public static String PACKAGE_BARCODE = "";

	//public static Packages pack = null;
	//public static Impinj scan = null;

	public static Connection conn = null;

	public static void main(String[] args) throws SQLException {

		myDate = new DateFunction();

		try {

			// Leggo file di Configurazione
			ReadProperties prop = new ReadProperties("CheckBox.properties");

			// Redirect Standard Out
			if (prop.getKey("createOutFile").equals("Y")) {

				// Attribuisco nome al File di OUT con Riferimento a Data e Ora
				myDate.RefreshDate();
				myDate.setDateSeparator("-");
				myDate.setTimeSeparator("-");
				myDate.setDttmSeparator("_");
				FileOutputStream fout = new FileOutputStream(prop.getKey("nameOutLog") + myDate.getFullDate() + prop.getKey("extentionOutLog"));
				PrintStream out = new PrintStream(fout);
				System.setOut(out);
				myDate.setDefaul();
			}

			// Redirect Standard Err
			if (prop.getKey("createErrFile").equals("Y")) {

				myDate.RefreshDate();
				myDate.setDateSeparator("-");
				myDate.setTimeSeparator("-");
				myDate.setDttmSeparator("_");
				FileOutputStream ferr = new FileOutputStream(prop.getKey("nameErrLog") + myDate.getFullDate() + prop.getKey("extentionErrLog"));
				PrintStream err = new PrintStream(ferr);
				System.setErr(err);
				myDate.setDefaul();
			}

			int portActivate = Integer.parseInt(prop.getKey("activate_port"));

			// DA SOSTITUIRE CON OPPORTUNA PARAMETRIZZAZIONE IN VISTA DEI THREAD
			String hostname = prop.getKey("IPAddress").trim(); // Indirizzo IP Reader

			// Istanzia l'oggetto Reader
			ImpinjReader reader = new ImpinjReader();

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

			if (prop.getKey("Antenna_1").equals("1")) {
				antennas.enableById(new short[] { 1 });
				antennas.getAntenna(1).setIsMaxRxSensitivity(Boolean.valueOf(prop.getKey("setIsMaxRxSensitivity_1")));
				antennas.getAntenna(1).setIsMaxTxPower(Boolean.valueOf(prop.getKey("setIsMaxRxSensitivity_1")));
				antennas.getAntenna(1).setTxPowerinDbm(Double.valueOf(prop.getKey("setTxPowerinDbm_1")));
				antennas.getAntenna(1).setRxSensitivityinDbm(Double.valueOf(prop.getKey("setRxSensitivityinDbm_1")));
			}

			if (prop.getKey("Antenna_2").equals("1")) {
				antennas.enableById(new short[] { 2 });
				antennas.getAntenna(2).setIsMaxRxSensitivity(Boolean.valueOf(prop.getKey("setIsMaxRxSensitivity_2")));
				antennas.getAntenna(2).setIsMaxTxPower(Boolean.valueOf(prop.getKey("setIsMaxRxSensitivity_2")));
				antennas.getAntenna(2).setTxPowerinDbm(Double.valueOf(prop.getKey("setTxPowerinDbm_2")));
				antennas.getAntenna(2).setRxSensitivityinDbm(Double.valueOf(prop.getKey("setRxSensitivityinDbm_2")));
			}

			if (prop.getKey("Antenna_3").equals("1")) {
				antennas.enableById(new short[] { 3 });
				antennas.getAntenna(3).setIsMaxRxSensitivity(Boolean.valueOf(prop.getKey("setIsMaxRxSensitivity_3")));
				antennas.getAntenna(3).setIsMaxTxPower(Boolean.valueOf(prop.getKey("setIsMaxRxSensitivity_3")));
				antennas.getAntenna(3).setTxPowerinDbm(Double.valueOf(prop.getKey("setTxPowerinDbm_3")));
				antennas.getAntenna(3).setRxSensitivityinDbm(Double.valueOf(prop.getKey("setRxSensitivityinDbm_3")));
			}

			if (prop.getKey("Antenna_4").equals("1")) {
				antennas.enableById(new short[] { 4 });
				antennas.getAntenna(4).setIsMaxRxSensitivity(Boolean.valueOf(prop.getKey("setIsMaxRxSensitivity_4")));
				antennas.getAntenna(4).setIsMaxTxPower(Boolean.valueOf(prop.getKey("setIsMaxRxSensitivity_4")));
				antennas.getAntenna(4).setTxPowerinDbm(Double.valueOf(prop.getKey("setTxPowerinDbm_4")));
				antennas.getAntenna(4).setRxSensitivityinDbm(Double.valueOf(prop.getKey("setRxSensitivityinDbm_4")));
			}

			// Configurazione Listener Lettura TAG
			reader.setTagReportListener(new TagReportListenerImplementation(prop));

			// Configurazione Listener Reader
			// reader.setReaderStartListener(new ReaderStartListenerImplementation());
			// reader.setReaderStopListener(new ReaderStopListenerImplementation());

			// Configurazione GPIO-INPUT
			// turn on these listeners to see how the GPI triggers work.
			reader.setGpiChangeListener(new GpiChangeListenerImplementation());

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
				if (!prop.getKey("online_port").equals(""))
					reader.setGpo(Integer.parseInt(prop.getKey("online_port")), true);
			}

			myDate.RefreshDate();
			System.out.println(myDate.getFullDate() + " READER STARTING ........");
			// reader.start();

			// ====================================================
			// CONNECTION
			conn = Utility.getConn();

			// ====================================================
			// PACKAGES & SCANNER
			pack = new Packages();
			scan = new Impinj();

			// ====================================================
			// BARCODE SERVER
			barcodeServer = new Thread(new BarcodeServer());
			barcodeServer.start();

			// wms = new Thread(new WMS());
			// wms.start();

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