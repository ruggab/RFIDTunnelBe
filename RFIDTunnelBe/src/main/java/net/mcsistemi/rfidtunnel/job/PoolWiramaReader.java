package net.mcsistemi.rfidtunnel.job;

import java.util.Hashtable;

public class PoolWiramaReader {

	private static Hashtable<String, Runnable> mapThread = new Hashtable<String, Runnable>();

	public static void addThread(String id, Runnable runnable) {
		mapThread.put(id, runnable);
	}

	public static Runnable getThread(String id) {
		return mapThread.get(id);
	}

	public static Runnable removeThread(String id) {
		return mapThread.remove(id);
	}


}