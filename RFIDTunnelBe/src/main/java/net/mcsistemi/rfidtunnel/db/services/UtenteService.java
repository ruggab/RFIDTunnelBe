package net.mcsistemi.rfidtunnel.db.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.mcsistemi.rfidtunnel.db.entity.Funzione;
import net.mcsistemi.rfidtunnel.db.entity.FunzioneProfilo;
import net.mcsistemi.rfidtunnel.db.entity.ProfiloUtente;
import net.mcsistemi.rfidtunnel.db.entity.Utente;
import net.mcsistemi.rfidtunnel.db.repository.FunzioneProfiloRepository;
import net.mcsistemi.rfidtunnel.db.repository.FunzioneRepository;
import net.mcsistemi.rfidtunnel.db.repository.ProfiloRepository;
import net.mcsistemi.rfidtunnel.db.repository.ProfiloUtenteRepository;
import net.mcsistemi.rfidtunnel.db.repository.UtenteRepository;

@Service
public class UtenteService  {
	
	Logger logger = Logger.getLogger(UtenteService.class);

	@Autowired
	private UtenteRepository utenteRepository;
	
	@Autowired
	private FunzioneRepository funzioneRepository;
	
	@Autowired
	private ProfiloUtenteRepository profiloUtenteRepository;
	
	@Autowired
	private FunzioneProfiloRepository funzioneProfiloRepository;


	public List<Utente> getAllUsers() {
		return utenteRepository.findAll();
	}

	public Utente getUsersByUsrAndPsw(String usr, String psw) throws Exception {
		
		Utente utente =  null;
		List<Utente> userList = utenteRepository.findByUsrAndPsw(usr, psw);
		if (userList.size() > 0) {
			utente = userList.get(0);
			utente.setListFunzioni(new ArrayList<Funzione>());
			List<ProfiloUtente> listProfiloUtente = profiloUtenteRepository.findByIdUtente(utente.getId());
			for (Iterator iterator = listProfiloUtente.iterator(); iterator.hasNext();) {
				ProfiloUtente profiloUtente = (ProfiloUtente) iterator.next();
				List<FunzioneProfilo> listFunzioneProfilo = funzioneProfiloRepository.findByIdProfilo(profiloUtente.getIdProfilo());
				for (Iterator iterator2 = listFunzioneProfilo.iterator(); iterator2.hasNext();) {
					FunzioneProfilo funzioneProfilo = (FunzioneProfilo) iterator2.next();
					Funzione funzione  = funzioneRepository.findById(funzioneProfilo.getIdFunzione()).get();
					utente.getListFunzioni().add(funzione);
				}
			}
		
		} else {
			logger.error("User o password errati");
			throw new Exception("User o password errati");
		}
		logger.info("User collegato correttamente");
		return utente;
	}

}