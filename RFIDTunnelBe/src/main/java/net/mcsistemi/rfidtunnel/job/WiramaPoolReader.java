package net.mcsistemi.rfidtunnel.job;

import java.util.Hashtable;

public class WiramaPoolReader {

	private static Hashtable<Long, Runnable> mapThread = new Hashtable<Long, Runnable>();

	public static void addThread(Long id, Runnable runnable) {
		mapThread.put(id, runnable);
	}

	public static Runnable getThread(Long id) {
		return mapThread.get(id);
	}

	public static Runnable removeThread(Long id) {
		return mapThread.remove(id);
	}


}