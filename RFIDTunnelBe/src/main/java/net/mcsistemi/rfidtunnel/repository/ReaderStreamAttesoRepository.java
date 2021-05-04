package net.mcsistemi.rfidtunnel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.mcsistemi.rfidtunnel.entity.ReaderStreamAtteso;

public interface ReaderStreamAttesoRepository extends JpaRepository<ReaderStreamAtteso, Long> {

	@Query(value = "select s.id, s.packId, epc,tid from reader_stream_atteso s where s.packId = ?1", nativeQuery = true)
	List<ReaderStreamAtteso> getReaderStreamExpectedByCollo(String packageData);
	
	//SOLO EPC
	@Query(value = "select a.package_data, a.epc  from reader_stream_atteso a where a.package_data = :packageData  "
			+ "except select s.package_data, s.epc from reader_stream s where s.pack_id = :packId and s.package_data = :packageData", nativeQuery = true)
	List<StreamEPCDifference> getDiffEPCExpectedRead(@Param ("packId") Long packId, @Param ("packageData") String packageData);

	@Query(value = "select s.package_data, s.epc from reader_stream s where s.pack_id = :packId and s.package_data = :packageData "
			+ "except select a.package_data, a.epc from reader_stream_atteso a where a.package_data = :packageData" , nativeQuery = true)
	List<StreamEPCDifference> getDiffEPCReadExpected(@Param ("packId") Long packId, @Param ("packageData") String packageData);
	
	//SOLO TID
	@Query(value = "select a.package_data, a.tid  from reader_stream_atteso a where a.package_data = :packageData "
			+ "except select s.package_data, s.tid from reader_stream s where s.pack_id = :packId and s.package_data = :packageData ", nativeQuery = true)
	List<StreamTIDDifference> getDiffTIDExpectedRead(@Param ("packId") Long packId, @Param ("packageData") String packageData);

	@Query(value = "select s.package_data, s.tid from reader_stream s where  s.pack_id = :packId and s.package_data = :packageData "
			+ "except select a.package_data, a.tid from reader_stream_atteso a where a where a.package_data = :packageData", nativeQuery = true)
	List<StreamTIDDifference> getDiffTIDReadExpected(@Param ("packId") Long packId, @Param ("packageData") String packageData);
	
	//SOLO USER
	@Query(value = "select a.package_data, a.user_data from reader_stream_atteso a where a.package_data = :packageData "
			+ "except select s.package_data, s.user_data from reader_stream where s.pack_id = :packId and s.package_data = :packageData ", nativeQuery = true)
	List<StreamUserDifference> getDiffUSERExpectedRead(@Param ("packId") Long packId, @Param ("packageData") String packageData);

	@Query(value = "select s.package_data, s.user_data from reader_stream s where s.pack_id = :packId and s.package_data = :packageData  "
			+ "except select a.package_data, a.user_data from reader_stream_atteso where a.package_data = :packageData", nativeQuery = true)
	List<StreamUserDifference> getDiffUSERReadExpected(@Param ("packId") Long packId, @Param ("packageData") String packageData);
	
	//SOLO BARCODE
	@Query(value = "select a.package_data, a.barcode  from reader_stream_atteso a where a.package_data = :packageData "
			+ "except select s.package_data, s.barcode from reader_stream s where s.pack_id = :packId and s.package_data = :packageData", nativeQuery = true)
	List<StreamBarcodeDifference> getDiffBCExpectedRead(@Param ("packId") Long packId, @Param ("packageData") String packageData);
	
	@Query(value = "select s.package_data, s.barcode from reader_stream s where s.pack_id = :packId and s.package_data = :packageData "
			+ "except select a.package_data, a.barcode from reader_stream_atteso a where a.package_data = :packageData ", nativeQuery = true)
	List<StreamBarcodeDifference> getDiffBCReadExpected(@Param ("packId") Long packId, @Param ("packageData") String packageData);
	
	//QUANTITA
	@Query(value = "select count (*) from reader_stream s where s.pack_id = :packId and s.package_data = :packageData ", nativeQuery = true)
	Integer getCountLetto(@Param ("packId") Long packId, @Param ("packageData") String packageData);
	
	@Query(value = "select count (*) from reader_stream_atteso a where a.package_data = :packageData ", nativeQuery = true)
	Integer getCountExpected(@Param ("packageData") String packageData);
	
	
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
