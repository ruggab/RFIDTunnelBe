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
import org.springframework.web.bind.annotation.RestController;

import net.smart.rfid.tunnel.db.entity.Dispositivo;
import net.smart.rfid.tunnel.db.services.DispositivoService;
import net.smart.rfid.tunnel.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:4200")
public class DispositiviRestAPIs {

	@Autowired
	private DispositivoService dispositivoService;

	@GetMapping("/dispositivo/{id}")
	public Dispositivo getDispositivoById(@PathVariable(value = "id") Long dispositivoId) throws Exception {
		try {
			return dispositivoService.getDispositivoById(dispositivoId);
		} catch (Exception e) {
			throw e;
		}
	}

	@PostMapping("/creaDispositivo")
	public void creaDispositivo(@RequestBody Dispositivo dispositivo) throws Exception, ResourceNotFoundException {
		try {
			dispositivoService.createDispositivo(dispositivo);
		} catch (Exception e) {
			throw e;
		}
	}

	@GetMapping("/allDispositivi")
	public List<Dispositivo> getAllDispositivi() throws Exception, ResourceNotFoundException {
		try {
			return dispositivoService.getAllDispositivi();
		} catch (Exception e) {
			throw e;
		}
	}
	


	@DeleteMapping("/deleteDispositivo/{id}")
	public void deleteDispositivo(@PathVariable(value = "id") Long readerId) throws Exception {
		try {
			dispositivoService.delete(readerId);
		} catch (Exception e) {
			throw e;
		}
	}

	@PutMapping("/updateDispositivo")
	public void updateDispositivo(@RequestBody Dispositivo dispositivo) throws Exception, ResourceNotFoundException {
		try {
			dispositivoService.save(dispositivo);
		} catch (Exception e) {
			throw e;
		}
	}

	@GetMapping("/readerRfidListFromDispositivi")
	public List<Dispositivo> getReaderRfidListFromDispositivi() throws Exception, ResourceNotFoundException {
		try {
			return dispositivoService.getReaderRfidList();
		} catch (Exception e) {
			throw e;
		}
	}

	@GetMapping("/readerBarcodeListFromDispositivi")
	public List<Dispositivo> getReaderBarcodeListFromDispositivi() throws Exception, ResourceNotFoundException {
		try {
			return dispositivoService.getReaderBarcodeList();
		} catch (Exception e) {
			throw e;
		}
	}
	
	@GetMapping("/isDeviceStart/{idDevice}")
	public boolean isDeviceStart(@PathVariable(value = "idDevice") Long idDevice) throws Exception, ResourceNotFoundException {
		try {
			boolean ret = dispositivoService.isDeviceStart(idDevice);
			return ret;
		} catch (Exception e) {
			throw e;
		}
	}

	
	
}
