package ChatAPP_RabitMQ.ConsumingMessageManagement;

public interface RabitMQListener {

	public void SendConsumedMessage(Object dtoMessage,String acknowledgeMessageID);
}
