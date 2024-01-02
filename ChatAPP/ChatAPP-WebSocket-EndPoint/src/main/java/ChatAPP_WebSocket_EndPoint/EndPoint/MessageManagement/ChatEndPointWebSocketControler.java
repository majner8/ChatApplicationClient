package ChatAPP_WebSocket_EndPoint.EndPoint.MessageManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.scheduling.annotation.Async;

import ChatAPP_Chat.Service.ChatMessageService;
import ChatAPP_WebSocket.Service.Chat.ProcessChatMessageService;
import chatAPP_DTO.Message.MessageDTO;
import chatAPP_DTO.Message.SawMessageDTO;
@Async
public class ChatEndPointWebSocketControler implements WebSocketChatEndPoint{

	public static final String SendMessagePath="";
	public static final String sawMessagePath="";
	public static final String changeMessagePath="";

	
	@Autowired
	private ProcessChatMessageService MessageService;
	@Override
	@MessageMapping()
	public void SendMessage(MessageDTO message,SimpMessageHeaderAccessor session) {
		this.MessageService.SendMessage(message);
	}


	@Override
	public void ChangeMessage(MessageDTO message,SimpMessageHeaderAccessor session) {
		
	}


	@Override
	public void sawMessage(SawMessageDTO message, SimpMessageHeaderAccessor session) {
		
	}

}
