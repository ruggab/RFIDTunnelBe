package net.mcsistemi.rfidtunnel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.mcsistemi.rfidtunnel.entity.Antenna;

@Repository
public interface AntennaRepository extends JpaSpecificationExecutor<Antenna>, JpaRepository<Antenna, Long> {
	
	@Query(value = "SELECT * FROM antenna a "+  
			"where 1 = 1 " + 
			"and a.id_reader = ?1", nativeQuery=true )
	public List<Antenna>  findByIdReader(Long idReader) throws Exception;
	
	@Modifying
	@Query(value = "DELETE  FROM antenna a "+  
			"where 1 = 1 " + 
			"and a.id_reader = ?1", nativeQuery=true )
	public void  deleteByIdReader(Long idReader) throws Exception;
	

}