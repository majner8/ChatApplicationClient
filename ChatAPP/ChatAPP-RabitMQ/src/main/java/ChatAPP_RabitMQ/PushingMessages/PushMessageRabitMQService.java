package ChatAPP_RabitMQ.PushingMessages;

import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import ChatAPP_RabitMQ.RabitMQMessageType;
import ChatAPP_RabitMQ.RabitMQProperties;
import chatAPP_DTO.Message.MessageDTO;

public class PushMessageRabitMQService implements PushMessageRabitMQInterface {


	@Autowired
    private RabbitTemplate rabbitTemplate;
	@Autowired
	private RabitMQProperties rabitMQProperties;
	
	@Override
	public void PushSentChatMessage(MessageDTO message, String EndPointWebSocketPath, long UserRecipientId) {

		   MessagePostProcessor messagePostProcessor = RBMQMessage -> {
			   MessageProperties messageProperties =new MessageProperties();
			   messageProperties.setMessageId(message.getMessageID());
			   messageProperties.setHeader(this.rabitMQProperties.getMessagePropertiesWebSocketEndPointName(), EndPointWebSocketPath);
			   messageProperties.setType(RabitMQMessageType.SendChatMessage.name());
			   messageProperties.setPriority(RabitMQMessageType.SendChatMessage.getPriority()); 
			   return RBMQMessage;
	        };
	        this.rabbitTemplate.convertAndSend(String.valueOf(UserRecipientId), message,messagePostProcessor);
	        
	}

}