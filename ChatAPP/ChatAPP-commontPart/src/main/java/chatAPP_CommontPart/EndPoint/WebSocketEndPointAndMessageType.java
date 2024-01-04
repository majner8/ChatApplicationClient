package chatAPP_CommontPart.EndPoint;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import chatAPP_CommontPart.Log4j2.Log4j2;

public enum WebSocketEndPointAndMessageType {
	config_startConsuming(""),
	config_stopConsuming(""),
	acknowledgeEndPoint_confirmMessage(""),
	chat_sendMessage(""),
	chat_sawMessage(""),
	chat_changeMessage("");

	
	
	private static Map<String,WebSocketEndPointAndMessageType> map;

	WebSocketEndPointAndMessageType(String value){
		this.path=value;
	}
	
	static {
		map=Collections.synchronizedMap(new HashMap());
		if(Log4j2.log.isTraceEnabled()) {
			Log4j2.log.trace(Log4j2.MarkerLog.StartApp.getMarker(),"WebSocketEndPointAndMessageType: I am starting init and loaded WebSocketEndPointPath");
		}
		Properties pr=new Properties();		
		for(WebSocketEndPointAndMessageType x:WebSocketEndPointAndMessageType.values()) {
			map.put(x.getPath(), x);
			boolean b=Boolean.getBoolean(pr.getProperty(x.name()));
			int rabitPri=Integer.parseInt(pr.getProperty(x.name()));
			Class<?>DTO=null;
			String dtoName=pr.getProperty(pr.getProperty(x.name()));
			try {
				
				DTO=Class.forName(dtoName);
			} catch (ClassNotFoundException e) {
				Log4j2.log.error(Log4j2.MarkerLog.StartApp.getMarker(),
						String.format("Error during init value in WebSocketEndPointAndMessageType"+System.lineSeparator()
								+ "Cannot found DTO class"+System.lineSeparator()
								+ "enum name: %s"+System.lineSeparator()
								+ "Properties DTO name: %s", x.name(),dtoName));
			}
			
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
	
	
}

