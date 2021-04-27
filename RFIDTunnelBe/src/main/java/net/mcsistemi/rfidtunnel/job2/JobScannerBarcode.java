package net.mcsistemi.rfidtunnel.job2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import net.mcsistemi.rfidtunnel.job.TunnelJob;
import net.mcsistemi.rfidtunnel.repository.ScannerStreamRepository;
//import net.mcsistemi.rfidtunnel.repository.TunnelLogRepository;

public class JobScannerBarcode implements Runnable {

	private String ip;
    private int port;
    private ScannerStreamRepository repository;
   /// private TunnelLogRepository logRep;
    
    Socket echoSocket = null;
    boolean running = true;

    Logger logger = LoggerFactory.getLogger(JobScannerBarcode.class);
	
    public JobScannerBarcode (String ip, int port, ScannerStreamRepository repository, TunnelLogRepository logRep) {
        this.ip = ip;
        this.port = port;
        this.repository = repository;
        this.logRep = logRep;
    }
    
    //@Override
    public void run() {

    	BufferedReader in = null;
        running = true;
       
        try {
        	in = connect();

            while (running) {

                String packId = null;

                try {
                    packId = in.readLine().toString();
                    
                    if(packId.equals("000000000000000")) {
                    	//Ping
                    }
                    else {
                    	logRep.save(new TunnelLog(new Date(), packId));
                    }
                } catch (NullPointerException ex) {
                    logger.info("Waiting for JobScannerBarcode streams ... ");
                    Thread.sleep(1000);
                    in = connect();
                    continue;
                }

            	//TunnelJob.packId = packId;
            	
            	try {
            		
            		if(!packId.equals("000000000000000")) {
	            		TunnelJob.packId = packId;
	            		
	            		// delete old wirama / scanner
	            		repository.deleteWiramaByPackId(packId);
	            		repository.deleteScannerByPackId(packId);
	            		
	            		System.out.println("****************");
	            		System.out.println(packId);
	            		System.out.println("****************");
	            		
	            		ScannerStream ss = new ScannerStream();
	            		ss.setPackId(packId);
	            		ss.setTimeStamp(new Date());
	            		
	            		//repository.save(new ScannerStream(packId, new Date()));
	            		repository.save(ss);
            		}
            	}catch(Exception e) {
            		//e.printStackTrace();
            		System.out.println(e.getMessage());
            	}
            	
            	logger.info("Barcode received "+packId);
            	
            }
        }
        catch(Exception e) {
            running = false;
            //e.printStackTrace();
        }
        finally {
            try {
                if(in!=null)
                    in.close();
                System.out.println("disconnecting from: "+ip+":"+port);

                try {
                    echoSocket.close();
                }
                catch(Exception e) {
                   // e.printStackTrace();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public BufferedReader connect() {
        BufferedReader in = null;
        // connect
        logger.info("connecting to: "+ip+":"+port);
        //logger.info("repository count (JobScannerBarcode): "+repository.count());

        try {
            echoSocket = new Socket(ip, port);
            in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
        } catch (UnknownHostException e) {
            logger.error("Unknown host: " + ip);
        } catch (IOException e) {
            logger.error("Unable to get streams from JobScannerBarcode");
        }

        return in;
    }

    public void closeSocket() {
    	System.out.println("Try to close Socket...");
        try {
            running = false;
        }
        catch(Exception e) {
        	e.printStackTrace();
        }
    }
}
