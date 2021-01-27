package net.mcsistemi.rfidtunnel.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.mcsistemi.rfidtunnel.entity.TunnelLog;
import net.mcsistemi.rfidtunnel.entity.WiramaStream;
import net.mcsistemi.rfidtunnel.job.TunnelJob;
import net.mcsistemi.rfidtunnel.repository.TunnelLogRepository;
import net.mcsistemi.rfidtunnel.repository.WiramaStreamRepository;

public class Wirama implements Runnable {

	private String ip;
	private int port;
	private WiramaStreamRepository repository;
	private String path;
	private TunnelLogRepository logRep;

	Socket echoSocket = null;
	boolean running = true;

	Logger logger = LoggerFactory.getLogger(Wirama.class);

	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");

	public Wirama(String ip, int port, WiramaStreamRepository repository, String path, TunnelLogRepository logRep) {
		this.ip = ip;
		this.port = port;
		this.repository = repository;
		this.path = path;
		this.logRep = logRep;
	}

	// @Override
	public void run() {

		BufferedReader in = null;
		running = true;

		try {
			in = connect();

			String op = "";
			String xx = "";
			List<WiramaStream> ws = new ArrayList<WiramaStream>();

			while (running) {
				try {
					String line = null;

					try {
						line = in.readLine().toString();

						logRep.save(new TunnelLog(new Date(), line));

					} catch (NullPointerException ex) {
						logger.info("Waiting for Wirama streams ... ");
						Thread.sleep(1000);
						in = connect();
						continue;
					}

					// logger.info("Wirama read "+line);
					String[] readed = line.split(" ");

					xx = readed[0];
					try {
						op = readed[1];
					} catch (Exception e) {
					}

					if (xx.equalsIgnoreCase("LOT_stop"))
						op = "LOT_stop";

					switch (op) {
					case "LOT_start":

						logger.info("packId (Wirama): " + TunnelJob.packId);
						if (TunnelJob.packId == null)
							TunnelJob.packId = "???????????????";
						ws.clear();

						break;
					case "LOT":

						// logger.info(readed[3]);
						ws.add(new WiramaStream(TunnelJob.packId, readed[3], new Date()));

						break;
					case "LOT_stop":

						logger.info("Pack Count " + ws.size());

						repository.saveAll(ws);

						String fileName = path + TunnelJob.packId + ".txt";
						// Write CSV File
						if (TunnelJob.packId == null) {
							fileName = path + "No_Barcode_" + sdf.format(new Date()) + ".txt";
						}

						PrintWriter writer = new PrintWriter(fileName, "UTF-8");
						for (WiramaStream s : ws) {
							writer.println(s.getEpc());
						}
						writer.close();

						TunnelJob.packId = null;
						ws.clear();

						break;
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
		logger.info("connecting to: " + ip + ":" + port);
		// logger.info("repository count (Scanner): "+repository.count());

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

	public void closeSocket() {

		System.out.println("Try to close Socket...");
		try {
			running = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
