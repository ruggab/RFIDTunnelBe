package net.mcsistemi.rfidtunnel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import net.mcsistemi.rfidtunnel.entity.ReaderStreamAtteso;

public interface ReaderStreamAttesoRepository extends JpaRepository<ReaderStreamAtteso, Long> {

	@Query(value = "select s.id, s.packId, epc,tid from \n"
			+ " reader_stream_atteso s where s.packId = ?1", nativeQuery = true)
	List<ReaderStreamAtteso> getReaderStreamExpectedByCollo(String packId);

	
	//SOLO EPC
	@Query(value = "select pack_id,epc  from reader_stream_atteso where pack_id = ?1 "
			+ "except select pack_id,epc from reader_stream where pack_id = ?1", nativeQuery = true)
	List<StreamEPCDifference> getDiffEPCExpectedRead(String packId);

	@Query(value = "select pack_id,epc from reader_stream where pack_id = ?1 "
			+ "except select pack_id,epc from reader_stream_atteso where pack_id = ?1", nativeQuery = true)
	List<StreamEPCDifference> getDiffEPCReadExpected(String packId);
	
	//SOLO TID
	@Query(value = "select pack_id,tid  from reader_stream_atteso where pack_id = ?1 "
			+ "except select pack_id,tid from reader_stream where pack_id = ?1", nativeQuery = true)
	List<StreamTIDDifference> getDiffTIDExpectedRead(String packId);

	@Query(value = "select pack_id,tid from reader_stream where pack_id = ?1 "
			+ "except select pack_id,tid from reader_stream_atteso where pack_id = ?1", nativeQuery = true)
	List<StreamTIDDifference> getDiffTIDReadExpected(String packId);
	
	//SOLO USER
	@Query(value = "select pack_id,user_data  from reader_stream_atteso where pack_id = ?1 "
			+ "except select pack_id,user_data from reader_stream where pack_id = ?1", nativeQuery = true)
	List<StreamUserDifference> getDiffUSERExpectedRead(String packId);

	@Query(value = "select pack_id,user_data from reader_stream where pack_id = ?1 "
			+ "except select pack_id,user_data from reader_stream_atteso where pack_id = ?1", nativeQuery = true)
	List<StreamUserDifference> getDiffUSERReadExpected(String packId);
	
	//SOLO BARCODE
	@Query(value = "select pack_id,barcode  from reader_stream_atteso where pack_id = ?1 "
			+ "except select pack_id,barcode from reader_stream where pack_id = ?1", nativeQuery = true)
	List<StreamBarcodeDifference> getDiffBCExpectedRead(String packId);
	
	@Query(value = "select pack_id,barcode from reader_stream where pack_id = ?1 "
			+ "except select pack_id,barcode from reader_stream_atteso where pack_id = ?1", nativeQuery = true)
	List<StreamBarcodeDifference> getDiffBCReadExpected(String packId);
	
	//QUANTITA
	@Query(value = "select count (*) from reader_stream where pack_id = ?1 ", nativeQuery = true)
	Integer getCountLetto(String packId);
	
	@Query(value = "select count (*) from reader_stream_atteso where pack_id = ?1 ", nativeQuery = true)
	Integer getCountExpected(String packId);
	


	
	
	
	public static interface StreamEPCDifference {
		public String getPackId();

		public String getEpc();
	}

	public static interface StreamTIDDifference {
		public String getPackId();

		public String getTid();
	}
	
	public static interface StreamUserDifference {
		public String getPackId();

		public String getUserData();
		
	}
	
	
	public static interface StreamBarcodeDifference {
		public String getPackId();

		public String getBarcode();
		
	}
	

}
