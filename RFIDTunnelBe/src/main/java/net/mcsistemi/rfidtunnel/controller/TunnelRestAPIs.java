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
import org.springframework.web.bind.annotation.RestController;

import net.mcsistemi.rfidtunnel.entity.Antenna;
import net.mcsistemi.rfidtunnel.entity.Dispositivo;
import net.mcsistemi.rfidtunnel.entity.Tipologica;
import net.mcsistemi.rfidtunnel.entity.Tunnel;
import net.mcsistemi.rfidtunnel.exception.ResourceNotFoundException;
import net.mcsistemi.rfidtunnel.services.DispositivoService;
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
	
	
	
}