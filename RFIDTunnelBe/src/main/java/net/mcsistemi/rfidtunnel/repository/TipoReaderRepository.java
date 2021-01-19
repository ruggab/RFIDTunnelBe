package net.mcsistemi.rfidtunnel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import net.mcsistemi.rfidtunnel.entity.TipoReader;

@Repository
public interface TipoReaderRepository extends JpaSpecificationExecutor<TipoReader>, JpaRepository<TipoReader, Long> {

	
	

}