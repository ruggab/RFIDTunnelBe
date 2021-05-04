package net.mcsistemi.rfidtunnel.listneroctane;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

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
	public void onTagReported(ImpinjReader reader, TagReport report) {
		List<Tag> tags = report.getTags();
		try {
			
			for (Tag t : tags) {
				logger.info("IMPINJ ---->>>> EPC: " + t.getEpc().toString());
				logger.info("IMPINJ ---->>>> TID: " + t.getTid().toString());

				if (this.tunnelJob.getTunnel().getIdSceltaGestColli() == 7 ) {
					ScannerStream scannerStream = this.tunnelJob.getTunnelService().getLastScanner();
					if (scannerStream != null) {
						logger.info("Package: " + scannerStream.getPackageData());
						this.tunnelJob.getTunnelService().saveStream(tunnelJob.getTunnel().getId(), reader.getAddress(), scannerStream, t);
					} else {
						//String packageData = "NO_BARCODE-" + this.tunnelJob.getTunnelService().getSeqNextVal();
						this.tunnelJob.getTunnelService().saveStream(tunnelJob.getTunnel().getId(), reader.getAddress(), null, t);
					}
					
				}
				s
			}

			// Gestioen Atteso
			if (this.tunnelJob.getTunnel().getIdSceltaGestAtteso() == 7) {
				int result = this.tunnelJob.getTunnelService().compareByPackage(packid, this.tunnelJob.getTunnel().isAttesoEpc(), 
				this.tunnelJob.getTunnel().isAttesoTid(), 
				this.tunnelJob.getTunnel().isAttesoUser(), 
				this.tunnelJob.getTunnel().isAttesoBarcode(), 
				this.tunnelJob.getTunnel().isAttesoQuantita());

				reader.setGpo(1, false);
				reader.setGpo(2, false);
				reader.setGpo(3, false);
				reader.setGpo(4, false);

				// Se OK
				if (result == 1) {
					reader.setGpo(1, true);
					reader.setGpo(2, true);
					logger.info("ATTESO OK");
				}
				// Se KO
				if (result == 2) {
					reader.setGpo(3, true);
					reader.setGpo(4, true);
					logger.info("ATTESO KO");
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
