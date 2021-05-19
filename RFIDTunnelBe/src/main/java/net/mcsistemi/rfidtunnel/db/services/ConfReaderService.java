package net.mcsistemi.rfidtunnel.db.services;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.impinj.octane.Tag;

import net.mcsistemi.rfidtunnel.db.entity.ConfAntenna;
import net.mcsistemi.rfidtunnel.db.entity.ConfPorta;
import net.mcsistemi.rfidtunnel.db.entity.ConfReader;
import net.mcsistemi.rfidtunnel.db.entity.ReaderStream;
import net.mcsistemi.rfidtunnel.db.repository.ConfAntennaRepository;
import net.mcsistemi.rfidtunnel.db.repository.ConfPortRepository;
import net.mcsistemi.rfidtunnel.db.repository.ConfReaderRepository;
import net.mcsistemi.rfidtunnel.db.repository.ReaderStreamRepository;

@Service
public class ConfReaderService implements IReaderService {


	@Autowired
	private ConfReaderRepository confReaderRepository;
	
	
	@Autowired
	private ConfAntennaRepository confAntennaRepository;
	
	@Autowired
	private ConfPortRepository confPortRepository;
	
	@Autowired
	private ReaderStreamRepository readerStreamRepository;
	
	
	public ConfReader getConfReaderByTunnelAndDispo(ConfReader confReader) throws Exception {
		ConfReader cr = new ConfReader();
		List<ConfReader> confReaderList = confReaderRepository.findByIdTunnelAndIdDispositivo(confReader.getIdTunnel(), confReader.getIdDispositivo());
		if (confReaderList.size() > 0) {
			cr = confReaderList.get(0);
			cr.getAntennas().addAll(confAntennaRepository.findByIdReader(cr.getId()));
			cr.getPorts().addAll(confPortRepository.findByIdReader(cr.getId()));
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
			confPortRepository.deleteByIdReader(confReader.getId());
			confReaderRepository.delete(confReader);
		}
		//confReaderRepository.deleteByIdTunnelAndIdDispositivo(confReader.getIdTunnel(), confReader.getIdDispositivo());
		//
		ConfReader cf = confReaderRepository.save(confReader);
		List<ConfAntenna> listaAntenna = confReader.getAntennas();
		for (Iterator iterator = listaAntenna.iterator(); iterator.hasNext();) {
			ConfAntenna antenna = (ConfAntenna) iterator.next();
			antenna.setIdConfReader(cf.getId());
			confAntennaRepository.save(antenna);
		}
		List<ConfPorta> listaPort = confReader.getPorts();
		for (Iterator iterator = listaPort.iterator(); iterator.hasNext();) {
			ConfPorta port = (ConfPorta) iterator.next();
			port.setIdConfReader(cf.getId());
			confPortRepository.save(port);
		}

	}

		
	

	public List<ConfAntenna> getAllAntenna(Long readerId) throws Exception {
		List<ConfAntenna> listAntenna = confAntennaRepository.findByIdReader(readerId);
		return listAntenna;
	}
	
	public List<ConfPorta> getAllConfPort(Long readerId) throws Exception {
		List<ConfPorta> listConfPort = confPortRepository.findByIdReader(readerId);
		return listConfPort;
	}
	
	
	
	
	

}