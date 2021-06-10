package net.mcsistemi.rfidtunnel.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.mcsistemi.rfidtunnel.db.entity.ConfAntenna;
import net.mcsistemi.rfidtunnel.db.entity.ConfPorta;

@Repository
public interface ConfPortRepository extends JpaSpecificationExecutor<ConfPorta>, JpaRepository<ConfPorta, Long> {
	
	@Query(value = "SELECT * FROM conf_porta a "+  
			"where 1 = 1 " + 
			"and a.id_conf_reader = ?1", nativeQuery=true )
	public List<ConfPorta>  findByIdReader(Long idReader) throws Exception;
	
	
	
	@Modifying
	@Query(value = "DELETE  FROM conf_porta a "+  
			"where 1 = 1 " + 
			"and a.id_conf_reader = ?1", nativeQuery=true )
	public void  deleteByIdReader(Long idReader) throws Exception;
	
	
	

}