package ChatAPP_WebSocket.Session;

import org.springframework.web.socket.WebSocketSession;

public interface WebSocketSessionManagementInterface {

	public void addSession(String sessionUserName,WebSocketSession session);
	public void removeSession(String sessionUserName,WebSocketSession session);

}
