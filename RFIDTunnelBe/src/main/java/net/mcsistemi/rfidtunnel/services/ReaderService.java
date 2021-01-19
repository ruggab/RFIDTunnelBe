package net.mcsistemi.rfidtunnel.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.mcsistemi.rfidtunnel.entity.TipoReader;
import net.mcsistemi.rfidtunnel.repository.TipoReaderRepository;

@Service
public class ReaderService implements IReaderService {

	@Autowired
	private TipoReaderRepository tipoReaderRepository;
	
	


	public List<TipoReader> findAllTipoReader() {
		return tipoReaderRepository.findAll();
	}

	
}