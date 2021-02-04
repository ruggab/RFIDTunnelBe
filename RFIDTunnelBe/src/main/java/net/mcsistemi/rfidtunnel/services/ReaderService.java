package net.mcsistemi.rfidtunnel.services;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.mcsistemi.rfidtunnel.entity.Reader;
import net.mcsistemi.rfidtunnel.entity.ReaderFactory;
import net.mcsistemi.rfidtunnel.entity.ReaderRfidInpinj;
import net.mcsistemi.rfidtunnel.entity.ReaderRfidWirama;
import net.mcsistemi.rfidtunnel.entity.TipoReader;
import net.mcsistemi.rfidtunnel.entity.TunnelLog;
import net.mcsistemi.rfidtunnel.form.ReaderForm;
import net.mcsistemi.rfidtunnel.job.ControlSubThread;
import net.mcsistemi.rfidtunnel.job.ReaderWiramaJob;
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
	
	public List<TipoReader> findAllTipoReader() {
		return tipoReaderRepository.findAll();
	}
	
	public Reader getReaderById(Long readerId) throws Exception {

		Optional<Reader> reader = readerRepository.findById(readerId);
		
		return reader.get();

	}

	public void createReader(Reader reader) throws Exception {
		Reader rr = null;
		if (reader.getIdTipoReader() == 1) {
			 rr = (ReaderRfidInpinj)reader;
		} else {
			rr = (ReaderRfidWirama)reader;
		}
		List<Reader> list = readerRepository.findByIpAdressAndPorta(reader.getIpAdress(), reader.getPorta());
		if (list.size() > 0) {
			throw new Exception("Attenzione IP e Porta già in uso per altro Reader");
		}
		//
		//Reader reader = ReaderFactory.getReader(readerForm);
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

	public void updateReader(Reader reader) throws Exception {
		
		readerRepository.deleteById(reader.getId());
		
		Reader rr = null;
		if (reader.getIdTipoReader() == 1) {
			 rr = (ReaderRfidInpinj)reader;
		} else {
			rr = (ReaderRfidWirama)reader;
		}
		
		//
		//Reader reader = ReaderFactory.getReader(readerForm);
		readerRepository.save(rr);

	}

	public String startReader(ReaderForm readerForm) throws Exception {
		String ret = "OK";
		try {

			Reader reader = null;
			List<Reader> list = readerRepository.findByIpAdressAndPorta(readerForm.getIpAdress(), readerForm.getPorta());
			if (list.size() > 0) {
				reader = list.get(0);
			}

			if (reader instanceof ReaderRfidWirama) {

				ReaderWiramaJob readerWiramaJob = new ReaderWiramaJob((ReaderRfidWirama) reader, this);
				ControlSubThread.addThread(reader.getId(), readerWiramaJob);
				readerWiramaJob.start();
			}
			if (reader instanceof ReaderRfidInpinj) {
				// reader.get
			}

		} catch (Exception e) {
			ret = "KO";
		}
		return ret;
	}

	public String stopReader(ReaderForm readerForm) throws Exception {
		String ret = "OK";
		try {

			List<Reader> list = readerRepository.findByIpAdressAndPorta(readerForm.getIpAdress(), readerForm.getPorta());
			// if (list.size() > 0) {
			// throw new Exception("Attenzione Reader Ambiguo");
			// }
			//

			Reader reader = list.get(0);
			if (reader instanceof ReaderRfidWirama) {

				ReaderWiramaJob readerWiramaJob = (ReaderWiramaJob) ControlSubThread.getThread(reader.getId());
				readerWiramaJob.stop();

			}
			if (reader instanceof ReaderRfidInpinj) {
				// reader.get
			}

		} catch (Exception e) {
			ret = "KO";
		}
		return ret;
	}
	
	public void createReaderlog(String ipAdress, String port, Date time, String msg) throws Exception {
		TunnelLog tunnelLog = new TunnelLog();
		tunnelLog.setTimeStamp(time);
		tunnelLog.setMessage(msg);
		
		tunnelLogRepository.save(tunnelLog);
	}

}