package net.mcsistemi.rfidtunnel.listneroctane;

import com.impinj.octane.ImpinjReader;
import com.impinj.octane.Tag;
import com.impinj.octane.TagReport;
import com.impinj.octane.TagReportListener;

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

public class TagReportListenerImplementation implements TagReportListener {

	Logger logger = LoggerFactory.getLogger(TagReportListenerImplementation.class);
	private ReaderService readerService;
	private TunnelService tunnelService;

	public TagReportListenerImplementation(ReaderService readerService) {
		this.readerService = readerService;
	}
	
	public TagReportListenerImplementation(TunnelService tunnelService) {
		this.tunnelService = tunnelService;
	}

	@Override
	public void onTagReported(ImpinjReader reader, TagReport report) {
		List<Tag> tags = report.getTags();
		try {
			for (Tag t : tags) {
				logger.info("IMPINJ ---->>>> EPC: " + t.getEpc().toString());
				logger.info("IMPINJ ---->>>> TID: " + t.getTid().toString());
				tunnelService.createReaderStream(reader.getAddress(), "", TunnelJob.packId,  t);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
