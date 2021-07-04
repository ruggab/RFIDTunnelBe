package net.smart.rfid.tunnel.job;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

public class JobRfidWirama implements Runnable, JobInterface {

	private ConfReader confReader;
	private Tunnel tunnel;

	Socket socketReader = null;

	boolean running = true;

	private static final Logger LOGGER = Logger.getLogger(JobRfidWirama.class);

	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
	TunnelService tunnelService = null;

	public JobRfidWirama(ConfReader confreader, TunnelService tunnelService) {
		this.confReader = confreader;
		this.tunnelService = tunnelService;
	}

	// @Override
	public void run() {
		BufferedReader inBufferReader = null;
		running = true;
		try {
			confReader.getDispositivo().setStato(running);
			this.tunnelService.aggiornaDispositivo(confReader.getDispositivo());
			String START = "GPIO_i 40";
			String STOP = "GPIO_i 04";
			if (confReader.isEnableLotsep()) {
				START = "LOT_start";
				STOP = "LOT_stop";
			}
			inBufferReader = connectReader();
			List<TagWirama> tags = null;
			HashMap<String, String> aa = null;
			while (running) {
				String line = inBufferReader.readLine().toString();
				// NEW
				if (confReader.isEnableNew()) {
					LOGGER.info("LINE = " + line);
					if (line.indexOf(START)!=-1) {
						tags = new ArrayList<TagWirama>();
					}
					if (line.indexOf("new")!=-1) {
						TagWirama tag = new TagWirama();
						String[] lineArray = line.split("\\ ");
						int indNew = Arrays.asList(lineArray).indexOf("new");
						String epc = lineArray[indNew+1];
						String sku = "";
						if (confReader.isEnableSku()) {
							sku = SGTIN96.decodeEpc(epc);
						}
						LOGGER.info("WIRAMA EPC = " + epc);
						LOGGER.info("WIRAMA SKU = " + sku);
						tag.setEpc(confReader.isEnableEpc() ? epc : "");
						tag.setSku(sku);
						tags.add(tag);
						// LOGGER.info("WIRAMA Other = " + lineArray[2]);
					}
					if (line.indexOf(STOP)!=-1) {
						this.tunnelService.gestioneStreamWirama(confReader, tags);
					}
				}
				// ROW
				if (confReader.isEnableRaw()) {
					LOGGER.info("LINE = " + line);
					if (line.indexOf(START)!=-1) {
						tags = new ArrayList<TagWirama>();
						aa = new HashMap<String, String>();
					}
					if (line.indexOf("raw")!=-1) {
						String[] lineArray = line.split("\\ ");
						int indRaw = Arrays.asList(lineArray).indexOf("raw");
						String epc = lineArray[indRaw+1];
						aa.put(epc, SGTIN96.decodeEpc(epc));
					}
					if (line.indexOf(STOP)!=-1) {
						for (Map.Entry<String, String> entry : aa.entrySet()) {
							TagWirama tag = new TagWirama();
							tag.setEpc(confReader.isEnableEpc() ? entry.getKey() : "");
							tag.setSku(confReader.isEnableSku() ? entry.getValue() : "");
							LOGGER.info("WIRAMA EPC = " + tag.getEpc());
							LOGGER.info("WIRAMA SKU = " + tag.getSku());
							tags.add(tag);
						}
						this.tunnelService.gestioneStreamWirama(confReader, tags);
					}
				}
			}
		} catch (Exception e) {
			running = false;
		} finally {
			try {
				if (inBufferReader != null) {
					inBufferReader.close();
				}
				LOGGER.info("disconnecting from: " + confReader.getDispositivo().getIpAdress() + ":" + confReader.getDispositivo().getPorta());
				if (socketReader != null) {
					socketReader.close();
				}
				confReader.getDispositivo().setStato(running);
				this.tunnelService.aggiornaDispositivo(confReader.getDispositivo());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public BufferedReader connectReader() throws Exception {
		BufferedReader in = null;
		// connect
		LOGGER.info("Connecting to Reader Wirama: " + confReader.getDispositivo().getIpAdress() + ":" + confReader.getDispositivo().getPorta());
		try {
			socketReader = new Socket(confReader.getDispositivo().getIpAdress(), confReader.getDispositivo().getPorta().intValue());
			in = new BufferedReader(new InputStreamReader(socketReader.getInputStream()));
		} catch (UnknownHostException e) {
			LOGGER.error("Unknown host: " + confReader.getDispositivo().getIpAdress());
			throw e;
		} catch (IOException e) {
			LOGGER.error("Unable to get streams from Wirama");
			throw e;
		}
		return in;
	}

	public void stop() {

		LOGGER.info("Stop thread ip: " + confReader.getDispositivo().getIpAdress());
		try {
			running = false;
			this.confReader.getDispositivo().setStato(running);
			this.tunnelService.aggiornaDispositivo(confReader.getDispositivo());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
