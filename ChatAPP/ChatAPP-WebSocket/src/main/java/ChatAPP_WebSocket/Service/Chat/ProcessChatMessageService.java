package ChatAPP_WebSocket.Service.Chat;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ChatAPP_Chat.ChatManagement.ChatManagementInterface;
import ChatAPP_RabitMQ.Producer.PushMessageRabitMQService;
import ChatAPP_Security.RequestPermision.MessageRequestPermision;
import chatAPP_CommontPart.ThreadLocal.ThreadLocalSimpMessageHeaderAccessor;
import chatAPP_DTO.Message.MessageDTO;
import chatAPP_DTO.Message.SawMessageDTO;
import chatAPP_database.Chat.Messages.MessageEntity;
import chatAPP_database.Chat.Messages.MessageRepositoryEntity;

@Service
public class ProcessChatMessageService {

	@Autowired
	private MessageRepositoryEntity messageRepo;
	@Autowired
	private MessageRequestPermision SecurityVerification;
	@Autowired
	private ThreadLocalSimpMessageHeaderAccessor sessionAttributeInterface;
	@Autowired
	private ChatManagementInterface chatManagement;
	@Autowired
	private PushMessageRabitMQService rabitMQPush;

	/** */
	public void SendMessage(MessageDTO message) {
		//verify if user has permision to write into chat
		//if not exception will be thrown and catch by global handler
		this.SecurityVerification.verifyChatWritePermission(message.getSenderID(), this.sessionAttributeInterface.getSessionOwnerUserID(), message.getChatID());
		MessageEntity entity=new MessageEntity(message);
		//message will be save, and update with order in table
		this.messageRepo.saveAndFlush(entity);
		message=entity.convertEntityToDTO();
		
		this.PushMessageToRabitMQService(message);
	}
	public void ChangeMessage(MessageDTO message) {
		//if message is not exist EntityWasNotFoundException would be thrown
		MessageEntity entity=this.messageRepo.findByPrimaryKey(message.getMessageID());
		//verify, if user has permision to change message(E.t.c it is owner of message)
		this.SecurityVerification.verifyMessageOwnership(entity.getSenderID(),message.getSenderID(),this.sessionAttributeInterface.getSessionOwnerUserID());
		
		//optimistic locking can be thrown, if message will be modify from other device
		this.messageRepo.saveAndFlush(entity);
		message=entity.convertEntityToDTO();
			
		this.PushMessageToRabitMQService(message);
	}
	public void sawMessage(SawMessageDTO message) {
		
	}
	
	private void PushMessageToRabitMQService(MessageDTO message) {
		//retrieved all memberIDOfChat
		List<Long> membersID=this.chatManagement.getUserIDofMembers();
		//push message to rabitMQ
		this.rabitMQPush.PushSentChatMessages(message, 
				membersID);	}
}
