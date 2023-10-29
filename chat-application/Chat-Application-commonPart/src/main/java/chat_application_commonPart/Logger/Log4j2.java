package chat_application_commonPart.Logger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;


public class Log4j2 {

	
    public static final Logger log = LogManager.getLogger(Log4j2.class);
	
	
	
	public static enum LogMarker{
		
		Security(MarkerManager.getMarker("")),Database(MarkerManager.getMarker("")
	),Validation(MarkerManager.getMarker(""))
	
		;
		
		private Marker marker;
		
		LogMarker(Marker marker){
			this.marker=marker;
		}
		
		public Marker getMarker() {
			return this.marker;
		}
		
		
	}
}
