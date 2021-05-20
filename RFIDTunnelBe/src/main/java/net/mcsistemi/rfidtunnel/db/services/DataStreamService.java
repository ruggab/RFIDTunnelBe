package net.mcsistemi.rfidtunnel.db.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.impinj.octane.ImpinjReader;
import com.impinj.octane.Tag;

import net.mcsistemi.rfidtunnel.db.entity.ConfAntenna;
import net.mcsistemi.rfidtunnel.db.entity.ConfReader;
import net.mcsistemi.rfidtunnel.db.entity.Dispositivo;
import net.mcsistemi.rfidtunnel.db.entity.ReaderStream;
import net.mcsistemi.rfidtunnel.db.entity.ReaderStreamAtteso;
import net.mcsistemi.rfidtunnel.db.entity.ScannerStream;
import net.mcsistemi.rfidtunnel.db.entity.Tipologica;
import net.mcsistemi.rfidtunnel.db.entity.Tunnel;
import net.mcsistemi.rfidtunnel.db.repository.ConfAntennaRepository;
import net.mcsistemi.rfidtunnel.db.repository.ConfPortRepository;
import net.mcsistemi.rfidtunnel.db.repository.ConfReaderRepository;
import net.mcsistemi.rfidtunnel.db.repository.DispositivoRepository;
import net.mcsistemi.rfidtunnel.db.repository.ReaderStreamAttesoRepository;
import net.mcsistemi.rfidtunnel.db.repository.ReaderStreamRepository;
import net.mcsistemi.rfidtunnel.db.repository.ScannerStreamRepository;
import net.mcsistemi.rfidtunnel.db.repository.TipologicaRepository;
import net.mcsistemi.rfidtunnel.db.repository.TunnelRepository;
import net.mcsistemi.rfidtunnel.db.repository.ReaderStreamAttesoRepository.StreamBarcodeDifference;
import net.mcsistemi.rfidtunnel.db.repository.ReaderStreamAttesoRepository.StreamEPCDifference;
import net.mcsistemi.rfidtunnel.db.repository.ReaderStreamAttesoRepository.StreamTIDDifference;
import net.mcsistemi.rfidtunnel.db.repository.ReaderStreamAttesoRepository.StreamUserDifference;
import net.mcsistemi.rfidtunnel.db.repository.ReaderStreamRepository.ReaderStreamOnly;
import net.mcsistemi.rfidtunnel.job.JobInterface;
import net.mcsistemi.rfidtunnel.job.JobRfidImpinj;
import net.mcsistemi.rfidtunnel.job.JobScannerBarcode;
import net.mcsistemi.rfidtunnel.model.TunnelDevice;

@Service
public class DataStreamService  {

	Logger logger = Logger.getLogger(DataStreamService.class);

	@Autowired
	private DispositivoRepository dispositivoRepository;

	@Autowired
	private TipologicaRepository tipologicaRepository;


	@Autowired
	private ConfReaderRepository confReaderRepository;

	@Autowired
	private ReaderStreamRepository readerStreamRepository;

	@Autowired
	private ScannerStreamRepository scannerStreamRepository;

	@Autowired
	private ReaderStreamAttesoRepository readerStreamAttesoRepository;

	
	public ScannerStream getLastScanner() throws Exception {
		ScannerStream scannerStream = scannerStreamRepository.getLastScanner();
		return scannerStream;
	}

	

	public void createReadStream(Long idTunnel, String ipAdress, ScannerStream ss, Tag tag) throws Exception {
		ReaderStream readerStream = new ReaderStream();
		readerStream.setIdTunnel(idTunnel);
		readerStream.setEpc(tag.getEpc().toHexString());
		readerStream.setTimeStamp(new Timestamp(System.currentTimeMillis()));
		readerStream.setTid(tag.getTid().toHexString());
		readerStream.setIpAdress(ipAdress);
		readerStream.setUserData("");
		readerStream.setPackId(ss.getId());
		readerStream.setPackageData(ss.getPackageData());
		readerStream.setAntennaPortNumber(tag.getAntennaPortNumber() + "");
		readerStream.setChannelInMhz(tag.getChannelInMhz() + "");
		readerStream.setFirstSeenTime(tag.getFirstSeenTime() + "");
		readerStream.setLastSeenTime(tag.getLastSeenTime() + "");
		readerStream.setModelName(tag.getModelDetails().getModelName().name());
		readerStream.setPeakRssiInDbm(tag.getPeakRssiInDbm() + "");
		readerStream.setPhaseAngleInRadians(tag.getPhaseAngleInRadians() + "");
		readerStream.setRfDopplerFrequency(tag.getRfDopplerFrequency() + "");
		readerStream.setTagSeenCount(tag.getTagSeenCount() + "");
		readerStream.setFirstSeenTime(tag.getFirstSeenTime() + "");
		readerStream.setLastSeenTime(tag.getLastSeenTime() + "");
		readerStreamRepository.save(readerStream);

	}

	public void createScannerStream(Long tunnelId, String packageData, String dettaglio) throws Exception {
		ScannerStream ss = new ScannerStream();
		ss.setIdTunnel(tunnelId);
		ss.setPackageData(packageData);
		ss.setTimeStamp(new Date());
		ss.setDettaglio(dettaglio);
		scannerStreamRepository.save(ss);
	}

	public void saveScannerStream(ScannerStream ss) throws Exception {

		scannerStreamRepository.save(ss);
	}

	public ReaderStreamAtteso createReaderStreamAtteso(String collo, String epc, String tid) throws Exception {
		ReaderStreamAtteso rsa = new ReaderStreamAtteso();
		rsa.setPackageData(collo);
		rsa.setEpc(epc);
		rsa.setTid(tid);
		ReaderStreamAtteso readerStreamAtteso = readerStreamAttesoRepository.save(rsa);
		return readerStreamAtteso;
	}

	public List<ReaderStreamAtteso> getReaderStreamAtteso(String collo) throws Exception {

		List<ReaderStreamAtteso> listStreamAtteso = readerStreamAttesoRepository.getReaderStreamExpectedByCollo(collo);
		return listStreamAtteso;
	}

	

	public int compareByPackage(ScannerStream scannerStream, Boolean epc, Boolean tid, Boolean user, Boolean barcode, Boolean quantita) throws Exception {
		int ret = 2;
		String comp = compareQuantitaByPackage(scannerStream.getId(), scannerStream.getPackageData(), epc, tid, user, barcode, quantita);
		String quantitaRet = comp.replace("KO", "");
		if (comp.contains("OK")) {
			ret = 1;
			quantitaRet = comp.replace("OK", "");
		}
		comp = comp.replace(quantitaRet, "");
		if (!quantita && ret == 1) {
			// Se la quantita è OK allora controllo anche il contenuto in caso di selezione
			// diversa da quantita

			if (epc) {
				comp = compareEPCByPackage(scannerStream.getId(), scannerStream.getPackageData());
			}
			if (tid) {
				comp = compareTIDByPackage(scannerStream.getId(), scannerStream.getPackageData());
			}
			if (user) {
				comp = compareUserByPackage(scannerStream.getId(), scannerStream.getPackageData());
			}
			if (barcode) {
				comp = compareBarcodeByPackage(scannerStream.getId(), scannerStream.getPackageData());
			}

		}
		if (comp.contains("OK")) {
			ret = 1;
		} else {
			ret = 2;
		}
		scannerStream.setEsito(comp);
		scannerStream.setQuantita(quantitaRet);
		scannerStreamRepository.save(scannerStream);
		return ret;
	}

	private String compareEPCByPackage(Long packId, String packageData) throws Exception {
		String ret = "OK";
		List<StreamEPCDifference> listDiffFromAttesoAndRead = readerStreamAttesoRepository.getDiffEPCExpectedRead(packId, packageData);
		if (listDiffFromAttesoAndRead.size() > 0) {
			ret = "KO - Expected > Read";
		}
		List<StreamEPCDifference> listDiffFromReadAndAtteso = readerStreamAttesoRepository.getDiffEPCReadExpected(packId, packageData);
		if (listDiffFromReadAndAtteso.size() > 0) {
			if (!StringUtils.isEmpty(ret))
				ret = ret + " AND ";
			ret = ret + " KO - Read > Expected";
		}
		return ret;
	}

	private String compareTIDByPackage(Long packId, String packageData) throws Exception {
		String ret = "OK";
		List<StreamTIDDifference> listDiffFromAttesoAndRead = readerStreamAttesoRepository.getDiffTIDExpectedRead(packId, packageData);
		if (listDiffFromAttesoAndRead.size() > 0) {
			ret = "KO - Expected > Read";
		}
		List<StreamTIDDifference> listDiffFromReadAndAtteso = readerStreamAttesoRepository.getDiffTIDReadExpected(packId, packageData);
		if (listDiffFromReadAndAtteso.size() > 0) {
			if (!StringUtils.isEmpty(ret))
				ret = ret + " AND ";
			ret = ret + " KO - Read > Expected";
		}
		return ret;
	}

	private String compareBarcodeByPackage(Long packId, String packageData) throws Exception {
		String ret = "OK";
		List<StreamBarcodeDifference> listDiffFromAttesoAndRead = readerStreamAttesoRepository.getDiffBCExpectedRead(packId, packageData);
		if (listDiffFromAttesoAndRead.size() > 0) {
			ret = "KO - Expected > Read";
		}
		List<StreamBarcodeDifference> listDiffFromReadAndAtteso = readerStreamAttesoRepository.getDiffBCReadExpected(packId, packageData);
		if (listDiffFromReadAndAtteso.size() > 0) {
			if (!StringUtils.isEmpty(ret))
				ret = ret + " AND ";
			ret = ret + " KO - Read > Expected";
		}
		return ret;
	}

	private String compareUserByPackage(Long packId, String packageData) throws Exception {
		String ret = "OK";
		List<StreamUserDifference> listDiffFromAttesoAndRead = readerStreamAttesoRepository.getDiffUSERExpectedRead(packId, packageData);
		if (listDiffFromAttesoAndRead.size() > 0) {
			ret = "KO - Expected > Read";
		}
		List<StreamUserDifference> listDiffFromReadAndAtteso = readerStreamAttesoRepository.getDiffUSERReadExpected(packId, packageData);
		if (listDiffFromReadAndAtteso.size() > 0) {
			if (!StringUtils.isEmpty(ret))
				ret = ret + " AND ";
			ret = ret + " KO - Read > Expected";
		}
		return ret;
	}

	private String compareQuantitaByPackage(Long packId, String packageData, boolean epc, boolean tid, boolean user, boolean barcode, boolean quantita) throws Exception {
		String ret = "OK";
		Integer letto = null;
		if (epc) {
		    letto = readerStreamAttesoRepository.getCountDistinctEpcLetto(packId, packageData);
		}
		if (tid||quantita) {
		    letto = readerStreamAttesoRepository.getCountDistinctTidLetto(packId, packageData);
		}
		if (user) {
		    letto = readerStreamAttesoRepository.getCountDistinctUserLetto(packId, packageData);
		}
		if (barcode) {
		    letto = readerStreamAttesoRepository.getCountDistinctBarcodeLetto(packId, packageData);
		}

		Integer atteso = readerStreamAttesoRepository.getCountExpected(packageData);
		if (letto.intValue() != atteso.intValue()) {
			ret = "KO";
		}
		return ret + letto;
	}

	public List<ReaderStream> getAllReaderStream() throws Exception {
		//
		List<ReaderStream> listReaderStream = readerStreamRepository.findAll(Sort.by(Sort.Direction.DESC, "timeStamp"));
		return listReaderStream;
	}
	
	
	public List<ReaderStreamOnly> getAllDistinctReaderStream() throws Exception {
		//
		List<ReaderStreamOnly> listReaderStream = readerStreamRepository.getReaderStreamDistinctList();
		return listReaderStream;
	}
	
	
	public List<ReaderStreamOnly> getDistinctReaderStreamByPackage(String packId) throws Exception {
		//
		List<ReaderStreamOnly> listReaderStream = readerStreamRepository.getReaderStreamDistinctByPackId(packId);
		return listReaderStream;
	}

	public List<ScannerStream> getAllScannerStream() throws Exception {
		//
		List<ScannerStream> listScannerStream = scannerStreamRepository.findAll(Sort.by(Sort.Direction.DESC, "timeStamp"));
		return listScannerStream;
	}
	
	@Transactional
	public void deleteAllData() throws Exception {
		//
		readerStreamRepository.deleteAll();
		scannerStreamRepository.deleteAll();
		
	}
	

	public void enableTrigger() throws Exception {
		scannerStreamRepository.enableTrigger();
		logger.info("Trigger ENABLED");
	}
	
	
	public void disableTrigger() throws Exception {
		scannerStreamRepository.disableTrigger();
		logger.info("Trigger DISABLED");
	}
	
	public Integer getTotalPackageReadDay() throws Exception {
		return scannerStreamRepository.getTotalPackageReadDay();
	}
	
	public Integer getTotalPackageKoDay() throws Exception {
		return scannerStreamRepository.getTotalPackageKoDay();
	}
  
	
	public Integer getTotalPackageReadLastWeek() throws Exception {
		return scannerStreamRepository.getTotalPackageReadLastWeek();
	}
  
	public Integer getTotalPackageKoLastWeek() throws Exception {
		return scannerStreamRepository.getTotalPackageKoLastWeek();
	}
		
	
	public Integer getTotalPackageReadLastMonth() throws Exception {
		return scannerStreamRepository.getTotalPackageReadLastMonth();
	}
  
	public Integer getTotalPackageKoLastMonth() throws Exception {
		return scannerStreamRepository.getTotalPackageKoLastMonth();
	}
	

	
}