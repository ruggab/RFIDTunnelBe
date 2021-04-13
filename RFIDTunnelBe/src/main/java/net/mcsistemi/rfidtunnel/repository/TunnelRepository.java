package net.mcsistemi.rfidtunnel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import net.mcsistemi.rfidtunnel.entity.Tunnel;

@Repository
public interface TunnelRepository extends JpaSpecificationExecutor<Tunnel>, JpaRepository<Tunnel, Long> {

	
	

}