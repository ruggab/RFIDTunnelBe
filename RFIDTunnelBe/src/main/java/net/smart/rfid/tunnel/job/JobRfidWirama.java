package net.smart.rfid.tunnel.job;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;

import com.impinj.octane.Tag;

import net.smart.rfid.tunnel.db.entity.ConfReader;
import net.smart.rfid.tunnel.db.entity.Dispositivo;
import net.smart.rfid.tunnel.db.entity.Tunnel;
import net.smart.rfid.tunnel.db.services.TunnelService;
import net.smart.rfid.tunnel.model.TagWirama;
import net.smart.rfid.tunnel.util.SGTIN96;

public class JobRfidWirama implements Runnable, JobInterface {

	private Thread worker;
	
	private Dispositivo dispositivo;
	private Tunnel tunnel;

	Socket socketReader = null;

	boolean running = true;

	private static final Logger LOGGER = Logger.getLogger(JobRfidWirama.class);

	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
	TunnelService tunnelService = null;

	public JobRfidWirama(Dispositivo dispositivo,Tunnel tunnel, TunnelService tunnelService) {
		this.tunnel = tunnel;
		this.dispositivo = dispositivo;
		this.tunnelService = tunnelService;
	}


	// @Override
	public void run() {
		BufferedReader inBufferReader = null;
		running = true;
		try {
			this.dispositivo.setStato(running);
			this.tunnelService.aggiornaDispositivo(this.dispositivo);
			inBufferReader = connectReader();
			List<TagWirama> tags = null;
			while (running) {
					String line = inBufferReader.readLine().toString();
					LOGGER.info("WIRAMA EPC = " + line);
					if (line.contains("GPIO_i 40")) {
						 tags = new ArrayList<TagWirama>();
					}
					if (line.contains("NEW")) {
						TagWirama tag = new TagWirama();
						String[] lineArray = line.split("\\ ");
						String epc = lineArray[2];
						String sku = SGTIN96.decodeEpc(epc);
						//aa.put(epc, SGTIN96.decodeEpc(epc));
						//tunnelService.create line,);
						LOGGER.info("WIRAMA EPC = " + epc);
						LOGGER.info("WIRAMA SKU = " + sku);
						tag.setEpc(epc);
						tag.setSku(sku);
						tags.add(tag);
						//LOGGER.info("WIRAMA Other = " + lineArray[2]);
					}
					if (line.contains("GPIO_i 04")) {
						this.tunnelService.gestioneStreamWirama(tunnel.getId(), this.dispositivo.getIpAdress(), tags);
					}
			}
		} catch (Exception e) {
			running = false;
		} finally {
			try {
				if (inBufferReader != null) {
					inBufferReader.close();
				}
				System.out.println("disconnecting from: " + dispositivo.getIpAdress() + ":" + dispositivo.getPorta());
				if (socketReader != null) {
					socketReader.close();
				}
				this.dispositivo.setStato(running);
				this.tunnelService.aggiornaDispositivo(dispositivo);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public BufferedReader connectReader() throws Exception {
		BufferedReader in = null;
		// connect
		LOGGER.info("Connecting to Reader Wirama: " + dispositivo.getIpAdress()  + ":" + dispositivo.getPorta());
		try {
			socketReader = new Socket(dispositivo.getIpAdress() ,  dispositivo.getPorta().intValue());
			in = new BufferedReader(new InputStreamReader(socketReader.getInputStream()));
		} catch (UnknownHostException e) {
			LOGGER.error("Unknown host: " + dispositivo.getIpAdress() );
			throw e;
		} catch (IOException e) {
			LOGGER.error("Unable to get streams from Wirama");
			throw e;
		}
		return in;
	}

	public void stop() {

		LOGGER.info("Stop thread ip: " + dispositivo.getIpAdress());
		try {
			running = false;
			this.dispositivo.setStato(running);
			this.tunnelService.aggiornaDispositivo(dispositivo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
