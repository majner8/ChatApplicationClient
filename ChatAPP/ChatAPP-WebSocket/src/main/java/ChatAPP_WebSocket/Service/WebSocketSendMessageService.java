package ChatAPP_WebSocket.Service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ChatAPP_RabitMQ.ConsumingMessageManagement.RabitMQMessagePublisher;
import chatAPP_DTO.Message.ConsumedMessageDTO;

public class WebSocketSendMessageService implements RabitMQMessagePublisher {
	
	//private final Map<String,WebSocketSession> webSocketSession;
	@Autowired
	private SimpMessagingTemplate SimpMessageTemplate;
	@Override
	public void SendConsumedMessage(byte[] bodyMessage, String acknowledgeMessageID, String webSocketSessionID) {
		ConsumedMessageDTO message=new ConsumedMessageDTO();
		message.setBodyOfMessage(bodyMessage);
		this.SimpMessageTemplate.convertandSend
		
		TextMessage WebSocketMessage;
		try {
			this.SimpMessageTemplate.
			 WebSocketMessage = new TextMessage(this.jsonConvertor.writeValueAsString(message));
			 WebSocketSession session=this.webSocketSession.get(webSocketSessionID);
			 session.send
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block

		}
        
	}

	@Override
	public void MessageDeliveryTimeoutExpire(String WebSocketID, String messageID) {
		// TODO Auto-generated method stub
		
	}



}
