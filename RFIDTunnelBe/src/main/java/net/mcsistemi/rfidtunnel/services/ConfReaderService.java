package net.mcsistemi.rfidtunnel.services;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.impinj.octane.Tag;

import net.mcsistemi.rfidtunnel.entity.ConfAntenna;
import net.mcsistemi.rfidtunnel.entity.ConfPorta;
import net.mcsistemi.rfidtunnel.entity.ConfReader;
import net.mcsistemi.rfidtunnel.entity.ReaderStream;
import net.mcsistemi.rfidtunnel.repository.ConfAntennaRepository;
import net.mcsistemi.rfidtunnel.repository.ConfPortRepository;
import net.mcsistemi.rfidtunnel.repository.ConfReaderRepository;
import net.mcsistemi.rfidtunnel.repository.ReaderStreamRepository;

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
	
	
	
	public void createReaderStream(String ipAdress, String port, String epc, String tid, String user, String packId,Timestamp time) throws Exception {
		ReaderStream readerStream = new ReaderStream();
		
		readerStream.setEpc(epc);
		readerStream.setTimeStamp(time);
		readerStream.setTid(tid);
		readerStream.setIpAdress(ipAdress);
		readerStream.setPort(port);
		readerStream.setPackId(packId);
		readerStream.setUserData(user);
		readerStreamRepository.save(readerStream);
	}


	public void createReaderStream(String ipAdress, String port, String packId, Tag tag) throws Exception {
		ReaderStream readerStream = new ReaderStream();
		
		readerStream.setEpc(tag.getEpc().toHexString());
		readerStream.setTimeStamp(new Timestamp(System.currentTimeMillis()));
		readerStream.setTid(tag.getTid().toHexString());
		readerStream.setIpAdress(ipAdress);
		readerStream.setPort(port);
		readerStream.setPackId(packId);
		readerStream.setUserData("");
		readerStream.setAntennaPortNumber(tag.getAntennaPortNumber()+"");
		readerStream.setChannelInMhz(tag.getChannelInMhz()+"");
		readerStream.setFirstSeenTime(tag.getFirstSeenTime()+"");
		readerStream.setLastSeenTime(tag.getLastSeenTime()+"");
		readerStream.setModelName(tag.getModelDetails().getModelName().name());
		readerStream.setPeakRssiInDbm(tag.getPeakRssiInDbm()+"");
		readerStream.setPhaseAngleInRadians(tag.getPhaseAngleInRadians()+"");
		readerStream.setRfDopplerFrequency(tag.getRfDopplerFrequency()+"");
		readerStream.setTagSeenCount(tag.getTagSeenCount()+"");
		readerStream.setUserData("");
		readerStream.setFirstSeenTime(tag.getFirstSeenTime()+"");
		readerStream.setLastSeenTime(tag.getLastSeenTime()+"");
		readerStreamRepository.save(readerStream);
	}
	

}