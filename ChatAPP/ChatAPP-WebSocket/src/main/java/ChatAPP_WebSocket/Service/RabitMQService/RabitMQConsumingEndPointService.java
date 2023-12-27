package ChatAPP_WebSocket.Service.RabitMQService;

import org.springframework.beans.factory.annotation.Autowired;

import ChatAPP_WebSocket.Session.ThreadLocalSimpMessageHeaderAccessor;

public class RabitMQConsumingEndPointService implements RabitMqConsumingServiceInterface{

	@Autowired
	private ThreadLocalSimpMessageHeaderAccessor simpSession;
	@Override
	public void StartConsuming() {

		String connectionID=this.simpSession.getConnectionID();
		
	}

	@Override
	public void StopConsuming() {
		
	}

}
