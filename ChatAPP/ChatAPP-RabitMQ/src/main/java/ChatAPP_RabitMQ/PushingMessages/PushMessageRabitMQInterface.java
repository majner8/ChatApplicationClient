package ChatAPP_RabitMQ.PushingMessages;

import java.util.List;

import chatAPP_DTO.Message.MessageDTO;

public interface PushMessageRabitMQInterface {


	public void PushMessage(MessageDTO message,String EndPointWebSocketPath,long recipientId);

	public default void PushMessage(MessageDTO message,String EndPointWebSocketPath,List<Long> recipientsId) {
		synchronized (recipientsId) {
			recipientsId.forEach((id)->{
				this.PushMessage(message, EndPointWebSocketPath, id);
			});
		}
		
	}
}
