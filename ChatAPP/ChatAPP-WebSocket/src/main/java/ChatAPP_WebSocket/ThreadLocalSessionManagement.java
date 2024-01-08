package ChatAPP_WebSocket;

import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;

import chatAPP_CommontPart.Data.Util.Triple;
import chatAPP_CommontPart.Properties.WebSocketProperties.WebSocketEndPointAndMessageType;
import chatAPP_CommontPart.ThreadLocal.ThreadLocalSessionSimpMessageHeaderAccessor;
import chatAPP_CommontPart.ThreadLocal.ThreadLocalSimpMessageHeaderAccessor;

@Service
public class ThreadLocalSessionManagement implements ThreadLocalSessionSimpMessageHeaderAccessor,ThreadLocalSimpMessageHeaderAccessor  {

	private final String ContainerListenerHeaderName="AMQListener";
	private ThreadLocal<Triple<SimpMessageHeaderAccessor,WebSocketEndPointAndMessageType,Long>> session=new ThreadLocal<>();
	
	private SimpMessageHeaderAccessor getFirst() {
		return this.session.get().getFirst();
	}
	private WebSocketEndPointAndMessageType  getSecond() {
		return this.session.get().getSecond();
	}
	private long getUserID() {
		return this.session.get().getThird();
	}
	@Override
	public void clear() {
		// TODO Auto-generated method stub
		this.session.remove();
	}
	
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
	public void setSimpMessageHeaderAccessor(SimpMessageHeaderAccessor par, WebSocketEndPointAndMessageType mesType) {
		this.session.set(Triple.of(par, mesType, Long.valueOf(par.getUser().getName())));
	}
	@Override
	public long getSessionOwnerUserID() {
		return this.getUserID();
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
	public String getProcessingWebSocketDestination() {
		return this.getSecond().getPath();
	}
	@Override
	public WebSocketEndPointAndMessageType getMessageType() {
		// TODO Auto-generated method stub
		return this.getSecond();
	}
	
	



}
