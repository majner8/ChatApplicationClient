package chatAPP_CommontPart.ThreadLocal;

import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

import chatAPP_CommontPart.EndPoint.WebSocketEndPointAndMessageType;

public interface ThreadLocalSessionSimpMessageHeaderAccessor {

	/**Metod clear current ThreadLocalValue */
	public void clear();
	public void setSimpMessageHeaderAccessor(SimpMessageHeaderAccessor par,WebSocketEndPointAndMessageType mesType);
	
}
