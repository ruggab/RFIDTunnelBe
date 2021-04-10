package net.mcsistemi.rfidtunnel.services;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.impinj.octane.OctaneSdkException;
import com.impinj.octane.Tag;

import net.mcsistemi.rfidtunnel.controller.DispositiviRestAPIs;
import net.mcsistemi.rfidtunnel.entity.Antenna;
import net.mcsistemi.rfidtunnel.entity.Dispositivo;
import net.mcsistemi.rfidtunnel.entity.Reader;
import net.mcsistemi.rfidtunnel.entity.ReaderRfidInpinj;
import net.mcsistemi.rfidtunnel.entity.ReaderRfidWirama;
import net.mcsistemi.rfidtunnel.entity.ReaderStream;
import net.mcsistemi.rfidtunnel.entity.Tipologica;
import net.mcsistemi.rfidtunnel.job.JobImpinj;
import net.mcsistemi.rfidtunnel.job.JobWiramaReader;
import net.mcsistemi.rfidtunnel.job.PoolImpinjReader;
import net.mcsistemi.rfidtunnel.job.PoolWiramaReader;
import net.mcsistemi.rfidtunnel.repository.AntennaRepository;
import net.mcsistemi.rfidtunnel.repository.DispositivoRepository;
import net.mcsistemi.rfidtunnel.repository.ReaderRepository;
import net.mcsistemi.rfidtunnel.repository.ReaderStreamRepository;
import net.mcsistemi.rfidtunnel.repository.TipoReaderRepository;
import net.mcsistemi.rfidtunnel.repository.TipologicaRepository;

@Service
public class DispositivoService implements IReaderService {

	@Autowired
	private TipoReaderRepository tipoReaderRepository;
	
	@Autowired
	private TipologicaRepository tipologicaRepository;

	@Autowired
	private ReaderRepository readerRepository;
	
	@Autowired
	private DispositivoRepository dispositivoRepository;


	@Autowired
	private ReaderStreamRepository readerStreamRepository;

	@Autowired
	private AntennaRepository antennaRepository;

	public List<Tipologica> findAllTipoReader() {
		return tipoReaderRepository.findAll();
	}
	
	public List<Tipologica> getAllTipoDispositivi() {
		return tipologicaRepository.findByContesto("DISPOSITIVO");
	}
	
	public List<Tipologica> getAllTipoReader() {
		return tipologicaRepository.findByContesto("TIPO_READER");
	}
	
	

	public Reader getReaderById(Long readerId) throws Exception {

		Optional<Reader> reader = readerRepository.findById(readerId);
		Reader readerObj = reader.get();

		readerObj.getListAntenna().addAll(antennaRepository.findByIdReader(readerId));

		return reader.get();

	}
	
	public Dispositivo getDispositivoById(Long dispositivoId) throws Exception {

		Optional<Dispositivo> dispositivo = dispositivoRepository.findById(dispositivoId);
		Dispositivo dispositivoObj = dispositivo.get();

		
		return dispositivoObj;

	}

	public void createReader(Reader reader) throws Exception {
		Reader rr = null;
		if (reader.getIdTipoReader() == 1) {
			rr = (ReaderRfidInpinj) reader;
		} else {
			rr = (ReaderRfidWirama) reader;
		}
		List<Reader> list = readerRepository.findByIpAdressOrderByIdTipoReader(reader.getIpAdress());
		if (list.size() > 0) {
			throw new Exception("Attenzione IP già in uso per altro Reader");
		}
		//
		// Reader reader = ReaderFactory.getReader(readerForm);
		readerRepository.save(reader);

		//
		// List<Reader> readerList = readerRepository.findAll();
		// return readerList;
	}

	public void createDispositivo(Dispositivo dispositivo) throws Exception {
		Reader rr = null;
		//Controllo se esiste un dispositivo gia presente con lo stesso ipadress
		List<Dispositivo> list = dispositivoRepository.findByIpAdress(dispositivo.getIpAdress());
		if (list.size() > 0) {
			throw new Exception("Attenzione IP già in uso per altro Dispositivo");
		}
		//
		//Salva dispositivo
		dispositivoRepository.save(dispositivo);
	}
	
	
	public List<Reader> getAllReader() throws Exception {
		//
		List<Reader> readerList = readerRepository.findAll(Sort.by(Sort.Direction.ASC, "ipAdress"));
		return readerList;
	}
	
	public List<Dispositivo> getAllDispositivi() throws Exception {
		//
		List<Dispositivo> dispositivoList = dispositivoRepository.findAll(Sort.by(Sort.Direction.ASC, "ipAdress"));
		return dispositivoList;
	}

	public void deleteReader(Long readerId) throws Exception {
		readerRepository.deleteById(readerId);
	}

	@Transactional
	public void updateReader(Reader reader) throws Exception {

		antennaRepository.deleteByIdReader(reader.getId());
		readerRepository.deleteById(reader.getId());
		Reader r = null;
		if (reader.getIdTipoReader() == 1) {
			r = (ReaderRfidInpinj) reader;
		} else {
			r = (ReaderRfidWirama) reader;
		}

		//
		// Reader reader = ReaderFactory.getReader(readerForm);
		r = readerRepository.save(r);
		List<Antenna> listaAntenna = reader.getListAntenna();
		for (Iterator iterator = listaAntenna.iterator(); iterator.hasNext();) {
			Antenna antenna = (Antenna) iterator.next();
			antenna.setIdReader(r.getId());
			antennaRepository.save(antenna);
		}

	}

	public List<Reader> startReader(Reader reader) throws Exception {
		List<Reader> listReader = null;
		try {
			reader = readerRepository.findById(reader.getId()).get();
			reader.getListAntenna().addAll(antennaRepository.findByIdReader(reader.getId()));
			// if (list.size() > 0) {
			// reader = list.get(0);
			// }

			if (reader instanceof ReaderRfidWirama) {
				ReaderRfidWirama readerRfidWirama = (ReaderRfidWirama) reader;
				JobWiramaReader jobWiramaReader = new JobWiramaReader((ReaderRfidWirama) reader, this);
				//JobWiramaCommand jobWiramaCommand = new JobWiramaCommand((ReaderRfidWirama) reader, this);
				PoolWiramaReader.addThread(readerRfidWirama.getIpAdress()+readerRfidWirama.getPorta(), jobWiramaReader);
				//PoolWiramaReader.addThread(reader.getIpAdress() + readerRfidWirama.getPortaComandi(), jobWiramaCommand);
				jobWiramaReader.start();
				//jobWiramaCommand.start();
			}
			if (reader instanceof ReaderRfidInpinj) {
				JobImpinj jobImpinj = new JobImpinj((ReaderRfidInpinj) reader, this);
				PoolImpinjReader.addJob(reader.getId(), jobImpinj);
				jobImpinj.start();
			}
			reader.setStato(true);
			readerRepository.save(reader);
			listReader = readerRepository.findAll(Sort.by(Sort.Direction.ASC, "ipAdress"));
		} catch (OctaneSdkException ex) {
			System.out.println(ex.getMessage());
			listReader = this.stopReader(reader);
			throw ex;
		} catch (Exception ex) {
			listReader = this.stopReader(reader);
			System.out.println(ex.getMessage());
			throw ex;
		}
		return listReader;
	}

	public List<Reader> stopReader(Reader reader) throws Exception {
		List<Reader> list = null;
		// List<Reader> list = readerRepository.findByIpAdressAndPortaOrderByIdTipoReader(readerForm.getIpAdress(),
		// readerForm.getPorta());
		reader = readerRepository.findById(reader.getId()).get();

		if (reader instanceof ReaderRfidWirama) {
			try {
				ReaderRfidWirama readerRfidWirama = (ReaderRfidWirama) reader;
				JobWiramaReader jobWirama = (JobWiramaReader) PoolWiramaReader.getThread(reader.getIpAdress() + readerRfidWirama.getPorta());
				//JobWiramaCommand jobWiramaCommand = (JobWiramaCommand) PoolWiramaReader.getThread(reader.getIpAdress() + readerRfidWirama.getPorta());
				jobWirama.stop();
				//jobWiramaCommand.stop();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				PoolWiramaReader.removeThread(reader.getIpAdress());
				reader.setStato(false);
				readerRepository.save(reader);
				list = readerRepository.findAll(Sort.by(Sort.Direction.ASC, "ipAdress"));
			}
		}
		if (reader instanceof ReaderRfidInpinj) {
			try {
				JobImpinj jobImpinj = (JobImpinj) PoolImpinjReader.getJob(reader.getId());
				if (jobImpinj != null) {
					jobImpinj.stop();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				// PoolImpinjReader.removeJob(reader.getId());
				reader.setStato(false);
				readerRepository.save(reader);
				list = readerRepository.findAll(Sort.by(Sort.Direction.ASC, "ipAdress"));
			}

		}

		return list;
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

	public void save(Reader reader) throws Exception {

		readerRepository.save(reader);
	}

	public List<Antenna> getAllAntenna(Long readerId) throws Exception {
		List<Antenna> listAntenna = antennaRepository.findByIdReader(readerId);
		return listAntenna;
	}
	
	public List<ReaderStream> getAllDataReader() throws Exception {
		//
		List<ReaderStream> readerDataList = readerStreamRepository.findAll(Sort.by(Sort.Direction.ASC, "ipAdress"));
		return readerDataList;
	}

}