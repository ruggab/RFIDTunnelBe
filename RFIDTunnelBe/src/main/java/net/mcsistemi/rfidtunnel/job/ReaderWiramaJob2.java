package net.mcsistemi.rfidtunnel.job;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import net.mcsistemi.rfidtunnel.model.Item;
import net.mcsistemi.rfidtunnel.util.SGTIN96;

public class ReaderWiramaJob2 implements Runnable {

	private String ip;
	private int port;

	public static int packCount = 0;

	Socket echoSocket = null;
	boolean running = true;

	public static List<String> tags = new ArrayList<String>();
	public static String PACKAGE_BARCODE2 = "";

	public ReaderWiramaJob2(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	// @Override
	public void run() {

		BufferedReader in = null;
		running = true;

		try {

			// connect
			System.out.println("Connecting to: " + ip + ":" + port);

			// notify
			// new Thread(new CallNotify("Connecting to: "+ip+":"+port+" *** WIRAMA TUNNEL")).start();

			try {
				echoSocket = new Socket(ip, port);
				in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
			} catch (UnknownHostException e) {
				System.err.println("Unknown host: " + ip);
			} catch (IOException e) {
				System.err.println("Unable to get streams from server");
			}

			System.out.println(running);

			// read
			String id = "";
			String op = "";
			String xx = "";
			String pack = "";
			String epc = "";

			String packageid = "";
			while (running) {

				try {

					// System.out.println(in.readLine());
					String[] readed = in.readLine().split(" ");

					op = readed[1];
					xx = readed[0];

					if (xx.equalsIgnoreCase("LOT_stop"))
						op = "LOT_stop";

					switch (op) {
					case "LOT_start":
						tags.clear();
						pack = readed[2];
						packageid = PACKAGE_BARCODE2;
						break;
					case "LOT":
						epc = readed[3];
						System.out.println(packCount + " NEW EPC: " + readed[3] + " Pack: " + pack);

						// notify
						// new Thread(new CallNotify(packCount+" NEW EPC: " + readed[3] + " Pack: "+pack)).start();

						packCount++;
						tags.add(readed[3]);
						break;

					case "LOT_stop":

						// write scanner
						System.out.println("LOT_stop...");
						// Write Scanner Detail...
						ArrayList<Item> r = new ArrayList<Item>();
						r.clear();
						for (String tag : tags) {
							try {
								r.add(new Item(tag, SGTIN96.decodeEpc(tag)));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						//TunnelRFID.scan.insertDetail(TunnelRFID.conn, r, packageid);
						// new Thread(new CallNotify("Package from Tunnel: "+packageid+". "+TunnelRFID.tags.size()+"
						// Items","L")).start();

						//TunnelRFID.idscan = -1;
						//TunnelRFID.idpack = -1;
						//TunnelRFID.type = "";
//						TunnelRFID.PACKAGE_BARCODE2 = "";
//						packageid = "";
//						TunnelRFID.items = null;

						break;
					}

				} catch (Exception e) {
					// e.printStackTrace();
				}
			}

		} catch (Exception e) {
			running = false;
			e.printStackTrace();
		} finally {

			try {
				if (in != null)
					in.close();
				System.out.println("disconnecting from: " + ip + ":" + port);

				try {
					echoSocket.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
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
