package net.mcsistemi.rfidtunnel.listneroctane;

import java.sql.Timestamp;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.impinj.octane.*;

import net.mcsistemi.rfidtunnel.entity.ReaderRfidInpinj;
import net.mcsistemi.rfidtunnel.services.DispositivoService;

public class TagOpCompleteListenerImplementation implements TagOpCompleteListener {

	Logger logger = LoggerFactory.getLogger(TagReportListenerImplementation.class);
	private DispositivoService readerService;
	private ReaderRfidInpinj readerRfidInpinj;

	public TagOpCompleteListenerImplementation(ReaderRfidInpinj readerRfidInpinj,DispositivoService readerService) {
		this.readerService = readerService;
		this.readerRfidInpinj = readerRfidInpinj;
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
					if (tr.getResult() == ReadResultStatus.Success && tr.getOpId() == new Short(readerRfidInpinj.getIdUser()+"")) {
						System.out.print(" USER: " + tr.getData().toHexWordString());
						user = tr.getData().toHexWordString();
					}
					if (tr.getResult() == ReadResultStatus.Success && tr.getOpId() == new Short(readerRfidInpinj.getIdTid()+"")) {
						System.out.print(" TID: " + tr.getData().toHexWordString());
						tid = tr.getData().toHexWordString();
						//
						epc = readerRfidInpinj.isEnableEpc() ?  epc : "";
						tid = readerRfidInpinj.isEnableTid() ? tid : "";
						user = readerRfidInpinj.isEnableUser() ? user : "";
						readerService.createReaderStream(reader.getAddress(), "", epc, tid, user, "",new Timestamp(System.currentTimeMillis()));
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
