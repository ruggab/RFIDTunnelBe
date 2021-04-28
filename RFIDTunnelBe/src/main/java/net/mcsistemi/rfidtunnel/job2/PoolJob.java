package net.mcsistemi.rfidtunnel.job2;

import java.util.Hashtable;

public class PoolJob {

	private static Hashtable<Long, Job> map = new Hashtable<Long, Job>();

	public static void addJob(Long id, Job job) {
		map.put(id, job);
	}

	public static Job getJob(Long id) {
		return map.get(id);
	}

	public static  void removeJob(Long id) {
		 map.remove(id);
	}


}