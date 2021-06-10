package net.mcsistemi.rfidtunnel;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.mcsistemi.rfidtunnel.db.entity.Dispositivo;
import net.mcsistemi.rfidtunnel.db.entity.Tunnel;
import net.mcsistemi.rfidtunnel.db.repository.DispositivoRepository;
import net.mcsistemi.rfidtunnel.db.repository.TunnelRepository;


@Component
public class EventOnStartServer {
	@Autowired private TunnelRepository tunnelRepository;
	@Autowired private DispositivoRepository dispositivoRepository;

	@PostConstruct
	@Transactional
	public void init() {
		List<Tunnel> tunnelList = tunnelRepository.findAll();
		for (Tunnel tunnel : tunnelList) {
			tunnel.setStato(false);
			tunnelRepository.save(tunnel);
		}
		List<Dispositivo> dispositivoList = dispositivoRepository.findAll();
		for (Dispositivo dispositivo : dispositivoList) {
			dispositivo.setStato(false);
			dispositivoRepository.save(dispositivo);
		}
		
	}
}
