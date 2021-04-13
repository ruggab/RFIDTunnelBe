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
import net.mcsistemi.rfidtunnel.entity.Tunnel;
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
import net.mcsistemi.rfidtunnel.repository.TunnelRepository;

@Service
public class TunnelService implements ITunnelService {

	
	
	
	@Autowired
	private DispositivoRepository dispositivoRepository;

	@Autowired
	private TunnelRepository tunnelRepository;
	
	@Autowired
	private AntennaRepository antennaRepository;
	
	
	public Tunnel getTunnelById(Long id) throws Exception {
		Optional<Tunnel> tunnel = tunnelRepository.findById(id);
		Tunnel tunnelObj = tunnel.get();
		return  tunnelObj;
	}

	public void create(Tunnel tunnel) throws Exception {
		Reader rr = null;
		//Controllo se esiste un dispositivo gia presente con lo stesso ipadress
//		List<Dispositivo> list = tunnelRepository.findByIpAdress(tunnel.);
//		if (list.size() > 0) {
//			throw new Exception("Attenzione IP già in uso per altro Dispositivo");
//		}
		//
		//Salva dispositivo
		tunnelRepository.save(tunnel);
	}
	
	

	
	public List<Tunnel> getAllTunnel() throws Exception {
		//
		List<Tunnel> tunnelList = tunnelRepository.findAll();
		return tunnelList;
	}

	public void delete(Long tunnelId) throws Exception {
		tunnelRepository.deleteById(tunnelId);
	}

	@Transactional
	public void save(Tunnel tunnel) throws Exception {
		tunnelRepository.save(tunnel);
	}

	public List<Antenna> getAllAntenna(Long readerId) throws Exception {
		List<Antenna> listAntenna = antennaRepository.findByIdReader(readerId);
		return listAntenna;
	}
	
	
	
	

}