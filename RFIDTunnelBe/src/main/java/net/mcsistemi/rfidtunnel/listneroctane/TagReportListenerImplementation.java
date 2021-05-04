package net.mcsistemi.rfidtunnel.listneroctane;

import java.util.List;

import org.apache.log4j.Logger;

import com.impinj.octane.ImpinjReader;
import com.impinj.octane.Tag;
import com.impinj.octane.TagReport;
import com.impinj.octane.TagReportListener;

import net.mcsistemi.rfidtunnel.entity.ScannerStream;
import net.mcsistemi.rfidtunnel.job2.TunnelJob;
import net.mcsistemi.rfidtunnel.services.ReaderService;

public class TagReportListenerImplementation implements TagReportListener {

	Logger logger = Logger.getLogger(TagReportListenerImplementation.class);
	private ReaderService readerService;

	private TunnelJob tunnelJob;

	public TagReportListenerImplementation(ReaderService readerService) {
		this.readerService = readerService;
	}

	public TagReportListenerImplementation(TunnelJob tunnelJob) {

		this.tunnelJob = tunnelJob;
	}

	@Override
	public void onTagReported(ImpinjReader impinjReader, TagReport report) {
		List<Tag> tags = report.getTags();
		try {
			ScannerStream scannerStream = this.tunnelJob.getTunnelService().gestioneStream(tunnelJob, impinjReader, tags);

			// Gestioen Atteso
			if (this.tunnelJob.getTunnel().getIdSceltaGestAtteso() == 7) {
				int result = this.tunnelJob.getTunnelService().compareByPackage(scannerStream.getId(), scannerStream.getPackageData(), this.tunnelJob.getTunnel().isAttesoEpc(), 
				this.tunnelJob.getTunnel().isAttesoTid(), 
				this.tunnelJob.getTunnel().isAttesoUser(), 
				this.tunnelJob.getTunnel().isAttesoBarcode(), 
				this.tunnelJob.getTunnel().isAttesoQuantita());

				impinjReader.setGpo(1, false);
				impinjReader.setGpo(2, false);
				impinjReader.setGpo(3, false);
				impinjReader.setGpo(4, false);

				// Se OK
				if (result == 1) {
					impinjReader.setGpo(1, true);
					impinjReader.setGpo(2, true);
					logger.info("ATTESO OK");
				}
				// Se KO
				if (result == 2) {
					impinjReader.setGpo(3, true);
					impinjReader.setGpo(4, true);
					logger.info("ATTESO KO");
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
