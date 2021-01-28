package net.mcsistemi.rfidtunnel.form;

public class ReaderForm {

	private String ipAdress;
	private String porta;
	private String tipoReaderSel;

	private String separatore;
	private String antenna1;
	private String antenna2;
	private String antenna3;
	private String antenna4;
	private String antenna5;

	public ReaderForm() {

	}

	

	public String getSeparatore() {
		return separatore;
	}

	public void setSeparatore(String separatore) {
		this.separatore = separatore;
	}

	public String getAntenna1() {
		return antenna1;
	}

	public void setAntenna1(String antenna1) {
		this.antenna1 = antenna1;
	}

	public String getAntenna2() {
		return antenna2;
	}

	public void setAntenna2(String antenna2) {
		this.antenna2 = antenna2;
	}

	public String getAntenna3() {
		return antenna3;
	}

	public void setAntenna3(String antenna3) {
		this.antenna3 = antenna3;
	}

	public String getAntenna4() {
		return antenna4;
	}

	public void setAntenna4(String antenna4) {
		this.antenna4 = antenna4;
	}

	public String getAntenna5() {
		return antenna5;
	}

	public void setAntenna5(String antenna5) {
		this.antenna5 = antenna5;
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
