package net.smart.rfid.tunnel.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import net.smart.rfid.tunnel.db.entity.Tipologica;

@Repository
public interface TipologicaRepository extends JpaSpecificationExecutor<Tipologica>, JpaRepository<Tipologica, Long> {

	
	List<Tipologica> findByContesto(String cotesto);

}