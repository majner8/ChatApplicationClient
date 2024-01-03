package ChatAPP_WebSocket_EndPoint.EndPoint.ConfirmMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

import ChatAPP_WebSocket.Service.Chat.ConfirmSentMessageService;

public class WebSocketEndPointAcknowledgeEndPoint implements WebSocketEndPointAcknowledgeEndPointInterface {

	@Autowired
	private ConfirmSentMessageService service;
	@Override
	public void ConfirmMessage(SimpMessageHeaderAccessor session, String messageIDToAck) {
		this.service.ConfirmMessage(messageIDToAck);
	}

	

	
}
