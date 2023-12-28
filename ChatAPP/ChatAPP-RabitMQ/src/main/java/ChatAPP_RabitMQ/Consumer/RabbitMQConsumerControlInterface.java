package ChatAPP_RabitMQ.Consumer;

public interface RabbitMQConsumerControlInterface {

	public void startConsume(String userdeviceID);
	public void stopConsume(String userdeviceID,boolean DoesDeviceDisconect);
}
