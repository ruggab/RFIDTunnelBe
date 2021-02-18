package net.mcsistemi.rfidtunnel.listneroctane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.impinj.octane.ImpinjReader;
import com.impinj.octane.ReaderStopEvent;
import com.impinj.octane.ReaderStopListener;

import net.mcsistemi.rfidtunnel.services.ReaderService;

public class ReaderStopListenerImplementation implements ReaderStopListener {

	Logger logger = LoggerFactory.getLogger(TagReportListenerImplementation.class);
	private ReaderService readerService;
	
	public ReaderStopListenerImplementation(ReaderService readerService) {
		this.readerService = readerService;
	}
	
	
    @Override
    public void onReaderStop(ImpinjReader reader, ReaderStopEvent e) {
        System.out.println("Listener - Reader_Stopped");
    }
}
