package ChatAPP_WebSocket_EndPoint.EndPoint.MessageManagement;

import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

import chatAPP_DTO.Message.MessageDTO;
import chatAPP_DTO.Message.SawMessageDTO;

public interface WebSocketChatEndPoint{
	

	public void SendMessage(MessageDTO message,SimpMessageHeaderAccessor session);
	
	public void ChangeMessage(MessageDTO message,SimpMessageHeaderAccessor session);

	public void sawMessage(SawMessageDTO message,SimpMessageHeaderAccessor session);
}
