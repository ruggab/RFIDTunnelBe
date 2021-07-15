package net.smart.rfid.tunnel.util;

import com.impinj.octane.AutoStartMode;
import com.impinj.octane.AutoStopMode;
import com.impinj.octane.GpoMode;
import com.impinj.octane.OctaneSdkException;
import com.impinj.octane.ReaderMode;
import com.impinj.octane.SearchMode;

public class Utils {

	public static ReaderMode getReaderMode(Integer idMode) throws OctaneSdkException {

		ReaderMode ret = null;
		switch (idMode) {
		case 1:
			ret = ReaderMode.AutoSetDenseReader;
			break;
		case 2:
			ret = ReaderMode.AutoSetCustom;
			break;
		case 3:
			ret = ReaderMode.AutoSetDenseReaderDeepScan;
			break;
		case 4:
			ret = ReaderMode.AutoSetStaticDRM;
			break;
		case 5:
			ret = ReaderMode.AutoSetStaticFast;
			break;
		case 6:
			ret = ReaderMode.DenseReaderM4;
			break;
		case 7:
			ret = ReaderMode.DenseReaderM4Two;
			break;
		case 8:
			ret = ReaderMode.DenseReaderM8;
			break;
		case 9:
			ret = ReaderMode.Hybrid;
			break;
		case 10:
			ret = ReaderMode.MaxMiller;
			break;
		case 11:
			ret = ReaderMode.MaxThroughput;
			break;
		default:
			ret = ReaderMode.AutoSetDenseReader;
			break;
		}
		return ret;
	}

	public static AutoStartMode getAutoStartMode(Integer idAutoStartMode) throws OctaneSdkException {

		AutoStartMode ret = null;
		switch (idAutoStartMode) {
		case 1:
			ret = AutoStartMode.GpiTrigger;
			break;
		case 2:
			ret = AutoStartMode.Immediate;
			break;
		case 3:
			ret = AutoStartMode.Periodic;
			break;
		default:
			ret = AutoStartMode.GpiTrigger;
			break;
		}
		return ret;
	}

	public static AutoStopMode getAutoStopMode(Integer idAutoStopMode) throws OctaneSdkException {
		AutoStopMode ret = null;

		switch (idAutoStopMode) {
		case 1:
			ret = AutoStopMode.GpiTrigger;
			break;
		case 2:
			ret = AutoStopMode.Duration;
			break;
		case 3:
			ret = AutoStopMode.None;
			break;
		default:
			ret = AutoStopMode.GpiTrigger;
			break;
		}
		return ret;
	}

	public static GpoMode getGpoMode(Long idGpoMode) throws OctaneSdkException {
		GpoMode ret = null;

		switch (idGpoMode.intValue()) {
		case 1:
			ret = GpoMode.Normal;
			break;
		case 2:
			ret = GpoMode.Pulsed;
			break;
		case 3:
			ret = GpoMode.ReaderInventoryStatus;
			break;
		case 4:
			ret = GpoMode.LLRPConnectionStatus;
			break;
		default:
			ret = GpoMode.Normal;
			break;
		}
		return ret;
	}

	public static SearchMode getSearchMode(Integer idSearchMode) throws OctaneSdkException {

		SearchMode ret = null;
		switch (idSearchMode) {
		case 1:
			ret = SearchMode.DualTarget;
			break;
		case 2:
			ret = SearchMode.DualTargetBtoASelect;
			break;
		case 3:
			ret = SearchMode.ReaderSelected;
			break;
		case 4:
			ret = SearchMode.SingleTargetReset;
			break;
		default:
			ret = SearchMode.SingleTargetReset;
			break;
		}
		return ret;
	}

	public static String fromHexToInt(String hex) {
		String ret = hex.substring(18, hex.length());
		int ret1 = Integer.parseInt(ret, 16);
		return ret1 + "";

	}

	public static void main(String[] args) {
		//String ret = fromHexToInt("E2801170200013FB54BD08ED");
		//System.out.println(ret);
		
		String aa = getSerialFromMask("XXXXX--XXXXXX","12345678909");
		
		System.out.println(aa);
	}

	public static String getSerialFromMask(String mask, String epc) {
		char splitter = 'X';
		char[] maskArr = mask.toCharArray();
		char[] textArr = epc.toCharArray();
		int textI = 0;
		for (int i = 0; i < maskArr.length; i++) {
			if (maskArr[i] != splitter) {

				if (maskArr[i] == '-' && textI < textArr.length) {
					maskArr[i] = textArr[textI];
				}
				textI++;
			}
		}
		String str = String. valueOf(maskArr);
		String ret = str.replace("X", "");
		return ret;
	}
	
	
    

}
