package net.mcsistemi.rfidtunnel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import net.mcsistemi.rfidtunnel.entity.ConfReader;

@Repository
public interface ConfReaderRepository extends JpaSpecificationExecutor<ConfReader>, JpaRepository<ConfReader, Long> {

	public void  deleteByIdTunnelAndIdDispositivo(Long idReader, Long idDispositivo) throws Exception;

}