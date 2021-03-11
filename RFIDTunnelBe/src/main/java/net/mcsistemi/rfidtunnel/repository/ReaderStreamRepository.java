package net.mcsistemi.rfidtunnel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.mcsistemi.rfidtunnel.entity.ReaderStream;

public interface ReaderStreamRepository extends JpaRepository<ReaderStream, Long> {

	
}
