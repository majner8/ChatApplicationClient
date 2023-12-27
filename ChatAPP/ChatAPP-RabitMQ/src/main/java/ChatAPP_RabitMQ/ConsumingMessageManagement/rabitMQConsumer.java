package ChatAPP_RabitMQ.ConsumingMessageManagement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;

import ChatAPP_RabitMQ.RabitMQMessageType;
import ChatAPP_RabitMQ.RabitMQProperties;
import chatAPP_CommontPart.Data.Util.Fourth;
import chatAPP_CommontPart.Data.Util.GarbageCollectorChech;
import chatAPP_CommontPart.Log4j2.Log4j2;

public class rabitMQConsumer implements ChannelAwareMessageListener,GarbageCollectorChech{
	
	@Autowired
	private RabitMQProperties properties;
	@Autowired
	private RabitMQMessagePublisher wsSessionPublisher;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private RabitMQMessageWasNotAcknowledge MessageWasNotAcknowledge;
	
	//deliveryTag, Channel of Message,TimeStamp
	private LinkedHashMap<String,Fourth<Long,Channel,Long,RabitMQMessageType>> unAcknowledgeMessage;
	
	private final String WebSocketID=null;
	
	@Async
	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		String messageID=message.getMessageProperties().getMessageId();
		String MessageTypeName=message.getMessageProperties().getHeader(this.properties.getMessagePropertiesWebSocketEndPointName());
		RabitMQMessageType messageType=RabitMQMessageType.valueOf(MessageTypeName);
		Fourth<Long,Channel,Long,RabitMQMessageType> properties=new Fourth<Long,Channel,Long,RabitMQMessageType>(message.getMessageProperties().getDeliveryTag(),channel,System.currentTimeMillis(),messageType);
		this.unAcknowledgeMessage.put(messageID,properties);
		
		Object dto=this.convertRabitMQBodyToDTO(message.getBody(), messageType);
		String WebSocketEndPoint=message.getMessageProperties().getHeader(this.properties.getMessagePropertiesWebSocketEndPointName());
		this.wsSessionPublisher.SendConsumedMessage(WebSocketEndPoint,dto, messageID, this.WebSocketID);

		
	}
	private Object convertRabitMQBodyToDTO(byte[] body,RabitMQMessageType typeOfMessage) throws JsonParseException, JsonMappingException, IOException {
		return this.objectMapper.readValue(body, typeOfMessage.getDtoClass());
	}

	@Async
	@Override
	public void PeriodicChech() {
		ArrayList<String>keyToRemove=new ArrayList<String>();
		long time=System.currentTimeMillis();
		synchronized(this.unAcknowledgeMessage) {
			this.unAcknowledgeMessage.forEach((K,V)->{
				if((time-V.getThird())<=this.properties.getUnacknowledgedMessageTimeout()) {
					//message is available
					//because of LinkedHAshMap, other message is avaiable to
					return;
				}				
				keyToRemove.add(K);
				this.SendNegativeAcknowledgement(V.getSecond(),V.getFirst(), V.getFourth().isShouldBeMessageRequired());
			});
			keyToRemove.forEach((K)->{
				this.unAcknowledgeMessage.remove(K);
				this.MessageWasNotAcknowledge.MessageDeliveryTimeoutExpire(this.WebSocketID, K);
			});
		}
	}
	@Async
	private void SendNegativeAcknowledgement(Channel channel,long develiryTag,boolean shouldBeRequired) {
		try {
			channel.basicNack(develiryTag, false, shouldBeRequired);
		} catch (IOException e) {
			Log4j2.log.error(Log4j2.MarkerLog.RabitMQ.getMarker(),"Problem with SendNegativeAkcnowledgement, deliveryTag "+ String.valueOf(develiryTag)+"   Exception:"+e);
		}
	}
	
}
