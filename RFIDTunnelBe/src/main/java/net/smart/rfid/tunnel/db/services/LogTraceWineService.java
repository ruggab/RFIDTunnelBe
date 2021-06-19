package net.smart.rfid.tunnel.db.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.smart.rfid.tunnel.db.entity.LogTraceWine;
import net.smart.rfid.tunnel.db.entity.Tipologica;
import net.smart.rfid.tunnel.db.repository.LogTraceWineRepository;
import net.smart.rfid.tunnel.db.repository.TipologicaRepository;

@Service
public class LogTraceWineService{

	
	@Autowired
	private LogTraceWineRepository logTraceWineRepository;
	
	
	
	public List<LogTraceWine> getAllLog() {
		return logTraceWineRepository.findAll();
	}
	
	

	
	
	
	
}