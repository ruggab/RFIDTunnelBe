package net.mcsistemi.rfidtunnel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.mcsistemi.rfidtunnel.entity.Tunnel;
import net.mcsistemi.rfidtunnel.entity.TunnelDispositivi;

@Repository
public interface TunnelDispositiviRepository extends JpaSpecificationExecutor<TunnelDispositivi>, JpaRepository<TunnelDispositivi, Long> {

	
	

}