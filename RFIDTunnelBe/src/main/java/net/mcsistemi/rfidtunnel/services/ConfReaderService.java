package net.mcsistemi.rfidtunnel.services;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.impinj.octane.Tag;

import net.mcsistemi.rfidtunnel.entity.Antenna;
import net.mcsistemi.rfidtunnel.entity.ConfAntenna;
import net.mcsistemi.rfidtunnel.entity.ConfReader;
import net.mcsistemi.rfidtunnel.entity.Reader;
import net.mcsistemi.rfidtunnel.entity.ReaderRfidInpinj;
import net.mcsistemi.rfidtunnel.entity.ReaderRfidWirama;
import net.mcsistemi.rfidtunnel.entity.ReaderStream;
import net.mcsistemi.rfidtunnel.repository.ConfAntennaRepository;
import net.mcsistemi.rfidtunnel.repository.ConfReaderRepository;

@Service
public class ConfReaderService implements IReaderService {


	@Autowired
	private ConfReaderRepository confReaderRepository;
	
	
	@Autowired
	private ConfAntennaRepository confAntennaRepository;

	
	
	
	
	

	public ConfReader getReaderById(Long readerId) throws Exception {
		ConfReader cr = new ConfReader();
		Optional<ConfReader> confReader = confReaderRepository.findById(readerId);
		if (confReader.isPresent()) {
			cr = confReader.get();
		}

		cr.getAntennas().addAll(confAntennaRepository.findByIdReader(readerId));

		return cr;

	}

	public void createReader(ConfReader reader) throws Exception {
		confReaderRepository.save(reader);
	}

	
	
	
	public List<ConfReader> getAllReader() throws Exception {
		//
		List<ConfReader> readerList = confReaderRepository.findAll();
		return readerList;
	}

	public void deleteReader(Long readerId) throws Exception {

		confReaderRepository.deleteById(readerId);

	}

//	@Transactional
//	public void updateReader(Reader reader) throws Exception {
//
//		antennaRepository.deleteByIdReader(reader.getId());
//		readerRepository.deleteById(reader.getId());
//		Reader r = null;
//		if (reader.getIdTipoReader() == 1) {
//			r = (ReaderRfidInpinj) reader;
//		} else {
//			r = (ReaderRfidWirama) reader;
//		}
//
//		//
//		// Reader reader = ReaderFactory.getReader(readerForm);
//		r = readerRepository.save(r);
//		List<Antenna> listaAntenna = reader.getListAntenna();
//		for (Iterator iterator = listaAntenna.iterator(); iterator.hasNext();) {
//			Antenna antenna = (Antenna) iterator.next();
//			antenna.setIdReader(r.getId());
//			antennaRepository.save(antenna);
//		}
//
//	}

		
	

	public List<ConfAntenna> getAllAntenna(Long readerId) throws Exception {
		List<ConfAntenna> listAntenna = confAntennaRepository.findByIdReader(readerId);
		return listAntenna;
	}
	
	

}