package chatAPP_database.Chat;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import chatAPP_DTO.Message.ChatInformationDTO.UserChatInformationDTO;



@Entity()
public class UserChats {
	public static final String userChatsTableName="users_chat";
	
	public static final String userIDcolumnName="user_id";
	public static final String chatIDcolumnName="chat_id";
	public static final String userNickNamecolumnName="user_nick_name_in_chat";
	public static final String chatNamecolumnName="chat_name_of_user";
	public static final String memberFromcolumnName="member_from";
	public static final String memberUntilcolumnName="member_until";
	public static final String lastSeenMessageIDcolumnName="last_seen_message_id";
	public static final String joinChatColumnName="appropriate_chat";
	
	@Column(name=UserChats.userNickNamecolumnName)
	//name of User
	private String userNickName;
	@Column(name=UserChats.chatNamecolumnName)
	//each user can have own chatName
	private String chatName;
	@Column(name=UserChats.memberFromcolumnName)
	private LocalDateTime memberFrom;
	@Column(name=UserChats.memberUntilcolumnName)
	private LocalDateTime memberUntil=null;
	@Column(name=UserChats.lastSeenMessageIDcolumnName)
	private String lastSeenMessageID;
    @EmbeddedId
	private CompositePrimaryKey primaryKey;
	
	@ManyToOne
	//@Column(name=UserChats.joinChatColumnName)
	@JoinColumn(name="chat",referencedColumnName=ChatEntity.chatIDColumnName)
	private ChatEntity chat;
	
	public UserChatInformationDTO convertEntityToDTO() {
		UserChatInformationDTO x=new UserChatInformationDTO();
		x.setChatName(chatName);
		x.setLastSeenMessageID(lastSeenMessageID);
		x.setMemberFrom(memberFrom);
		x.setMemberUntil(memberUntil);
		x.setUserID(this.primaryKey.getUserID());
		x.setUserNickName(userNickName);
		return x;
	}
    @Embeddable
	public static class CompositePrimaryKey implements Serializable{

		@Column(name=UserChats.userIDcolumnName)
		private long userID;
		@Column(name=UserChats.chatIDcolumnName)
		private String chatID;	
		public long getUserID() {
			return userID;
		}
		public void setUserID(long userID) {
			this.userID = userID;
		}
		public String getChatID() {
			return chatID;
		}
		public void setChatID(String chatID) {
			this.chatID = chatID;
		}
	}

	
	public CompositePrimaryKey getPrimaryKey() {
		return primaryKey;
	}
	public ChatEntity getChat() {
		return chat;
	}
	public void setPrimaryKey(CompositePrimaryKey primaryKey) {
		this.primaryKey = primaryKey;
	}
	public void setChat(ChatEntity chat) {
		this.chat = chat;
	}
	public String getUserNickName() {
		return userNickName;
	}
	public void setUserNickName(String userNickName) {
		this.userNickName = userNickName;
	}
	public String getChatName() {
		return chatName;
	}
	public void setChatName(String chatName) {
		this.chatName = chatName;
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
	public String getLastSeenMessageID() {
		return lastSeenMessageID;
	}
	public void setLastSeenMessageID(String lastSeenMessageID) {
		this.lastSeenMessageID = lastSeenMessageID;
	}
	
	
}
