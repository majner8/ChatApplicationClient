package ChatAPP_RabitMQ.Producer;

import java.util.List;

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
	
}
