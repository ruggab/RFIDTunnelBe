package net.smart.rfid.tunnel.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import net.smart.rfid.tunnel.db.entity.Utente;

@Repository
public interface UtenteRepository extends JpaSpecificationExecutor<Utente>, JpaRepository<Utente, Long> {

	
	public List<Utente> findByUsrAndPsw(String usr, String psw);

}