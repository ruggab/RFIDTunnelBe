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
import net.mcsistemi.rfidtunnel.services.ReaderService;

public class JobWiramaReader implements Runnable {

	private Thread worker;
	
	private ReaderRfidWirama readerRfidWirama;

	Socket socketReader = null;

	boolean running = true;

	Logger logger = LoggerFactory.getLogger(JobWiramaReader.class);

	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
	ReaderService readerService = null;

	public JobWiramaReader(ReaderRfidWirama readerRfidWirama, ReaderService readerService) {
		
		this.readerRfidWirama = readerRfidWirama;
		this.readerService = readerService;
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
					readerService.createReaderStream(readerRfidWirama.getIpAdress(), readerRfidWirama.getPorta(), line, "", "", new Timestamp(System.currentTimeMillis()));
					logger.info("WIRAMA ---->>>>:" + line);
			}
		} catch (Exception e) {
			running = false;
		} finally {
			try {
				if (inBufferReader != null) {
					inBufferReader.close();
				}
				System.out.println("disconnecting from: " + readerRfidWirama.getIpAdress() + ":" + readerRfidWirama.getPorta());
				if (socketReader != null) {
					socketReader.close();
				}
				readerRfidWirama.setStato(running);
				readerService.save(readerRfidWirama);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public BufferedReader connectReader() throws Exception {
		BufferedReader in = null;
		// connect
		logger.info("Connecting to Reader Wirama: " + readerRfidWirama.getIpAdress() + ":" + readerRfidWirama.getPorta());
		try {
			socketReader = new Socket(readerRfidWirama.getIpAdress(), new Integer(readerRfidWirama.getPorta()));
			in = new BufferedReader(new InputStreamReader(socketReader.getInputStream()));
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
