package net.mcsistemi.rfidtunnel.form;

public class ReaderForm {

	private String ipAdress;
	private String porta;
	private String tipoReaderSel;
	private String separatore;
	

	public ReaderForm() {

	}

	

	public String getSeparatore() {
		return separatore;
	}

	public void setSeparatore(String separatore) {
		this.separatore = separatore;
	}

	

	public String getPorta() {
		return porta;
	}

	public void setPorta(String porta) {
		this.porta = porta;
	}

	public String getIpAdress() {
		return ipAdress;
	}

	public void setIpAdress(String ipAdress) {
		this.ipAdress = ipAdress;
	}

	public String getTipoReaderSel() {
		return tipoReaderSel;
	}

	public void setTipoReaderSel(String tipoReaderSel) {
		this.tipoReaderSel = tipoReaderSel;
	}

}
