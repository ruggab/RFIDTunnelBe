package net.mcsistemi.rfidtunnel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import net.mcsistemi.rfidtunnel.entity.Dispositivo;
import net.mcsistemi.rfidtunnel.entity.Reader;

@Repository
public interface DispositivoRepository extends JpaSpecificationExecutor<Dispositivo>, JpaRepository<Dispositivo, Long> {

	List<Dispositivo> findByIpAdress(String ipAdress);
	

}