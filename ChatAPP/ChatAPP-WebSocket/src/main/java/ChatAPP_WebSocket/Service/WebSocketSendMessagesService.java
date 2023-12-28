package ChatAPP_WebSocket.Service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import ChatAPP_RabitMQ.ConsumingMessageManagement.RabitMQMessagePublisher;
import ChatAPP_WebSocket.WebSocketHeaderAttributeName;

public class WebSocketSendMessagesService implements RabitMQMessagePublisher {
	
	//private final Map<String,WebSocketSession> webSocketSession;
	@Autowired
	private SimpMessagingTemplate SimpMessageTemplate;
	@Autowired
	private WebSocketHeaderAttributeName attribute;
	@Override
	public void SendConsumedMessage(String webSocketEndPoint,Object dtoMessage, String acknowledgeMessageID, String userID) {
		HashMap<String,Object>header=new HashMap<String,Object>();
		header.put(this.attribute.getAcknowledgeIdHeaderName(), acknowledgeMessageID);
		this.SimpMessageTemplate.convertAndSendToUser(userID, webSocketEndPoint, dtoMessage, header);        
	}

	



}
