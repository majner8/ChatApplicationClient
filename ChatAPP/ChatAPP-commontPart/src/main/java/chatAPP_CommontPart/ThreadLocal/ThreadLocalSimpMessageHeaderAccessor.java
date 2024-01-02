package chatAPP_CommontPart.ThreadLocal;

import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

public interface ThreadLocalSimpMessageHeaderAccessor {

	public long getSessionOwnerUserID();
	public SimpMessageHeaderAccessor getSimpMessageHeaderAccessor();
	
	//will be count from called path
	public int getRabitMQSendPriority();
	/**Metod return string name of rabitMQMessageType */
	public String getType();
	public String getCurrentProcessWebSocketDestination();
	/**Metod return unique ID for currect connected device
	 * ID is contain deviceID+UserID */
	public default String getConnectionID() 
	{return this.getSimpMessageHeaderAccessor().getUser().getName();}
	
	public boolean IsUserConsumingNow();
	
	
	
	/**Metod set to attribute of session
	 * @return false-if operation could not be sucesfull, because SimpleMessageListenerContainer instance already exist
	 * otherwise true */
	public boolean setSimpleMessageListenerContainer(SimpleMessageListenerContainer container);
	
	public SimpleMessageListenerContainer getSimpleMessageListenerContainer();

	public static enum rabitMQMessageType{
		
	}
}
