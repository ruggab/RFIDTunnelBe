package net.smart.rfid.tunnel.db.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.smart.rfid.tunnel.db.entity.Tipologica;
import net.smart.rfid.tunnel.db.repository.TipologicaRepository;

@Service
public class TipologicaService{

	
	@Autowired
	private TipologicaRepository tipologicaRepository;
	
	
	
	
	
	
	public List<Tipologica> getListTipologica(String contesto) {
		return tipologicaRepository.findByContesto(contesto);
	}
	
	
	public String getDescrizioneById(Long id) {
		Optional<Tipologica> opt = tipologicaRepository.findById(id);
		String desc = opt.get().getDescrizione();
		return desc;
	}

	
	
	
	
}