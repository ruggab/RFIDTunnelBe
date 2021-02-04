package net.mcsistemi.rfidtunnel.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import net.mcsistemi.rfidtunnel.form.ReaderForm;

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

	@Column(length = 4)
	protected String porta;

	@Column(length = 1)
	protected String separatore;
	
	

	public Reader() {
	}

	public Reader(ReaderForm form) {
		this.ipAdress = form.getIpAdress();
		this.porta = form.getPorta();
		this.separatore = form.getSeparatore();
		this.idTipoReader = new Long(form.getTipoReaderSel());
	}

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

	public String getPorta() {
		return porta;
	}

	public void setPorta(String porta) {
		this.porta = porta;
	}

	public String getSeparatore() {
		return separatore;
	}

	public void setSeparatore(String separatore) {
		this.separatore = separatore;
	}

}
