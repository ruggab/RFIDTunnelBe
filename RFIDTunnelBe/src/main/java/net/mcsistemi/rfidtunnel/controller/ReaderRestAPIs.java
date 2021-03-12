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

import net.mcsistemi.rfidtunnel.entity.Antenna;
import net.mcsistemi.rfidtunnel.entity.Reader;
import net.mcsistemi.rfidtunnel.entity.ReaderRfidInpinj;
import net.mcsistemi.rfidtunnel.entity.ReaderRfidWirama;
import net.mcsistemi.rfidtunnel.entity.ReaderStream;
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
	
	@GetMapping("/reader/{id}")
	public Reader getReaderById(@PathVariable(value = "id") Long readerId) throws Exception {
		try {
			return readerService.getReaderById(readerId);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@PostMapping("/creaReaderInpinj")
	public void creaReaderInpinj(@RequestBody ReaderRfidInpinj reader) throws Exception, ResourceNotFoundException {
		try {
			readerService.createReader(reader);
		} catch (Exception e) {
			throw e;
		}
	}
	 
	@PostMapping("/creaReaderWirama")
	public void creaReaderWirama(@RequestBody ReaderRfidWirama reader) throws Exception, ResourceNotFoundException {
		try {
			readerService.createReader(reader);
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
	
	@GetMapping("/allAntenna/{id}")
	public List<Antenna> getAllAntenna(@PathVariable(value = "id") Long readerId) throws Exception, ResourceNotFoundException {
		try {
			return readerService.getAllAntenna(readerId);
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
	public void updateReader(@RequestBody Reader reader) throws Exception, ResourceNotFoundException {
		try {
			readerService.updateReader(reader);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@PutMapping("/updateReaderInpinj")
	public void updateReaderInpinj(@RequestBody ReaderRfidInpinj reader) throws Exception, ResourceNotFoundException {
		try {
			readerService.updateReader(reader);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@PutMapping("/updateReaderWirama")
	public void updateReaderWirama(@RequestBody ReaderRfidWirama reader) throws Exception, ResourceNotFoundException {
		try {
			readerService.updateReader(reader);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@PostMapping("/startReader")
	public List<Reader>  startReader(@RequestBody Reader reader) throws Exception, ResourceNotFoundException {
		try {
			List<Reader> listReader = readerService.startReader(reader);
			Map<String, String> response = new HashMap<>();
			response.put("stato", "ok");
			response.put("msg", "Reader started");
			return listReader;
		} catch (Exception e) {
			throw e;
		}

	}
	
	@PostMapping("/stopReader")
	public List<Reader> stopReader(@RequestBody Reader reader) throws Exception, ResourceNotFoundException {
		try {
			List<Reader> listReader =  readerService.stopReader(reader);
			Map<String, String> response = new HashMap<>();
			response.put("stato", "ok");
			response.put("msg", "Reader stopped");
			return listReader;
		} catch (Exception e) {
			throw e;
		}

	}
	
	
	@GetMapping("/allDataReader")
	public List<ReaderStream> getAllDataReader() throws Exception, ResourceNotFoundException {
		try {
			return readerService.getAllDataReader();
		} catch (Exception e) {
			throw e;
		}
	}


}
