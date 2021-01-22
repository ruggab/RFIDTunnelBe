package net.mcsistemi.rfidtunnel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.mcsistemi.rfidtunnel.entity.Proprieta;

public interface PropertiesRepository extends JpaRepository<Proprieta, Long> {
	
	Proprieta findByChiave(String chiave);
	
}
