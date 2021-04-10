package net.mcsistemi.rfidtunnel.listneroctane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.impinj.octane.ImpinjReader;
import com.impinj.octane.ReaderStartEvent;
import com.impinj.octane.ReaderStartListener;

import net.mcsistemi.rfidtunnel.services.DispositivoService;

public class ReaderStartListenerImplementation implements ReaderStartListener {

	Logger logger = LoggerFactory.getLogger(TagReportListenerImplementation.class);
	private DispositivoService readerService;
	
	public ReaderStartListenerImplementation(DispositivoService readerService) {
		this.readerService = readerService;
	}
    @Override
    public void onReaderStart(ImpinjReader reader, ReaderStartEvent e) {
        System.out.println("Listener - Reader_Started");
    }
}
