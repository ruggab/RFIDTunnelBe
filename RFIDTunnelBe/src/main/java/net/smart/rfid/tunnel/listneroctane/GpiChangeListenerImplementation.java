package net.smart.rfid.tunnel.listneroctane;

import com.impinj.octane.GpiChangeListener;
import com.impinj.octane.GpiEvent;
import com.impinj.octane.ImpinjReader;

import net.smart.rfid.tunnel.db.services.DispositivoService;

public class GpiChangeListenerImplementation implements GpiChangeListener {

	private DispositivoService readerService;

	public GpiChangeListenerImplementation(DispositivoService readerService) {

		this.readerService = readerService;
	}

	@Override
	public void onGpiChanged(ImpinjReader reader, GpiEvent event) {

		// System.out.println("Ingresso : OnGipChanged - GPI Change--port: " + event.getPortNumber() + " status: " +
		// event.isState());

		try {
			if (event.isState()) {
				// reader.setGpo(1, false);
				reader.setGpo(2, false);
				reader.setGpo(3, false);
				reader.setGpo(4, false);
			}

		} catch (Exception ex) {
			System.err.println(ex.getMessage());
		}

	}
}