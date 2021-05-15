package net.mcsistemi.rfidtunnel.db.services;

import java.util.List;

import org.springframework.stereotype.Service;

import net.mcsistemi.rfidtunnel.db.entity.Utente;

@Service
public interface IUtenteService {

	public List<Utente> getAllUsers() throws Exception;

	public Utente getUsersByUsrAndPsw(String usr, String psw) throws Exception;

}