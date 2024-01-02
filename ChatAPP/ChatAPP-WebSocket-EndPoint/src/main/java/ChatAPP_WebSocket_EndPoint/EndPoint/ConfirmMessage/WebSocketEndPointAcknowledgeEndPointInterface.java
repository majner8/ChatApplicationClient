package ChatAPP_WebSocket_EndPoint.EndPoint.ConfirmMessage;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

public interface WebSocketEndPointAcknowledgeEndPointInterface {

	public void ConfirmMessage(SimpMessageHeaderAccessor session,@DestinationVariable String messageIDToAck);
}
