package net.mcsistemi.rfidtunnel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import net.mcsistemi.rfidtunnel.entity.Utente;

@Repository
public interface UtenteRepository extends JpaSpecificationExecutor<Utente>, JpaRepository<Utente, Long> {

	
	public List<Utente> findByUsrAndPsw(String usr, String psw);

}