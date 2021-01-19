package net.mcsistemi.rfidtunnel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.mcsistemi.rfidtunnel.entity.Properties;

public interface PropertiesRepository extends JpaRepository<Properties, Long> {
	
	Properties findByKey(String key);
	
}
