package net.mcsistemi.rfidtunnel.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.mcsistemi.rfidtunnel.entity.Tipologica;
import net.mcsistemi.rfidtunnel.services.TipologicaService;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:4200")
public class TipologicaRestAPIs {

	@Autowired
	private TipologicaService tipologicaService;

	
	
	@GetMapping("/tipologica/{contesto}")
	public List<Tipologica> getAllTipoDispositivi(@PathVariable(value = "contesto") String contesto) throws Exception {
		List<Tipologica> listTipo = null;
		try {
			listTipo = tipologicaService.getListTipologica(contesto);
		} catch (Exception e) {
			throw e;
		}
		return listTipo;
	}
	
	
	
	
	
  


}
