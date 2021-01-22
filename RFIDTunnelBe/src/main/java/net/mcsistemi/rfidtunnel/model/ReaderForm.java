package net.mcsistemi.rfidtunnel.model;

public class ReaderForm {


	private String ipAdress;
	private String porta;
	private String tipoReaderSel;
	
	
	public ReaderForm() {
		
	}
	
	public ReaderForm(String ipAdress, String porta, String tipoReaderSel) {
		this.ipAdress = ipAdress;
		this.porta = porta;
		this.tipoReaderSel = tipoReaderSel;
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
