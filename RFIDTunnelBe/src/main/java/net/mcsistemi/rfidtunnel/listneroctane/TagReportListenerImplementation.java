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
				
				if (reader.getName() != null) {
					System.out.print(" Reader_name: " + reader.getName());
				} else {
					System.out.print(" Reader_ip: " + reader.getAddress());
				}

				if (t.isAntennaPortNumberPresent()) {
					System.out.print(" antenna: " + t.getAntennaPortNumber());
				}

				if (t.isFirstSeenTimePresent()) {
					System.out.print(" first: " + t.getFirstSeenTime().ToString());
				}

				if (t.isLastSeenTimePresent()) {
					System.out.print(" last: " + t.getLastSeenTime().ToString());
				}

				if (t.isSeenCountPresent()) {
					System.out.print(" count: " + t.getTagSeenCount());
				}

				if (t.isRfDopplerFrequencyPresent()) {
					System.out.print(" doppler: " + t.getRfDopplerFrequency());
				}

				if (t.isPeakRssiInDbmPresent()) {
					System.out.print(" peak_rssi: " + t.getPeakRssiInDbm());
				}

				if (t.isChannelInMhzPresent()) {
					System.out.print(" chan_MHz: " + t.getChannelInMhz());
				}

				if (t.isRfPhaseAnglePresent()) {
					System.out.print(" phase angle: " + t.getPhaseAngleInRadians());
				}

				if (t.isFastIdPresent()) {
					System.out.print("\n     fast_id: " + t.getTid().toHexString());

					System.out.print(" model: " + t.getModelDetails().getModelName());

					System.out.print(" epcsize: " + t.getModelDetails().getEpcSizeBits());

					System.out.print(" usermemsize: " + t.getModelDetails().getUserMemorySizeBits());
				}

				System.out.println("");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
