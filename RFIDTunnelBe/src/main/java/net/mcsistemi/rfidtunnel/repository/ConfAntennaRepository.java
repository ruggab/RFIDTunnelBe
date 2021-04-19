package net.mcsistemi.rfidtunnel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.mcsistemi.rfidtunnel.entity.Antenna;
import net.mcsistemi.rfidtunnel.entity.ConfAntenna;

@Repository
public interface ConfAntennaRepository extends JpaSpecificationExecutor<ConfAntenna>, JpaRepository<ConfAntenna, Long> {
	
	@Query(value = "SELECT * FROM conf_antenna a "+  
			"where 1 = 1 " + 
			"and a.id_conf_reader = ?1", nativeQuery=true )
	public List<ConfAntenna>  findByIdReader(Long idReader) throws Exception;
	
	@Modifying
	@Query(value = "DELETE  FROM conf_antenna a "+  
			"where 1 = 1 " + 
			"and a.id_conf_reader = ?1", nativeQuery=true )
	public void  deleteByIdReader(Long idReader) throws Exception;
	

}