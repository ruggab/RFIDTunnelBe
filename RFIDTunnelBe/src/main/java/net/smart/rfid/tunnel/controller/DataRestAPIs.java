package net.smart.rfid.tunnel.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.smart.rfid.tunnel.db.entity.ReaderStream;
import net.smart.rfid.tunnel.db.entity.ReaderStreamAtteso;
import net.smart.rfid.tunnel.db.entity.ScannerStream;
import net.smart.rfid.tunnel.db.entity.Tunnel;
import net.smart.rfid.tunnel.db.repository.ReaderStreamRepository.ReaderStreamOnly;
import net.smart.rfid.tunnel.db.services.DataStreamService;
import net.smart.rfid.tunnel.db.services.TunnelService;
import net.smart.rfid.tunnel.exception.ResourceNotFoundException;
import net.smart.rfid.tunnel.model.TunnelDevice;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:4200")
public class DataRestAPIs {

	
	@Autowired
	private DataStreamService dataStreamService;

	
	
	
	@GetMapping("/readerStreamAtteso/{packId}")
	public List<ReaderStreamAtteso> getReaderStreamAtteso(@PathVariable(value = "packId") String packId) throws Exception {
		try {
			return dataStreamService.getReaderStreamAtteso(packId);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@PutMapping ("/createStreamAtteso")
	public  ReaderStreamAtteso createStreamAtteso(@RequestParam String collo,  @RequestParam(required = false) String epc,  @RequestParam(required = false) String tid) throws Exception {
		try {
			return dataStreamService.createReaderStreamAtteso(collo, epc, tid);
		} catch (Exception e) {
			throw e;
		}
	}
	

	
	
	@GetMapping("/allReaderStream")
	public List<ReaderStream> getAllReaderStream() throws Exception, ResourceNotFoundException {
		try {
			return dataStreamService.getAllReaderStream();
		} catch (Exception e) {
			throw e;
		}
	}
	
	@GetMapping("/distinctReaderStreamByPackage/{packId}")
	public List<ReaderStreamOnly> getDistinctReaderStreamByPackage(@PathVariable(value = "packId") String packId) throws Exception, ResourceNotFoundException {
		try {
			return dataStreamService.getDistinctReaderStreamByPackage(packId);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@GetMapping("/allDistinctReaderStream")
	public List<ReaderStreamOnly> getAllDistinctReaderStream() throws Exception, ResourceNotFoundException {
		try {
			return dataStreamService.getAllDistinctReaderStream();
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	@GetMapping("/allScannerStream")
	public List<ScannerStream> getAllScannerStream() throws Exception, ResourceNotFoundException {
		try {
			return dataStreamService.getAllScannerStream();
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	@DeleteMapping("/deleteAllData")
	public void deleteAllData() throws Exception, ResourceNotFoundException {
		try {
			dataStreamService.deleteAllData();
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	@GetMapping("/enableTrigger")
	public void enableTrigger() throws Exception, ResourceNotFoundException {
		try {
			dataStreamService.enableTrigger();
		} catch (Exception e) {
			throw e;
		}
	}
	
	@GetMapping("/disableTrigger")
	public void disableTrigger() throws Exception, ResourceNotFoundException {
		try {
			dataStreamService.disableTrigger();
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	@GetMapping("/getTotalPackageReadDay")
	public Integer getTotalPackageReadDay() throws Exception, ResourceNotFoundException {
		try {
			return dataStreamService.getTotalPackageReadDay();
		} catch (Exception e) {
			throw e;
		}
	}
	
	@GetMapping("/getTotalPackageKoDay")
	public Integer getTotalPackageKoDay() throws Exception, ResourceNotFoundException {
		try {
			return dataStreamService.getTotalPackageKoDay();
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	@GetMapping("/getTotalPackageReadLastWeek")
	public Integer getTotalPackageReadLastWeek() throws Exception, ResourceNotFoundException {
		try {
			return dataStreamService.getTotalPackageReadLastWeek();
		} catch (Exception e) {
			throw e;
		}
	}
	
	@GetMapping("/getTotalPackageKoLastWeek")
	public Integer getTotalPackageKoLastWeek() throws Exception, ResourceNotFoundException {
		try {
			return dataStreamService.getTotalPackageKoLastWeek();
		} catch (Exception e) {
			throw e;
		}
	}
  
	@GetMapping("/getTotalPackageReadLastMonth")
	public Integer getTotalPackageReadLastMonth() throws Exception, ResourceNotFoundException {
		try {
			return dataStreamService.getTotalPackageReadLastMonth();
		} catch (Exception e) {
			throw e;
		}
	}	
	
	@GetMapping("/getTotalPackageKoLastMonth")
	public Integer getTotalPackageKoLastMonth() throws Exception, ResourceNotFoundException {
		try {
			return dataStreamService.getTotalPackageKoLastMonth();
		} catch (Exception e) {
			throw e;
		}
	}	
  

}
