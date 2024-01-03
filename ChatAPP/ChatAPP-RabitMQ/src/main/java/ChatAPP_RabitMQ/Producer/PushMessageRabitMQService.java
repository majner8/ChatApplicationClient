package ChatAPP_RabitMQ.Producer;

import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import ChatAPP_RabitMQ.RabitMQProperties;
import chatAPP_CommontPart.ThreadLocal.ThreadLocalSimpMessageHeaderAccessor;
import chatAPP_DTO.Message.MessageDTO;

public class PushMessageRabitMQService implements RabitMQMessageProducerInterface {


	@Autowired
    private RabbitTemplate rabbitTemplate;
	@Autowired
	private RabitMQProperties rabitMQProperties;
	@Autowired
	private ThreadLocalSimpMessageHeaderAccessor threadLocalWebSocketSession;
	@Override
	public void PushSentChatMessage(MessageDTO message, long UserRecipientId) {

		   MessagePostProcessor messagePostProcessor = RBMQMessage -> {
			   MessageProperties messageProperties =new MessageProperties();
			   messageProperties.setMessageId(message.getMessageID());
			   messageProperties.setHeader(this.rabitMQProperties.getMessagePropertiesWebSocketEndPointName(), this.threadLocalWebSocketSession.getProcessingWebSocketDestination());
			   messageProperties.setHeader(this.rabitMQProperties.getHaveToBeMessageRequiredHeaderName(), this.threadLocalWebSocketSession.haveToBeMessageRequired());
			   //as type, it is send type of appropriate DTO class
			   messageProperties.setType(this.threadLocalWebSocketSession.getDTOClassName());
			   messageProperties.setPriority(this.threadLocalWebSocketSession.getRabitMQSendPriority()); 
			   return RBMQMessage;
	        };

	        this.rabbitTemplate.convertAndSend(String.valueOf(UserRecipientId), message, messagePostProcessor);
	        
	}

}