package net.mcsistemi.rfidtunnel.services;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.mcsistemi.rfidtunnel.entity.ConfAntenna;
import net.mcsistemi.rfidtunnel.entity.ConfReader;
import net.mcsistemi.rfidtunnel.repository.ConfAntennaRepository;
import net.mcsistemi.rfidtunnel.repository.ConfReaderRepository;

@Service
public class ConfReaderService implements IReaderService {


	@Autowired
	private ConfReaderRepository confReaderRepository;
	
	
	@Autowired
	private ConfAntennaRepository confAntennaRepository;
	
	
	
	public ConfReader getConfReaderByTunnelAndDispo(ConfReader confReader) throws Exception {
		ConfReader cr = new ConfReader();
		List<ConfReader> confReaderList = confReaderRepository.findByIdTunnelAndIdDispositivo(confReader.getIdTunnel(), confReader.getIdDispositivo());
		if (confReaderList.size() > 0) {
			cr = confReaderList.get(0);
			cr.getAntennas().addAll(confAntennaRepository.findByIdReader(cr.getId()));
		}
		return cr;
	}
	
	
	public ConfReader getReaderById(Long readerId) throws Exception {
		ConfReader cr = new ConfReader();
		Optional<ConfReader> confReader = confReaderRepository.findById(readerId);
		if (confReader.isPresent()) {
			cr = confReader.get();
		}

		cr.getAntennas().addAll(confAntennaRepository.findByIdReader(readerId));

		return cr;

	}

	public void save(ConfReader reader) throws Exception {
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

	@Transactional
	public void updateConfReader(ConfReader confReader) throws Exception {
		if (confReader.getId()!=null) {
			confAntennaRepository.deleteByIdReader(confReader.getId());
		}
		confReaderRepository.deleteByIdTunnelAndIdDispositivo(confReader.getIdTunnel(), confReader.getIdDispositivo());
		//
		ConfReader cf = confReaderRepository.save(confReader);
		List<ConfAntenna> listaAntenna = confReader.getAntennas();
		for (Iterator iterator = listaAntenna.iterator(); iterator.hasNext();) {
			ConfAntenna antenna = (ConfAntenna) iterator.next();
			antenna.setIdConfReader(cf.getId());
			confAntennaRepository.save(antenna);
		}

	}

		
	

	public List<ConfAntenna> getAllAntenna(Long readerId) throws Exception {
		List<ConfAntenna> listAntenna = confAntennaRepository.findByIdReader(readerId);
		return listAntenna;
	}
	
	

}