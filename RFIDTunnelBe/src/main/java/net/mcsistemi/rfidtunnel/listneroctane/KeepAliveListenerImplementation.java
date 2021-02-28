package net.mcsistemi.rfidtunnel.listneroctane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.impinj.octane.ImpinjReader;
import com.impinj.octane.KeepaliveEvent;
import com.impinj.octane.KeepaliveListener;

import net.mcsistemi.rfidtunnel.entity.ReaderRfidInpinj;
import net.mcsistemi.rfidtunnel.services.ReaderService;

public class KeepAliveListenerImplementation implements KeepaliveListener {
	
	Logger logger = LoggerFactory.getLogger(TagReportListenerImplementation.class);
	static net.mcsistemi.rfidtunnel.util.DateFunction myDate;
	private ReaderRfidInpinj readerRfidInpinj=null;
	private ReaderService readerService;
	
	
	
	public KeepAliveListenerImplementation(ReaderRfidInpinj readerRfidInpinj, ReaderService readerService) {
		this.readerRfidInpinj=readerRfidInpinj;   
		this.readerService=readerService;   
	}

    @Override
    public void onKeepalive(ImpinjReader reader, KeepaliveEvent e) {
        
    	if(!reader.isConnected()) {
    		System.out.println("READER NON CONNESSO");
    		try {	        	                        		
        		System.out.println(myDate.getFullDate()+" Reader try to Re-Connecting...........");
        		// Connessione ed Attivazione LED su Tunnel
                reader.connect(readerRfidInpinj.getIpAdress());
                if(reader.isConnected()) {
                	reader.setGpo(1, true);
                	System.out.println(myDate.getFullDate()+" Reader Re-Connected");
                }else
                	System.out.println(myDate.getFullDate()+" Reader Re-Connection Failed");
        	}catch(Exception ex) {
        		System.err.println(ex.getMessage());
        	}
    	}else    
    		System.out.println("READER CONNESSO");
    		
    }
}
