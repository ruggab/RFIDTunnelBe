package net.mcsistemi.rfidtunnel.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.mcsistemi.rfidtunnel.entity.Reader;
import net.mcsistemi.rfidtunnel.entity.TipoReader;
import net.mcsistemi.rfidtunnel.exception.ResourceNotFoundException;
import net.mcsistemi.rfidtunnel.model.ReaderForm;
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

}
