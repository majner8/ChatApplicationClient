package ChatAPP_WebSocket_EndPoint.EndPoint.ConfirmMessage;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

public class WebSocketEndPointAcknowledgeEndPoint implements WebSocketEndPointAcknowledgeEndPointInterface {

	@MessageMapping("")
	@Override
	public void ConfirmMessage(SimpMessageHeaderAccessor session,@DestinationVariable String path1) {
		
	}

	
}
