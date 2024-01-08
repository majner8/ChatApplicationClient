package ChatAPP_WebSocket.Service.RabitMQService;

public interface RabitMqConsumingServiceInterface {

	public void StartConsuming(int offSetStart,int offSetEnd);
	
	public void StopConsuming();
}
