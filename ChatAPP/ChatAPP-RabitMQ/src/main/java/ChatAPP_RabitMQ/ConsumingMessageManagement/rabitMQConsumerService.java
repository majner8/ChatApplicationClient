package ChatAPP_RabitMQ.ConsumingMessageManagement;

import java.util.HashMap;

import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class rabitMQConsumerService {
	
	@Autowired
	private RabitMQMessagePublisher WebSocketSessionService;
			//key-random generated WebSocket session ID
	private HashMap<String,Pair<SimpleMessageListenerContainer,rabitMQConsumer>> rabitMQListenerContainer;
	
	
	
	/**Class refers to WebSocketSessionService, which have to manage send MessageTo appropriate user */
	
}
