package ChatAPP_WebSocket.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import chatAPP_DTO.Message.MessageDTO;
import chatAPP_database.Chat.Messages.MessageEntity;
import chatAPP_database.Chat.Messages.MessageRepositoryEntity;

@Service
public class ProcessChatMessageService {

	@Autowired
	private MessageRepositoryEntity messageRepo;
	
	public void SendMessage(MessageDTO message,WebSocketSession session) {
		MessageEntity entity=new MessageEntity(message);
		//message will be save, and update with order in table
		this.messageRepo.saveAndFlush(entity);
		message=entity.convertEntityToDTO();
	}
	public void ChangeMessage(MessageDTO message,WebSocketSession session) {
		//if message is not exist EntityWasNotFoundException would be thrown
		MessageEntity entity=this.messageRepo.findByPrimaryKey(message.getMessageID());
		
		//verify, if user has permision
		
		this.messageRepo.saveAndFlush(entity);
		message=entity.convertEntityToDTO();
	}
	
}
