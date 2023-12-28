package ChatAPP_WebSocket.Service.RabitMQService;

import org.springframework.beans.factory.annotation.Autowired;

import chatAPP_CommontPart.ThreadLocal.ThreadLocalSimpMessageHeaderAccessor;


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
