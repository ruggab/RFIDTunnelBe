package net.mcsistemi.rfidtunnel.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author Gabriele
 *
 */
@Entity
@Table(name = "reader")
public class Reader {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	protected Long id;

	protected Long idTipoReader;

	@Column(length = 15)
	protected String ipAdress;

	@Column(length = 1)
	protected String separatore;
	
	protected Boolean keepAlive;

	public Boolean getKeepAlive() {
		return keepAlive;
	}

	public void setKeepAlive(Boolean keepAlive) {
		this.keepAlive = keepAlive;
	}

	protected Boolean stato;
	
	@Transient
	protected List<Antenna> listAntenna = new ArrayList<Antenna>();

	public Reader() {
	}

//	public Reader(ReaderForm form) {
//		this.ipAdress = form.getIpAdress();
//		this.porta = form.getPorta();
//		this.separatore = form.getSeparatore();
//		this.idTipoReader = new Long(form.getTipoReaderSel());
//	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdTipoReader() {
		return idTipoReader;
	}

	public void setIdTipoReader(Long idTipoReader) {
		this.idTipoReader = idTipoReader;
	}

	public String getIpAdress() {
		return ipAdress;
	}

	public void setIpAdress(String ipAdress) {
		this.ipAdress = ipAdress;
	}

	

	public String getSeparatore() {
		return separatore;
	}

	public void setSeparatore(String separatore) {
		this.separatore = separatore;
	}

	public List<Antenna> getListAntenna() {
		return listAntenna;
	}

	public void setListAntenna(List<Antenna> listAntenna) {
		this.listAntenna = listAntenna;
	}

	public Boolean getStato() {
		return stato;
	}

	public void setStato(Boolean stato) {
		this.stato = stato;
	}
	
	

}
