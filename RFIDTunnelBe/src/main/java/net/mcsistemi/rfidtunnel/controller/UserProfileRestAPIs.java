package net.mcsistemi.rfidtunnel.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.mcsistemi.rfidtunnel.entity.Utente;
import net.mcsistemi.rfidtunnel.services.UtenteService;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:4200")
public class UserProfileRestAPIs {

	@Autowired
	private UtenteService utenteService;

	@GetMapping("/login")
	public Utente login(@Valid @RequestParam String user, @Valid @RequestParam String password) throws Exception {
		Utente utente = null;
		try {
			utente =  utenteService.getUsersByUsrAndPsw(user, password);
		} catch (Exception e) {
			throw e;
		}
		return utente;
	}

}
