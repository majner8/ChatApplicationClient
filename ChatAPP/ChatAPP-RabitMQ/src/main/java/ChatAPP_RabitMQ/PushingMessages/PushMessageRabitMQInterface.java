package ChatAPP_RabitMQ.PushingMessages;

import java.util.List;

import chatAPP_DTO.Message.MessageDTO;

public interface PushMessageRabitMQInterface {


	public void PushSentChatMessage(MessageDTO message,String EndPointWebSocketPath,long UserRecipientId);

	public default void PushSentChatMessages(MessageDTO message,String EndPointWebSocketPath,List<Long> UserRecipientIds) {
		synchronized (UserRecipientIds) {
			UserRecipientIds.forEach((id)->{
				this.PushSentChatMessage(message, EndPointWebSocketPath, id);
			});
		}
		
	}
}
