package net.mcsistemi.rfidtunnel.listneroctane;

import com.impinj.octane.ConnectionLostListener;
import com.impinj.octane.ImpinjReader;

import net.mcsistemi.rfidtunnel.entity.ReaderRfidInpinj;
import net.mcsistemi.rfidtunnel.services.DispositivoService;
import net.mcsistemi.rfidtunnel.util.DateFunction;

public class ConnectionLostListenerImplement implements ConnectionLostListener {
	static DateFunction myDate;
	private ReaderRfidInpinj readerRfidInpinj;
	private DispositivoService readerService;

	public ConnectionLostListenerImplement(ReaderRfidInpinj readerRfidInpinj, DispositivoService readerService) {
		this.readerRfidInpinj = readerRfidInpinj;
		this.readerService = readerService;
	}

	@Override
	public void onConnectionLost(ImpinjReader reader) {
		System.out.println("----Entrata ConnectionLostListenerImplement");
		try {
			readerRfidInpinj.setStato(false);
			readerService.save(readerRfidInpinj);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("-Uscita ConnectionLostListenerImplement------->");
	}
}
