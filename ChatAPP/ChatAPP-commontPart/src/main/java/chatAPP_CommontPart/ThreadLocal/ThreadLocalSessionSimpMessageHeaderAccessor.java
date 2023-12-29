package chatAPP_CommontPart.ThreadLocal;

import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

public interface ThreadLocalSessionSimpMessageHeaderAccessor {

	/**Metod clear current ThreadLocalValue */
	public void clear();
	public void setSimpMessageHeaderAccessor(SimpMessageHeaderAccessor par);
}
