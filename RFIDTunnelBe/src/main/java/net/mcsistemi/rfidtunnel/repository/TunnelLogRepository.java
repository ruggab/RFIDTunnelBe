package net.mcsistemi.rfidtunnel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import net.mcsistemi.rfidtunnel.model.TunnelLog;

public interface TunnelLogRepository extends JpaRepository<TunnelLog, Long> {

	List<TunnelLog> findAll();

	@Query(value = "select * from log order by time_stamp desc limit 500", nativeQuery = true)
	List<TunnelLog> findAllLimit();

}
