package ChatAPP_WebSocket;

import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.data.util.Pair;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;

import chatAPP_CommontPart.ThreadLocal.ThreadLocalSessionSimpMessageHeaderAccessor;
import chatAPP_CommontPart.ThreadLocal.ThreadLocalSimpMessageHeaderAccessor;

@Service
public class ThreadLocalSessionManagement implements ThreadLocalSessionSimpMessageHeaderAccessor,ThreadLocalSimpMessageHeaderAccessor  {

	private final String ContainerListenerHeaderName="AMQListener";
	private ThreadLocal<Pair<SimpMessageHeaderAccessor,Long>> session=new ThreadLocal<>();
	
	private SimpMessageHeaderAccessor getFirst() {
		return this.session.get().getFirst();
	}
	private long getSecond() {
		return this.session.get().getSecond();
	}
	@Override
	public void clear() {
		// TODO Auto-generated method stub
		this.session.remove();
	}

	@Override
	public void setSimpMessageHeaderAccessor(SimpMessageHeaderAccessor par) {
		// TODO Auto-generated method stub
		this.session.set(Pair.of(par,Long.parseLong(par.getUser().getName())));
	}

	public static class 
	
	
	@Override
	public boolean setSimpleMessageListenerContainer(SimpleMessageListenerContainer container) {
		SimpMessageHeaderAccessor ses=this.session.get().getFirst();
		synchronized(ses) {
			if(ses.getHeader(this.ContainerListenerHeaderName)!=null)return false;
			ses.setHeader(ContainerListenerHeaderName, ses);
		}
		return true;
	}
	
	@Override
	public long getSessionOwnerUserID() {
		return this.getSecond();
	}

	@Override
	public SimpMessageHeaderAccessor getSimpMessageHeaderAccessor() {
		return this.getFirst();
	}

	@Override
	public boolean IsUserConsumingNow() {
		synchronized(this.getFirst()) {
			if(this.getFirst().getHeader(this.ContainerListenerHeaderName)==null)return false;
			return true;						
		}
	}

	

	@Override
	public SimpleMessageListenerContainer getSimpleMessageListenerContainer() {
		// TODO Auto-generated method stub
		return (SimpleMessageListenerContainer)this.getFirst().getHeader(this.ContainerListenerHeaderName);
	}

	@Override
	public int getRabitMQSendPriority() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean haveToBeMessageRequired() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public String getProcessingWebSocketDestination() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getDTOClassName() {
		// TODO Auto-generated method stub
		return null;
	}



}
