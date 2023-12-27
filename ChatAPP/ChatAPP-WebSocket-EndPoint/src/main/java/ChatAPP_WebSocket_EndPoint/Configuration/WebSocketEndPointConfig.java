package ChatAPP_WebSocket_EndPoint.Configuration;

import org.springframework.messaging.handler.annotation.MessageMapping;

public class WebSocketEndPointConfig implements WebSocketEndPointConfigInterface {
	
	public static final String StartConsumingPath="";
	public static final String StopConsumingPath="";

	
	@MessageMapping(StartConsumingPath)
	@Override
	public void StartConsumingMessage() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void StopConsumingMessage() {
		// TODO Auto-generated method stub
		
	}

}
