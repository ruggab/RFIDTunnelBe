package net.mcsistemi.rfidtunnel.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.mcsistemi.rfidtunnel.entity.ScannerStream;

public interface ScannerStreamRepository extends JpaRepository<ScannerStream, Long> {
	
	@Query(value="select s.id, s.pack_id , s.time_stamp, count(distinct(epc)) epc_count from scanner s\n" + 
			"join wirama w on s.pack_id = w.pack_id\n" + 
			"group by s.id, s.pack_id, s.time_stamp order by s.time_stamp desc ", 
			  nativeQuery = true)
	List<ScannerStream> getScanner();
	
	@Modifying
	@Transactional
	@Query(value="delete from scanner where pack_id = ?1 ", 
			  nativeQuery = true)
	void deleteScannerByPackId(String packId);
	
	@Modifying
	@Transactional
	@Query(value="delete from wirama where pack_id = ?1 ", 
			  nativeQuery = true)
	void deleteWiramaByPackId(String packId);
	
	
}
