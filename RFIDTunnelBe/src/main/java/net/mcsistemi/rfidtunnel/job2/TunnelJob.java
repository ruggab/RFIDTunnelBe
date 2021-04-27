package net.mcsistemi.rfidtunnel.job2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import net.mcsistemi.rfidtunnel.entity.ConfReader;
import net.mcsistemi.rfidtunnel.entity.Dispositivo;
import net.mcsistemi.rfidtunnel.entity.ScannerStream;
import net.mcsistemi.rfidtunnel.entity.Tunnel;
import net.mcsistemi.rfidtunnel.repository.PropertiesRepository;
import net.mcsistemi.rfidtunnel.repository.ScannerStreamRepository;
import net.mcsistemi.rfidtunnel.services.TunnelService;

public class TunnelJob {

	Logger logger = LoggerFactory.getLogger(TunnelJob.class);

	// @Autowired
	// private ScannerStreamRepository scannerRep;

	// @Autowired
	// private WiramaStreamRepository wiramaRep;
	//
	// @Autowired
	// private TunnelLogRepository logRep;

	public static String packId = null; // <-- JobScannerBarcode

	//
	private Tunnel tunnel;
	private List<ConfReader> listReaderImpinj;
	private List<ConfReader> listReaderWirama;
	private List<Dispositivo> listBarcode;
	private TunnelService tunnelService;

	public TunnelJob(Tunnel tunnel, List<ConfReader> listReaderImpinj, List<ConfReader> listReaderWirama, List<Dispositivo> listBarcode, TunnelService tunnelService) {
		this.tunnel = tunnel;
		this.listReaderImpinj = listReaderImpinj;
		this.listReaderWirama = listReaderWirama;
		this.listBarcode = listBarcode;
		this.tunnelService = tunnelService;
	}

	public void startTunnel() {
		try {
			for (Iterator iterator = this.listBarcode.iterator(); iterator.hasNext();) {
				Dispositivo dispositivo = (Dispositivo) iterator.next();
				JobScannerBarcode scanner = new JobScannerBarcode(this.tunnel, dispositivo, tunnelService);
				Thread scannerThread = new Thread(scanner);
				scannerThread.start();
			}

			for (Iterator iterator = this.listReaderImpinj.iterator(); iterator.hasNext();) {
				ConfReader confReader = (ConfReader) iterator.next();
				JobRfidImpinj jobRfidImpinj = new JobRfidImpinj(confReader, tunnelService);
				jobRfidImpinj.start();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void stopScanner(JobScannerBarcode scanner) {

		scanner.closeSocket();

	}
}
