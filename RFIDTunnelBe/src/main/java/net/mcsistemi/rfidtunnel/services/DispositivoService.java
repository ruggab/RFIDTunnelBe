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
public class DispositivoService implements IDispositivoService {

	@Autowired
	private DispositivoRepository dispositivoRepository;

	@Autowired
	private AntennaRepository antennaRepository;

	@Autowired
	private TipologicaRepository tipologicaRepository;

	public Dispositivo getDispositivoById(Long dispositivoId) throws Exception {
		Optional<Dispositivo> dispositivo = dispositivoRepository.findById(dispositivoId);
		Dispositivo dispositivoObj = dispositivo.get();
		return dispositivoObj;
	}

	public void createDispositivo(Dispositivo dispositivo) throws Exception {
		Reader rr = null;
		// Controllo se esiste un dispositivo gia presente con lo stesso ipadress
		List<Dispositivo> list = dispositivoRepository.findByIpAdress(dispositivo.getIpAdress());
		if (list.size() > 0) {
			throw new Exception("Attenzione IP già in uso per altro Dispositivo");
		}
		//
		// Salva dispositivo
		dispositivoRepository.save(dispositivo);
	}

	public List<Dispositivo> getAllDispositivi() throws Exception {
		//
		List<Dispositivo> dispositivoList = dispositivoRepository.findAll(Sort.by(Sort.Direction.ASC, "ipAdress"));
		for (Iterator iterator = dispositivoList.iterator(); iterator.hasNext();) {
			Dispositivo dispositivo = (Dispositivo) iterator.next();	
			Tipologica tip = tipologicaRepository.getOne(dispositivo.getIdTipoDispositivo());
			dispositivo.setDescTipoDispositivo(tip.getDescrizione());
			
		}
		return dispositivoList;
	}

	public void delete(Long dispoId) throws Exception {
		dispositivoRepository.deleteById(dispoId);
	}

	@Transactional
	public void save(Dispositivo dispositivo) throws Exception {

		dispositivoRepository.save(dispositivo);
	}

	public List<Antenna> getAllAntenna(Long readerId) throws Exception {
		List<Antenna> listAntenna = antennaRepository.findByIdReader(readerId);
		return listAntenna;
	}

	public List<Dispositivo> getReaderRfidList() throws Exception {
		//
		List<Dispositivo> dispositivoList = dispositivoRepository.findByIdTipoDispositivo(new Long(1));
		return dispositivoList;
	}

	public List<Dispositivo> getReaderBarcodeList() throws Exception {
		//
		List<Dispositivo> dispositivoList = dispositivoRepository.findByIdTipoDispositivo(new Long(2));
		return dispositivoList;
	}

}