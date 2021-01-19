package net.mcsistemi.rfidtunnel.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.mcsistemi.rfidtunnel.entity.Properties;
import net.mcsistemi.rfidtunnel.exception.ResourceNotFoundException;
import net.mcsistemi.rfidtunnel.repository.PropertiesRepository;

@RestController
@RequestMapping("/api/v1")
public class PropertiesRestAPIs {

	@Autowired
	private PropertiesRepository repository;

	@GetMapping("/properties")
	public Page<Properties> getScannerStream(Pageable pageable) {
		return repository.findAll(pageable);
	}

	@PostMapping("/properties")
	public Properties createProperties(@Valid @RequestBody Properties properties) {
		return repository.save(properties);
	}

	@PutMapping("/properties/{id}")
	public Properties updateProperties(@PathVariable long id, @Valid @RequestBody Properties propertiesRequest) {
		return repository.findById(id).map(properties -> {
			properties.setkey(propertiesRequest.getkey());
			properties.setValue(propertiesRequest.getValue());
			return repository.save(properties);
		}).orElseThrow(() -> new ResourceNotFoundException("Properties not found with id " + id));
	}

	@DeleteMapping("/properties/{id}")
	public ResponseEntity<?> deleteProperties(@PathVariable Long id) {
		return repository.findById(id).map(properties -> {
			repository.delete(properties);
			return ResponseEntity.ok().build();
		}).orElseThrow(() -> new ResourceNotFoundException("Properties not found with id " + id));
	}

}
