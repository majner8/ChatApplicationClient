package chatAPP_database.Chat;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity()
public class ChatEntity {
	public static final String chatEntityTableName="chats";
		
	public static final String defaultChatNameColumnName="default_chat_name";
	public static final String chatIDColumnName="chat_id";
	public static final String createdByUserIDColumnName="created_by_user_id";


	@Column(name=ChatEntity.defaultChatNameColumnName)
	private String defaultChatName;
	@Id
	@Column(name=ChatEntity.chatIDColumnName)
	private String ChatID;
	@Column(name=ChatEntity.createdByUserIDColumnName)
	private long createdByUserID;
	@OneToMany(mappedBy="chat")
	private Set<UserChats> chat;
	
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
	public long getCreatedByUserID() {
		return createdByUserID;
	}
	public void setCreatedByUserID(long createdByUserID) {
		this.createdByUserID = createdByUserID;
	}
	
	public Set<UserChats> getChat() {
		return chat;
	}
	public void setChat(Set<UserChats> chat) {
		this.chat = chat;
	}
	
	
}
