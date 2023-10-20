package Logger;

import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Log4j2 {

	@Autowired
	public static  Logger log;
	
	
	public static enum LogMarker{
		
		Security(MarkerManager.getMarker("")),Database(MarkerManager.getMarker(""))
	
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
