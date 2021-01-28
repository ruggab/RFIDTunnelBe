package net.mcsistemi.rfidtunnel.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
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

import net.mcsistemi.rfidtunnel.entity.Reader;
import net.mcsistemi.rfidtunnel.entity.TipoReader;
import net.mcsistemi.rfidtunnel.exception.ResourceNotFoundException;
import net.mcsistemi.rfidtunnel.form.ReaderForm;
import net.mcsistemi.rfidtunnel.services.ReaderService;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:4200")
public class ReaderRestAPIs {

	@Autowired
	private ReaderService readerService;

	@GetMapping("/tipoReaderList")
	public List<TipoReader> getAllTipoReader() throws Exception {
		List<TipoReader> listTipoReader = null;
		try {
			listTipoReader = readerService.findAllTipoReader();
		} catch (Exception e) {
			throw e;
		}
		return listTipoReader;
	}
	
	@PostMapping("/creaReader")
	public void creaReader(@RequestBody ReaderForm readerForm) throws Exception, ResourceNotFoundException {
		try {
			readerService.createReader(readerForm);
		} catch (Exception e) {
			throw e;
		}

	}
	 
  
	@GetMapping("/allreader")
	public List<Reader> getAllReader() throws Exception, ResourceNotFoundException {
		try {
			return readerService.getAllReader();
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	@DeleteMapping("/deleteReader/{id}")
	public void deleteReader(@PathVariable(value = "id") Long readerId) throws Exception {
		try {
			readerService.deleteReader(readerId);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@PutMapping("/updateReader")
	public void updateReader(@RequestBody ReaderForm readerForm) throws Exception, ResourceNotFoundException {
		try {
			readerService.updateReader(readerForm);
		} catch (Exception e) {
			throw e;
		}

	}
	
	@PostMapping("/startReader")
	public Map<String, String>  startReader(@RequestBody ReaderForm readerForm) throws Exception, ResourceNotFoundException {
		try {
			readerService.startReader(readerForm);
			Map<String, String> response = new HashMap<>();
			response.put("stato", "ok");
			response.put("msg", "Reader started");
			return response;
		} catch (Exception e) {
			throw e;
		}

	}
	
	@PostMapping("/stopReader")
	public Map<String, String> stopReader(@RequestBody ReaderForm readerForm) throws Exception, ResourceNotFoundException {
		try {
			readerService.stopReader(readerForm);
			Map<String, String> response = new HashMap<>();
			response.put("stato", "ok");
			response.put("msg", "Reader stopped");
			return response;
		} catch (Exception e) {
			throw e;
		}

	}


}
