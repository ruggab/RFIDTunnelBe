package net.mcsistemi.rfidtunnel.listneroctane;

import com.impinj.octane.ImpinjReader;
import com.impinj.octane.Tag;
import com.impinj.octane.TagReport;
import com.impinj.octane.TagReportListener;

import net.mcsistemi.rfidtunnel.entity.ConfReader;
import net.mcsistemi.rfidtunnel.entity.Tunnel;
import net.mcsistemi.rfidtunnel.job2.TunnelJob;
import net.mcsistemi.rfidtunnel.services.ConfReaderService;
import net.mcsistemi.rfidtunnel.services.DispositivoService;
import net.mcsistemi.rfidtunnel.services.ReaderService;
import net.mcsistemi.rfidtunnel.services.TunnelService;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class TagReportListenerImplementation implements TagReportListener {

	Logger logger = LoggerFactory.getLogger(TagReportListenerImplementation.class);
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
			String packid = tunnelJob.getPackId();
			for (Tag t : tags) {
				logger.info("IMPINJ ---->>>> EPC: " + t.getEpc().toString());
				logger.info("IMPINJ ---->>>> TID: " + t.getTid().toString());

				if (this.tunnelJob.getTunnel().getIdSceltaGestColli() == 7 && StringUtils.isEmpty(packid)) {
					packid = "NO_BARCODE-" + this.tunnelJob.getTunnelService().getSeqNextVal();
				}
				this.tunnelJob.getTunnelService().createReaderStream(tunnelJob.getTunnel().getId(), reader.getAddress(), packid, t);
			}
			int result = 0;
			if (this.tunnelJob.getTunnel().getIdSceltaGestAtteso() == 7) {
				result = this.tunnelJob.getTunnelService().compareByPackage(packid, 
						this.tunnelJob.getTunnel().isAttesoEpc(), 
						this.tunnelJob.getTunnel().isAttesoTid(), 
						this.tunnelJob.getTunnel().isAttesoUser(), 
						this.tunnelJob.getTunnel().isAttesoBarcode(), 
						this.tunnelJob.getTunnel().isAttesoQuantita());
			}
			if (result == 1) {
				reader.setGpo(1, false);
				reader.setGpo(2, false);
				reader.setGpo(1, true);
				reader.setGpo(2, true);
			}
			if (result == 2) {
				reader.setGpo(1, false);
				reader.setGpo(2, false);
				reader.setGpo(2, true);
				
			}
			// this.tunnelJob.getConfReader(reader.getAddress()).getPorts()
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
