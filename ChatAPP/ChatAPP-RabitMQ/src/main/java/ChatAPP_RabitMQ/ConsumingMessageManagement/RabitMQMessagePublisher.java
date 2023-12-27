package ChatAPP_RabitMQ.ConsumingMessageManagement;


/**Class refers to WebSocketSessionService, which have to manage send MessageTo appropriate user */
public interface RabitMQMessagePublisher {


	/** 
	 * @bodyMessage*/
	public void SendConsumedMessage(String WebSocketDestination,Object messageDTO,String acknowledgeMessageID,String webSocketSessionID);

}
