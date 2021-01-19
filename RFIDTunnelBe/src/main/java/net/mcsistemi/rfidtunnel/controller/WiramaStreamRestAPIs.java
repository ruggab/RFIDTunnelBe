package net.mcsistemi.rfidtunnel.controller;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.mcsistemi.rfidtunnel.entity.ScannerStream;
import net.mcsistemi.rfidtunnel.entity.WiramaStream;
import net.mcsistemi.rfidtunnel.job.TunnelJob;
import net.mcsistemi.rfidtunnel.model.SimpleMessage;
import net.mcsistemi.rfidtunnel.repository.PropertiesRepository;
import net.mcsistemi.rfidtunnel.repository.ScannerStreamRepository;
import net.mcsistemi.rfidtunnel.repository.WiramaStreamRepository;

@RestController
@RequestMapping("/api/v1")
public class WiramaStreamRestAPIs {
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
	
	@Autowired
    private ScannerStreamRepository scanRep;
	
	@Autowired
    private WiramaStreamRepository repository;
	
	@Autowired
	private PropertiesRepository propertiesRep;
	
	
	@GetMapping("/tunnel")
	public Page<WiramaStream> getWiramaStream(Pageable pageable) {
        return repository.findAll(pageable);
    }
	
	@GetMapping("/tunnel/{packId}/epcs-list")
    public List<WiramaStream> getEpcsByPackId(@PathVariable String packId) {
        return repository.findByPackId(packId);
    }
	
	@GetMapping("/tunnel/{packId}/epcs")
    public Page<WiramaStream> getEventsEAS(Pageable pageable, @PathVariable String packId) {
		
		List<WiramaStream> ws = repository.findByPackId(packId);
		
		int start = (int) pageable.getOffset();
		int end = (start + pageable.getPageSize()) > ws.size() ? ws.size() : (start + pageable.getPageSize());
		Page<WiramaStream> pages = new PageImpl<WiramaStream>(ws.subList(start, end), pageable, ws.size());
				
		return pages;
    }
	
	@PostMapping("/tunnel")
    public SimpleMessage savePack (@RequestBody String params) {
		
		SimpleMessage sm = new SimpleMessage();
		
		System.out.println(params);
		
		try {
			
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(params);
			
			String packId = json.get("packId").toString();
			ArrayList<String> epcs = (ArrayList<String>) json.get("epcs");
			
			List<WiramaStream> ws = new ArrayList<WiramaStream>();
	        
			String PATH = propertiesRep.findByKey("CSV_DIRECTORY").getValue();

			scanRep.deleteWiramaByPackId(packId);
			scanRep.deleteScannerByPackId(packId);
			
    		scanRep.save(new ScannerStream(packId, new Date()));
    		
    		for(String epc : epcs) {
    			ws.add(new WiramaStream(packId, epc, new Date()));
            }
    		repository.saveAll(ws);
    		
    		String fileName = PATH+packId+".txt";
        	// Write CSV File
        	if(TunnelJob.packId==null) {
        		fileName = PATH+"No_Barcode_"+sdf.format(new Date())+".txt";
        	}
        	
        	PrintWriter writer = new PrintWriter(fileName, "UTF-8");
        	for(WiramaStream s : ws) {
        		writer.println(s.getEpc());
        	}
        	writer.close();
    		
    	
			sm.setCode("200");
			sm.setMessage("Pack Imported");
			
		}
		catch(Exception e) {
			
			sm.setCode("500");
			sm.setMessage(e.getMessage());
		}
        return sm;
    }
}
