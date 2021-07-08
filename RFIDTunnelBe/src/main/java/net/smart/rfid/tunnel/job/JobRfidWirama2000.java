package net.smart.rfid.tunnel.job;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.impinj.octane.Tag;

import net.smart.rfid.tunnel.db.entity.ConfReader;
import net.smart.rfid.tunnel.db.entity.Dispositivo;
import net.smart.rfid.tunnel.db.entity.Tunnel;
import net.smart.rfid.tunnel.db.services.TunnelService;
import net.smart.rfid.tunnel.model.TagWirama;
import net.smart.rfid.tunnel.util.SGTIN96;

public class JobRfidWirama2000 implements Runnable {


	Socket socketReader = null;
	boolean running = true;
	private static final Logger LOGGER = Logger.getLogger(JobRfidWirama2000.class);
	
	private String ip;
	private int port;
	
	PrintWriter pw = null;
	public JobRfidWirama2000(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}
	//Run Method
	public void run() {
			BufferedReader inBufferReader = null;
			//
			running = true;
			try {
				
				String START = "Inventory Started";
				String STOP = "Inventory Stopped";
				
				inBufferReader = connectReader();
				List<String> epcList= null;
				//Richiesta ping ogni 60 sec
				pw.println("PING 60");
	            pw.flush();
				
				while (running) {
					//Readeng Line Stream
					String line = inBufferReader.readLine().toString();
					//Verifico se lo stream contiene la risposta del ping, se la contiene rispondiamo con PING_ACK
					if (line.indexOf("PING") !=-1) {
						pw.println("PING_ACK");
			            pw.flush();
			            LOGGER.info("PING = " + line);
					}
					//Verifico se lo stream contiene il messaggio di start per la lettura degli epc
					if (line.indexOf(START)!=-1) {
						epcList = new ArrayList<String>();
					}
					//Gli EPC sono contenuti in uno stream che contiene la stringa NEW 
					if (line.toUpperCase().indexOf("NEW")!=-1) {
						//Carico in un array tutte le word separate da uno spazio, la word successiva alla word NEW è l'EPC
						String[] lineArray = line.split("\\ ");
						List<String> listLine = Arrays.asList(lineArray);
						int indNew = listLine.indexOf("NEW");
						//Estrapolo l'EPC
						String epc = lineArray[indNew+1];
						LOGGER.info("WIRAMA EPC = " + epc);
						//Carico gli EPC in un array
						epcList.add(epc);
					}
					//Verifico se arriva il messaggio di STOP lettura Stream, ciò significa che tutti gli EPC sono stati letti e sono contenuti nell'array epcList
					if (line.indexOf(STOP)!=-1) {
						//Manage array of epc (STAMPO tutti gli EPC letti)
						for (String epc : epcList) {
							LOGGER.info(epc);
						}
						
					}
					//fine while si attende nuva line STREAM
					
				}
				
				 
				
			//GEstone eccezione 
				
			} catch (Exception e) {
				try {
					running = false;
					if (inBufferReader != null) {
						inBufferReader.close();
					}
					LOGGER.info("disconnecting from reader");
					if (socketReader != null) {
						socketReader.close();
					}
					stop();
				} catch (Exception e1) {
					LOGGER.error(e1.getMessage());
				}
			}
		}



		//metodo che stabilisce la connessione al socket
		public BufferedReader connectReader() throws Exception {
			BufferedReader in = null;
			try {
				socketReader = new Socket(ip, port);
				in = new BufferedReader(new InputStreamReader(socketReader.getInputStream()));
				pw = new PrintWriter(socketReader.getOutputStream(), true);
			} catch (UnknownHostException e) {
				LOGGER.error("Unknown host: "+ ip);
				throw e;
			} catch (IOException e) {
				LOGGER.error("Unable to get streams from Wirama");
				throw e;
			}
			return in;
		}

		//Stop Exsternal command
		public void stop() {
			try {
				running = false;
				if (socketReader != null && !socketReader.isClosed()) {
					LOGGER.info("Closed  Socket Reader Wirama ip: xxx.xxx.xxx.xxx");
					socketReader.close();
				}
			
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
}
