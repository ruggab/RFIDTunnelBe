package net.mcsistemi.rfidtunnel.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.impinj.octane.OctaneSdkException;
import com.impinj.octane.Tag;

import net.mcsistemi.rfidtunnel.entity.Antenna;
import net.mcsistemi.rfidtunnel.entity.ConfAntenna;
import net.mcsistemi.rfidtunnel.entity.ConfReader;
import net.mcsistemi.rfidtunnel.entity.Dispositivo;
import net.mcsistemi.rfidtunnel.entity.Reader;
import net.mcsistemi.rfidtunnel.entity.ReaderRfidInpinj;
import net.mcsistemi.rfidtunnel.entity.ReaderRfidWirama;
import net.mcsistemi.rfidtunnel.entity.ReaderStream;
import net.mcsistemi.rfidtunnel.entity.ReaderStreamAtteso;
import net.mcsistemi.rfidtunnel.entity.ScannerStream;
import net.mcsistemi.rfidtunnel.entity.Tipologica;
import net.mcsistemi.rfidtunnel.entity.Tunnel;
import net.mcsistemi.rfidtunnel.job.JobImpinj;
import net.mcsistemi.rfidtunnel.job.JobWiramaReader;
import net.mcsistemi.rfidtunnel.job.PoolImpinjReader;
import net.mcsistemi.rfidtunnel.job.PoolWiramaReader;
import net.mcsistemi.rfidtunnel.job2.PoolJob;
import net.mcsistemi.rfidtunnel.job2.TunnelJob;
import net.mcsistemi.rfidtunnel.repository.AntennaRepository;
import net.mcsistemi.rfidtunnel.repository.ConfAntennaRepository;
import net.mcsistemi.rfidtunnel.repository.ConfPortRepository;
import net.mcsistemi.rfidtunnel.repository.ConfReaderRepository;
import net.mcsistemi.rfidtunnel.repository.DispositivoRepository;
import net.mcsistemi.rfidtunnel.repository.ReaderStreamAttesoRepository;
import net.mcsistemi.rfidtunnel.repository.ReaderStreamRepository;
import net.mcsistemi.rfidtunnel.repository.ScannerStreamRepository;
import net.mcsistemi.rfidtunnel.repository.TipologicaRepository;
import net.mcsistemi.rfidtunnel.repository.TunnelRepository;

@Service
public class TunnelService implements ITunnelService {

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

		if (tunnel.getId() != null) {
			tunnelRepository.deleteById(tunnel.getId());
		}
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
			PoolJob.addJob(tunnelJob.getTunnel().getNome() + "_" + tunnelJob.getTunnel().getId(), tunnelJob);
			tunnelJob.startTunnel();

			tunnel.setStato(true);
			tunnelRepository.save(tunnel);
			listTunnel = tunnelRepository.findAll();
		} catch (Exception ex) {
			listTunnel = this.stop(tunnel);
			System.out.println(ex.getMessage());
			throw ex;
		}
		return listTunnel;
	}

	public List<Tunnel> stop(Tunnel tunnel) throws Exception {
		List<Tunnel> listTunnel = null;
		try {
			TunnelJob tunnelJob = (TunnelJob) PoolJob.getJob(tunnel.getNome() + "_" + tunnel.getId());
			tunnelJob.stopTunnel();
			PoolJob.removeJob(tunnelJob.getTunnel().getNome() + "_" + tunnelJob.getTunnel().getId());
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			throw ex;
		} finally {
			// PoolJob.removeJob(reader.getId());
			tunnel.setStato(false);
			tunnelRepository.save(tunnel);
			listTunnel = tunnelRepository.findAll();
		}
		return listTunnel;

	}

	public void createReaderStream(String ipAdress, String port, String epc, String tid, String user, String packId, Timestamp time) throws Exception {
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

	public void createScannerStream(String packId) throws Exception {
		ScannerStream ss = new ScannerStream();
		ss.setPackId(packId);
		ss.setTimeStamp(new Date());
		scannerStreamRepository.save(ss);
	}

	public ReaderStreamAtteso  createReaderStreamAtteso(String collo, String epc, String tid) throws Exception {
		ReaderStreamAtteso rsa = new ReaderStreamAtteso();
		rsa.setPackId(collo);
		rsa.setEpc(epc);
		rsa.setTid(tid);
		ReaderStreamAtteso readerStreamAtteso =  readerStreamAttesoRepository.save(rsa);
		return readerStreamAtteso;
	}

	public List<ReaderStreamAtteso> getReaderStreamAtteso(String collo) throws Exception {

		List<ReaderStreamAtteso> listStreamAtteso = readerStreamAttesoRepository.getReaderStreamAttesoByCollo(collo);
		return listStreamAtteso;
	}

}