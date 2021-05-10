package net.mcsistemi.rfidtunnel.services;

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
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.impinj.octane.ImpinjReader;
import com.impinj.octane.Tag;

import net.mcsistemi.rfidtunnel.entity.ConfAntenna;
import net.mcsistemi.rfidtunnel.entity.ConfReader;
import net.mcsistemi.rfidtunnel.entity.Dispositivo;
import net.mcsistemi.rfidtunnel.entity.ReaderStream;
import net.mcsistemi.rfidtunnel.entity.ReaderStreamAtteso;
import net.mcsistemi.rfidtunnel.entity.ScannerStream;
import net.mcsistemi.rfidtunnel.entity.Tipologica;
import net.mcsistemi.rfidtunnel.entity.Tunnel;
import net.mcsistemi.rfidtunnel.job.JobInterface;
import net.mcsistemi.rfidtunnel.job.JobRfidImpinj;
import net.mcsistemi.rfidtunnel.job.JobScannerBarcode;
import net.mcsistemi.rfidtunnel.repository.ConfAntennaRepository;
import net.mcsistemi.rfidtunnel.repository.ConfPortRepository;
import net.mcsistemi.rfidtunnel.repository.ConfReaderRepository;
import net.mcsistemi.rfidtunnel.repository.DispositivoRepository;
import net.mcsistemi.rfidtunnel.repository.ReaderStreamAttesoRepository;
import net.mcsistemi.rfidtunnel.repository.ReaderStreamAttesoRepository.StreamBarcodeDifference;
import net.mcsistemi.rfidtunnel.repository.ReaderStreamAttesoRepository.StreamEPCDifference;
import net.mcsistemi.rfidtunnel.repository.ReaderStreamAttesoRepository.StreamTIDDifference;
import net.mcsistemi.rfidtunnel.repository.ReaderStreamAttesoRepository.StreamUserDifference;
import net.mcsistemi.rfidtunnel.repository.ReaderStreamRepository;
import net.mcsistemi.rfidtunnel.repository.ScannerStreamRepository;
import net.mcsistemi.rfidtunnel.repository.TipologicaRepository;
import net.mcsistemi.rfidtunnel.repository.TunnelRepository;

@Service
public class TunnelService implements ITunnelService {

	Logger logger = Logger.getLogger(TunnelService.class);

	@Autowired
	private DispositivoRepository dispositivoRepository;

	@Autowired
	private TipologicaRepository tipologicaRepository;

	@Autowired
	private TunnelRepository tunnelRepository;

	@Autowired
	private ConfAntennaRepository confAntennaRepository;

	@Autowired
	private ConfPortRepository confPortRepository;

	@Autowired
	private ConfReaderRepository confReaderRepository;

	@Autowired
	private ReaderStreamRepository readerStreamRepository;

	@Autowired
	private ScannerStreamRepository scannerStreamRepository;

	@Autowired
	private ReaderStreamAttesoRepository readerStreamAttesoRepository;

	private Hashtable<String, JobInterface> mapDispo = new Hashtable<String, JobInterface>();

	public Tunnel getTunnelById(Long id) throws Exception {
		Optional<Tunnel> tunnel = tunnelRepository.findById(id);
		Tunnel tunnelObj = tunnel.get();
		Set<Dispositivo> dispoSet = tunnelObj.getDispositivi();
		for (Iterator iterator = dispoSet.iterator(); iterator.hasNext();) {
			Dispositivo dispositivo = (Dispositivo) iterator.next();
			Tipologica tip = tipologicaRepository.getOne(dispositivo.getIdTipoDispositivo());
			dispositivo.setDescTipoDispositivo(tip.getDescrizione());
			if (dispositivo.getIdModelloReader() != null) {
				tip = tipologicaRepository.getOne(dispositivo.getIdModelloReader());
				dispositivo.setDescModelloReader(tip.getDescrizione());
			} else {
				dispositivo.setDescModelloReader("");
			}
		}

		setDescrizioniInTunnel(tunnelObj);
		return tunnelObj;
	}

	public void create(Tunnel tunnel) throws Exception {
		tunnelRepository.save(tunnel);
	}

	public List<Tunnel> getAllTunnel() throws Exception {
		//
		List<Tunnel> tunnelList = tunnelRepository.findAll();
		for (Iterator iterator = tunnelList.iterator(); iterator.hasNext();) {
			Tunnel tunnel = (Tunnel) iterator.next();
			setDescrizioniInTunnel(tunnel);
		}
		return tunnelList;
	}

	private void setDescrizioniInTunnel(Tunnel tunnel) {
		Tipologica tip = null;
		if (tunnel.getIdSceltaGestColli() != null) {
			tip = tipologicaRepository.getOne(tunnel.getIdSceltaGestColli());
			tunnel.setDescSceltaGestColli(tip.getDescrizione());
		}
		if (tunnel.getIdSceltaTipoColli() != null) {
			tip = tipologicaRepository.getOne(tunnel.getIdSceltaTipoColli());
			tunnel.setDescSceltaTipoColli(tip.getDescrizione());
		}
		if (tunnel.getIdTipoFormatoEPC() != null) {
			tip = tipologicaRepository.getOne(tunnel.getIdTipoFormatoEPC());
			tunnel.setDescTipoFormatoEPC(tip.getDescrizione());
		}
		if (tunnel.getIdTipoReaderSelected() != null) {
			tip = tipologicaRepository.getOne(tunnel.getIdTipoReaderSelected());
			tunnel.setDescTipoReaderSelected(tip.getDescrizione());
		}
		if (tunnel.getIdReaderBarcodeSelected() != null) {
			Dispositivo dispo = dispositivoRepository.getOne(tunnel.getIdReaderBarcodeSelected());
			tunnel.setDescReaderBarcodeSelected(dispo.getIpAdress());
		}
		if (tunnel.getIdReaderRfidSelected() != null) {
			Dispositivo dispo = dispositivoRepository.getOne(tunnel.getIdReaderRfidSelected());
			tunnel.setDescReaderRfidSelected(dispo.getIpAdress());
		}
	}

	public void delete(Long tunnelId) throws Exception {
		tunnelRepository.deleteById(tunnelId);
	}

	@Transactional
	public void save(Tunnel tunnel) throws Exception {

		tunnelRepository.save(tunnel);
	}

	@Transactional
	public void aggiornaDispositivo(Dispositivo dispo) throws Exception {
		dispositivoRepository.save(dispo);
	}

	@Transactional
	public void aggiornaConfReader(ConfReader confReader) throws Exception {
		confReaderRepository.save(confReader);
	}

	public List<ConfAntenna> getAllAntenna(Long readerId) throws Exception {
		List<ConfAntenna> listAntenna = confAntennaRepository.findByIdReader(readerId);
		return listAntenna;
	}

	public List<Tunnel> start(Tunnel tunnel) throws Exception {
		List<Tunnel> listTunnel = null;
		try {
			Optional<Tunnel> tunnelOne = tunnelRepository.findById(tunnel.getId());
			tunnel = tunnelOne.get();
			Set<Dispositivo> dispoSet = tunnel.getDispositivi();
			for (Iterator iterator = dispoSet.iterator(); iterator.hasNext();) {
				Dispositivo dispositivo = (Dispositivo) iterator.next();
				// Se il tipo dispositivo è un reader rfid Impinj recuper la configurazione
				// annessa
				if (dispositivo.getIdTipoDispositivo() == 1 && dispositivo.getIdModelloReader() == 5) {

					ConfReader confReader = confReaderRepository.findByIdTunnelAndIdDispositivo(tunnel.getId(), dispositivo.getId()).get(0);
					confReader.getAntennas().addAll(confAntennaRepository.findByIdReader(confReader.getId()));
					confReader.getPorts().addAll(confPortRepository.findByIdReader(confReader.getId()));
					confReader.setDispositivo(dispositivo);
					JobRfidImpinj jobRfidImpinj = new JobRfidImpinj(tunnel, this, confReader);
					jobRfidImpinj.run();
					mapDispo.put(tunnel.getId() + "|" + dispositivo.getId(), jobRfidImpinj);

				}
				// Se il tipo dispositivo è un reader rfid Wiram recuper la configurazione annessa
				if (dispositivo.getIdTipoDispositivo() == 1 && dispositivo.getIdModelloReader() == 6) {
					ConfReader confReader = confReaderRepository.findByIdTunnelAndIdDispositivo(tunnel.getId(), dispositivo.getId()).get(0);
					confReader.setDispositivo(dispositivo);
					// START WIRAMA DA GESTIRE
				}
				// Se il tipo dispositivo è un Barcode
				if (dispositivo.getIdTipoDispositivo() == 2) {
					JobScannerBarcode scanner = new JobScannerBarcode(tunnel, this, dispositivo);
					Thread scannerThread = new Thread(scanner);
					logger.info("Starting Barcode " + dispositivo.getNome() + " ip:" + dispositivo.getIpAdress());
					scannerThread.start();
					mapDispo.put(tunnel.getId() + "|" + dispositivo.getId(), scanner);
				}

			}
			String nomeDeviceKo = "";
			boolean tuttiPartiti = true;
			// Ricarico i dispositivi con stato aggiornato per capire se tutti sono Startati
			for (Iterator iterator = dispoSet.iterator(); iterator.hasNext();) {
				Dispositivo dispositivo = (Dispositivo) iterator.next();
				Dispositivo dispoAggiornato = this.dispositivoRepository.getOne(dispositivo.getId());
				if (!dispoAggiornato.isStato()) {
					nomeDeviceKo = dispositivo.getNome();
					tuttiPartiti = false;
					break;
				}
			}

			
			if (!tuttiPartiti) {
				throw new Exception("Attention the devices " + nomeDeviceKo + " has not started");
			}
			tunnel.setStato(tuttiPartiti);
			tunnelRepository.save(tunnel);
			listTunnel = tunnelRepository.findAll();

		} catch (Exception ex) {
			listTunnel = this.stop(tunnel);

			logger.info(ex.getMessage());
			throw ex;
		}
		return listTunnel;
	}

	public List<Tunnel> stop(Tunnel tunnel) throws Exception {
		List<Tunnel> listTunnel = null;
		try {

			List<JobInterface> lisJob = new ArrayList<JobInterface>(mapDispo.values());
			for (Iterator iterator = lisJob.iterator(); iterator.hasNext();) {
				JobInterface keyDispo = (JobInterface) iterator.next();
				keyDispo.stop();
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			throw ex;
		} finally {

			tunnel.setStato(false);
			tunnelRepository.save(tunnel);
			listTunnel = tunnelRepository.findAll();
		}
		return listTunnel;

	}

	private ScannerStream getLastScanner() throws Exception {
		ScannerStream scannerStream = scannerStreamRepository.getLastScanner();
		return scannerStream;
	}

	@Transactional
	public ScannerStream gestioneStream(Long idTunnel, ImpinjReader reader, List<Tag> tags) throws Exception {
		// Salva il package nello Scanner Stream
		// if (tunnelJob.getTunnel().getIdSceltaGestColli() == 7 ) {
		ScannerStream scannerStream = scannerStreamRepository.getLastScanner();
		if (scannerStream != null) {
			logger.info("Package: " + scannerStream.getPackageData());
			scannerStream.setDettaglio(true);
			scannerStream = scannerStreamRepository.save(scannerStream);
		} else {
			scannerStream = new ScannerStream();
			String packageData = "NO_BARCODE-" + tunnelRepository.getSeqNextVal();
			scannerStream.setIdTunnel(idTunnel);
			scannerStream.setPackageData(packageData);
			scannerStream.setDettaglio(true);
			scannerStream.setTimeStamp(new Date());
			scannerStream = scannerStreamRepository.save(scannerStream);
			logger.info("Package: " + scannerStream.getPackageData());
		}
		// }
		// Salvo il reader stream
		for (Tag t : tags) {
			logger.info("IMPINJ ---->>>> EPC: " + t.getEpc().toString());
			logger.info("IMPINJ ---->>>> TID: " + t.getTid().toString());
			this.createReadStream(idTunnel, reader.getAddress(), scannerStream, t);
		}
		return scannerStream;
	}

	private void createReadStream(Long idTunnel, String ipAdress, ScannerStream ss, Tag tag) throws Exception {
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

	public void createScannerStream(Long tunnelId, String packageData, boolean dettaglio) throws Exception {
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

	public Integer getSeqNextVal() throws Exception {
		Integer nextVal = tunnelRepository.getSeqNextVal();
		return nextVal;
	}

	public int compareByPackage(ScannerStream scannerStream, Boolean epc, Boolean tid, Boolean user, Boolean barcode, Boolean quantita) throws Exception {
		int ret = 2;
		String comp = compareQuantitaByPackage(scannerStream.getId(), scannerStream.getPackageData(), epc, tid, user, barcode);
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

	private String compareQuantitaByPackage(Long packId, String packageData, boolean epc, boolean tid, boolean user, boolean barcode) throws Exception {
		String ret = "OK";
		Integer letto = null;
		if (epc) {
		    letto = readerStreamAttesoRepository.getCountDistinctEpcLetto(packId, packageData);
		}
		if (tid) {
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
}