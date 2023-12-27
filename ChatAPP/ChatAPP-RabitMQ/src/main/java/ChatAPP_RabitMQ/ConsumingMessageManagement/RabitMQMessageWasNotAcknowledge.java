package ChatAPP_RabitMQ.ConsumingMessageManagement;

public interface RabitMQMessageWasNotAcknowledge {
	public void MessageDeliveryTimeoutExpire(String WebSocketID,String messageID);

}
