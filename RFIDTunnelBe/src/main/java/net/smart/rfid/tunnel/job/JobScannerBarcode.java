package net.smart.rfid.tunnel.job;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import org.apache.log4j.Logger;
import org.hamcrest.number.IsCloseTo;

import net.smart.rfid.tunnel.db.entity.Dispositivo;
import net.smart.rfid.tunnel.db.entity.ScannerStream;
import net.smart.rfid.tunnel.db.entity.Tunnel;
import net.smart.rfid.tunnel.db.services.TunnelService;

public class JobScannerBarcode implements Runnable, JobInterface {

	Logger logger = Logger.getLogger(JobScannerBarcode.class);

	private Dispositivo dispositivo;
	private Tunnel tunnel;
	private TunnelService tunnelSevice;

	Socket echoSocket = null;
	boolean running = true;

	public JobScannerBarcode(Tunnel tunnel, TunnelService tunnelSevice, Dispositivo dispositivo) throws UnknownHostException, IOException {
		this.tunnel = tunnel;
		this.dispositivo = dispositivo;
		this.tunnelSevice = tunnelSevice;
		echoSocket = new Socket(this.dispositivo.getIpAdress(), this.dispositivo.getPorta().intValue());
		echoSocket.close();
	}

	// @Override
	public void run() {
		BufferedReader in = null;
		running = true;
		try {
			in = connect();
			String packageData = "";
			String stream = "";
			// START BARCODE
			dispositivo.setStato(running);
			tunnelSevice.aggiornaDispositivo(dispositivo);
			while (running) {
				try {
					stream = in.readLine().toString();
				} catch (NullPointerException e) {
					logger.info("Waiting for JobScannerBarcode streams ... ");
					Thread.sleep(1000);
					in = connect();
					continue;
				}
				packageData = packageData + stream;
				if (packageData.contains(tunnel.getMsgEnd())) {
					packageData = packageData.substring(0, packageData.indexOf(tunnel.getMsgEnd()));
					logger.info("****************");
					logger.info(packageData);
					logger.info("****************");
					// SE il package è noread
					if (packageData.equals(tunnel.getMsgNoRead())) {
						packageData = tunnel.getMsgNoRead() + "-" + tunnelSevice.getSeqNextVal();
					} 
					tunnelSevice.createScannerStream(tunnel.getId(), packageData, "N");
					packageData = "";
					stream = "";
				} else {
					continue;
				}
				logger.info("STREAM received " + stream);
			}
		} catch (Exception e) {
			try {
				running = false;
				if (in != null) {
					in.close();
					logger.info("disconnecting from: " + this.dispositivo.getIpAdress() + ":" + this.dispositivo.getPorta());
				}
				if (echoSocket != null && !echoSocket.isClosed()) {
					echoSocket.close();
				}
			} catch (IOException e1) {
				logger.error(e1.toString() + " - " + e1.getMessage());
			}
		}
	}

	public BufferedReader connect() {
		BufferedReader in = null;
		// connect
		logger.info("connecting to: " + this.dispositivo.getIpAdress() + ":" + this.dispositivo.getPorta());
		// logger.info("repository count (JobScannerBarcode): "+repository.count());

		try {
			echoSocket = new Socket(this.dispositivo.getIpAdress(), this.dispositivo.getPorta().intValue());
			in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));

		} catch (UnknownHostException e) {
			logger.error("Unknown host: " + this.dispositivo.getIpAdress());
			stop();
		} catch (IOException e) {
			logger.error("Connection error to BARCODE " + this.dispositivo.getIpAdress() + " port: " + this.dispositivo.getPorta().intValue());
			stop();
		} catch (Exception e) {
			logger.error("Connection error to BARCODE " + this.dispositivo.getIpAdress() + " port: " + this.dispositivo.getPorta().intValue());
			stop();
		}

		return in;
	}

	@Override
	public void stop() {
		
		try {
			running = false;
			if (echoSocket != null && !echoSocket.isClosed()) {
				logger.info("Closed  Socket Scannere Barcode");
				echoSocket.close();
			}
			dispositivo.setStato(running);
			tunnelSevice.aggiornaDispositivo(dispositivo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
