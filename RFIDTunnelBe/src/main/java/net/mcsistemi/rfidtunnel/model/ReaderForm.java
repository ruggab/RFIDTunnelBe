package net.mcsistemi.rfidtunnel.model;

public class ReaderForm {


	private String ipAddress;
	private String porta;
	private String tipoReaderSel;
	
	
	public ReaderForm() {
		
	}
	
	public ReaderForm(String ipAddress, String porta, String tipoReaderSel) {
		this.ipAddress = ipAddress;
		this.porta = porta;
		this.tipoReaderSel = tipoReaderSel;
	}

	
	public String getPorta() {
		return porta;
	}

	public void setPorta(String porta) {
		this.porta = porta;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getTipoReaderSel() {
		return tipoReaderSel;
	}

	public void setTipoReaderSel(String tipoReaderSel) {
		this.tipoReaderSel = tipoReaderSel;
	}

	

	
}
