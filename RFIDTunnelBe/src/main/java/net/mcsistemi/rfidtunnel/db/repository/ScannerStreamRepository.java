package net.mcsistemi.rfidtunnel.db.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.mcsistemi.rfidtunnel.db.entity.ScannerStream;

public interface ScannerStreamRepository extends JpaRepository<ScannerStream, Long> {
	
//	@Query(value="select s.id, s.pack_id , s.time_stamp, count(distinct(epc)) epc_count from scanner_stream s\n" + 
//			"join reader_stream w on s.pack_id = w.pack_id\n" + 
//			"group by s.id, s.pack_id, s.time_stamp order by s.time_stamp desc ", 
//			  nativeQuery = true)
//	List<ScannerStream> getStreamCountEpc();
//	
//	@Query(value="select s.id, s.pack_id , s.time_stamp, count(distinct(tid)) epc_count from scanner_stream s\n" + 
//			"join reader_stream w on s.pack_id = w.pack_id\n" + 
//			"group by s.id, s.pack_id, s.time_stamp order by s.time_stamp desc ", 
//			  nativeQuery = true)
//	List<ScannerStream> getStreamCountTid();
	
	@Modifying
	@Transactional
	@Query(value="delete from scanner_stream where pack_id = ?1 ", nativeQuery = true)
	void deleteScannerByPackId(String packId);
	
	
	@Query(value="select * from scanner_stream where dettaglio = 'N' order by time_stamp desc fetch first 1 rows only ", nativeQuery = true)
    ScannerStream getLastScanner();
	
	@Query(value="select * from scanner_stream where dettaglio = 'N' order by time_stamp desc  ", nativeQuery = true)
    List<ScannerStream> getScannerNoDetail();
	
	
	
	@Modifying
	@Transactional
	@Query(value="ALTER TABLE scanner_stream ENABLE TRIGGER  expt_ean128 ", nativeQuery = true)
	void enableTrigger();
	
	
	@Modifying
	@Transactional
	@Query(value="ALTER TABLE scanner_stream DISABLE TRIGGER  expt_ean128 ", nativeQuery = true)
	void disableTrigger();
	
	
	@Query(value="SELECT count(*) FROM scanner_stream WHERE DATE(time_stamp) = current_date   ", nativeQuery = true)
    Integer getTotalPackageReadDay();
	
	@Query(value="SELECT count(*) FROM scanner_stream WHERE DATE(time_stamp) = current_date  and esito = 'KO' ", nativeQuery = true)
    Integer getTotalPackageKoDay();
	
	//From Monday to Sunday
	@Query(value="SELECT count(*) from scanner_stream WHERE time_stamp >= date_trunc('week', CURRENT_TIMESTAMP - interval '1 week') and time_stamp < date_trunc('week', CURRENT_TIMESTAMP)   ", nativeQuery = true)
	Integer getTotalPackageReadLastWeek();
	
	//From Monday to Sunday
	@Query(value="SELECT count(*) from scanner_stream WHERE time_stamp >= date_trunc('week', CURRENT_TIMESTAMP - interval '1 week') and time_stamp < date_trunc('week', CURRENT_TIMESTAMP) and esito = 'KO'  ", nativeQuery = true)
	Integer getTotalPackageKoLastWeek();
	
	
	//From 1 to 30/31 mese precedente
	@Query(value="SELECT count(*) from scanner_stream where time_stamp >= date_trunc('month', now()) - interval '1 month' and time_stamp < date_trunc('month', now())  ", nativeQuery = true)
	Integer getTotalPackageReadLastMonth();
	
	//From 1 to 30/31 mese precedente
	@Query(value="SELECT count(*) from scanner_stream where time_stamp >= date_trunc('month', now()) - interval '1 month' and time_stamp < date_trunc('month', now())  and esito = 'KO'", nativeQuery = true)
	Integer getTotalPackageKoLastMonth();
}
