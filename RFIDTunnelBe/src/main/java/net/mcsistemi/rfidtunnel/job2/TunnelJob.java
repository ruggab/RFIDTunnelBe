package net.mcsistemi.rfidtunnel.job2;

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.mcsistemi.rfidtunnel.entity.ConfReader;
import net.mcsistemi.rfidtunnel.entity.Dispositivo;
import net.mcsistemi.rfidtunnel.entity.Tunnel;
import net.mcsistemi.rfidtunnel.services.TunnelService;

public class TunnelJob extends Job {

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

	public void startTunnel() throws Exception {
		try {
			for (Iterator iterator = this.listBarcode.iterator(); iterator.hasNext();) {
				Dispositivo dispositivo = (Dispositivo) iterator.next();
				JobScannerBarcode scanner = new JobScannerBarcode(this.tunnel, dispositivo, tunnelService);
				PoolJob.addJob(dispositivo.getId(), scanner);
				Thread scannerThread = new Thread(scanner);
				scannerThread.start();
			}

			for (Iterator iterator = this.listReaderImpinj.iterator(); iterator.hasNext();) {
				ConfReader confReader = (ConfReader) iterator.next();
				JobRfidImpinj jobRfidImpinj = new JobRfidImpinj(this.tunnel, confReader, tunnelService);
				PoolJob.addJob(confReader.getIdDispositivo(), jobRfidImpinj);
				jobRfidImpinj.start();
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

	}

	public void stopTunnel() throws Exception {
		try {

			for (Iterator iterator = this.listBarcode.iterator(); iterator.hasNext();) {
				Dispositivo dispositivo = (Dispositivo) iterator.next();
				JobScannerBarcode jobScannerBarcode = (JobScannerBarcode) PoolJob.getJob(dispositivo.getId());
				jobScannerBarcode.closeSocket();
				PoolJob.removeJob(dispositivo.getId());
			}
			for (Iterator iterator = this.listReaderImpinj.iterator(); iterator.hasNext();) {
				ConfReader confReader = (ConfReader) iterator.next();
				JobRfidImpinj jobRfidImpinj = (JobRfidImpinj) PoolJob.getJob(confReader.getId());
				jobRfidImpinj.stop();
				PoolJob.removeJob(confReader.getId());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

	}

	public Tunnel getTunnel() {
		return tunnel;
	}

	public void setTunnel(Tunnel tunnel) {
		this.tunnel = tunnel;
	}

	public List<ConfReader> getListReaderImpinj() {
		return listReaderImpinj;
	}

	public void setListReaderImpinj(List<ConfReader> listReaderImpinj) {
		this.listReaderImpinj = listReaderImpinj;
	}

	public List<ConfReader> getListReaderWirama() {
		return listReaderWirama;
	}

	public void setListReaderWirama(List<ConfReader> listReaderWirama) {
		this.listReaderWirama = listReaderWirama;
	}

	public List<Dispositivo> getListBarcode() {
		return listBarcode;
	}

	public void setListBarcode(List<Dispositivo> listBarcode) {
		this.listBarcode = listBarcode;
	}

}
