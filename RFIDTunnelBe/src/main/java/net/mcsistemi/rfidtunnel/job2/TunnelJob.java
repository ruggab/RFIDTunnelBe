package net.mcsistemi.rfidtunnel.job2;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import net.mcsistemi.rfidtunnel.entity.ScannerStream;
import net.mcsistemi.rfidtunnel.repository.PropertiesRepository;
import net.mcsistemi.rfidtunnel.repository.ScannerStreamRepository;

public class TunnelJob  {

	Logger logger = LoggerFactory.getLogger(TunnelJob.class);
	
	public String TUNNEL_IP;
	public int TUNNEL_PORT;
	public String SCANNER_IP;
	public int SCANNER_PORT;
	public String PATH;

	Wirama wirama = null;
	JobScannerBarcode scanner = null;
	
	@Autowired
	private PropertiesRepository propertiesRep;
	
	@Autowired
    private ScannerStreamRepository scannerRep;
	
	@Autowired
    private WiramaStreamRepository wiramaRep;
	
	@Autowired
    private TunnelLogRepository logRep;
	
	
	public static String packId = null; // <-- JobScannerBarcode
	
	@Bean
	public void execute(JobExecutionContext context) 
			throws JobExecutionException {
		
		try {
			
			TUNNEL_IP = propertiesRep.findByKey("TUNNEL_IP").getValue();
			TUNNEL_PORT = Integer.parseInt(propertiesRep.findByKey("TUNNEL_PORT").getValue());
			SCANNER_IP = propertiesRep.findByKey("SCANNER_IP").getValue();
			SCANNER_PORT = Integer.parseInt(propertiesRep.findByKey("SCANNER_PORT").getValue());
			PATH = propertiesRep.findByKey("CSV_DIRECTORY").getValue();

			// Tunnel Wirama
			wirama = new Wirama(TUNNEL_IP, TUNNEL_PORT, wiramaRep, PATH, logRep);
						
			// JobScannerBarcode
			scanner = new JobScannerBarcode(SCANNER_IP, SCANNER_PORT, scannerRep, logRep);

			Thread tunnelThread = new Thread(wirama);
			Thread scannerThread = new Thread(scanner);

			tunnelThread.start();
			scannerThread.start();

//			checker = new ThreadChecker(new HashMap<Thread, Runnable>() {{ put(tunnelThread, wirama) ; put(scannerThread, scanner); }});
//
//			Thread threadChecker = new Thread(checker);
//			threadChecker.start();
		}
		catch(Exception e) {
			
		}
		
	}
	
	public void saveScanner(ScannerStream s) {
		scannerRep.save(s);
	}

    public void stopTunnel(Wirama wirama) {

        wirama.closeSocket();
        
    }

    public void stopScanner(JobScannerBarcode scanner) {

        scanner.closeSocket();
        
    }
}
