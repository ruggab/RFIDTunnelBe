package net.mcsistemi.rfidtunnel.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "funzione_profilo")
public class FunzioneProfilo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	
	@NotBlank
	private Long idProfilo;

	@NotBlank
	private Long idFunzione;
	
	
	
	public FunzioneProfilo() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdProfilo() {
		return idProfilo;
	}

	public void setIdProfilo(Long idProfilo) {
		this.idProfilo = idProfilo;
	}

	public Long getIdFunzione() {
		return idFunzione;
	}

	public void setIdFunzione(Long idFunzione) {
		this.idFunzione = idFunzione;
	}

	

	

}
