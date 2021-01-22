package net.mcsistemi.rfidtunnel.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.mcsistemi.rfidtunnel.entity.Reader;
import net.mcsistemi.rfidtunnel.entity.TipoReader;
import net.mcsistemi.rfidtunnel.model.ReaderForm;
import net.mcsistemi.rfidtunnel.repository.ReaderRepository;
import net.mcsistemi.rfidtunnel.repository.TipoReaderRepository;

@Service
public class ReaderService implements IReaderService {

	@Autowired
	private TipoReaderRepository tipoReaderRepository;
	
	@Autowired
	private ReaderRepository readerRepository;


	public List<TipoReader> findAllTipoReader() {
		return tipoReaderRepository.findAll();
	}

	public List<Reader> createReader(ReaderForm readerForm) {
		 Reader reader = new Reader();
		 reader.setIdTipoReader(new Long(readerForm.getTipoReaderSel()));
		 reader.setIpadress(readerForm.getIpAdress());
		 reader.setPort(readerForm.getPorta());
		 readerRepository.save(reader);
		 //
		 List<Reader> readerList = readerRepository.findAll();
		 return readerList;
	}
	
}