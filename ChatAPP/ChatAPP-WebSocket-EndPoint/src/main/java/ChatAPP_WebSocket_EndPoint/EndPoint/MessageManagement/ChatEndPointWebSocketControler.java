package ChatAPP_WebSocket_EndPoint.EndPoint.MessageManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.scheduling.annotation.Async;

import ChatAPP_Chat.Service.ChatMessageService;
import chatAPP_DTO.Message.MessageDTO;
@Async
public class ChatEndPointWebSocketControler implements WebSocketChatEndPoint{

	public static final String SendMessagePath="";
	public static final String sawMessagePath="";
	public static final String changeMessagePath="";

	
	@Autowired
	private ChatMessageService MessageService;
	@Override
	@MessageMapping()
	public void SendMessage(MessageDTO message,SimpMessageHeaderAccessor session) {
	}


	@Override
	public void ChangeMessage(MessageDTO message,SimpMessageHeaderAccessor session) {
		
	}

}
