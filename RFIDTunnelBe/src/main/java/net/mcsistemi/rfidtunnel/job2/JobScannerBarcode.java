package net.mcsistemi.rfidtunnel.job2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.mcsistemi.rfidtunnel.entity.Dispositivo;
import net.mcsistemi.rfidtunnel.entity.ScannerStream;
import net.mcsistemi.rfidtunnel.entity.Tunnel;
//import net.mcsistemi.rfidtunnel.job.TunnelJob;
import net.mcsistemi.rfidtunnel.repository.ScannerStreamRepository;
import net.mcsistemi.rfidtunnel.services.TunnelService;
//import net.mcsistemi.rfidtunnel.repository.TunnelLogRepository;

public class JobScannerBarcode implements Runnable {

	Logger logger = LoggerFactory.getLogger(JobScannerBarcode.class);
	
	private Tunnel tunnel; 
	private Dispositivo dispositivo;
	private TunnelService tunnelService;
	

	Socket echoSocket = null;
	boolean running = true;

	

	public JobScannerBarcode(Tunnel tunnel, Dispositivo dispositivo, TunnelService tunnelService) {
		this.tunnel = tunnel;
		this.dispositivo = dispositivo;
		this.tunnelService = tunnelService;
	}

	// @Override
	public void run() {

		BufferedReader in = null;
		running = true;

		try {
			in = connect();

			while (running) {

				String packId = null;

				try {
					packId = in.readLine().toString();

					if (packId.equals(tunnel.getMsgNoRead())) {
						// Ping
					} else {
						//logRep.save(new TunnelLog(new Date(), packId));
					}
				} catch (NullPointerException ex) {
					logger.info("Waiting for JobScannerBarcode streams ... ");
					Thread.sleep(1000);
					in = connect();
					continue;
				}

				// TunnelJob.packId = packId;

				try {

					if (!packId.equals(tunnel.getMsgNoRead())) {
						TunnelJob.packId = packId;

						// delete old wirama / scanner
						//repository.deleteWiramaByPackId(packId);
						//repository.deleteScannerByPackId(packId);

						System.out.println("****************");
						System.out.println(packId);
						System.out.println("****************");

						ScannerStream ss = new ScannerStream();
						ss.setPackId(packId);
						ss.setTimeStamp(new Date());

						// repository.save(new ScannerStream(packId, new Date()));
						//repository.save(ss);
					}
				} catch (Exception e) {
					// e.printStackTrace();
					System.out.println(e.getMessage());
				}

				logger.info("Barcode received " + packId);

			}
		} catch (Exception e) {
			running = false;
			// e.printStackTrace();
		} finally {
			try {
				if (in != null)
					in.close();
				System.out.println("disconnecting from: " + this.dispositivo.getIpAdress() + ":" + this.dispositivo.getPorta());

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
		logger.info("connecting to: "+ this.dispositivo.getIpAdress() + ":" + this.dispositivo.getPorta());
		// logger.info("repository count (JobScannerBarcode): "+repository.count());

		try {
			echoSocket = new Socket(this.dispositivo.getIpAdress(), this.dispositivo.getPorta().intValue());
			in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
		} catch (UnknownHostException e) {
			logger.error("Unknown host: " + this.dispositivo.getIpAdress());
		} catch (IOException e) {
			logger.error("Unable to get streams from JobScannerBarcode");
		}

		return in;
	}

	public void closeSocket() {
		System.out.println("Try to close Socket...");
		try {
			running = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
