package net.smart.rfid.tunnel.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import net.smart.rfid.tunnel.db.entity.Profilo;

@Repository
public interface ProfiloRepository extends JpaSpecificationExecutor<Profilo>, JpaRepository<Profilo, Long> {

	
	

}