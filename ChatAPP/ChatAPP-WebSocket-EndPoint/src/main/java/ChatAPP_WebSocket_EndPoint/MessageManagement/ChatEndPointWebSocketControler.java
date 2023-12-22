package ChatAPP_WebSocket_EndPoint.MessageManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.socket.WebSocketSession;

import ChatAPP_Chat.Service.ChatMessageService;
import chatAPP_DTO.Message.MessageDTO;
@Async
public class ChatEndPointWebSocketControler implements WebSocketChatEndPoint{

	@Autowired
	private ChatMessageService MessageService;
	@Override
	public void SendMessage(MessageDTO message,WebSocketSession session) {

	}


	@Override
	public void ChangeMessage(MessageDTO message,WebSocketSession session) {
		
	}

}
