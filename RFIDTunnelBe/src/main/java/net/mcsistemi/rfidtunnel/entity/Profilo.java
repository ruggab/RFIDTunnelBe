package net.mcsistemi.rfidtunnel.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "profilo")
public class Profilo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
    private Long id;
	
	private String codice;
	
	private String descrizione;
	
	@Transient
	private List<Funzione> listFunzione;
	
	public Profilo(){}
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodice() {
		return codice;
	}


	public void setCodice(String codice) {
		this.codice = codice;
	}


	public String getDescrizione() {
		return descrizione;
	}


	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}


	public List<Funzione> getListFunzione() {
		return listFunzione;
	}


	public void setListFunzione(List<Funzione> listFunzione) {
		this.listFunzione = listFunzione;
	}

	
	
	
}
