package ChatAPP_WebSocket.Session;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import ChatAPP_RabitMQ.Queue.RabbitMQQueueManager.CustomRabitMQQueue;
import ChatAPP_RabitMQ.Queue.RabbitMQQueueManagerInterface;
import chatAPP_CommontPart.Log4j2.Log4j2;
import chatAPP_CommontPart.ThreadLocal.ThreadLocalSimpMessageHeaderAccessor;

@Service
public class WebSocketStartConsumingRabitMQMessageService implements WebSocketStartConsumingServiceInterface{

	  @Autowired
	  private ConnectionFactory RabitMQconnectionFactory;
	@Autowired
	private RabbitMQQueueManagerInterface MqManagement;
		
	@Autowired
	private ThreadLocalSimpMessageHeaderAccessor simpMessage;
	@Override
	public boolean StartConsumingMessage() {		
		if(this.simpMessage.IsUserConsumingNow()) {
			this.makeWarmLog(true);
			throw new AccessDeniedException("WebSocket consuming");
			}		
		  SimpleMessageListenerContainer container=new SimpleMessageListenerContainer(this.RabitMQconnectionFactory);
		  if(!this.simpMessage.setSimpleMessageListenerContainer(container)) {
			  this.makeWarmLog(true);
			  throw new AccessDeniedException("WebSocket consuming");
			  }
		  CustomRabitMQQueue que=this.MqManagement.getDeviceQueueName();
		  container.addQueueNames(que.getQueueName());
		  container.start();
		  if(que.isWasQueueCreated()) {
			  this.makeAsyncQuickSynchronization();
			  return true;
		  }
		  
		return false;
	}
	@Async
	private void makeAsyncQuickSynchronization() {
		
	}
	@Override
	public boolean StopConsumingMessage() {

		if(!this.simpMessage.IsUserConsumingNow()) {
			this.makeWarmLog(false);
			return false;
			}
		SimpleMessageListenerContainer container=this.simpMessage.getSimpleMessageListenerContainer();
		container.stop();
		this.simpMessage.setSimpleMessageListenerContainer(null);
		return true;
	}
	private void makeWarmLog(boolean doesStartOperation) {
		if(doesStartOperation) {
			String message=String.format("start consuming operation could not be sucesfull"+System.lineSeparator()
			+"device is consuming now."+System.lineSeparator()
			+"userDeviceID: %s", this.simpMessage.getConnectionID());
			Log4j2.log.warn(Log4j2.MarkerLog.WebSocket.getMarker(),message);
		
		}
		else {
			String message=String.format("stop consuming operation could not be sucesfull"+System.lineSeparator()
			+"device is not consuming now."+System.lineSeparator()
			+"userDeviceID: %s", this.simpMessage.getConnectionID());
			Log4j2.log.warn(Log4j2.MarkerLog.WebSocket.getMarker(),message);
			
		}
	}
}
