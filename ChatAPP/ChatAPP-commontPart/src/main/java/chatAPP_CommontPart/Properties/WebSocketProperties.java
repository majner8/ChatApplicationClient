package chatAPP_CommontPart.Properties;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import chatAPP_CommontPart.Log4j2.Log4j2;
@Component
@PropertySource("classpath:additional-config.properties")
@ConfigurationProperties(prefix = "some.prefix")
public class WebSocketProperties {
	@Autowired
	private Environment env;
	private  Map<String,WebSocketEndPointAndMessageType> map=Collections.synchronizedMap(new HashMap());
	public  WebSocketEndPointAndMessageType getWebSocketEndPointAndMessageTypeByEndPoint(String calledEndPoint) {
		return map.get(calledEndPoint);
	}
	
	private int getRabitMQPriority(WebSocketEndPointAndMessageType type) {
		String pr=this.env.getProperty("RabitMQPriory");
		return Integer.parseInt(pr);
	}
	private boolean HaveToBeMessageRequired(WebSocketEndPointAndMessageType type) {
		String pr=this.env.getProperty("haveToBeMessageRequired");
		return Boolean.getBoolean(pr);

	}
	private Class<?> getDtoClass(WebSocketEndPointAndMessageType type) {
		String dtoName=this.env.getProperty("ResponseDTOClassName");
		try {
			return Class.forName(dtoName);
		} catch (ClassNotFoundException e) {
			Log4j2.log.error(Log4j2.MarkerLog.StartApp.getMarker(),
					String.format("Error during init value in WebSocketEndPointAndMessageType"+System.lineSeparator()
							+ "Cannot found DTO class"+System.lineSeparator()
							+ "enum name: %s"+System.lineSeparator()
							+ "Properties DTO name: %s", type.name(),dtoName));
			return null;
		}

	}
    @PostConstruct
	private void init() {
		if(Log4j2.log.isTraceEnabled()) {
			Log4j2.log.trace(Log4j2.MarkerLog.StartApp.getMarker(),"WebSocketEndPointAndMessageType: I am starting init and loaded WebSocketEndPointPath");
		}	
		for(WebSocketEndPointAndMessageType x:WebSocketEndPointAndMessageType.values()) {
			map.put(x.getPath(), x);
			x.setDtoClass(this.getDtoClass(x));
			x.setHaveToBeMessageRequired(this.HaveToBeMessageRequired(x));
			x.setRabitMQPriority(this.getRabitMQPriority(x));
			if(Log4j2.log.isTraceEnabled()) {
				Log4j2.log.trace(Log4j2.MarkerLog.StartApp.getMarker(),String.format("%s : %s", x.name(),x.getPath()));
			}
		}
		if(Log4j2.log.isDebugEnabled()) {
			Log4j2.log.debug(Log4j2.MarkerLog.StartApp.getMarker(),"WebSocketEndPointAndMessageType: map with endPoint value was init and loaded");
		}	

    }

     
	public static enum WebSocketEndPointAndMessageType {
		config_startConsuming(""),
		config_stopConsuming(""),
		acknowledgeEndPoint_confirmMessage(""),
		chat_sendMessage(""),
		chat_sawMessage(""),
		chat_changeMessage("");


		WebSocketEndPointAndMessageType(String value){
			this.path=value;
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
	}

	
	
}
