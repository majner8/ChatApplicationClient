package ChatAPP_RabitMQ.ConsumingMessageManagement;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;

import com.rabbitmq.client.Channel;

public class rabitMQConsumer implements ChannelAwareMessageListener{

	
	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		
	}

}
