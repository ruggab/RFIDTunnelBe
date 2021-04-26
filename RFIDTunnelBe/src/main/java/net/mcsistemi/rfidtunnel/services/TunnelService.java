package net.mcsistemi.rfidtunnel.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.impinj.octane.OctaneSdkException;

import net.mcsistemi.rfidtunnel.entity.Antenna;
import net.mcsistemi.rfidtunnel.entity.ConfAntenna;
import net.mcsistemi.rfidtunnel.entity.ConfReader;
import net.mcsistemi.rfidtunnel.entity.Dispositivo;
import net.mcsistemi.rfidtunnel.entity.Reader;
import net.mcsistemi.rfidtunnel.entity.ReaderRfidInpinj;
import net.mcsistemi.rfidtunnel.entity.ReaderRfidWirama;
import net.mcsistemi.rfidtunnel.entity.Tipologica;
import net.mcsistemi.rfidtunnel.entity.Tunnel;
import net.mcsistemi.rfidtunnel.job.JobImpinj;
import net.mcsistemi.rfidtunnel.job.JobWiramaReader;
import net.mcsistemi.rfidtunnel.job.PoolImpinjReader;
import net.mcsistemi.rfidtunnel.job.PoolWiramaReader;
import net.mcsistemi.rfidtunnel.repository.AntennaRepository;
import net.mcsistemi.rfidtunnel.repository.ConfAntennaRepository;
import net.mcsistemi.rfidtunnel.repository.ConfReaderRepository;
import net.mcsistemi.rfidtunnel.repository.DispositivoRepository;
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
	private ConfReaderRepository confReaderRepository;
	
	
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
		return  tunnelObj;
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
			for (Iterator iterator = dispoSet.iterator(); iterator.hasNext();) {
				Dispositivo dispositivo = (Dispositivo) iterator.next();
				//Se il tipo dispositivo è un reader rfid recuper la configurazione annessa
				if (dispositivo.getIdTipoDispositivo() == 1 && dispositivo.getIdModelloReader() == 6) {
					listReaderImpinj.addAll(confReaderRepository.findByIdTunnelAndIdDispositivo(tunnel.getId(), dispositivo.getId()));
				} else {
					listReaderImpinj.addAll(confReaderRepository.findByIdTunnelAndIdDispositivo(tunnel.getId(), dispositivo.getId()));
				}
				
			}
				
				
			// reader = list.get(0);
			// }

//			if (reader instanceof ReaderRfidWirama) {
//				ReaderRfidWirama readerRfidWirama = (ReaderRfidWirama) reader;
//				JobWiramaReader jobWiramaReader = new JobWiramaReader((ReaderRfidWirama) reader, this);
//				//JobWiramaCommand jobWiramaCommand = new JobWiramaCommand((ReaderRfidWirama) reader, this);
//				PoolWiramaReader.addThread(readerRfidWirama.getIpAdress()+readerRfidWirama.getPorta(), jobWiramaReader);
//				//PoolWiramaReader.addThread(reader.getIpAdress() + readerRfidWirama.getPortaComandi(), jobWiramaCommand);
//				jobWiramaReader.start();
//				//jobWiramaCommand.start();
//			}
//			if (reader instanceof ReaderRfidInpinj) {
//				JobImpinj jobImpinj = new JobImpinj((ReaderRfidInpinj) reader, this);
//				PoolImpinjReader.addJob(reader.getId(), jobImpinj);
//				jobImpinj.start();
//			}
			//tunnel.setStato(true);
			tunnelRepository.save(tunnel);
			listTunnel = tunnelRepository.findAll();
//		} catch (OctaneSdkException ex) {
//			System.out.println(ex.getMessage());
//			listTunnel = this.stop(tunnel);
//			throw ex;
		} catch (Exception ex) {
			listTunnel = this.stop(tunnel);
			System.out.println(ex.getMessage());
			throw ex;
		}
		return listTunnel;
	}

	public List<Tunnel> stop(Tunnel tunnel) throws Exception {
		List<Tunnel> list = null;
		// List<Reader> list = readerRepository.findByIpAdressAndPortaOrderByIdTipoReader(readerForm.getIpAdress(),
		// readerForm.getPorta());
		tunnel = tunnelRepository.findById(tunnel.getId()).get();

//		if (tunnel instanceof ReaderRfidWirama) {
//			try {
//				ReaderRfidWirama readerRfidWirama = (ReaderRfidWirama) reader;
//				JobWiramaReader jobWirama = (JobWiramaReader) PoolWiramaReader.getThread(reader.getIpAdress() + readerRfidWirama.getPorta());
//				//JobWiramaCommand jobWiramaCommand = (JobWiramaCommand) PoolWiramaReader.getThread(reader.getIpAdress() + readerRfidWirama.getPorta());
//				jobWirama.stop();
//				//jobWiramaCommand.stop();
//			} catch (Exception e) {
//				e.printStackTrace();
//			} finally {
//				PoolWiramaReader.removeThread(reader.getIpAdress());
//				reader.setStato(false);
//				readerRepository.save(reader);
//				list = readerRepository.findAll(Sort.by(Sort.Direction.ASC, "ipAdress"));
//			}
//		}
//		if (reader instanceof ReaderRfidInpinj) {
//			try {
//				JobImpinj jobImpinj = (JobImpinj) PoolImpinjReader.getJob(reader.getId());
//				if (jobImpinj != null) {
//					jobImpinj.stop();
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			} finally {
//				// PoolImpinjReader.removeJob(reader.getId());
//				reader.setStato(false);
//				readerRepository.save(reader);
//				list = readerRepository.findAll(Sort.by(Sort.Direction.ASC, "ipAdress"));
//			}
//
//		}

		return list;
	}
	
	

}