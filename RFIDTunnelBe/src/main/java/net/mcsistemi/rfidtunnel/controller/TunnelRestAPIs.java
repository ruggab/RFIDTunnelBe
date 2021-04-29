package net.mcsistemi.rfidtunnel.controller;

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

import net.mcsistemi.rfidtunnel.entity.ReaderStreamAtteso;
import net.mcsistemi.rfidtunnel.entity.Tunnel;
import net.mcsistemi.rfidtunnel.exception.ResourceNotFoundException;
import net.mcsistemi.rfidtunnel.repository.ReaderStreamAttesoRepository.StreamEPCDifference;
import net.mcsistemi.rfidtunnel.services.TunnelService;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:4200")
public class TunnelRestAPIs {

	@Autowired
	private TunnelService tunnelService;

	

	@GetMapping("/tunnel/{id}")
	public Tunnel getTunnelById(@PathVariable(value = "id") Long tunnelId) throws Exception {
		try {
			return tunnelService.getTunnelById(tunnelId);
		} catch (Exception e) {
			throw e;
		}
	}
	
	

	@PostMapping("/creaTunnel")
	public void creaTunnel(@RequestBody Tunnel tunnel) throws Exception, ResourceNotFoundException {
		try {
			tunnelService.create(tunnel);
		} catch (Exception e) {
			throw e;
		}
	}
  
	
	
	@GetMapping("/allTunnel")
	public List<Tunnel> getAllTunnel() throws Exception, ResourceNotFoundException {
		try {
			List<Tunnel> listTunnel = tunnelService.getAllTunnel();
			return listTunnel;
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	
	
	@DeleteMapping("/deleteTunnel/{id}")
	public void deleteTunnel(@PathVariable(value = "id") Long tunnelId) throws Exception {
		try {
			tunnelService.delete(tunnelId);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@PutMapping("/updateTunnel")
	public void updateTunnel(@RequestBody Tunnel tunnel) throws Exception, ResourceNotFoundException {
		try {
			tunnelService.delete(tunnel.getId());
			tunnelService.save(tunnel);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@PutMapping("/saveTunnel")
	public void salvaTunnel(@RequestBody Tunnel tunnel) throws Exception, ResourceNotFoundException {
		try {
			tunnelService.save(tunnel);
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	@PostMapping("/startTunnel")
	public List<Tunnel>  start(@RequestBody Tunnel tunnel) throws Exception, ResourceNotFoundException {
		try {
			List<Tunnel> listTunnel = tunnelService.start(tunnel);
			
			return listTunnel;
		} catch (Exception e) {
			throw e;
		}

	}
	
	@PostMapping("/stopTunnel")
	public List<Tunnel> stop(@RequestBody Tunnel tunnel) throws Exception, ResourceNotFoundException {
		try {
			List<Tunnel> listTunnel =  tunnelService.stop(tunnel);
			
			return listTunnel;
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	@GetMapping("/readerStreamAtteso/{packId}")
	public List<ReaderStreamAtteso> getReaderStreamAtteso(@PathVariable(value = "packId") String packId) throws Exception {
		try {
			return tunnelService.getReaderStreamAtteso(packId);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@PutMapping ("/createStreamAtteso")
	public  ReaderStreamAtteso createStreamAtteso(@RequestParam String collo,  @RequestParam(required = false) String epc,  @RequestParam(required = false) String tid) throws Exception {
		try {
			return tunnelService.createReaderStreamAtteso(collo, epc, tid);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@GetMapping("/compareEPCByPackage/{packId}")
	public String compareEPCByPackage(@PathVariable(value = "packId") String packId) throws Exception {
		try {
			return tunnelService.compareEPCByPackage(packId);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@GetMapping("/compareTIDByPackage/{packId}")
	public String compareTIDByPackage(@PathVariable(value = "packId") String packId) throws Exception {
		try {
			return tunnelService.compareTIDByPackage(packId);
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	
	@GetMapping("/getDiffEPCExpectedRead")
	public List<StreamEPCDifference> getDiffEPCExpectedRead() throws Exception {
		try {
			return tunnelService.getDiffEPCExpectedRead();
		} catch (Exception e) {
			throw e;
		}
	}
	
	@GetMapping("/getDiffEPCReadExpected")
	public List<StreamEPCDifference> getDiffEPCReadExpected() throws Exception {
		try {
			return tunnelService.getDiffEPCReadExpected();
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	@GetMapping("/getDiffTIDExpectedRead")
	public List<StreamEPCDifference> getDiffTIDExpectedRead() throws Exception {
		try {
			return tunnelService.getDiffEPCExpectedRead();
		} catch (Exception e) {
			throw e;
		}
	}
	
	@GetMapping("/getDiffTIDReadExpected")
	public List<StreamEPCDifference> getDiffTIDReadExpected() throws Exception {
		try {
			return tunnelService.getDiffEPCReadExpected();
		} catch (Exception e) {
			throw e;
		}
	}
	
}
