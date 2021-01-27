package net.mcsistemi.rfidtunnel.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.mcsistemi.rfidtunnel.entity.TunnelLog;
import net.mcsistemi.rfidtunnel.repository.TunnelLogRepository;

@RestController
@RequestMapping("/api/v1")
public class TunnelLogRestAPIs {
	
	@Autowired
    private TunnelLogRepository repository;
	
	@GetMapping("/log")
	public Page<TunnelLog> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
	
	@GetMapping("/logList")
	public List<TunnelLog> getListAll() {
        return repository.findAllLimit();
    }
}
