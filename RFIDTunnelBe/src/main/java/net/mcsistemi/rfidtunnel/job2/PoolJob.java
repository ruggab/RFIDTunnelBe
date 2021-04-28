package net.mcsistemi.rfidtunnel.job2;

import java.util.Hashtable;

public class PoolJob {

	private static Hashtable<String, Job> map = new Hashtable<String, Job>();

	public static void addJob(String id, Job job) {
		map.put(id, job);
	}

	public static Job getJob(String id) {
		return map.get(id);
	}

	public static  void removeJob(String id) {
		 map.remove(id);
	}


}