package net.mcsistemi.rfidtunnel.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import net.mcsistemi.rfidtunnel.db.entity.Tipologica;

@Repository
public interface TipoReaderRepository extends JpaSpecificationExecutor<Tipologica>, JpaRepository<Tipologica, Long> {

	
	

}