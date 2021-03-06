package net.smart.rfid.tunnel.db.services;

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

import net.smart.rfid.tunnel.db.entity.ConfAntenna;
import net.smart.rfid.tunnel.db.entity.ConfReader;
import net.smart.rfid.tunnel.db.entity.Dispositivo;
import net.smart.rfid.tunnel.db.entity.ReaderStream;
import net.smart.rfid.tunnel.db.entity.ScannerStream;
import net.smart.rfid.tunnel.db.entity.Tipologica;
import net.smart.rfid.tunnel.db.entity.Tunnel;
import net.smart.rfid.tunnel.db.repository.ConfAntennaRepository;
import net.smart.rfid.tunnel.db.repository.ConfPortRepository;
import net.smart.rfid.tunnel.db.repository.ConfReaderRepository;
import net.smart.rfid.tunnel.db.repository.DispositivoRepository;
import net.smart.rfid.tunnel.db.repository.ReaderStreamAttesoRepository;
import net.smart.rfid.tunnel.db.repository.ReaderStreamRepository;
import net.smart.rfid.tunnel.db.repository.ScannerStreamRepository;
import net.smart.rfid.tunnel.db.repository.TipologicaRepository;
import net.smart.rfid.tunnel.db.repository.TunnelRepository;
import net.smart.rfid.tunnel.db.repository.ReaderStreamAttesoRepository.StreamBarcodeDifference;
import net.smart.rfid.tunnel.db.repository.ReaderStreamAttesoRepository.StreamEPCDifference;
import net.smart.rfid.tunnel.db.repository.ReaderStreamAttesoRepository.StreamTIDDifference;
import net.smart.rfid.tunnel.db.repository.ReaderStreamAttesoRepository.StreamUserDifference;
import net.smart.rfid.tunnel.exception.BusinessException;
import net.smart.rfid.tunnel.job.JobInterface;
import net.smart.rfid.tunnel.job.JobRfidImpinj;
import net.smart.rfid.tunnel.job.JobRfidWirama;
import net.smart.rfid.tunnel.job.JobRfidWirama2000;
import net.smart.rfid.tunnel.job.JobScannerBarcode;
import net.smart.rfid.tunnel.model.TagWirama;
import net.smart.rfid.tunnel.model.TunnelDevice;
import net.smart.rfid.tunnel.util.SGTIN96;
import net.smart.rfid.tunnel.util.SSCC96;
import net.smart.rfid.tunnel.util.Utils;

@Service
public class TunnelService {

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

	private Hashtable<String, Runnable> mapW = new Hashtable<String, Runnable>();

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
		for (Iterator iterator1 = tunnelList.iterator(); iterator1.hasNext();) {
			Tunnel tunnel = (Tunnel) iterator1.next();
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
		for (Dispositivo dispositivo : tunnel.getDispositivi()) {
			dispositivo.setIdTunnel(tunnel.getId());
			dispositivoRepository.save(dispositivo);
		}
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
			if (tunnel.isStato()) {
				throw new BusinessException("Tunnel già in esecuzione");
			}
			Set<Dispositivo> dispoSet = tunnel.getDispositivi();
			String errorMessage = "";
			for (Iterator iterator = dispoSet.iterator(); iterator.hasNext();) {
				Dispositivo dispositivo = (Dispositivo) iterator.next();
				// Se il tipo dispositivo è un reader rfid INPINJ recuper la configurazione annessa
				if (dispositivo.getIdTipoDispositivo() == 1 && dispositivo.getIdModelloReader() == 5) {
					if (dispositivo.isStato()) {
						throw new BusinessException("Questo Dispositivo è già attivo");
					}
					try {

						ConfReader confReader = confReaderRepository.findByIdTunnelAndIdDispositivo(tunnel.getId(), dispositivo.getId()).get(0);
						confReader.getAntennas().addAll(confAntennaRepository.findByIdReader(confReader.getId()));
						confReader.getPorts().addAll(confPortRepository.findByIdReader(confReader.getId()));
						confReader.setDispositivo(dispositivo);
						confReader.setTunnel(tunnel);
						JobRfidImpinj jobRfidImpinj = new JobRfidImpinj(this, confReader);
						jobRfidImpinj.run();
						mapDispo.put(tunnel.getId() + "|" + dispositivo.getId(), jobRfidImpinj);
					} catch (Exception e) {
						logger.error(e.getMessage());
						errorMessage = errorMessage + "Start Error Device " + dispositivo.getNome() + "  \n  ";
					}

				}
				// Se il tipo dispositivo è un reader rfid WIRAMA recuper la configurazione annessa
				if (dispositivo.getIdTipoDispositivo() == 1 && (dispositivo.getIdModelloReader() == 6||dispositivo.getIdModelloReader() == 16)) {
					if (dispositivo.isStato()) {
						throw new BusinessException("This device is already active: " + dispositivo.getNome());
					}
					try {
						ConfReader confReader = confReaderRepository.findByIdTunnelAndIdDispositivo(tunnel.getId(), dispositivo.getId()).get(0);
						confReader.setDispositivo(dispositivo);
						confReader.setTunnel(tunnel);
						JobRfidWirama jobRfidWirama = new JobRfidWirama(confReader, this);
						Thread wiramaThread = new Thread(jobRfidWirama);
						wiramaThread.start();
						mapDispo.put(tunnel.getId() + "|" + dispositivo.getId(), jobRfidWirama);
					} catch (Exception e) {
						logger.error(e.getMessage());
						errorMessage = errorMessage + "Start Error Device " + dispositivo.getNome() + " \n  ";
					}
				}
				// Se il tipo dispositivo è un PACKAGE BARCODE
				if (dispositivo.getIdTipoDispositivo() == 2) {
					if (dispositivo.isStato()) {
						throw new BusinessException("This device is already active: " + dispositivo.getNome());
					}
					try {
						JobScannerBarcode scanner = new JobScannerBarcode(tunnel, this, dispositivo);
						Thread scannerThread = new Thread(scanner);
						logger.info("Starting Barcode " + dispositivo.getNome() + " ip:" + dispositivo.getIpAdress());
						// Executor executor = Executors.newSingleThreadExecutor();
						// executor.execute(scanner);
						scannerThread.start();
						mapDispo.put(tunnel.getId() + "|" + dispositivo.getId(), scanner);
					} catch (Exception e) {
						logger.error(e.getMessage());
						errorMessage = errorMessage + "Start Error Device " + dispositivo.getNome() + " \n ";
					}
				}
			}
			if (errorMessage != "") {
				throw new Exception(errorMessage);
			}
			// Ritardo un secondo per esser certi che il bar code sia stato aggiornato
			Thread.sleep(1000);
			// Controllo dispositivi partiti e relativi messaggi
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

		} catch (BusinessException ex) {
			logger.info(ex.getMessage());
			throw ex;
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
			// List<JobInterface> lisJob = new ArrayList<JobInterface>(mapDispo.values());
			for (String key : mapDispo.keySet()) {
				String[] td = key.split("\\|");
				String idTunnel = td[0];
				String idDispo = td[1];
				if (idTunnel.equals(tunnel.getId() + "")) {
					JobInterface job = mapDispo.get(idTunnel + "|" + idDispo);
					job.stop();
				}
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

	public List<Tunnel> stopOther(Tunnel tunnel, Dispositivo dispositivoExC) throws Exception {
		List<Tunnel> listTunnel = null;
		try {
			// List<JobInterface> lisJob = new ArrayList<JobInterface>(mapDispo.values());
			for (String key : mapDispo.keySet()) {
				String[] td = key.split("\\|");
				String idTunnel = td[0];
				String idDispo = td[1];
				if (idTunnel.equals(tunnel.getId() + "") && !idDispo.equals(dispositivoExC.getId() + "")) {
					JobInterface job = mapDispo.get(idTunnel + "|" + idDispo);
					job.stop();
				}
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

	@Transactional
	public ScannerStream gestioneStreamInpinjRFID(ConfReader confReader, List<Tag> tags) throws Exception {
		// Carico tutti i colli che non hanno reader (dettaglio) associati
		List<Tag> newListTag = new ArrayList<Tag>();
		String packageData = "";
		for (Tag t : tags) {
			String epc = t.getEpc().toString();
			String appo = SSCC96.decodeSSCC96(epc, confReader.getTunnel().getFormatoEPC());
			if (packageData.isEmpty()) {
				newListTag.add(t);
			} else {
				packageData = appo;
			}
		}
		//Se alla fine del ciclo il barcode è ancora vuoto
		if (packageData.isEmpty()) {
			packageData = "NO_BARCODE-" + tunnelRepository.getSeqNextVal();
		}
		// Se sono in questa funzione senza colli letti associa a questi un collo di tipo NO_BARCODE con seq
		ScannerStream lastScannerStream = new ScannerStream();
		lastScannerStream.setIdTunnel(confReader.getIdTunnel());
		lastScannerStream.setPackageData(packageData);
		lastScannerStream.setDettaglio("Y");
		lastScannerStream.setTimeStamp(new Date());
		lastScannerStream = scannerStreamRepository.save(lastScannerStream);
		logger.info("Package: " + lastScannerStream.getPackageData());
		for (Tag t : newListTag) {
			this.createReadStream(confReader, lastScannerStream, t);
		}
		return lastScannerStream;
	}
	
	@Transactional
	public ScannerStream gestioneStreamWiramaRFID(ConfReader confReader, List<TagWirama> tags) throws Exception {
		// Carico tutti i colli che non hanno reader (dettaglio) associati
		List<TagWirama> newListTag = new ArrayList<TagWirama>();
		String packageData = "";
		for (TagWirama t : tags) {
			String epc = t.getEpc().toString();
			String appo = SSCC96.decodeSSCC96(epc, confReader.getTunnel().getFormatoEPC());
			if (packageData.isEmpty()) {
				newListTag.add(t);
			} else {
				packageData = appo;
			}
		}
		//Se alla fine del ciclo il barcode è ancora vuoto
		if (packageData.isEmpty()) {
			packageData = "NO_BARCODE-" + tunnelRepository.getSeqNextVal();
		}
		// Se sono in questa funzione senza colli letti associa a questi un collo di tipo NO_BARCODE con seq
		ScannerStream lastScannerStream = new ScannerStream();
		lastScannerStream.setIdTunnel(confReader.getIdTunnel());
		lastScannerStream.setPackageData(packageData);
		lastScannerStream.setDettaglio("Y");
		lastScannerStream.setTimeStamp(new Date());
		lastScannerStream = scannerStreamRepository.save(lastScannerStream);
		logger.info("Package: " + lastScannerStream.getPackageData());
		for (TagWirama t : newListTag) {
			this.createReadStreamWirama(confReader, lastScannerStream, t);
		}
		return lastScannerStream;
	}

	@Transactional
	public ScannerStream gestioneStreamInpinjBARCODE(ConfReader confReader, List<Tag> tags) throws Exception {
		// Carico tutti i colli che non hanno reader (dettaglio) associati
		ScannerStream lastScannerStream = null;
		List<ScannerStream> scannerStreamList = scannerStreamRepository.getScannerNoDetail();
		if (scannerStreamList.size() > 0) {
			// Recupero l'ultimo collo letto (la query era desc) e gli associo il dettaglio
			lastScannerStream = scannerStreamList.get(0);
			logger.info("Package: " + lastScannerStream.getPackageData());
			lastScannerStream.setDettaglio("Y");
			lastScannerStream = scannerStreamRepository.save(lastScannerStream);
			// Setto ERROR ai precedenti colli
			for (int i = 1; i < scannerStreamList.size(); i++) {
				ScannerStream scannerStream = scannerStreamList.get(i);
				scannerStream.setDettaglio("ERROR");
				scannerStream = scannerStreamRepository.save(scannerStream);
			}
		} else {
			// Se sono in questa funzione senza colli letti associa a questi un collo di tipo NO_BARCODE con seq
			lastScannerStream = new ScannerStream();
			String packageData = "NO_BARCODE-" + tunnelRepository.getSeqNextVal();
			lastScannerStream.setIdTunnel(confReader.getIdTunnel());
			lastScannerStream.setPackageData(packageData);
			lastScannerStream.setDettaglio("Y");
			lastScannerStream.setTimeStamp(new Date());
			lastScannerStream = scannerStreamRepository.save(lastScannerStream);
			logger.info("Package: " + lastScannerStream.getPackageData());
		}
		// Leggo i readers e li associo all'ultimo collo arrivato
		for (Tag t : tags) {
			this.createReadStream(confReader, lastScannerStream, t);
		}
		return lastScannerStream;
	}

	private void createReadStream(ConfReader confreader, ScannerStream ss, Tag tag) throws Exception {
		ReaderStream readerStream = new ReaderStream();
		readerStream.setIdTunnel(confreader.getIdTunnel());
		readerStream.setTimeStamp(new Timestamp(System.currentTimeMillis()));

		readerStream.setEpc(confreader.isEnableEpc() ? tag.getEpc().toHexString() : "");

		readerStream.setTid(confreader.isEnableTid() ? tag.getTid().toHexString() : "");

		readerStream.setSku(confreader.isEnableSku() ? SGTIN96.decodeEpc(tag.getEpc().toHexString()) : "");

		readerStream.setIpAdress(confreader.getDispositivo().getIpAdress());
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

	@Transactional
	public ScannerStream gestioneStreamWiramaBARCODE(ConfReader confReader, List<TagWirama> tags) throws Exception {
		// Carico tutti i colli che non hanno reader (dettaglio) associati
		ScannerStream lastScannerStream = null;
		List<ScannerStream> scannerStreamList = scannerStreamRepository.getScannerNoDetail();
		if (scannerStreamList.size() > 0) {
			// Recupero l'ultimo collo letto (la query era desc) e gli associo il dettaglio
			lastScannerStream = scannerStreamList.get(0);
			logger.info("Package: " + lastScannerStream.getPackageData());
			lastScannerStream.setDettaglio("Y");
			lastScannerStream = scannerStreamRepository.save(lastScannerStream);
			// Setto ERROR ai precedenti colli
			for (int i = 1; i < scannerStreamList.size(); i++) {
				ScannerStream scannerStream = scannerStreamList.get(i);
				scannerStream.setDettaglio("ERROR");
				scannerStream = scannerStreamRepository.save(scannerStream);
			}
		} else {
			// Se sono in questa funzione senza colli letti associa a questi un collo di tipo NO_BARCODE con seq
			lastScannerStream = new ScannerStream();
			String packageData = "NO_BARCODE-" + tunnelRepository.getSeqNextVal();
			lastScannerStream.setIdTunnel(confReader.getTunnel().getId());
			lastScannerStream.setPackageData(packageData);
			lastScannerStream.setDettaglio("Y");
			lastScannerStream.setTimeStamp(new Date());
			lastScannerStream = scannerStreamRepository.save(lastScannerStream);
			logger.info("Package: " + lastScannerStream.getPackageData());
		}
		// Leggo i readers e li associo all'ultimo collo arrivato
		for (TagWirama t : tags) {
			this.createReadStreamWirama(confReader, lastScannerStream, t);
		}
		return lastScannerStream;
	}

	private void createReadStreamWirama(ConfReader confReader, ScannerStream ss, TagWirama tag) throws Exception {
		ReaderStream readerStream = new ReaderStream();
		readerStream.setIdTunnel(confReader.getTunnel().getId());
		readerStream.setTimeStamp(new Timestamp(System.currentTimeMillis()));
		readerStream.setEpc(tag.getEpc());
		readerStream.setSku(tag.getSku());
		readerStream.setIpAdress(confReader.getDispositivo().getIpAdress());
		readerStream.setPackId(ss.getId());
		readerStream.setPackageData(ss.getPackageData());
		readerStreamRepository.save(readerStream);
	}

	public Integer getSeqNextVal() throws Exception {
		Integer nextVal = tunnelRepository.getSeqNextVal();
		return nextVal;
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
		if (tid || quantita) {
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

	public void createScannerStream(Long tunnelId, String packageData, String dettaglio) throws Exception {

		List<ScannerStream> scannerStreamList = scannerStreamRepository.getScannerNoDetail();
		if (scannerStreamList.size() > 0) {
			// Setto ERROR ai precedenti colli
			for (int i = 0; i < scannerStreamList.size(); i++) {
				ScannerStream scannerStream = scannerStreamList.get(i);
				scannerStream.setDettaglio("ERROR");
				scannerStreamRepository.save(scannerStream);
			}
		}
		ScannerStream ss = new ScannerStream();
		ss.setIdTunnel(tunnelId);
		ss.setPackageData(packageData);
		ss.setTimeStamp(new Date());
		ss.setDettaglio(dettaglio);
		scannerStreamRepository.save(ss);
	}

	public List<ScannerStream> getScannerNoDetail() throws Exception {

		return scannerStreamRepository.getScannerNoDetail();
	}

	public List<TunnelDevice> findAllTunnelDevice() throws Exception {
		//
		List<TunnelDevice> ret = new ArrayList<TunnelDevice>();
		List<Tunnel> tunnels = tunnelRepository.findAll();
		for (Iterator iterator = tunnels.iterator(); iterator.hasNext();) {
			Tunnel tunnel = (Tunnel) iterator.next();
			List<Dispositivo> dispoList = dispositivoRepository.findDispositiviByTunnel(tunnel.getId());
			for (Dispositivo dispositivo : dispoList) {
				TunnelDevice tunnelDevice = new TunnelDevice();
				tunnelDevice.setDispositivo(dispositivo);
				tunnelDevice.setTunnel(tunnel);
				ret.add(tunnelDevice);
			}
		}
		return ret;
	}

	public boolean isTunnelStart(Long idTunnel) throws Exception {
		Tunnel tunnel = tunnelRepository.getOne(idTunnel);

		return tunnel.isStato();
	}

	public ScannerStream saveScannerStream(ScannerStream ss) throws Exception {

		return scannerStreamRepository.save(ss);
	}

	public void start2000() throws Exception {

		try {

			JobRfidWirama2000 jobRfidWirama = new JobRfidWirama2000("192.168.51.41", 7240);
			mapW.put("W2000", jobRfidWirama);
			Thread wiramaThread = new Thread(jobRfidWirama);
			wiramaThread.start();

		} catch (Exception e) {
			logger.error(e.getMessage());

		}
	}

	public void stop2000() throws Exception {
		try {

			JobRfidWirama2000 jobRfidWirama = (JobRfidWirama2000) mapW.get("W2000");
			jobRfidWirama.stop();

		} catch (Exception ex) {
			logger.error(ex.getMessage());
			throw ex;
		}

	}

}