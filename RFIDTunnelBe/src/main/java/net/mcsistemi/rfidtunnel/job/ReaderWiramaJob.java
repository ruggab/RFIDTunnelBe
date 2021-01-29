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

public class ReaderWiramaJob implements Runnable {

	private Thread worker;
	private String ip;
	private int port;

	Socket echoSocket = null;
	boolean running = true;

	Logger logger = LoggerFactory.getLogger(ReaderWiramaJob.class);

	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
	ReaderService readerService = null;

	public ReaderWiramaJob(ReaderRfidWirama confReaderWiramaJob, ReaderService readerService) {
		this.ip = confReaderWiramaJob.getIpAdress();
		this.port = new Integer(confReaderWiramaJob.getPorta());
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
				try {
					String line = null;

					try {
						line = in.readLine().toString();

						readerService.createReaderlog("", "", new Date(), line);
						logger.info(line);
					} catch (NullPointerException ex) {
						logger.info("Waiting for Wirama streams ... ");
						Thread.sleep(1000);
						in = connect();
						continue;
					}
				} catch (Exception e) {
					// e.printStackTrace();
				}
			}
		} catch (Exception e) {
			running = false;
			// e.printStackTrace();
		} finally {
			try {
				if (in != null)
					in.close();
				System.out.println("disconnecting from: " + ip + ":" + port);

				try {
					echoSocket.close();
				} catch (Exception e) {
					// e.printStackTrace();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public BufferedReader connect() {
		BufferedReader in = null;
		// connect
		logger.info("Connecting to Reader Wirama: " + ip + ":" + port);
		try {
			echoSocket = new Socket(ip, port);
			in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
		} catch (UnknownHostException e) {
			logger.error("Unknown host: " + ip);
		} catch (IOException e) {
			logger.error("Unable to get streams from Wirama");
		}
		return in;
	}

	public void stop() {

		System.out.println("stop thread ip " + ip);
		try {
			running = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
