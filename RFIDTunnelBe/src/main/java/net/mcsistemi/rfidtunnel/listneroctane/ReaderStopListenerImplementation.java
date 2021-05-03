package net.mcsistemi.rfidtunnel.listneroctane;


import org.apache.log4j.Logger;

import com.impinj.octane.ImpinjReader;
import com.impinj.octane.ReaderStopEvent;
import com.impinj.octane.ReaderStopListener;

import net.mcsistemi.rfidtunnel.services.DispositivoService;

public class ReaderStopListenerImplementation implements ReaderStopListener {

	Logger logger = Logger.getLogger(TagReportListenerImplementation.class);
	private DispositivoService readerService;
	
	public ReaderStopListenerImplementation(DispositivoService readerService) {
		this.readerService = readerService;
	}
	
	
    @Override
    public void onReaderStop(ImpinjReader reader, ReaderStopEvent e) {
        System.out.println("Listener - Reader_Stopped");
    }
}
