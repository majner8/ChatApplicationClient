package ChatAPP_RabitMQ.Bean;

import org.springframework.beans.factory.ObjectFactory;

import ChatAPP_RabitMQ.ConsumingMessageManagement.rabitMQConsumer;

public interface CustomRabitMQConsumerObjectFactory extends ObjectFactory<rabitMQConsumer>{

	public default rabitMQConsumer getRabitMQConsumer(String param) {
		rabitMQConsumer x=this.getObject();
		x.setWebSocketID(param);
		return x;
		
	}
}
