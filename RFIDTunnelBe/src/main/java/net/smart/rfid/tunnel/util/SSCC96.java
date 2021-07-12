package net.smart.rfid.tunnel.util;

import java.math.BigInteger;

import org.apache.log4j.Logger;

public class SSCC96 {
	
	static Logger logger = Logger.getLogger(SSCC96.class);

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String barcode = decodeSSCC96("3155EBCD4C000013EC000000");
		logger.info("SSCC-96 Barcode: " + barcode);
	}

	public static String decodeSSCC96 (String epc) {
		
		String barcode = "";
		
		if(epc.length()==24) {
			
			if(epc.substring(0,2).equals("31")) { 
		
				// Se inizia con 31 Ã¨ SSCC
				String header = "";
				String filter = "";
				String partition = "";
				String company_prefix = "";
				String extension = "";
				String RFU = "";
				
				int cp_length = 0;
				int cp_digits = 0;
				int ex_length = 0;
				int ex_digits = 0;
				
				try {
		    		
					String epcbin = "";
					
					int start = 0;
					for(int i=0;i<epc.length();i++) {
						//System.out.println(i);
						String bin="0000"+hexToBin(epc.substring(i,start+1));
						bin=bin.substring(bin.length()-4,bin.length()); 
						epcbin+=bin;
						start+=1;
					}
					
					if(epcbin.length()<96) {
		    			epcbin = "0000000000"+epcbin;
		    			epcbin = epcbin.substring(epcbin.length()-96,epcbin.length());
		    		}
		    		//System.out.println(hexToBin(toHexadecimal(t.getEpc().toHexString().replaceAll(" ", "").trim())));
		    		//System.out.println(epcbin);
		    		
		    		
		    		header = epcbin.substring(0,8);
		    		filter = epcbin.substring(8,11);
		    		partition = epcbin.substring(11,14);
		    		
		    		//System.out.println(header);
		    		//System.out.println(filter);
		    		//System.out.println(partition);
		    		
		    		partition = ""+Integer.parseInt(epcbin.substring(11,14), 2 );
		    		
		    		switch (partition) {
		            case "0":  
		            	cp_length = 40;
		            	cp_digits = 12;
		            	ex_length = 18;
		            	ex_digits = 5;
		                break;
		            case "1":  
		            	cp_length = 37;
		            	cp_digits = 11;
		            	ex_length = 21;
		            	ex_digits = 6;
		                break;
		            case "2": 
		            	cp_length = 34;
		            	cp_digits = 10;
		            	ex_length = 24;
		            	ex_digits = 7;
		                break;
		            case "3":  
		            	cp_length = 30;
		            	cp_digits = 9;
		            	ex_length = 28;
		            	ex_digits = 8;
		                break;
		            case "4": 
		            	cp_length = 27;
		            	cp_digits = 8;
		            	ex_length = 31;
		            	ex_digits = 9;
		                break;
		            case "5": 
		            	cp_length = 24;
		            	cp_digits = 7;
		            	ex_length = 34;
		            	ex_digits = 10;
		                break;
		            case "6": 
		            	cp_length = 20;
		            	cp_digits = 6;
		            	ex_length = 38;
		            	ex_digits = 11;
		                break;
		    		}
		    		
		    		
		    		company_prefix = ""+(int) Long.parseLong(epcbin.substring(14, (14+cp_length)), 2);
		    		extension = epcbin.substring((14+cp_length), (14+cp_length+ex_length));
		    		extension =  ""+new BigInteger(extension, 2);
		    		
		    		
		    		switch (partition) {
		            case "0":  
		            	company_prefix = "000000000000"+company_prefix;
		            	company_prefix = company_prefix.substring(company_prefix.length()-12,company_prefix.length());
		            	extension = "00000"+extension;
		            	extension = extension.substring(extension.length()-5, extension.length());
		                break;
		            case "1":  
		            	company_prefix = "00000000000"+company_prefix;
		            	company_prefix = company_prefix.substring(company_prefix.length()-11,company_prefix.length());
		            	extension = "000000"+extension;
		            	extension = extension.substring(extension.length()-6, extension.length());
		                break;
		            case "2": 
		            	company_prefix = "0000000000"+company_prefix;
		            	company_prefix = company_prefix.substring(company_prefix.length()-10,company_prefix.length());
		            	extension = "0000000"+extension;
		            	extension = extension.substring(extension.length()-7, extension.length());
		                break;
		            case "3":  
		            	company_prefix = "000000000"+company_prefix;
		            	company_prefix = company_prefix.substring(company_prefix.length()-9,company_prefix.length());
		            	extension = "00000000"+extension;
		            	extension = extension.substring(extension.length()-8, extension.length());
		                break;
		            case "4": 
		            	company_prefix = "00000000"+company_prefix;
		            	company_prefix = company_prefix.substring(company_prefix.length()-8,company_prefix.length());
		            	extension = "000000000"+extension;
		            	extension = extension.substring(extension.length()-9, extension.length());
		                break;
		            case "5": 
		            	company_prefix = "0000000"+company_prefix;
		            	company_prefix = company_prefix.substring(company_prefix.length()-7,company_prefix.length());
		            	extension = "0000000000"+extension;
		            	extension = extension.substring(extension.length()-10, extension.length());
		                break;
		            case "6": 
		            	company_prefix = "000000"+company_prefix;
		            	company_prefix = company_prefix.substring(company_prefix.length()-6,company_prefix.length());
		            	extension = "00000000000"+extension;
		            	extension = extension.substring(extension.length()-11, extension.length());
		                break;
		    		}
		    		    		
		    		//extension = epcbin.substring((14+cp_length), (14+cp_length+ex_length));
		    		
		    		//System.out.println(company_prefix);
		    		//System.out.println(extension);
		    		
		    		barcode = company_prefix+extension;
		    		
		    	}
		    	catch(Exception e) {
		    		e.printStackTrace();
		    	}
			}
			else {
				logger.info("*** "+epc+" non e' un SSCC-96");
			}
		
		}
		else {
			logger.info("Lunghezza EPC errata");
		}
		
		return barcode;
		
	}

	static String hexToBin(String s) {
		// System.out.println(s);
		return new BigInteger(s, 16).toString(2);
	}
}
