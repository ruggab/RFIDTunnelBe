package net.mcsistemi.rfidtunnel.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "utente")
public class Utente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Transient
	private List<Funzione> listFunzioni;

	
	@NotBlank
	private String usr;

	@NotBlank
	private String psw;
	
	
	
	public Utente() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	

	public String getUsr() {
		return usr;
	}

	public void setUsr(String usr) {
		this.usr = usr;
	}

	public String getPsw() {
		return psw;
	}

	public void setPsw(String psw) {
		this.psw = psw;
	}

	public List<Funzione> getListFunzioni() {
		return listFunzioni;
	}

	public void setListFunzioni(List<Funzione> listFunzioni) {
		this.listFunzioni = listFunzioni;
	}

	


	

}
