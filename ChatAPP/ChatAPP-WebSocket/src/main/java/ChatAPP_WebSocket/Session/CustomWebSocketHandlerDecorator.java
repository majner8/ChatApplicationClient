package ChatAPP_WebSocket.Session;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;


public class CustomWebSocketHandlerDecorator extends WebSocketHandlerDecorator {
	

	public CustomWebSocketHandlerDecorator(WebSocketHandler delegate) {
		super(delegate);
	}
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    	super.afterConnectionEstablished(session);
    }
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
    }
}
