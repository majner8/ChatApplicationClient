package chatAPP_DTO.Message;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


public  class ChatInformationDTO {

	
	private long createdByUserID;
	private String defaultChatName;
	private String ChatID;
	
	private List<UserChatInformationDTO> user;
	
	

		public long getCreatedByUserID() {
		return createdByUserID;
	}



	public void setCreatedByUserID(long createdByUserID) {
		this.createdByUserID = createdByUserID;
	}



	public String getDefaultChatName() {
		return defaultChatName;
	}



	public void setDefaultChatName(String defaultChatName) {
		this.defaultChatName = defaultChatName;
	}



	public String getChatID() {
		return ChatID;
	}



	public void setChatID(String chatID) {
		ChatID = chatID;
	}



	public List<UserChatInformationDTO> getUser() {
		return user;
	}



	public void setUser(List<UserChatInformationDTO> user) {
		this.user = user;
	}



		public static class UserChatInformationDTO{
			private String userNickName;
			private LocalDateTime memberFrom;
			private LocalDateTime memberUntil;
			private long userID;
			private String chatName;
			private String lastSeenMessageID;
			public String getUserNickName() {
				return userNickName;
			}
			public void setUserNickName(String userNickName) {
				this.userNickName = userNickName;
			}
			
			public LocalDateTime getMemberFrom() {
				return memberFrom;
			}
			public void setMemberFrom(LocalDateTime memberFrom) {
				this.memberFrom = memberFrom;
			}
			public LocalDateTime getMemberUntil() {
				return memberUntil;
			}
			public void setMemberUntil(LocalDateTime memberUntil) {
				this.memberUntil = memberUntil;
			}
			public long getUserID() {
				return userID;
			}
			public void setUserID(long userID) {
				this.userID = userID;
			}
			public String getChatName() {
				return chatName;
			}
			public void setChatName(String chatName) {
				this.chatName = chatName;
			}
			public String getLastSeenMessageID() {
				return lastSeenMessageID;
			}
			public void setLastSeenMessageID(String lastSeenMessageID) {
				this.lastSeenMessageID = lastSeenMessageID;
			}
	
			
	}
	
}
