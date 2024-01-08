package ChatAPP_RabitMQ.Listener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.rabbitmq.client.Channel;

import ChatAPP_RabitMQ.RabitMQProperties;
import ChatAPP_RabitMQ.Consumer.RabbitMQConsumerControlInterface;
import chatAPP_CommontPart.Log4j2.Log4j2;
import chatAPP_CommontPart.Properties.WebSocketProperties.WebSocketEndPointAndMessageType;
@Service
public class rabitMQListenerService implements ChannelAwareMessageListener,RabbitMQConsumerControlInterface{

	private final Map<String,unAcknowledgeMessages> ListOfConnectedDevice=Collections.synchronizedMap(
			new HashMap<String,unAcknowledgeMessages>());
	@Autowired
	private RabitMQProperties properties;
	@Autowired
	private RabbitMQMessageRelayInterface relay;
	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		MessageProperties properties=message.getMessageProperties();
		String messageID=properties.getMessageId();
		WebSocketEndPointAndMessageType type=WebSocketEndPointAndMessageType.valueOf(properties.getType());
		String webSocketEndPoint=type.getPath();
			//name is same as userdeviceID, userID+deviceID
		String recipientID=properties.getConsumerQueue();
		boolean haveToBeMessageRequired=type.isHaveToBeMessageRequired();
		unAcknowledgeMessages messages=this.ListOfConnectedDevice.get(recipientID);
		if(messages==null) {
			//Consuming was stopped, have to returned consumed Message
			new ChatAPP_RabitMQ.Listener.rabitMQListenerService.unAcknowledgeMessages.AcknowledgeMessage(messageID,properties.getDeliveryTag(),
					channel,System.currentTimeMillis(),haveToBeMessageRequired)
			.NackMessage();
			Log4j2.log.warn(Log4j2.MarkerLog.RabitMQ.getMarker(),
					String.format(
							"Cannot find unAcknowledgeMessages in Map. Messages was Nack and return to rabitMQ Queue"+System.lineSeparator()+
							"Consumer Queue ID: %s "+System.lineSeparator()+
							"returned MessageID: ", 
							properties.getConsumerQueue(),properties.getMessageId()));
			return;
		}
		messages
		.AddUnAcknodlegeMessage(messageID, channel, properties.getDeliveryTag(), haveToBeMessageRequired);
		Class<?>dtoClass=type.getDtoClass();
		this.relay.SendConsumedMessage(webSocketEndPoint,messageID,message.getBody(), dtoClass, recipientID);
	}
	
	/**Metod verify, if time expiration of unAcknowledgeMessage
	 * Metod work async*/
	@Async
	private void MessageTime(unAcknowledgeMessages toChech) {
		toChech.ExpirationCheck();
	}
	
	public void AckMessage(String SessionID,String messageID) {
		unAcknowledgeMessages mes=this.ListOfConnectedDevice.get(SessionID);
		if(mes==null) {
			Log4j2.log.warn(Log4j2.MarkerLog.RabitMQ.getMarker(),
					String.format(
							"Cannot find unAcknowledgeMessages instance"+System.lineSeparator()+
							"userDeviceID: %s"+System.lineSeparator()+
							"messageID: %s", 
							SessionID,messageID));
			return;
		}
		mes.PositiveAcknowledgement(messageID);
	}
	public void NackMessage(String SessionID,String messageID) {
		unAcknowledgeMessages mes=this.ListOfConnectedDevice.get(SessionID);
		if(mes==null) {
			Log4j2.log.warn(Log4j2.MarkerLog.RabitMQ.getMarker(),
					String.format(
							"Cannot find unAcknowledgeMessages instance"+System.lineSeparator()+
							"userDeviceID: %s"+System.lineSeparator()+
							"messageID: %s", 
							SessionID,messageID));
			return;
		}
		mes.NegativeAcknowledgement(messageID);
	}
	
	private final class unAcknowledgeMessages {
		private final String sessionID;
		private final Map<String,AcknowledgeMessage>listOfMessage=
				Collections.synchronizedMap(new HashMap<>());
		
		protected unAcknowledgeMessages(String sessionID) {
			this.sessionID = sessionID;
		}

		private void AddUnAcknodlegeMessage(String messageID,Channel channel,long deliveryTag,boolean haveToBeMessageRequired) {
			AcknowledgeMessage message=new AcknowledgeMessage(
					messageID,
					deliveryTag,
					channel,
					System.currentTimeMillis(),
					haveToBeMessageRequired);
			this.listOfMessage.put(messageID, message);
		}
		
		private void PositiveAcknowledgement(String messageID)  {
			AcknowledgeMessage mes=this.listOfMessage.get(messageID);
			if(mes==null) {
				return;
			}
				try {
					mes.AckMessage();
				} catch (IOException e) {
					Log4j2.log.error(Log4j2.MarkerLog.RabitMQ.getMarker(),
							String.format(e+System.lineSeparator(),
									"Cannot make PositiveAcknowledgement of message"+System.lineSeparator()+
									"messageID: %s"+System.lineSeparator(), 
									messageID));
				}
				finally {
					this.listOfMessage.remove(messageID);
				}
		}
		private void NegativeAcknowledgement(String messageID)  {
			AcknowledgeMessage mes=this.listOfMessage.get(messageID);
			if(mes==null) {
				return;
			}
				try {
					mes.NackMessage();
				} catch (IOException e) {
					Log4j2.log.error(Log4j2.MarkerLog.RabitMQ.getMarker(),
							String.format(e+System.lineSeparator(),
									"Cannot make NegativeAcknowledgement of message"+System.lineSeparator()+
									"messageID: %s"+System.lineSeparator(), 
									messageID));
				}
				finally {
					this.listOfMessage.remove(messageID);
				}
		}
		
		private void NegativeAcknowledgementAllMessages() {
			synchronized(this.listOfMessage) {
				this.listOfMessage.forEach((K,V)->{
					try {
						V.NackMessage();
					} catch (IOException e) {
						Log4j2.log.error(Log4j2.MarkerLog.RabitMQ.getMarker(),
								String.format(e+System.lineSeparator(),
										"Cannot make negativeAcknowledgement of message"+System.lineSeparator()+
										"messageID: %s"+System.lineSeparator(), 
										K));	
					}
				});
			}
		}
		
		private void ExpirationCheck() {
			List<Pair<String,AcknowledgeMessage>> expirationMessages=new ArrayList<>();
			synchronized(this.listOfMessage) {
				long currentTime=System.currentTimeMillis();
				this.listOfMessage.forEach((K,V)->{
					
					if(V.doesMessageExpired(currentTime,properties)) {
						//message expired have to be remove
						expirationMessages.add(	Pair.of(K, V));
						Log4j2.log.trace(Log4j2.MarkerLog.RabitMQ.getMarker(),
								
								"UnAcknowledgeMessage expired, will be send back toRabitMQ "+System.lineSeparator()
								+"MessageID: "+K);
					}
				});
			}
			
			expirationMessages.forEach((V)->{
				try {
					V.getSecond().NackMessage();
					this.listOfMessage.remove(V.getFirst());
				} catch (IOException e) {
					
				}
			});
			
		}
		private static final class AcknowledgeMessage{
			private final String messageID;
			private long deliveryTag;
			private Channel rabitChannel;
			private long timeStamp;
			private boolean shouldBeMessageRequiredAfterExpiration;
			private boolean wasMessageAlreadyAcknowledged=false;
			
			protected AcknowledgeMessage(String messageID,long deliveryTag, Channel rabitChannel, long timeStamp,
					boolean shouldBeMessageRequiredAfterExpiration) {
				this.messageID=messageID;
				this.deliveryTag = deliveryTag;
				this.rabitChannel = rabitChannel;
				this.timeStamp = timeStamp;
				this.shouldBeMessageRequiredAfterExpiration = shouldBeMessageRequiredAfterExpiration;
			}
			/**@return true-if Ack operation was sucessful
			 * @return false, if Ack operation was not be sucesfull-acknowledgement has been done before */
			private boolean AckMessage() throws IOException {
				synchronized(this) {
					if(wasMessageAlreadyAcknowledged) {
						Log4j2.log.warn(Log4j2.MarkerLog.RabitMQ.getMarker(),
								String.format("MessageID: %s could not be Ack, because it was acknowledge before"
								, this.messageID));
						return false;
					}
					this.rabitChannel.basicAck(deliveryTag, false);
					Log4j2.log.trace(Log4j2.MarkerLog.RabitMQ.getMarker(),
							String.format("MessageID: %s was Ack"
							, this.messageID));
					wasMessageAlreadyAcknowledged=true;
				}
				return true;
			}

			/**@return true-if Nack operation was sucessful
			 * @return false, if Nack operation was not be sucesfull-acknowledgement has been done before */
			private boolean NackMessage() throws IOException {
				synchronized(this) {
					if(wasMessageAlreadyAcknowledged) {
						Log4j2.log.warn(Log4j2.MarkerLog.RabitMQ.getMarker(),
								String.format("MessageID: %s could not be Nack, because it was acknowledge before"
								, this.messageID));
						return false;
					}
					this.rabitChannel.basicNack(deliveryTag, false, this.shouldBeMessageRequiredAfterExpiration);
					wasMessageAlreadyAcknowledged=true;
					Log4j2.log.trace(Log4j2.MarkerLog.RabitMQ.getMarker(),
							String.format("MessageID: %s was Nack"+System.lineSeparator()
							+"Was message required: %s"
							, this.messageID,this.shouldBeMessageRequiredAfterExpiration));
				}
				return true;
			}			
	
			private boolean doesMessageExpired(long currentTime,RabitMQProperties properties) {
			
				return ((this.timeStamp-currentTime)>properties.getUnacknowledgedMessageTimeout());
			}
		}
	}


	@Override
	public void startConsume(String userdeviceID) {
		unAcknowledgeMessages mes=new unAcknowledgeMessages(userdeviceID);
		this.ListOfConnectedDevice.put(userdeviceID, mes);
	}


	@Override
	public void stopConsume(String userdeviceID,boolean DoesDeviceDisconect) {
		if(!DoesDeviceDisconect) {
			return;
		}
		unAcknowledgeMessages mes;
		synchronized(this.ListOfConnectedDevice) {
			mes=this.ListOfConnectedDevice.get(userdeviceID);
			this.ListOfConnectedDevice.remove(userdeviceID);
		}
		mes.NegativeAcknowledgementAllMessages();
	}
	
	@Scheduled()
	public void Timer() {
		Log4j2.log.debug(Log4j2.MarkerLog.RabitMQ.getMarker(),
				String.format("Start expiration periodical check"));
		
		synchronized(this.ListOfConnectedDevice) {
			this.ListOfConnectedDevice.forEach((K,V)->{
				this.MessageTime(V);
			});
		}
	}
}
