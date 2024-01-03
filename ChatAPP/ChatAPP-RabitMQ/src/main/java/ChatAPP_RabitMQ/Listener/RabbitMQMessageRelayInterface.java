package ChatAPP_RabitMQ.Listener;


public interface RabbitMQMessageRelayInterface {

	public void SendConsumedMessage(String webSocketEndPointPath,String messageID,byte [] bodyMessage,Class<?> DToClassName,String recipientID);
	
	public void MessageTimeoutExpired(String recipientID,String messageID);
}
