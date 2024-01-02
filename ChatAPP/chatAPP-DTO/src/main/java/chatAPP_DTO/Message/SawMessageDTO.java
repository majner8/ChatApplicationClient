package chatAPP_DTO.Message;

import java.time.LocalDateTime;

public class SawMessageDTO {

	private long userID;
	private String chatID;
	private String messageID;
	private LocalDateTime timeStamp;
	public long getUserID() {
		return userID;
	}
	public String getChatID() {
		return chatID;
	}
	public String getMessageID() {
		return messageID;
	}
	public LocalDateTime getTimeStamp() {
		return timeStamp;
	}
	public void setUserID(long userID) {
		this.userID = userID;
	}
	public void setChatID(String chatID) {
		this.chatID = chatID;
	}
	public void setMessageID(String messageID) {
		this.messageID = messageID;
	}
	public void setTimeStamp(LocalDateTime timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	
}
