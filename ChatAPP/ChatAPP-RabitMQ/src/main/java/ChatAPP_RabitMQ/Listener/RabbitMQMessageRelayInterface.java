package ChatAPP_RabitMQ.Listener;

import chatAPP_CommontPart.ThreadLocal.ThreadLocalSimpMessageHeaderAccessor.rabitMQMessageType;

public interface RabbitMQMessageRelayInterface {

	public void SendConsumedMessage(String webSocketEndPointPath,String messageID,byte [] bodyMessage,rabitMQMessageType MessageType,String recipientID);
	
	public void MessageTimeoutExpired(String recipientID,String messageID);
}
