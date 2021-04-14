package net.mcsistemi.rfidtunnel.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.mcsistemi.rfidtunnel.entity.Tipologica;
import net.mcsistemi.rfidtunnel.repository.TipologicaRepository;

@Service
public class TipologicaService implements IDispositivoService {

	
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