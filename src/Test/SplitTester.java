package Test;

public class SplitTester {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String x="|cU3gxiW42Qk8QrgB7UucnSeEmz3U18Wpr|cahooj|cUucnSeEmz3U18Wpr|c2023-06-11 23:48:21|c1|cdq4idPr"
	;
		
		if(x.startsWith("|c",0)) {
			x=x.substring(2, x.length());
		}

		for(String i:x.split("\\|c")) {
		}
	}

}
