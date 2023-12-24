package ChatAPP_RabitMQ.PushingMessages;

import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import ChatAPP_RabitMQ.RabitMQMessageType;
import chatAPP_DTO.Message.MessageDTO;

public class PushMessageRabitMQService implements PushMessageRabitMQInterface {


	@Autowired
    private RabbitTemplate rabbitTemplate;

	
	@Override
	public void PushSentChatMessage(MessageDTO message, String EndPointWebSocketPath, long UserRecipientId) {

		   MessagePostProcessor messagePostProcessor = RBMQMessage -> {
			   MessageProperties messageProperties =new MessageProperties();
			   messageProperties.setMessageId(message.getMessageID());
			   messageProperties.setType(RabitMQMessageType.SendChatMessage.name());
			   messageProperties.setPriority(RabitMQMessageType.SendChatMessage.getPriority()); 
			   return RBMQMessage;
	        };
	        this.rabbitTemplate.convertAndSend(String.valueOf(UserRecipientId), message,messagePostProcessor);
	        
	}

}