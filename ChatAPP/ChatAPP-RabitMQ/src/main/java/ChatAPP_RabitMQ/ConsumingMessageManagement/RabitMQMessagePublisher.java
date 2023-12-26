package ChatAPP_RabitMQ.ConsumingMessageManagement;


/**Class refers to WebSocketSessionService, which have to manage send MessageTo appropriate user */
public interface RabitMQMessagePublisher {


	/** 
	 * @bodyMessage*/
	public void SendConsumedMessage(byte []bodyMessage,String acknowledgeMessageID,String webSocketSessionID);
	/**Interface is call, when some of message is not acknowledge in time */
	public void MessageDeliveryTimeoutExpire(String WebSocketID,String messageID);
}
