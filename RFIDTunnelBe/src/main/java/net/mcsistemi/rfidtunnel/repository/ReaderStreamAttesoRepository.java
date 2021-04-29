package net.mcsistemi.rfidtunnel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import net.mcsistemi.rfidtunnel.entity.ReaderStreamAtteso;

public interface ReaderStreamAttesoRepository extends JpaRepository<ReaderStreamAtteso, Long> {

	@Query(value = "select s.id, s.packId, epc,tid from \n"
			+ " reader_stream_atteso s where s.packId = ?1", nativeQuery = true)
	List<ReaderStreamAtteso> getReaderStreamExpectedByCollo(String packId);

	@Query(value = "select pack_id,epc  from reader_stream_atteso where pack_id = ?1 "
			+ "except select pack_id,epc from reader_stream where pack_id = ?1", nativeQuery = true)
	List<StreamEPCDifference> getDiffEPCExpectedRead(String packId);

	@Query(value = "select pack_id,tid  from reader_stream_atteso where pack_id = ?1 "
			+ "except select pack_id,tid from reader_stream where pack_id = ?1", nativeQuery = true)
	List<StreamTIDDifference> getDiffTIDExpectedRead(String packId);

	@Query(value = "select pack_id,epc from reader_stream where pack_id = ?1 "
			+ "except select pack_id,epc from reader_stream_atteso where pack_id = ?1", nativeQuery = true)
	List<StreamEPCDifference> getDiffEPCReadExpected(String packId);

	@Query(value = "select pack_id,tid from reader_stream where pack_id = ?1 "
			+ "except select pack_id,tid from reader_stream_atteso where pack_id = ?1", nativeQuery = true)
	List<StreamTIDDifference> getDiffTIDReadExpected(String packId);

	@Query(value = "select pack_id, epc from reader_stream_atteso "
			+ "except select pack_id,epc from reader_stream", nativeQuery = true)
	List<StreamEPCDifference> getDiffEPCExpectedRead();

	@Query(value = "select pack_id,tid from reader_stream_atteso "
			+ "except select pack_id,tid from reader_stream", nativeQuery = true)
	List<StreamTIDDifference> getDiffTIDExpectedRead();

	@Query(value = "select pack_id,epc from reader_stream where pack_id = ?1 "
			+ "except select pack_id,epc from reader_stream_atteso where pack_id = ?1", nativeQuery = true)
	List<StreamEPCDifference> getDiffEPCReadExpected();

	@Query(value = "select pack_id,tid from reader_stream where pack_id = ?1 "
			+ "except select pack_id,tid from reader_stream_atteso where pack_id = ?1", nativeQuery = true)
	List<StreamTIDDifference> getDiffTIDReadExpected();

	
	
	public static interface StreamEPCDifference {
		public String getPackId();

		public String getEpc();
	}

	public static interface StreamTIDDifference {
		public String getPackId();

		public String getTid();
	}

}
