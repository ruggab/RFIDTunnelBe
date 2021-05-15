package net.mcsistemi.rfidtunnel.job;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import net.mcsistemi.rfidtunnel.db.entity.Dispositivo;
import net.mcsistemi.rfidtunnel.db.entity.Tunnel;
import net.mcsistemi.rfidtunnel.db.services.TunnelService;

public class JobScannerBarcode    implements Runnable, JobInterface {

	Logger logger = Logger.getLogger(JobScannerBarcode.class);

	
	private Dispositivo dispositivo;
	private Tunnel tunnel;
	private TunnelService tunnelSevice;


	Socket echoSocket = null;
	boolean running = true;

	public JobScannerBarcode(Tunnel tunnel, TunnelService tunnelSevice,  Dispositivo dispositivo) {
		this.tunnel = tunnel;
		this.dispositivo = dispositivo;
		this.tunnelSevice = tunnelSevice;
	}

	// @Override
	public void run() {

		BufferedReader in = null;
		running = true;

		try {
			in = connect();
			String packId = "";
			String stream = "";
			//START BARCODE
			dispositivo.setStato(running);
			tunnelSevice.aggiornaDispositivo(dispositivo);
			while (running) {

				

				try {
					stream = in.readLine().toString();

				} catch (NullPointerException e) {
					// e.printStackTrace();
					
					logger.info("Waiting for JobScannerBarcode streams ... ");
					Thread.sleep(1000);
					in = connect();
					continue;
				}
				
				packId = packId + stream;
				if (packId.contains(tunnel.getMsgEnd())) {
					
					packId = packId.substring(0, packId.indexOf(tunnel.getMsgEnd()));
					
					logger.info("****************");
					logger.info(packId);
					logger.info("****************");
					//SE il package è noread
					if (packId.equals(tunnel.getMsgNoRead())) {
						packId = tunnel.getMsgNoRead() + "-" + tunnelSevice.getSeqNextVal();
					} else {
						tunnelSevice.createScannerStream(tunnel.getId(), packId, "N");
						packId = "";
						stream = "";
					}
					
				} else {
					continue;
				}


				logger.info("STREAM received " + stream);

			}
			//Stop BARCODE
			//dispositivo.setStato(false);
			//tunnelSevice.aggiornaDispositivo(dispositivo);
		} catch (Exception e) {
			running = false;
		} finally {
			try {
				if (in != null) in.close();
				logger.info("disconnecting from: " + this.dispositivo.getIpAdress() + ":" + this.dispositivo.getPorta());

				try {
					if (echoSocket != null) echoSocket.close();
				} catch (Exception e) {
					logger.error(e.toString() + " - " + e.getMessage());
				}
			} catch (IOException e) {
				logger.error(e.toString() + " - " + e.getMessage());
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
			running = false;
			logger.error("Unknown host: " + this.dispositivo.getIpAdress());
		} catch (IOException e) {
			logger.error("Connection error to BARCODE " + this.dispositivo.getIpAdress() + " port: " +  this.dispositivo.getPorta().intValue());
			running = false;
		} catch (Exception e) {
			logger.error("Connection error to BARCODE " + this.dispositivo.getIpAdress() + " port: " +  this.dispositivo.getPorta().intValue());
			running = false;
		}

		return in;
	}

	

	@Override
	public void stop() {
		logger.info("Try to close Socket...");
		try {
			running = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
}
