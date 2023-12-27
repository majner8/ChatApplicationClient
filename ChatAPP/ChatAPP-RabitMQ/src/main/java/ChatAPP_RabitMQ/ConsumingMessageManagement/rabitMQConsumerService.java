package ChatAPP_RabitMQ.ConsumingMessageManagement;

import java.util.HashMap;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import chatAPP_CommontPart.ThreadLocal.ThreadLocalSimpMessageHeaderAccessor;

@Service
public class rabitMQConsumerService {
	
    @Autowired
    private ObjectFactory<rabitMQConsumer> rabitMQConsumerBeanProvider;

	@Autowired
	private RabitMQMessagePublisher WebSocketSessionService;
			//key-random Principal name-deviceID+userID
	private HashMap<String,Pair<SimpleMessageListenerContainer,rabitMQConsumer>> rabitMQListenerContainer;
	private ThreadLocalSimpMessageHeaderAccessor currectSession;
	
	public void StartConsuming() {
		String principalID=this.currectSession.getConnectionID();
		
		//create rabitMQConsumer
	}
	
	/**Class refers to WebSocketSessionService, which have to manage send MessageTo appropriate user */
	
}
