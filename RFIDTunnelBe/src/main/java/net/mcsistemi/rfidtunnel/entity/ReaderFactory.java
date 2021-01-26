package net.mcsistemi.rfidtunnel.entity;

import net.mcsistemi.rfidtunnel.model.ReaderForm;

public class ReaderFactory {
	

	public static Reader getReader(ReaderForm form) {
		Reader retval = null;
		
		switch (form.getTipoReaderSel()) {
		case "":
			retval = new Reader(form);
			break;
		case "1":
			retval = new ReaderRfidInpinj(form);
			break;
		case "2":
			retval = new ReaderRfidWirama(form);
			break;
		}
		return retval;
	}
}
