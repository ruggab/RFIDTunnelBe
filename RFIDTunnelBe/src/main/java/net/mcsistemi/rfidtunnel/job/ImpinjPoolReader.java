package net.mcsistemi.rfidtunnel.job;

import java.util.Hashtable;

import com.impinj.octane.ImpinjReader;

public class ImpinjPoolReader {

	private static Hashtable<Long, ReaderImpinjJob> map = new Hashtable<Long, ReaderImpinjJob>();

	public static void addJob(Long id, ReaderImpinjJob readerImpinjJob) {
		map.put(id, readerImpinjJob);
	}

	public static ReaderImpinjJob getJob(Long id) {
		return map.get(id);
	}

	public static ReaderImpinjJob removeJob(Long id) {
		return map.remove(id);
	}


}