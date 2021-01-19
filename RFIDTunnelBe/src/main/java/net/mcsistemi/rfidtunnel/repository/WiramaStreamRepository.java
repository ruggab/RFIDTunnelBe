package net.mcsistemi.rfidtunnel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import net.mcsistemi.rfidtunnel.entity.WiramaStream;

public interface WiramaStreamRepository extends JpaRepository<WiramaStream, Long> {

	List<WiramaStream> findByPackId(String packId);

	long countByPackId(String packId);
}
