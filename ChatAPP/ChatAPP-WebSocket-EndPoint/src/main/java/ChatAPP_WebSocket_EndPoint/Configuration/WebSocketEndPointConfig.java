package ChatAPP_WebSocket_EndPoint.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;

import ChatAPP_WebSocket.Service.RabitMQService.RabitMQConsumingEndPointService;
import ChatAPP_WebSocket_EndPoint.WebSocketEndPointPath;

public class WebSocketEndPointConfig implements WebSocketEndPointConfigInterface {
	
	public static final String StartConsumingPath=WebSocketEndPointPath.Config_StartConsumingPath;
	public static final String StopConsumingPath=WebSocketEndPointPath.Config_StopConsumingPath;

	@Autowired
	private RabitMQConsumingEndPointService service;
	@MessageMapping(WebSocketEndPointPath.Config_StartConsumingPath)
	@Override
	public void StartConsumingMessage(@DestinationVariable int offSetStart,@DestinationVariable int offSetEnd)  {
		this.service.StartConsuming(offSetStart,offSetEnd);
	}

	@Override
	public void StopConsumingMessage() {
		// TODO Auto-generated method stub
		
	}

	

}
