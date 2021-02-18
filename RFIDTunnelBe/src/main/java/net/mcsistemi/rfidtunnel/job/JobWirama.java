package net.mcsistemi.rfidtunnel.job;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.mcsistemi.rfidtunnel.entity.ReaderRfidWirama;
import net.mcsistemi.rfidtunnel.entity.TunnelLog;
import net.mcsistemi.rfidtunnel.repository.TunnelLogRepository;
import net.mcsistemi.rfidtunnel.services.ReaderService;

public class JobWirama implements Runnable {

	private Thread worker;
	
	private ReaderRfidWirama readerRfidWirama;

	Socket echoSocket = null;
	boolean running = true;

	Logger logger = LoggerFactory.getLogger(JobWirama.class);

	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
	ReaderService readerService = null;

	public JobWirama(ReaderRfidWirama readerRfidWirama, ReaderService readerService) {
		
		this.readerRfidWirama = readerRfidWirama;
		this.readerService = readerService;
	}

	public void start() {
		worker = new Thread(this);
		worker.start();
	}

	// @Override
	public void run() {
		BufferedReader in = null;
		running = true;
		try {
			in = connect();
			while (running) {
					String line = in.readLine().toString();
					readerService.createReaderlog(readerRfidWirama.getIpAdress(), readerRfidWirama.getPorta(), new Date(), line);
					logger.info("WIRAMA ---->>>>:" + line);
			}
		} catch (Exception e) {
			running = false;
			// e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				System.out.println("disconnecting from: " + readerRfidWirama.getIpAdress() + ":" + readerRfidWirama.getPorta());
				echoSocket.close();
				readerRfidWirama.setStato(false);
				readerService.save(readerRfidWirama);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public BufferedReader connect() throws Exception {
		BufferedReader in = null;
		// connect
		logger.info("Connecting to Reader Wirama: " + readerRfidWirama.getIpAdress() + ":" + readerRfidWirama.getPorta());
		try {
			echoSocket = new Socket(readerRfidWirama.getIpAdress(), new Integer(readerRfidWirama.getPorta()));
			in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
		} catch (UnknownHostException e) {
			logger.error("Unknown host: " + readerRfidWirama.getIpAdress());
			throw e;
		} catch (IOException e) {
			logger.error("Unable to get streams from Wirama");
			throw e;
		}
		return in;
	}

	public void stop() {

		System.out.println("Stop thread ip: " + readerRfidWirama.getIpAdress());
		try {
			running = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
