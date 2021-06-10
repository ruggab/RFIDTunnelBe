package net.smart.rfid.tunnel.job;

public interface JobInterface {

	public abstract  void run() throws Exception;
	
	public abstract  void stop() throws Exception;
}
