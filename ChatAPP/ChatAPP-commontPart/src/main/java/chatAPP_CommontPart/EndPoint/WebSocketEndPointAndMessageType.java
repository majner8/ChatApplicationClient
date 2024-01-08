package chatAPP_CommontPart.EndPoint;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import chatAPP_CommontPart.Log4j2.Log4j2;

public enum WebSocketEndPointAndMessageType {
	config_startConsuming(),
	config_stopConsuming(),
	acknowledgeEndPoint_confirmMessage(),
	chat_sendMessage(),
	chat_sawMessage(),
	chat_changeMessage();

	private static Map<String,WebSocketEndPointAndMessageType> map;

	WebSocketEndPointAndMessageType(){
	}
	
	static {
		map=Collections.synchronizedMap(new HashMap());
		if(Log4j2.log.isTraceEnabled()) {
			Log4j2.log.trace(Log4j2.MarkerLog.StartApp.getMarker(),"WebSocketEndPointAndMessageType: I am starting init and loaded WebSocketEndPointPath");
		}
		WebSocketProperties pr=new WebSocketProperties();
		
		for(WebSocketEndPointAndMessageType x:WebSocketEndPointAndMessageType.values()) {
			String path=pr.getEndPointPathName(x);
			boolean b=pr.haveToBeMessageRequired(x);
			int rabitPri=pr.getRabitMQPriry(x);
			Class<?>DTO=pr.getDTOClass(x);
			x.setDtoClass(DTO);
			x.setHaveToBeMessageRequired(b);
			x.setRabitMQPriority(rabitPri);
			if(Log4j2.log.isTraceEnabled()) {
				Log4j2.log.trace(Log4j2.MarkerLog.StartApp.getMarker(),String.format("%s : %s", x.name(),x.getPath()));
			}
		}
		if(Log4j2.log.isDebugEnabled()) {
			Log4j2.log.debug(Log4j2.MarkerLog.StartApp.getMarker(),"WebSocketEndPointAndMessageType: map with endPoint value was init and loaded");
		}	
	}
	private String path;
	private int rabitMQPriority;
	private Class<?>dtoClass;
	private boolean haveToBeMessageRequired;
	
	
	
	
	
	public String getPath() {
		return path;
	}




	public int getRabitMQPriority() {
		return rabitMQPriority;
	}




	public Class<?> getDtoClass() {
		return dtoClass;
	}




	public boolean isHaveToBeMessageRequired() {
		return haveToBeMessageRequired;
	}


	private void setRabitMQPriority(int rabitMQPriority) {
		this.rabitMQPriority = rabitMQPriority;
	}




	private void setDtoClass(Class<?> dtoClass) {
		this.dtoClass = dtoClass;
	}

	private void setHaveToBeMessageRequired(boolean haveToBeMessageRequired) {
		this.haveToBeMessageRequired = haveToBeMessageRequired;
	}




	public static WebSocketEndPointAndMessageType getWebSocketEndPointAndMessageTypeByEndPoint(String calledEndPoint) {
		return map.get(calledEndPoint);
	}
	
	public static class WebSocketProperties{
		private final Properties propertyFile;
		private final String propertiesPreflix="";
		private final String propertiesHaveToBeRequiredValue="HaveToBeRequired";
		private final String propertiesEndPointhPathValue="EndPointhPath";
		private final String propertiesAMQPrioryValue="AMQPriory";
		private final String propertiesDToClassNameValue="DToClassName";

		public WebSocketProperties() {
			Properties pr=new Properties();

		}
		
		private String JoinPath(String pathSuflix,WebSocketEndPointAndMessageType mes) {
			return String.join(".", this.propertiesPreflix+mes.name()+pathSuflix);
		}
		public boolean haveToBeMessageRequired(WebSocketEndPointAndMessageType mes) {
			String propPath=this.JoinPath(this.propertiesHaveToBeRequiredValue, mes);
			return Boolean.valueOf(this.propertyFile.getProperty(propPath));
		}
		public int getRabitMQPriry(WebSocketEndPointAndMessageType mes) {
			String propPath=this.JoinPath(this.propertiesAMQPrioryValue, mes);
			return Integer.parseInt(this.propertyFile.getProperty(propPath));

		}
		public Class<?> getDTOClass(WebSocketEndPointAndMessageType mes){
			String propPath=this.JoinPath(this.propertiesDToClassNameValue, mes);
			Class<?>DTO=null;
			try {
				
				DTO=Class.forName(propPath);
			} catch (ClassNotFoundException e) {
				Log4j2.log.error(Log4j2.MarkerLog.StartApp.getMarker(),
						String.format("Error during init value in WebSocketEndPointAndMessageType"+System.lineSeparator()
								+ "Cannot found DTO class"+System.lineSeparator()
								+ "enum name: %s"+System.lineSeparator()
								+ "Properties DTO path: %s", mes.name(),propPath));
			}
			return DTO;
		}
		public String getEndPointPathName(WebSocketEndPointAndMessageType mes) {
			String propPath=this.JoinPath(this.propertiesEndPointhPathValue, mes);
			return this.propertyFile.getProperty(propPath);
		}
		
	}
}

