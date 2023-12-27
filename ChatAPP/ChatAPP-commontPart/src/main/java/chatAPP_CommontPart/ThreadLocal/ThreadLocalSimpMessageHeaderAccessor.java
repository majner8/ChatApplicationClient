package chatAPP_CommontPart.ThreadLocal;

import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

public interface ThreadLocalSimpMessageHeaderAccessor {

	public long getSessionOwnerUserID();
	public SimpMessageHeaderAccessor getSimpMessageHeaderAccessor();
	/**Metod return unique ID for currect connected device
	 * ID is contain deviceID+UserID */
	public default String getConnectionID() 
	{return this.getSimpMessageHeaderAccessor().getUser().getName();}
	
}
