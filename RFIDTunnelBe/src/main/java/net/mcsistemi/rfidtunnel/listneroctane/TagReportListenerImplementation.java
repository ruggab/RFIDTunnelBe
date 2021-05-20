package net.mcsistemi.rfidtunnel.listneroctane;

import java.util.List;

import org.apache.log4j.Logger;

import com.impinj.octane.ImpinjReader;
import com.impinj.octane.Tag;
import com.impinj.octane.TagReport;
import com.impinj.octane.TagReportListener;

import net.mcsistemi.rfidtunnel.db.entity.ConfReader;
import net.mcsistemi.rfidtunnel.db.entity.ScannerStream;
import net.mcsistemi.rfidtunnel.db.entity.Tunnel;
import net.mcsistemi.rfidtunnel.db.services.TunnelService;

public class TagReportListenerImplementation implements TagReportListener {

	Logger logger = Logger.getLogger(TagReportListenerImplementation.class);

	private TunnelService tunnelService;
	private ConfReader confReader;



	public TagReportListenerImplementation(TunnelService tunnelService, ConfReader confReader) {
		this.tunnelService = tunnelService;
		this.confReader = confReader;
	}

	@Override
	public void onTagReported(ImpinjReader impinjReader, TagReport report) {
		List<Tag> tags = report.getTags();
		try {
			ScannerStream scannerStream = this.tunnelService.gestioneStream(confReader, impinjReader, tags);

			// Gestioen Atteso
			if (confReader.getTunnel().getIdSceltaGestAtteso() == 7) {
				int result = this.tunnelService.compareByPackage(scannerStream, 
						confReader.getTunnel().isAttesoEpc(), 
						confReader.getTunnel().isAttesoTid(), 
						confReader.getTunnel().isAttesoUser(), 
						confReader.getTunnel().isAttesoBarcode(), 
						confReader.getTunnel().isAttesoQuantita());

				impinjReader.setGpo(1, false);
				impinjReader.setGpo(2, false);
				impinjReader.setGpo(3, false);
				impinjReader.setGpo(4, false);

				String esito = "";
				// Se OK
				if (result == 1) {
					impinjReader.setGpo(1, true);
					impinjReader.setGpo(2, true);
					esito = "ATTESO OK";
				}
				// Se KO
				if (result == 2) {
					impinjReader.setGpo(3, true);
					impinjReader.setGpo(4, true);
					esito = "ATTESO KO";
				}
				//
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
