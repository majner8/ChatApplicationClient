package ChatAPP_RabitMQ.Queue;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import chatAPP_CommontPart.ThreadLocal.ThreadLocalSimpMessageHeaderAccessor;
@Component
public class RabbitMQQueueManager implements RabbitMQQueueManagerInterface {

	@Autowired
	private AmqpAdmin amqpAdmin;
	@Autowired
	private ThreadLocalSimpMessageHeaderAccessor simpMessage;
	@Autowired
	private TopicExchange topicExchange; //have to be created bean
	@Override
	public CustomRabitMQQueue getDeviceQueueName() {
		String queueName=this.simpMessage.getConnectionID();
		if(this.amqpAdmin.getQueueInfo(queueName)!=null) {
			return new CustomRabitMQQueue(queueName,false);
		}
		Queue q=this.createuserDeviceQueue(queueName);
		this.BindQueue(this.simpMessage.getSessionOwnerUserID(),q);
		return new CustomRabitMQQueue(queueName,true);
	}
	
	@Async
	private void BindQueue(long userID,Queue userDdeviceQueue) {
		  // Declare and bind the queue
	    Binding binding = BindingBuilder.bind(userDdeviceQueue).to(this.topicExchange).with(String.valueOf(userID));
	    this.amqpAdmin.declareBinding(binding);
	}
	private Queue createuserDeviceQueue(String queueName) {
		Queue que=new Queue(queueName,true,false,false);
		this.amqpAdmin.declareQueue(que);
		return que;
	}
	
	/**class, contain rabitMQ Queue name, and boolean value, if Queue was created, in this request */
	public final static class CustomRabitMQQueue {
		private final String queueName;
		private final boolean wasQueueCreated;
		
		public CustomRabitMQQueue(String queueName, boolean wasQueueCreated) {
			this.queueName = queueName;
			this.wasQueueCreated = wasQueueCreated;
		}
		public String getQueueName() {
			return queueName;
		}
		public boolean isWasQueueCreated() {
			return wasQueueCreated;
		}
		
	}
}
