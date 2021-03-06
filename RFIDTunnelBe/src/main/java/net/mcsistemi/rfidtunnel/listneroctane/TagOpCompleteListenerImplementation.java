package net.mcsistemi.rfidtunnel.listneroctane;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.impinj.octane.*;

import net.mcsistemi.rfidtunnel.entity.ReaderRfidInpinj;
import net.mcsistemi.rfidtunnel.services.ReaderService;

public class TagOpCompleteListenerImplementation implements TagOpCompleteListener {

	Logger logger = LoggerFactory.getLogger(TagReportListenerImplementation.class);
	private ReaderService readerService;
	private ReaderRfidInpinj readerRfidInpinj;

	public TagOpCompleteListenerImplementation(ReaderRfidInpinj readerRfidInpinj,ReaderService readerService) {
		this.readerService = readerService;
		this.readerRfidInpinj = readerRfidInpinj;
	}

	public void onTagOpComplete(ImpinjReader reader, TagOpReport results) {
		System.out.println("onTagOpComplete");
		String message = "";
		try {
			for (TagOpResult t : results.getResults()) {
				System.out.println("IMPINJ ---->>>> EPC: " + t.getTag().getEpc().toHexString());
				logger.debug("IMPINJ ---->>>> EPC: " + t.getTag().getEpc().toHexString());
				message = "EPC: " + t.getTag().getEpc().toHexString();
				if (t instanceof TagReadOpResult) {
					TagReadOpResult tr = (TagReadOpResult) t;
					System.out.print(" READ: id: " + tr.getOpId());
					System.out.print(" sequence: " + tr.getSequenceId());
					System.out.print(" result: " + tr.getResult().toString());
					if (tr.getResult() == ReadResultStatus.Success && tr.getOpId() == new Short(readerRfidInpinj.getIdUser()+"")) {
						System.out.print(" USER: " + tr.getData().toHexWordString());
						message = message + " USER: " + tr.getData().toHexWordString();
					}
					if (tr.getResult() == ReadResultStatus.Success && tr.getOpId() == new Short(readerRfidInpinj.getIdTid()+"")) {
						System.out.print(" TID: " + tr.getData().toHexWordString());
						message = message + " TID: " + tr.getData().toHexWordString();
						readerService.createReaderlog(reader.getAddress(), "", new Date(), message);
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
