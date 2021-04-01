package net.mcsistemi.rfidtunnel.listneroctane;

import com.impinj.octane.ImpinjReader;
import com.impinj.octane.Tag;
import com.impinj.octane.TagReport;
import com.impinj.octane.TagReportListener;
import net.mcsistemi.rfidtunnel.services.ReaderService;

import java.sql.Timestamp;
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
				readerService.createReaderStream(reader.getAddress(), "", t.getEpc().toString(), "", "", "",new Timestamp(System.currentTimeMillis()));
				System.out.print(" Antenna: " + t.getAntennaPortNumber());
				System.out.print(" EPC: " + t.getEpc().toString());
				System.out.print(" EPC: " + t.getTid().toString());
				System.out.print(" EPC: " + t.getChannelInMhz());
				System.out.print(" EPC: " + t.getCrc());
				System.out.print(" EPC: " + t.getPcBits());
				System.out.print(" EPC: " + t.getPeakRssiInDbm());
				System.out.print(" EPC: " + t.getPhaseAngleInRadians());
				System.out.print(" EPC: " + t.getRfDopplerFrequency());
				System.out.print(" EPC: " + t.getTagSeenCount());
				System.out.print(" EPC: " + t.getFirstSeenTime());
				System.out.print(" EPC: " + t.getGpsCoodinates());
				System.out.print(" EPC: " + t.getLastSeenTime());
				System.out.print(" EPC: " + t.getModelDetails());
				System.out.print(" EPC: " + t.getReadOperationResults());
				
				
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
