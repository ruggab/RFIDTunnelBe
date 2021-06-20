package net.smart.rfid.tunnel.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.smart.rfid.tunnel.db.entity.LogTraceWine;
import net.smart.rfid.tunnel.db.services.LogTraceWineService;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:4200")
public class LogTraceWineRestAPIs {

	@Autowired
	private LogTraceWineService logTraceWineService;

	
	
	@GetMapping("/allLogTraceWine")
	public List<LogTraceWine> getAllLogTraceWine() throws Exception {
		List<LogTraceWine> listLogTraceWine = null;
		try {
			listLogTraceWine = logTraceWineService.getAllLog();
		} catch (Exception e) {
			throw e;
		}
		return listLogTraceWine;
	}
	
	
	
	
	
  


}
