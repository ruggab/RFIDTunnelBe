package net.mcsistemi.rfidtunnel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import net.mcsistemi.rfidtunnel.entity.Reader;

@Repository
public interface ReaderRepository extends JpaSpecificationExecutor<Reader>, JpaRepository<Reader, Long> {

	
	

}