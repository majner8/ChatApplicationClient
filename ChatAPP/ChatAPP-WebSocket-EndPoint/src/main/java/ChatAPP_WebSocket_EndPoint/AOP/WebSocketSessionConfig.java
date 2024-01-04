package ChatAPP_WebSocket_EndPoint.AOP;

import chatAPP_CommontPart.ThreadLocal.ThreadLocalSessionSimpMessageHeaderAccessor.WebSocketSessionThreadLocalMessageAttribute;

public interface WebSocketSessionConfig {

	public WebSocketSessionThreadLocalMessageAttribute
	getWebSocketSessionThreadLocalMessageAttribute(String[]calledEndPoint);
}
