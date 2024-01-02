package ChatAPP_WebSocket;

import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;

import chatAPP_CommontPart.ThreadLocal.ThreadLocalSessionSimpMessageHeaderAccessor;
import chatAPP_CommontPart.ThreadLocal.ThreadLocalSimpMessageHeaderAccessor;

@Service
public class ThreadLocalSessionManagement implements ThreadLocalSessionSimpMessageHeaderAccessor,ThreadLocalSimpMessageHeaderAccessor  {

	private ThreadLocal<SimpMessageHeaderAccessor> session=new ThreadLocal<>();
	@Override
	public void clear() {
		// TODO Auto-generated method stub
		this.session.remove();
	}

	@Override
	public void setSimpMessageHeaderAccessor(SimpMessageHeaderAccessor par) {
		// TODO Auto-generated method stub
		this.session.set(par);
	}

	@Override
	public long getSessionOwnerUserID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public SimpMessageHeaderAccessor getSimpMessageHeaderAccessor() {
		// TODO Auto-generated method stub
		return this.session.get();
	}

	@Override
	public boolean IsUserConsumingNow() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean setSimpleMessageListenerContainer(SimpleMessageListenerContainer container) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public SimpleMessageListenerContainer getSimpleMessageListenerContainer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getRabitMQSendPriority() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCurrentProcessWebSocketDestination() {
		// TODO Auto-generated method stub
		return null;
	}

}
