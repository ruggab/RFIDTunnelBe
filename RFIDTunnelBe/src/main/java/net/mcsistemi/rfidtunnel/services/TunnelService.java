package net.mcsistemi.rfidtunnel.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.impinj.octane.Tag;

import net.mcsistemi.rfidtunnel.entity.ConfAntenna;
import net.mcsistemi.rfidtunnel.entity.ConfReader;
import net.mcsistemi.rfidtunnel.entity.Dispositivo;
import net.mcsistemi.rfidtunnel.entity.ReaderStream;
import net.mcsistemi.rfidtunnel.entity.ReaderStreamAtteso;
import net.mcsistemi.rfidtunnel.entity.ScannerStream;
import net.mcsistemi.rfidtunnel.entity.Tipologica;
import net.mcsistemi.rfidtunnel.entity.Tunnel;
import net.mcsistemi.rfidtunnel.job2.PoolJob;
import net.mcsistemi.rfidtunnel.job2.TunnelJob;
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

	Logger logger = LoggerFactory.getLogger(TunnelService.class);

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

//		if (tunnel.getId() != null) {
//			tunnelRepository.deleteById(tunnel.getId());
//		}
		tunnelRepository.save(tunnel);
	}

	@Transactional
	public void aggiornaDispositivo(Dispositivo dispo) throws Exception {
		dispositivoRepository.save(dispo);
	}

	public List<ConfAntenna> getAllAntenna(Long readerId) throws Exception {
		List<ConfAntenna> listAntenna = confAntennaRepository.findByIdReader(readerId);
		return listAntenna;
	}

	public List<Tunnel> start(Tunnel tunnel) throws Exception {
		List<Tunnel> listTunnel = null;
		try {
			Optional<Tunnel> tunnelOne = tunnelRepository.findById(tunnel.getId());
			Tunnel tunnelObj = tunnelOne.get();
			Set<Dispositivo> dispoSet = tunnelObj.getDispositivi();
			List<ConfReader> listReaderImpinj = new ArrayList<ConfReader>();
			List<ConfReader> listReaderWirama = new ArrayList<ConfReader>();
			List<Dispositivo> listBarcode = new ArrayList<Dispositivo>();
			for (Iterator iterator = dispoSet.iterator(); iterator.hasNext();) {
				Dispositivo dispositivo = (Dispositivo) iterator.next();
				// Se il tipo dispositivo è un reader rfid Impinj recuper la configurazione annessa
				if (dispositivo.getIdTipoDispositivo() == 1 && dispositivo.getIdModelloReader() == 5) {
					ConfReader confReader = confReaderRepository.findByIdTunnelAndIdDispositivo(tunnel.getId(), dispositivo.getId()).get(0);
					confReader.getAntennas().addAll(confAntennaRepository.findByIdReader(confReader.getId()));
					confReader.getPorts().addAll(confPortRepository.findByIdReader(confReader.getId()));
					confReader.setDispositivo(dispositivo);
					listReaderImpinj.add(confReader);
				}
				// Se il tipo dispositivo è un reader rfid Wiram recuper la configurazione annessa
				if (dispositivo.getIdTipoDispositivo() == 1 && dispositivo.getIdModelloReader() == 6) {
					ConfReader confReader = confReaderRepository.findByIdTunnelAndIdDispositivo(tunnel.getId(), dispositivo.getId()).get(0);
					confReader.setDispositivo(dispositivo);
					listReaderWirama.add(confReader);
				}
				// Se il tipo dispositivo è un Barcode
				if (dispositivo.getIdTipoDispositivo() == 2) {
					listBarcode.add(dispositivo);
				}

			}
			TunnelJob tunnelJob = new TunnelJob(tunnel, listReaderImpinj, listReaderWirama, listBarcode, this);
			tunnelJob.startTunnel();
			PoolJob.addJob(tunnelJob.getTunnel().getNome() + "_" + tunnelJob.getTunnel().getId(), tunnelJob);
			

			tunnel.setStato(true);
			tunnelRepository.save(tunnel);
			listTunnel = tunnelRepository.findAll();
		} catch (Exception ex) {
			if (tunnel.isStato()) {
				listTunnel = this.stop(tunnel);
			}
			logger.info(ex.getMessage());
			throw ex;
		}
		return listTunnel;
	}

	public List<Tunnel> stop(Tunnel tunnel) throws Exception {
		List<Tunnel> listTunnel = null;
		try {
			TunnelJob tunnelJob = (TunnelJob) PoolJob.getJob(tunnel.getNome() + "_" + tunnel.getId());
			if (tunnelJob != null) {
				tunnelJob.stopTunnel();
				PoolJob.removeJob(tunnelJob.getTunnel().getNome() + "_" + tunnelJob.getTunnel().getId());
			}

		} catch (Exception ex) {
			logger.error(ex.getMessage());
			throw ex;
		} finally {
			// PoolJob.removeJob(reader.getId());
			tunnel.setStato(false);
			tunnelRepository.save(tunnel);
			listTunnel = tunnelRepository.findAll();
		}
		return listTunnel;

	}

	public void createReaderStream(String ipAdress, String epc, String tid, String user, String packId, Timestamp time) throws Exception {
		ReaderStream readerStream = new ReaderStream();

		readerStream.setEpc(epc);
		readerStream.setTimeStamp(time);
		readerStream.setTid(tid);
		readerStream.setIpAdress(ipAdress);
		readerStream.setPackId(packId);
		readerStream.setUserData(user);
		readerStreamRepository.save(readerStream);
	}

	public void createReaderStream(Long idTunnel, String ipAdress, String packId, Tag tag) throws Exception {
		ReaderStream readerStream = new ReaderStream();
		readerStream.setIdTunnel(idTunnel);
		readerStream.setEpc(tag.getEpc().toHexString());
		readerStream.setTimeStamp(new Timestamp(System.currentTimeMillis()));
		readerStream.setTid(tag.getTid().toHexString());
		readerStream.setIpAdress(ipAdress);
		readerStream.setPackId(packId);
		readerStream.setUserData("");
		readerStream.setAntennaPortNumber(tag.getAntennaPortNumber() + "");
		readerStream.setChannelInMhz(tag.getChannelInMhz() + "");
		readerStream.setFirstSeenTime(tag.getFirstSeenTime() + "");
		readerStream.setLastSeenTime(tag.getLastSeenTime() + "");
		readerStream.setModelName(tag.getModelDetails().getModelName().name());
		readerStream.setPeakRssiInDbm(tag.getPeakRssiInDbm() + "");
		readerStream.setPhaseAngleInRadians(tag.getPhaseAngleInRadians() + "");
		readerStream.setRfDopplerFrequency(tag.getRfDopplerFrequency() + "");
		readerStream.setTagSeenCount(tag.getTagSeenCount() + "");
		readerStream.setUserData("");
		readerStream.setFirstSeenTime(tag.getFirstSeenTime() + "");
		readerStream.setLastSeenTime(tag.getLastSeenTime() + "");
		readerStreamRepository.save(readerStream);
	}

	public void createScannerStream(Long tunnelId, String packId) throws Exception {
		ScannerStream ss = new ScannerStream();
		ss.setIdTunnel(tunnelId);
		ss.setPackId(packId);
		ss.setTimeStamp(new Date());
		scannerStreamRepository.save(ss);
	}

	public ReaderStreamAtteso createReaderStreamAtteso(String collo, String epc, String tid) throws Exception {
		ReaderStreamAtteso rsa = new ReaderStreamAtteso();
		rsa.setPackId(collo);
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

	public int compareByPackage(String packId, Boolean epc, Boolean tid, Boolean user, Boolean barcode, Boolean quantita) throws Exception {
		int ret = 2;
		if (epc) {
			String comp = compareEPCByPackage(packId);
			if (comp.equals("OK")) {
				ret = 1;
			}
		}
		if (tid) {
			String comp = compareTIDByPackage(packId);
			if (comp.equals("OK")) {
				ret = 1;
			}
		}
		if (user) {
			String comp = compareUserByPackage(packId);
			if (comp.equals("OK")) {
				ret = 1;
			}
		}
		if (barcode) {
			String comp = compareBarcodeByPackage(packId);
			if (comp.equals("OK")) {
				ret = 1;
			}
		}
		if (quantita) {
			String comp = compareQuantitaByPackage(packId);
			if (comp.equals("OK")) {
				ret = 1;
			}
		}

		return ret;
	}

	public String compareEPCByPackage(String packId) throws Exception {
		String ret = "OK";
		List<StreamEPCDifference> listDiffFromAttesoAndRead = readerStreamAttesoRepository.getDiffEPCExpectedRead(packId);
		if (listDiffFromAttesoAndRead.size() > 0) {
			ret = "KO - Expected > Read";
		}
		List<StreamEPCDifference> listDiffFromReadAndAtteso = readerStreamAttesoRepository.getDiffEPCReadExpected(packId);
		if (listDiffFromReadAndAtteso.size() > 0) {
			if (!StringUtils.isEmpty(ret))
				ret = ret + " AND ";
			ret = ret + " KO - Read > Expected";
		}
		return ret;
	}

	public String compareTIDByPackage(String packId) throws Exception {
		String ret = "OK";
		List<StreamTIDDifference> listDiffFromAttesoAndRead = readerStreamAttesoRepository.getDiffTIDExpectedRead(packId);
		if (listDiffFromAttesoAndRead.size() > 0) {
			ret = "KO - Expected > Read";
		}
		List<StreamTIDDifference> listDiffFromReadAndAtteso = readerStreamAttesoRepository.getDiffTIDReadExpected(packId);
		if (listDiffFromReadAndAtteso.size() > 0) {
			if (!StringUtils.isEmpty(ret))
				ret = ret + " AND ";
			ret = ret + " KO - Read > Expected";
		}
		return ret;
	}

	public String compareBarcodeByPackage(String packId) throws Exception {
		String ret = "OK";
		List<StreamBarcodeDifference> listDiffFromAttesoAndRead = readerStreamAttesoRepository.getDiffBCExpectedRead(packId);
		if (listDiffFromAttesoAndRead.size() > 0) {
			ret = "KO - Expected > Read";
		}
		List<StreamBarcodeDifference> listDiffFromReadAndAtteso = readerStreamAttesoRepository.getDiffBCReadExpected(packId);
		if (listDiffFromReadAndAtteso.size() > 0) {
			if (!StringUtils.isEmpty(ret))
				ret = ret + " AND ";
			ret = ret + " KO - Read > Expected";
		}
		return ret;
	}

	public String compareUserByPackage(String packId) throws Exception {
		String ret = "OK";
		List<StreamUserDifference> listDiffFromAttesoAndRead = readerStreamAttesoRepository.getDiffUSERExpectedRead(packId);
		if (listDiffFromAttesoAndRead.size() > 0) {
			ret = "KO - Expected > Read";
		}
		List<StreamUserDifference> listDiffFromReadAndAtteso = readerStreamAttesoRepository.getDiffUSERReadExpected(packId);
		if (listDiffFromReadAndAtteso.size() > 0) {
			if (!StringUtils.isEmpty(ret))
				ret = ret + " AND ";
			ret = ret + " KO - Read > Expected";
		}
		return ret;
	}

	public String compareQuantitaByPackage(String packId) throws Exception {
		String ret = "OK";
		List<StreamUserDifference> listDiffFromAttesoAndRead = readerStreamAttesoRepository.getDiffUSERExpectedRead(packId);
		if (listDiffFromAttesoAndRead.size() > 0) {
			ret = "KO - Expected > Read";
		}
		List<StreamUserDifference> listDiffFromReadAndAtteso = readerStreamAttesoRepository.getDiffUSERReadExpected(packId);
		if (listDiffFromReadAndAtteso.size() > 0) {
			if (!StringUtils.isEmpty(ret))
				ret = ret + " AND ";
			ret = ret + " KO - Read > Expected";
		}
		return ret;
	}
	
	public List<ReaderStream> getAllDataStream() throws Exception {
		//
		List<ReaderStream> readerDataList = readerStreamRepository.findAll(Sort.by(Sort.Direction.ASC, "packId"));
		return readerDataList;
	}

}