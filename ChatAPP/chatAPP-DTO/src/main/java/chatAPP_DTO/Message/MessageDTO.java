package chatAPP_DTO.Message;

import java.time.LocalDateTime;



public class MessageDTO {

	private long order;
	private String chatID;
	private long senderID;
	private String messageID;
	private String message;
	private LocalDateTime receivedTime;
	private boolean wasMessageRemoved=false;
	private Enum typeOfAction;
	private String referencMessageID;
	private long version;
	

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public String getReferencMessageID() {
		return referencMessageID;
	}

	public void setReferencMessageID(String referencMessageID) {
		this.referencMessageID = referencMessageID;
	}

	public Enum getTypeOfAction() {
		return typeOfAction;
	}

	public void setTypeOfAction(Enum typeOfAction) {
		this.typeOfAction = typeOfAction;
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
	
	
	
}
