package net.smart.rfid.tunnel.listneroctane;


import org.apache.log4j.Logger;

import com.impinj.octane.ImpinjReader;
import com.impinj.octane.ReaderStartEvent;
import com.impinj.octane.ReaderStartListener;

import net.smart.rfid.tunnel.db.services.DispositivoService;

public class ReaderStartListenerImplementation implements ReaderStartListener {

	Logger logger = Logger.getLogger(TagReportListenerImplementation.class);
	private DispositivoService readerService;
	
	public ReaderStartListenerImplementation(DispositivoService readerService) {
		this.readerService = readerService;
	}
    @Override
    public void onReaderStart(ImpinjReader reader, ReaderStartEvent e) {
        System.out.println("Listener - Reader_Started");
    }
}
