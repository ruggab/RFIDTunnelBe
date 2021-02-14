package net.mcsistemi.rfidtunnel.job;

import com.impinj.octane.ImpinjReader;
import com.impinj.octane.OctaneSdkException;
import com.impinj.octane.Settings;

import net.mcsistemi.rfidtunnel.entity.ReaderRfidInpinj;
import net.mcsistemi.rfidtunnel.listneroctane.TagReportListenerImplementation;
import net.mcsistemi.rfidtunnel.services.ReaderService;

public class JobImpinj2 implements JobImpinjInterface {

	private ImpinjReader reader = null;

	public JobImpinj2(ReaderRfidInpinj readerRfidInpinj, ReaderService readerService) throws OctaneSdkException, Exception {

		try {
			String hostname = readerRfidInpinj.getIpAdress();

			reader = new ImpinjReader();

			// Connect
			System.out.println("Connecting to " + hostname);
			reader.connect(hostname);

			// Get the default settings
			Settings settings = reader.queryDefaultSettings();

			// Apply the new settings
			reader.applySettings(settings);

			// connect a listener
			reader.setTagReportListener(new TagReportListenerImplementation(readerService));

			// Start the reader
			reader.start();

			System.out.println("Start Done");
		} catch (OctaneSdkException ex) {
			System.out.println(ex.getMessage());
			throw ex;
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace(System.out);
			throw ex;
		}
	}

	public void start() throws OctaneSdkException, Exception {
		try {
			if (this.reader.isConnected()) {
				// Start the reader
				this.reader.start();
			}
			System.out.println("Start Done");
		} catch (OctaneSdkException ex) {
			throw ex;
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			throw ex;
		}
	}

	public void stop() throws OctaneSdkException, Exception {
		try {

			System.out.println("Stopping  " + reader.getAddress());
			reader.stop();

			System.out.println("Disconnecting from " + reader.getAddress());
			reader.disconnect();

			System.out.println("Stopping e Disconnecting Done");
		} catch (OctaneSdkException ex) {
			throw ex;
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			throw ex;
		}
	}

}