package ChatAPP_Chat.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import ChatAPP_Chat.ChatManagement.ActiveChat;
import ChatAPP_Chat.ChatManagement.ActiveChatManagementInterface;
import chatAPP_DTO.Message.MessageDTO;
import chatAPP_database.Chat.Messages.MessageEntity;
import chatAPP_database.Chat.Messages.MessageRepositoryEntity;

@Service
public class ChatMessageService {

	@Autowired
	private ActiveChatManagementInterface activeChatManagement;
	@Autowired
	private MessageRepositoryEntity MessageRepo;
	
	public void SendMessage(MessageDTO previousMessage, String chatID) {
		//Message have to be persist
		MessageEntity entityMes=new MessageEntity(previousMessage);
			
			//saving operation could return duplicate primary key error
			this.MessageRepo.saveAndFlush(entityMes);
			
		MessageDTO message=entityMes.convertEntityToDTO();
		
		
		ActiveChat chat=this.activeChatManagement.getActiveChat(chatID);
		
	}
	
	public void sawMessage(long userID,String messageID,String chatID) {
	}
}

