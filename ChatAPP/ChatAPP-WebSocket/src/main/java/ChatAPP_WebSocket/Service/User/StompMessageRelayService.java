package ChatAPP_WebSocket.Service.User;

import java.io.IOException;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import ChatAPP_RabitMQ.Listener.RabbitMQMessageRelayInterface;
import ChatAPP_WebSocket.WebSocketHeaderAttributeName;
import chatAPP_CommontPart.Log4j2.Log4j2;

public class StompMessageRelayService implements RabbitMQMessageRelayInterface {

	@Autowired
	private SimpMessagingTemplate SimpMessageTemplate;
	@Autowired
	private WebSocketHeaderAttributeName attribute;
	@Autowired
	private ObjectMapper jsonMapper;
	
	@Override
	public void MessageTimeoutExpired(String recipientID, String messageID) {
		//have to be done 
	}
	@Override
	public void SendConsumedMessage(String webSocketEndPointPath, String messageID, byte[] bodyMessage,
			Class<?> DToClass, String recipientID) {
		HashMap<String,Object>header=new HashMap<String,Object>();
		Object dto;
	
		try {
			
			dto = this.jsonMapper.readValue(bodyMessage, DToClass);
			header.put(this.attribute.getAcknowledgeIdHeaderName(), messageID);
			this.SimpMessageTemplate.convertAndSendToUser(recipientID, webSocketEndPointPath, dto,header);
		
		} catch (IOException e) {
			Log4j2.log.error(Log4j2.MarkerLog.WebSocket.getMarker(),e
					+ ""+System.lineSeparator()
					+ "Cannot convert consumed rabitMQ message, to dto object."+System.lineSeparator()
					+ "Sending process failed"+System.lineSeparator()
					+ "connectedUserDeviceID: "+recipientID+System.lineSeparator()
					+ "MessageID: "+messageID+System.lineSeparator()
					+ "");
		}
		
	}
	
}
