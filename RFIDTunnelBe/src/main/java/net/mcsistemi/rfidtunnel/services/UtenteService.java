package net.mcsistemi.rfidtunnel.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.mcsistemi.rfidtunnel.entity.Funzione;
import net.mcsistemi.rfidtunnel.entity.FunzioneProfilo;
import net.mcsistemi.rfidtunnel.entity.ProfiloUtente;
import net.mcsistemi.rfidtunnel.entity.Utente;
import net.mcsistemi.rfidtunnel.repository.FunzioneProfiloRepository;
import net.mcsistemi.rfidtunnel.repository.FunzioneRepository;
import net.mcsistemi.rfidtunnel.repository.ProfiloRepository;
import net.mcsistemi.rfidtunnel.repository.ProfiloUtenteRepository;
import net.mcsistemi.rfidtunnel.repository.UtenteRepository;

@Service
public class UtenteService implements IUtenteService {

	@Autowired
	private UtenteRepository utenteRepository;
	
	@Autowired
	private ProfiloRepository profiloRepository;
	
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
		if (userList.size() == 1) {
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
			throw new Exception("Attenzione due utenti con stessa user");
		}
		
		return utente;
	}

}