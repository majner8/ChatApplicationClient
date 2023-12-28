package ChatAPP_RabitMQ.Listener;

import ChatAPP_RabitMQ.RabitMQMessageType;

public interface RabbitMQMessageRelayInterface {

	public void SendConsumedMessage(String webSocketEndPointPath,String messageID,byte [] bodyMessage,RabitMQMessageType MessageType,String recipientID);
	
	public void MessageTimeoutExpired(String recipientID,String messageID);
}
