package net.mcsistemi.rfidtunnel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import net.mcsistemi.rfidtunnel.entity.ReaderStreamAtteso;

public interface ReaderStreamAttesoRepository extends JpaRepository<ReaderStreamAtteso, Long> {

	@Query(value="select s.id, s.packId, epc,tid from \n" + 
			" reader_stream_atteso s where s.packId = ?1", 
			  nativeQuery = true)
	List<ReaderStreamAtteso> getReaderStreamAttesoByCollo(String packId);
}
