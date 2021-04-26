package net.mcsistemi.rfidtunnel.job2;

import java.util.Hashtable;

public class PoolImpinjReader {

	private static Hashtable<Long, JobImpinjInterface> map = new Hashtable<Long, JobImpinjInterface>();

	public static void addJob(Long id, JobImpinjInterface impinjJob) {
		map.put(id, impinjJob);
	}

	public static JobImpinjInterface getJob(Long id) {
		return map.get(id);
	}

	public static JobImpinjInterface removeJob(Long id) {
		return map.remove(id);
	}


}