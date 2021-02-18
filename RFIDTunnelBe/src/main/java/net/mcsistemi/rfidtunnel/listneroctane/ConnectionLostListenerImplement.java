package net.mcsistemi.rfidtunnel.listneroctane;

import com.impinj.octane.ConnectionLostListener;
import com.impinj.octane.ImpinjReader;

import net.mcsistemi.rfidtunnel.entity.ReaderRfidInpinj;
import net.mcsistemi.rfidtunnel.services.ReaderService;
import net.mcsistemi.rfidtunnel.util.DateFunction;



public class ConnectionLostListenerImplement implements ConnectionLostListener {
	static DateFunction myDate;
	private ReaderRfidInpinj readerRfidInpinj;
	private ReaderService readerService;
	
	public ConnectionLostListenerImplement(ReaderRfidInpinj readerRfidInpinj,ReaderService readerService) {
		this.readerRfidInpinj=readerRfidInpinj;     
		this.readerService = readerService;
	}

    @Override
    public void onConnectionLost(ImpinjReader reader) {
    	System.out.println("-Entrata onConnectionLost------->");
    	myDate=new DateFunction();
    	
    	try {
	        System.out.println("Connection to the Reader is Lost...........");      
	        
	        int tries=0;
	        boolean forEver=true;
	        
	        while(forEver) {
	        	forEver=!reader.isConnected();
	        	Thread.sleep(2000); // Attesa di 2 sec.
        		
	        	System.out.println(myDate.getFullDate()+" Reader try to Re-Connecting...........");
        		// Connessione ed Attivazione LED su Tunnel
                reader.connect(readerRfidInpinj.getIpAdress());
                if(reader.isConnected()) {
                	reader.setGpo(readerRfidInpinj.getOnlinePort(), true);
                	System.out.println(myDate.getFullDate()+" Reader Re-Connected");
                }else
                	System.out.println(myDate.getFullDate()+" Reader Re-Connection attempt :"+tries+" Failed");
        	}
    
    	}catch(Exception ex) {
    		System.err.println(ex.getMessage());
    	}
    	System.out.println("-Uscita onConnectionLost------->");
    }
}
