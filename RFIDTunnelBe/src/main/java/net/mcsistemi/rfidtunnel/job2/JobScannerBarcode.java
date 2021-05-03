package net.mcsistemi.rfidtunnel.job2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import net.mcsistemi.rfidtunnel.entity.Dispositivo;

public class JobScannerBarcode extends Job implements Runnable {

	Logger logger = Logger.getLogger(JobScannerBarcode.class);

	private TunnelJob tunnelJob;
	private Dispositivo dispositivo;


	Socket echoSocket = null;
	boolean running = true;

	public JobScannerBarcode(TunnelJob tunnelJob, Dispositivo dispositivo) {
		this.tunnelJob = tunnelJob;
		this.dispositivo = dispositivo;
	
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

					if (packId.equals(tunnelJob.getTunnel().getMsgNoRead())) {
						packId = tunnelJob.getTunnel().getMsgNoRead() + "-" + tunnelJob.getTunnelService().getSeqNextVal();
					} else {
						// logRep.save(new TunnelLog(new Date(), packId));
					}
				} catch (NullPointerException ex) {
					logger.info("Waiting for JobScannerBarcode streams ... ");
					Thread.sleep(1000);
					in = connect();
					continue;
				}

				// TunnelJob.packId = packId;

				try {

					if (!packId.equals(tunnelJob.getTunnel().getMsgNoRead())) {
						tunnelJob.setPackId(packId);

						// delete old wirama / scanner
						// repository.deleteWiramaByPackId(packId);
						// repository.deleteScannerByPackId(packId);

						logger.info("****************");
						logger.info(packId);
						logger.info("****************");

						

						tunnelJob.getTunnelService().createScannerStream(tunnelJob.getTunnel().getId(), packId);
					}
				} catch (Exception e) {
					// e.printStackTrace();
					logger.error(e.toString() + " - " + e.getMessage());
				}

				logger.info("Barcode received " + packId);

			}
		} catch (Exception e) {
			running = false;
		} finally {
			try {
				if (in != null)
					in.close();
				logger.info("disconnecting from: " + this.dispositivo.getIpAdress() + ":" + this.dispositivo.getPorta());

				try {
					echoSocket.close();
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
