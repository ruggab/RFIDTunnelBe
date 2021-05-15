package net.mcsistemi.rfidtunnel.listneroctane;

import org.apache.log4j.Logger;

import com.impinj.octane.ImpinjReader;
import com.impinj.octane.ReadResultStatus;
import com.impinj.octane.TagOpCompleteListener;
import com.impinj.octane.TagOpReport;
import com.impinj.octane.TagOpResult;
import com.impinj.octane.TagReadOpResult;

import net.mcsistemi.rfidtunnel.db.entity.ConfReader;
import net.mcsistemi.rfidtunnel.db.services.TunnelService;

public class TagOpCompleteListenerImplementation implements TagOpCompleteListener {

	Logger logger = Logger.getLogger(TagReportListenerImplementation.class);
	private TunnelService tunnelService;
	private ConfReader confReader;
	private String ipAdress;

	
	public TagOpCompleteListenerImplementation(ConfReader confReader,TunnelService tunnelService) {
		this.tunnelService = tunnelService;
		this.confReader = confReader;
	}

	
	
	
	public void onTagOpComplete(ImpinjReader reader, TagOpReport results) {
		System.out.println("onTagOpComplete");
		String epc = "";
		String tid = "";
		String user = "";
		try {
			for (TagOpResult t : results.getResults()) {
				System.out.println("IMPINJ ---->>>> EPC: " + t.getTag().getEpc().toHexString());
				logger.debug("IMPINJ ---->>>> EPC: " + t.getTag().getEpc().toHexString());
				epc = t.getTag().getEpc().toHexString();
				if (t instanceof TagReadOpResult) {
					TagReadOpResult tr = (TagReadOpResult) t;
					System.out.print(" READ: id: " + tr.getOpId());
					System.out.print(" sequence: " + tr.getSequenceId());
					System.out.print(" result: " + tr.getResult().toString());
					if (tr.getResult() == ReadResultStatus.Success && tr.getOpId() == new Short(111+"")) {
						System.out.print(" USER: " + tr.getData().toHexWordString());
						user = tr.getData().toHexWordString();
					}
					if (tr.getResult() == ReadResultStatus.Success && tr.getOpId() == new Short(222+"")) {
						System.out.print(" TID: " + tr.getData().toHexWordString());
						tid = tr.getData().toHexWordString();
						//
						epc = confReader.isEnableEpc() ?  epc : "";
						tid = confReader.isEnableTid() ? tid : "";
						user = confReader.isEnableUser() ? user : "";
						//tunnelService.createReaderStream(this.ipAdress, epc, tid, user, "",new Timestamp(System.currentTimeMillis()));
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	
}
