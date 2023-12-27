package ChatAPP_WebSocket.Session;

import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

public interface ThreadLocalSimpMessageHeaderAccessor {

	public long getSessionOwnerUserID();
	public SimpMessageHeaderAccessor getSimpMessageHeaderAccessor();
}
