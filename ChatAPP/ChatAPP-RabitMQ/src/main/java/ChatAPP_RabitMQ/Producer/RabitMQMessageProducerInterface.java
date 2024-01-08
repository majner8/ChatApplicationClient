package ChatAPP_RabitMQ.Producer;

import java.util.List;

import chatAPP_CommontPart.Properties.WebSocketProperties.WebSocketEndPointAndMessageType;
import chatAPP_DTO.Message.MessageDTO;

public interface RabitMQMessageProducerInterface {


	public void PushSentChatMessage(MessageDTO message,long UserRecipientId);

	public default void PushSentChatMessages(MessageDTO message,List<Long> UserRecipientIds) {
		synchronized (UserRecipientIds) {
			UserRecipientIds.forEach((id)->{
				this.PushSentChatMessage(message, id);
			});
		}
		
	}
	public void PushMessageFromAsyncProcess(MessageDTO message,String queueName,WebSocketEndPointAndMessageType mesType);
	public default void PushMessageFromAsyncProcess(List<MessageDTO> messages,String queueName,WebSocketEndPointAndMessageType mesType) {
		synchronized(messages) {
			messages.forEach((m)->{
				this.PushMessageFromAsyncProcess(m, queueName, mesType);
			});
		}
	}
	
}
