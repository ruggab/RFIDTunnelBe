package net.smart.rfid.tunnel.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import net.smart.rfid.tunnel.db.entity.ProfiloUtente;

@Repository
public interface ProfiloUtenteRepository extends JpaSpecificationExecutor<ProfiloUtente>, JpaRepository<ProfiloUtente, Long> {

	public List<ProfiloUtente> findByIdUtente(Long idUtente);

}