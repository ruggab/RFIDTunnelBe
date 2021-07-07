package net.smart.rfid.tunnel.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.smart.rfid.tunnel.db.entity.Dispositivo;

@Repository
public interface DispositivoRepository extends JpaSpecificationExecutor<Dispositivo>, JpaRepository<Dispositivo, Long> {

	List<Dispositivo> findByIpAdress(String ipAdress);
	
	
	List<Dispositivo> findByIdTipoDispositivo(Long idTipoDispositivo);
	
	
	@Query(value = "select a.*, b.tunnel_id  from dispositivo a, tunnel_dispositivi b where b.dispositivi_id = a.id", nativeQuery=true )
	List<Dispositivo> findAllDispositivi();
	
	@Query(value = "select a.* from dispositivo a, tunnel_dispositivi b where b.dispositivi_id = a.id and b.tunnel_id = ?1 order by a.nome", nativeQuery=true )
	List<Dispositivo> findDispositiviByTunnel(Long idTunnel);
	

}