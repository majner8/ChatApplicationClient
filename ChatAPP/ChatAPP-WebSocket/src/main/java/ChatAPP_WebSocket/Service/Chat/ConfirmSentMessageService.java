package ChatAPP_WebSocket.Service.Chat;

import ChatAPP_RabitMQ.Listener.rabitMQListenerService;
import chatAPP_CommontPart.ThreadLocal.ThreadLocalSimpMessageHeaderAccessor;

public class ConfirmSentMessageService {

	private rabitMQListenerService rabitMQ;
	private ThreadLocalSimpMessageHeaderAccessor session;
	
	public void ConfirmMessage(String messageID) {
		this.rabitMQ.AckMessage(this.session.getConnectionID(), messageID);
	}
}
