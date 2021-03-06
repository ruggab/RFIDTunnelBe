package net.mcsistemi.rfidtunnel.listneroctane;

import com.impinj.octane.ImpinjReader;
import com.impinj.octane.Tag;
import com.impinj.octane.TagReport;
import com.impinj.octane.TagReportListener;
import net.mcsistemi.rfidtunnel.services.ReaderService;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TagReportListenerImplementation implements TagReportListener {

	Logger logger = LoggerFactory.getLogger(TagReportListenerImplementation.class);
	private ReaderService readerService;

	public TagReportListenerImplementation(ReaderService readerService) {
		this.readerService = readerService;
	}

	@Override
	public void onTagReported(ImpinjReader reader, TagReport report) {
		List<Tag> tags = report.getTags();
		try {
			for (Tag t : tags) {
				logger.debug("IMPINJ ---->>>> EPC: " + t.getEpc().toString());
				readerService.createReaderlog(reader.getAddress(), "", new Date(), t.getEpc().toString());
				System.out.print(" EPC: " + t.getEpc().toString());
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
