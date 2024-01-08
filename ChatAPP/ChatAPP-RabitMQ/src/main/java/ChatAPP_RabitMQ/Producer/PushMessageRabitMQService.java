package ChatAPP_RabitMQ.Producer;

import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import ChatAPP_RabitMQ.RabitMQProperties;
import chatAPP_CommontPart.Properties.WebSocketProperties.WebSocketEndPointAndMessageType;
import chatAPP_CommontPart.ThreadLocal.ThreadLocalSimpMessageHeaderAccessor;
import chatAPP_DTO.Message.MessageDTO;

public class PushMessageRabitMQService implements RabitMQMessageProducerInterface {


	@Autowired
    private RabbitTemplate rabbitTemplate;
	@Autowired
	private RabitMQProperties rabitMQProperties;
	@Autowired
	private ThreadLocalSimpMessageHeaderAccessor threadLocalWebSocketSession;
	private void PushMessageToRabitMQ(String exchangeKey,MessageDTO mes,MessagePostProcessor messagePostProcessor ) {
	
        this.rabbitTemplate.convertAndSend(exchangeKey, mes, messagePostProcessor);

	}
	@Override
	public void PushSentChatMessage(MessageDTO message, long UserRecipientId) {
		this.PushMessageToRabitMQ(String.valueOf(UserRecipientId), message, this.getMessagePostProcessor(message, this.threadLocalWebSocketSession.getMessageType())); 
	}
	@Override
	public void PushMessageFromAsyncProcess(MessageDTO message, String queueName,
			WebSocketEndPointAndMessageType mesType) {
		this.PushMessageToRabitMQ(queueName, message, this.getMessagePostProcessor(message, mesType));
	}
	private MessagePostProcessor getMessagePostProcessor(MessageDTO message,WebSocketEndPointAndMessageType mesType) {
		 return RBMQMessage -> {
			   MessageProperties messageProperties =new MessageProperties();
			   messageProperties.setMessageId(message.getMessageID());
			   messageProperties.setType(mesType.name());
			   messageProperties.setPriority(mesType.getRabitMQPriority()); 
			   return RBMQMessage;
	        };
	}
}