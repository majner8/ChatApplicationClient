package ChatAPP_WebSocket;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;

@Service
public class SynchronizedThreadLocalWebSocketSession  {

	private ThreadLocal<ConcurrentWebSocketSessionDecorator> session=new ThreadLocal<ConcurrentWebSocketSessionDecorator>();
	
	public void setConcurrentWebSocketSession(WebSocketSession session) {
		this.session.remove();
		Map<String, Object> attributes=session.getAttributes();
		Object synSession;
		synchronized(attributes) {
			synSession=attributes.get(CustomHandshake.ConcurrentWebSocketSessionDecoratorClaimName);
		}
		if(synSession==null||!(synSession instanceof ConcurrentWebSocketSessionDecorator)) {
			
			return;
		}
		this.session.set((ConcurrentWebSocketSessionDecorator)synSession);
		
	}

	public WebSocketSession getConCurrentSession() {
		return this.session.get();
	}
}
