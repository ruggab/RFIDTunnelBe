package net.mcsistemi.rfidtunnel.services;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.impinj.octane.OctaneSdkException;

import net.mcsistemi.rfidtunnel.entity.Antenna;
import net.mcsistemi.rfidtunnel.entity.Reader;
import net.mcsistemi.rfidtunnel.entity.ReaderRfidInpinj;
import net.mcsistemi.rfidtunnel.entity.ReaderRfidWirama;
import net.mcsistemi.rfidtunnel.entity.TipoReader;
import net.mcsistemi.rfidtunnel.entity.TunnelLog;
import net.mcsistemi.rfidtunnel.job.JobImpinj;
import net.mcsistemi.rfidtunnel.job.JobWirama;
import net.mcsistemi.rfidtunnel.job.PoolImpinjReader;
import net.mcsistemi.rfidtunnel.job.PoolWiramaReader;
import net.mcsistemi.rfidtunnel.repository.AntennaRepository;
import net.mcsistemi.rfidtunnel.repository.ReaderRepository;
import net.mcsistemi.rfidtunnel.repository.TipoReaderRepository;
import net.mcsistemi.rfidtunnel.repository.TunnelLogRepository;

@Service
public class ReaderService implements IReaderService {

	@Autowired
	private TipoReaderRepository tipoReaderRepository;

	@Autowired
	private ReaderRepository readerRepository;

	@Autowired
	private TunnelLogRepository tunnelLogRepository;

	@Autowired
	private AntennaRepository antennaRepository;

	public List<TipoReader> findAllTipoReader() {
		return tipoReaderRepository.findAll();
	}

	public Reader getReaderById(Long readerId) throws Exception {

		Optional<Reader> reader = readerRepository.findById(readerId);
		Reader readerObj = reader.get();

		readerObj.getListAntenna().addAll(antennaRepository.findByIdReader(readerId));

		return reader.get();

	}

	public void createReader(Reader reader) throws Exception {
		Reader rr = null;
		if (reader.getIdTipoReader() == 1) {
			rr = (ReaderRfidInpinj) reader;
		} else {
			rr = (ReaderRfidWirama) reader;
		}
		List<Reader> list = readerRepository.findByIpAdressAndPortaOrderByIdTipoReader(reader.getIpAdress(), reader.getPorta());
		if (list.size() > 0) {
			throw new Exception("Attenzione IP e Porta già in uso per altro Reader");
		}
		//
		// Reader reader = ReaderFactory.getReader(readerForm);
		readerRepository.save(reader);

		//
		// List<Reader> readerList = readerRepository.findAll();
		// return readerList;
	}

	public List<Reader> getAllReader() throws Exception {
		//
		List<Reader> readerList = readerRepository.findAll();
		return readerList;
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

				JobWirama readerWiramaJob = new JobWirama((ReaderRfidWirama) reader, this);
				PoolWiramaReader.addThread(reader.getId(), readerWiramaJob);
				readerWiramaJob.start();
			}
			if (reader instanceof ReaderRfidInpinj) {
				JobImpinj jobImpinj = new JobImpinj((ReaderRfidInpinj) reader, this);
				PoolImpinjReader.addJob(reader.getId(), jobImpinj);
				jobImpinj.start();
			}
			reader.setStato(true);
			readerRepository.save(reader);
			listReader = readerRepository.findAll();
		} catch (OctaneSdkException ex) {
			System.out.println(ex.getMessage());
			throw ex;
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace(System.out);
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
				JobWirama jobWirama = (JobWirama) PoolWiramaReader.getThread(reader.getId());
				jobWirama.stop();
			} catch (Exception e) {
				throw e;
			} finally {
				PoolWiramaReader.removeThread(reader.getId());
				reader.setStato(false);
				readerRepository.save(reader);
				list = readerRepository.findAll();
			}
		}
		if (reader instanceof ReaderRfidInpinj) {
			try {
				JobImpinj jobImpinj = (JobImpinj) PoolImpinjReader.getJob(reader.getId());
				jobImpinj.stop();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				PoolImpinjReader.removeJob(reader.getId());
				reader.setStato(false);
				readerRepository.save(reader);
				list = readerRepository.findAll();
			}

		}

		return list;
	}

	public void createReaderlog(String ipAdress, String port, Date time, String msg) throws Exception {
		TunnelLog tunnelLog = new TunnelLog();
		tunnelLog.setTimeStamp(time);
		tunnelLog.setMessage(msg);

		tunnelLogRepository.save(tunnelLog);
	}
	
	public void save(Reader reader) throws Exception {
		
		readerRepository.save(reader);
	}
	
	public List<Antenna> getAllAntenna(Long readerId) throws Exception {
		List<Antenna> listAntenna = antennaRepository.findByIdReader(readerId);
		return listAntenna;
	}

}