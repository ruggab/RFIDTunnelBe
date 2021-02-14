package net.mcsistemi.rfidtunnel.job;

import com.impinj.octane.OctaneSdkException;

public interface JobImpinjInterface {

	public abstract  void start() throws OctaneSdkException, Exception;
	
	public abstract  void stop() throws OctaneSdkException, Exception;
}
