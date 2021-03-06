package net.smart.rfid.tunnel.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.smart.rfid.tunnel.db.entity.ConfAntenna;
import net.smart.rfid.tunnel.db.entity.ConfPorta;
import net.smart.rfid.tunnel.db.entity.ConfReader;
import net.smart.rfid.tunnel.db.services.ConfReaderService;
import net.smart.rfid.tunnel.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:4200")
public class ConfReaderRestAPIs {

	@Autowired
	private ConfReaderService confReaderService;

 
	
	
	@GetMapping("/confReader/{id}")
	public ConfReader getReaderById(@PathVariable(value = "id") Long id) throws Exception {
		try {
			ConfReader cf =  confReaderService.getReaderById(id);
			return cf;
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	
	@PostMapping("/confReaderByTunnelAndDispo")
	public ConfReader getConfReaderByTunnelAndDispo(@RequestBody ConfReader reader) throws Exception {
		try {
			ConfReader cf =  confReaderService.getConfReaderByTunnelAndDispo(reader);
			return cf;
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	@PostMapping("/creaConfReader")
	public void creaConfReader(@RequestBody ConfReader reader) throws Exception, ResourceNotFoundException {
		try {
			confReaderService.save(reader);
		} catch (Exception e) {
			throw e;
		}
	}
	 
	
	@PostMapping("/associaConfReader")
	public void associaReader(@RequestBody ConfReader reader) throws Exception, ResourceNotFoundException {
		try {
			confReaderService.updateConfReader(reader);
		} catch (Exception e) {
			throw e;
		}
	}

  
	@GetMapping("/allConfReader")
	public List<ConfReader> getAllReader() throws Exception, ResourceNotFoundException {
		try {
			return confReaderService.getAllReader();
		} catch (Exception e) {
			throw e;
		}
	}
	
	@GetMapping("/allConfAntenna/{id}")
	public List<ConfAntenna> getAllAntenna(@PathVariable(value = "id") Long readerId) throws Exception, ResourceNotFoundException {
		try {
			return confReaderService.getAllAntenna(readerId);
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	@GetMapping("/allConfPort/{id}")
	public List<ConfPorta> getAllConfPort(@PathVariable(value = "id") Long readerId) throws Exception, ResourceNotFoundException {
		try {
			return confReaderService.getAllConfPort(readerId);
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	
	@DeleteMapping("/deleteConfReader/{id}")
	public void deleteReader(@PathVariable(value = "id") Long readerId) throws Exception {
		try {
			confReaderService.deleteReader(readerId);
		} catch (Exception e) {
			throw e;
		}
	}
	
//	@PutMapping("/updateReader")
//	public void updateReader(@RequestBody Reader reader) throws Exception, ResourceNotFoundException {
//		try {
//			readerService.updateReader(reader);
//		} catch (Exception e) {
//			throw e;
//		}
//	}
	
//	@PutMapping("/updateReaderInpinj")
//	public void updateReaderInpinj(@RequestBody ReaderRfidInpinj reader) throws Exception, ResourceNotFoundException {
//		try {
//			readerService.updateReader(reader);
//		} catch (Exception e) {
//			throw e;
//		}
//	}
//	
//	@PutMapping("/updateReaderWirama")
//	public void updateReaderWirama(@RequestBody ReaderRfidWirama reader) throws Exception, ResourceNotFoundException {
//		try {
//			readerService.updateReader(reader);
//		} catch (Exception e) {
//			throw e;
//		}
//	}
	
	
	
	


}
