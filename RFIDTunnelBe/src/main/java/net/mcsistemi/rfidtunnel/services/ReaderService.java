package net.mcsistemi.rfidtunnel.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import net.mcsistemi.rfidtunnel.entity.Reader;
import net.mcsistemi.rfidtunnel.entity.ReaderFactory;
import net.mcsistemi.rfidtunnel.entity.ReaderRfidInpinj;
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

	public List<Reader> createReader(ReaderForm readerForm) throws Exception  {
		
		 List<Reader> list = readerRepository.findByIpAdressAndPorta(readerForm.getIpAdress(), readerForm.getPorta());
		 if (list.size() > 0) {
			 throw new Exception("Attenzione IP e Porta già in uso per altro Reader");
		 }
		 //
		 Reader reader = ReaderFactory.getReader(readerForm);
		 readerRepository.save(reader);
		 
		 //
		 List<Reader> readerList = readerRepository.findAll();
		 return readerList;
	}
	
	public List<Reader> getAllReader() throws Exception  {
		 //
		 List<Reader> readerList = readerRepository.findAll();
		 return readerList;
	}
	
	
	public List<Reader> deleteEmployee(Long readerId) throws Exception {
		
		readerRepository.deleteById(readerId);
		List<Reader> readerList = readerRepository.findAll();
		return readerList;
	}
	
	public void updateReader(ReaderForm readerForm) throws Exception  {
		
		 List<Reader> list = readerRepository.findByIpAdressAndPorta(readerForm.getIpAdress(), readerForm.getPorta());
		 if (list.size() > 0) {
			 Reader reader = list.get(0);
			 readerRepository.deleteById(reader.getId());
		 }
		 //
		 Reader reader = ReaderFactory.getReader(readerForm);
		 readerRepository.save(reader);
		 
	}
	
}