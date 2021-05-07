package net.mcsistemi.rfidtunnel.job;

public interface JobInterface {

	public abstract  void run() throws Exception;
	
	public abstract  void stop() throws Exception;
}
