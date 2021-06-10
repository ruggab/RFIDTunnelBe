package net.smart.rfid.tunnel.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import net.smart.rfid.tunnel.db.entity.Funzione;

@Repository
public interface FunzioneRepository extends JpaSpecificationExecutor<Funzione>, JpaRepository<Funzione, Long> {

}