package net.mcsistemi.rfidtunnel.job;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

import net.mcsistemi.rfidtunnel.db.entity.ConfReader;
import net.mcsistemi.rfidtunnel.db.services.TunnelService;

public class JobRfidWirama implements Runnable, JobInterface {

	private Thread worker;
	
	private ConfReader confReader;

	Socket socketReader = null;

	boolean running = true;

	private static final Logger LOGGER = Logger.getLogger(JobRfidWirama.class);

	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
	TunnelService tunnelService = null;

	public JobRfidWirama(ConfReader confReader, TunnelService tunnelService) {
		this.confReader = confReader;
		this.tunnelService = tunnelService;
	}

	public void start() {
		worker = new Thread(this);
		worker.start();
	}

	// @Override
	public void run() {
		BufferedReader inBufferReader = null;
		running = true;
		try {
			inBufferReader = connectReader();
			while (running) {
					String line = inBufferReader.readLine().toString();
					//tunnelService.create line,);
					LOGGER.info("WIRAMA ---->>>>:" + line);
			}
		} catch (Exception e) {
			running = false;
		} finally {
			try {
				if (inBufferReader != null) {
					inBufferReader.close();
				}
				System.out.println("disconnecting from: " + confReader.getDispositivo().getIpAdress() + ":" + confReader.getDispositivo().getPorta());
				if (socketReader != null) {
					socketReader.close();
				}
				this.confReader.getDispositivo().setStato(running);
				this.tunnelService.aggiornaDispositivo(this.confReader.getDispositivo());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public BufferedReader connectReader() throws Exception {
		BufferedReader in = null;
		// connect
		LOGGER.info("Connecting to Reader Wirama: " + confReader.getDispositivo().getIpAdress()  + ":" + confReader.getDispositivo().getPorta());
		try {
			socketReader = new Socket(confReader.getDispositivo().getIpAdress() ,  confReader.getDispositivo().getPorta().intValue());
			in = new BufferedReader(new InputStreamReader(socketReader.getInputStream()));
		} catch (UnknownHostException e) {
			LOGGER.error("Unknown host: " + confReader.getDispositivo().getIpAdress() );
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
