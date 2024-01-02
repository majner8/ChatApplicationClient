package ChatAPP_WebSocket.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;

import ChatAPP_Security.Authorization.CustomSecurityContextHolder.CustomSecurityContextHolder;
import ChatAPP_WebSocket.WebSocketHeaderAttributeName;


public class CustomWebSocketHandlerDecoratorss extends WebSocketHandlerDecorator {
	
	public CustomWebSocketHandlerDecoratorss(WebSocketHandler delegate) {
		super(delegate);
		// TODO Auto-generated constructor stub
	}
	@Autowired
	private WebSocketHeaderAttributeName headerName;
	@Autowired
	private WebSocketSessionManagementInterface sessionManagement;
	public CustomWebSocketHandlerDecorator(WebSocketHandler delegate) {
		super(delegate);
	}
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    	String sessionIdentifier=CustomSecurityContextHolder.getCustomSecurityContext().getCustomUserDetails().getUsername();  	
    	session.getAttributes().put(this.headerName.getSessionIdentifierHeaderName(), sessionIdentifier);
    	this.sessionManagement.addSession(sessionIdentifier, session);
    	super.afterConnectionEstablished(session);
    }
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
    	String headerName=(String)session.getAttributes().get(this.headerName.getSessionIdentifierHeaderName());
    	this.sessionManagement.removeSession(headerName, session);
    	super.afterConnectionClosed(session, closeStatus);
    }
}
