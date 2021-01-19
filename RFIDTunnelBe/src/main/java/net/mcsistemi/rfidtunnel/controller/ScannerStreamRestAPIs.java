package net.mcsistemi.rfidtunnel.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.mcsistemi.rfidtunnel.entity.ScannerStream;
import net.mcsistemi.rfidtunnel.repository.ScannerStreamRepository;

@RestController
@RequestMapping("/api/v1")
public class ScannerStreamRestAPIs {
	
	@Autowired
    private ScannerStreamRepository repository;
	
	@GetMapping("/scanner")
	public Page<ScannerStream> getScannerStream(Pageable pageable) {
        return repository.findAll(pageable);
    }
	
	@GetMapping("/scanner2")
	public Page<ScannerStream> getScannerStream2(Pageable pageable) {
		
		List<ScannerStream> ws = repository.getScanner();
		
		int start = (int) pageable.getOffset();
		int end = (start + pageable.getPageSize()) > ws.size() ? ws.size() : (start + pageable.getPageSize());
		Page<ScannerStream> pages = new PageImpl<ScannerStream>(ws.subList(start, end), pageable, ws.size());
				
		return pages;
    }
	
}
