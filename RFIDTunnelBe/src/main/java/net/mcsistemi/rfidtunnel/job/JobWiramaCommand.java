package net.mcsistemi.rfidtunnel.job;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

import net.mcsistemi.rfidtunnel.entity.ReaderRfidWirama;
import net.mcsistemi.rfidtunnel.listneroctane.TagReportListenerImplementation;
import net.mcsistemi.rfidtunnel.services.DispositivoService;
import net.mcsistemi.rfidtunnel.services.ReaderService;

public class JobWiramaCommand implements Runnable {

	private Thread worker;

	private ReaderRfidWirama readerRfidWirama;

	Socket socketCommand = null;
	boolean running = true;
	

	Logger LOGGER = Logger.getLogger(JobWiramaCommand.class);

	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
	ReaderService readerService = null;

	public JobWiramaCommand(ReaderRfidWirama readerRfidWirama, ReaderService readerService) {

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
		OutputStreamWriter out = null;
		running = true;
		try {
			in = connectSocket();
			while (running) {
				// osw =new OutputStreamWriter(socketCommand.getOutputStream(), "UTF-8");
			    //    osw.write(msg, 0, msg.length());
				out = new OutputStreamWriter(socketCommand.getOutputStream(), "UTF-8");
				out.append("get status");
				out.append("\n");
				out.flush();
				String line = in.readLine().toString();
				if (line.equals("ko")) {
					readerRfidWirama.setStato(false);
					readerService.save(readerRfidWirama);
					LOGGER.info("WIRAMA ---->>>>:" + line);
					running = false;
				}
				
			}
		} catch (Exception e) {
			running = false;
			// e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
				LOGGER.info("disconnecting from: " + readerRfidWirama.getIpAdress() + ":" + readerRfidWirama.getPortaComandi());
				socketCommand.close();
				readerRfidWirama.setStato(false);
				readerService.save(readerRfidWirama);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	

	public BufferedReader connectSocket() throws Exception {
		BufferedReader in = null;
		// connect
		LOGGER.info("Connecting to Reader Wirama: " + readerRfidWirama.getIpAdress() + ":" + readerRfidWirama.getPortaComandi());
		try {
			socketCommand = new Socket(readerRfidWirama.getIpAdress(), new Integer(readerRfidWirama.getPortaComandi()));
			in = new BufferedReader(new InputStreamReader(socketCommand.getInputStream()));
		} catch (UnknownHostException e) {
			LOGGER.error("Unknown host: " + readerRfidWirama.getIpAdress());
			throw e;
		} catch (IOException e) {
			LOGGER.error("Unable to get streams from Wirama");
			throw e;
		}
		return in;
	}
	


	public void stop() {

		LOGGER.info("Stop thread ip: " + readerRfidWirama.getIpAdress());
		try {
			running = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
