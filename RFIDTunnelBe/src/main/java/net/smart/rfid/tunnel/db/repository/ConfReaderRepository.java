package net.smart.rfid.tunnel.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import net.smart.rfid.tunnel.db.entity.ConfReader;

@Repository
public interface ConfReaderRepository extends JpaSpecificationExecutor<ConfReader>, JpaRepository<ConfReader, Long> {

	public void  deleteByIdTunnelAndIdDispositivo(Long idTunnel, Long idDispositivo) throws Exception;
	
	public List<ConfReader>  findByIdTunnelAndIdDispositivo(Long idTunnel, Long idDispositivo) throws Exception;

}