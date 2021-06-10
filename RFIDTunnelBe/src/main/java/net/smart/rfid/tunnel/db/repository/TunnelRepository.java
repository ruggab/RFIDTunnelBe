package net.smart.rfid.tunnel.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.smart.rfid.tunnel.db.entity.Tunnel;

@Repository
public interface TunnelRepository extends JpaSpecificationExecutor<Tunnel>, JpaRepository<Tunnel, Long> {

	
	@Query(value = "SELECT nextval('serial') ", nativeQuery = true)
	Integer getSeqNextVal();
	
	@Query(value = "SELECT * from tunnel a inner join tunnel_dispositivi b on a.id = b.tunnel_id where b.dispositivi_id =?1 ", nativeQuery = true)
	List<Tunnel> getTunnelFromDisp(Long idDispo);
	
}