package chatAPP_database.Chat.Messages;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToOne;

import chatAPP_DTO.Message.MessageDTO;



@Entity()
public class MessageEntity {
	public static final String messageEntityTableName="messages";

	public static final String chatIDColumnName="id";
	public static final String senderIDColumnName="sender_id";
	public static final String messageIDColumnName="message_id";
	public static final String messageColumnName="message";
	public static final String receivedTimeColumnName="time_stamp";
	public static final String wasMessageRemovedColumnName="was_message_removed";
	public static final String orderColumnName="order_of_message";
	
	public static final String ColumnNamereferenctMessageID="referenct_to_message_id";
	public static final String ColumnNameextendsAction="exteds_action";
	public static final String JPQLorderName="order"; 
	
	@Column(name=orderColumnName)
	private long order;
	@Column(name=MessageEntity.chatIDColumnName)
	private String chatID;
	@Column(name=MessageEntity.senderIDColumnName)
	private long senderID;
	//unique by client
	@Column(name=MessageEntity.messageIDColumnName)
	private String messageID;
	@Column(name=MessageEntity.messageColumnName)
	private String message;
	@Column(name=MessageEntity.receivedTimeColumnName)
	private LocalDateTime receivedTime;
	@Column(name=MessageEntity.wasMessageRemovedColumnName)
	private boolean wasMessageRemoved=false;
	@Column(name=MessageEntity.ColumnNamereferenctMessageID)
    private String referenctMessageID;
	@Column(name=MessageEntity.ColumnNamereferenctMessageID)
    private MessageTypeOfAction TypeOfMessage;
	
	@javax.persistence.Version
	private long Version;
	
	/**Enum represent TypeOfMessage
	 * */
	//normal message, reply...
	public static enum MessageTypeOfAction{
		
	}
    
	public  MessageEntity(MessageDTO messageDTO) {
		this.order = messageDTO.getOrder();
		this.chatID = messageDTO.getChatID();
		this.senderID = messageDTO.getSenderID();
		this.messageID = messageDTO.getMessageID();
		this.message = messageDTO.getMessage();
		this.receivedTime = messageDTO.getReceivedTime();
		this.wasMessageRemoved = messageDTO.isWasMessageRemoved();
		this.referenctMessageID = messageDTO.getReferencMessageID();
		this.TypeOfMessage = (MessageTypeOfAction)messageDTO.getTypeOfAction();

	}
	
	


	public MessageDTO convertEntityToDTO() {
		MessageDTO mes=new MessageDTO();
		mes.setChatID(this.getChatID());
		mes.setMessage(this.getMessage());
		mes.setMessageID(this.getMessageID());
		mes.setOrder(this.getOrder());
		mes.setReceivedTime(this.getReceivedTime());
		mes.setReferencMessageID(this.getReferenctMessageID());
		mes.setSenderID(this.getSenderID());
		mes.setTypeOfAction(this.getTypeOfMessage());
		mes.setWasMessageRemoved(this.isWasMessageRemoved());
		mes.setVersion(this.Version);
		return mes;
	}
	
    
	public long getVersion() {
		return Version;
	}




	public void setVersion(long version) {
		Version = version;
	}




	public long getOrder() {
		return order;
	}

	public void setOrder(long order) {
		this.order = order;
	}

	public String getChatID() {
		return chatID;
	}

	public void setChatID(String chatID) {
		this.chatID = chatID;
	}

	public long getSenderID() {
		return senderID;
	}

	public void setSenderID(long senderID) {
		this.senderID = senderID;
	}

	public String getMessageID() {
		return messageID;
	}

	public void setMessageID(String messageID) {
		this.messageID = messageID;
	}

	public String getMessage() {
		
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LocalDateTime getReceivedTime() {
		return receivedTime;
	}

	public void setReceivedTime(LocalDateTime receivedTime) {
		this.receivedTime = receivedTime;
	}

	public boolean isWasMessageRemoved() {
		return wasMessageRemoved;
	}

	public void setWasMessageRemoved(boolean wasMessageRemoved) {
		this.wasMessageRemoved = wasMessageRemoved;
	}

	public String getReferenctMessageID() {
		return referenctMessageID;
	}

	public void setReferenctMessageID(String referenctMessageID) {
		this.referenctMessageID = referenctMessageID;
	}




	public MessageTypeOfAction getTypeOfMessage() {
		return TypeOfMessage;
	}




	public void setTypeOfMessage(MessageTypeOfAction typeOfMessage) {
		TypeOfMessage = typeOfMessage;
	}
	



	
}
