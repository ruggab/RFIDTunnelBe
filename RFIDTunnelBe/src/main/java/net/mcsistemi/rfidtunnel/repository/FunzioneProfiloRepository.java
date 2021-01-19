package net.mcsistemi.rfidtunnel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import net.mcsistemi.rfidtunnel.entity.FunzioneProfilo;

@Repository
public interface FunzioneProfiloRepository extends JpaSpecificationExecutor<FunzioneProfilo>, JpaRepository<FunzioneProfilo, Long> {

	public List<FunzioneProfilo> findByIdProfilo(Long IdProfilo);
}