package net.smart.rfid.tunnel.util;

import org.apache.log4j.Logger;
import org.epctagcoder.option.SSCC.SSCCExtensionDigit;
import org.epctagcoder.option.SSCC.SSCCFilterValue;
import org.epctagcoder.option.SSCC.SSCCTagSize;
import org.epctagcoder.parse.SSCC.ParseSSCC;
import org.epctagcoder.result.SSCC;

public class SSCC96 {
	
	static Logger logger = Logger.getLogger(SSCC96.class);

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String barcode = decodeSSCC96("3155EBCD4C000013EC000000");
		System.out.println("parseSSCC  "+ barcode );
		try {
			ParseSSCC parseSSCC = ParseSSCC.Builder()
					   .withRFIDTag( "3055EBCD4C000013EC000000" )
					   .build();
			SSCC sscc = parseSSCC.getSSCC();
			System.out.println("parseSSCC  "+ sscc.toString() );
			//
			ParseSSCC parseSSCC2 = ParseSSCC.Builder()
					.withCompanyPrefix("023356789")
					.withExtensionDigit( SSCCExtensionDigit.EXTENSION_3 )
					.withSerial("0200002")
					.withTagSize( SSCCTagSize.BITS_96 )
					.withFilterValue( SSCCFilterValue.RESERVED_5 )
					.build();
			SSCC sscc2 = parseSSCC2.getSSCC();
			String compPrefix = sscc2.getCompanyPrefix();
			String serial = sscc2.getSerial();
			System.out.println("compPrefix  "+ compPrefix );
			System.out.println("serial  "+ serial );
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}
		
	}

	public static String decodeSSCC96(String epc) {
		String barcode = "";
		try {
			ParseSSCC parseSSCC = ParseSSCC.Builder().withRFIDTag(epc).build();
			SSCC sscc = parseSSCC.getSSCC();
			String compPrefix = sscc.getCompanyPrefix();
			barcode = sscc.getSerial();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		//
		return barcode;
		
	}

	
}
