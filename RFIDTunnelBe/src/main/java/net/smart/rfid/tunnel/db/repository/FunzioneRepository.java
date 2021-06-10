package net.mcsistemi.rfidtunnel.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import net.mcsistemi.rfidtunnel.db.entity.Funzione;

@Repository
public interface FunzioneRepository extends JpaSpecificationExecutor<Funzione>, JpaRepository<Funzione, Long> {

}