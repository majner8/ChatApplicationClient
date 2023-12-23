package ChatAPP_WebSocket.Service;

import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import ChatAPP_Chat.ChatManagement.ChatManagementInterface;
import ChatAPP_RabitMQ.PushingMessages.PushMessageRabitMQInterface;
import ChatAPP_Security.RequestPermision.MessageRequestPermision;
import ChatAPP_WebSocket.Session.WebSocketSessionAttributeInterface;
import chatAPP_DTO.Message.MessageDTO;
import chatAPP_database.Chat.Messages.MessageEntity;
import chatAPP_database.Chat.Messages.MessageRepositoryEntity;

@Service
public class ProcessChatMessageService {

	@Autowired
	private MessageRepositoryEntity messageRepo;
	@Autowired
	private MessageRequestPermision SecurityVerification;
	@Autowired
	private WebSocketSessionAttributeInterface sessionAttributeInterface;
	@Autowired
	private ChatManagementInterface chatManagement;
	@Autowired
	private PushMessageRabitMQInterface rabitMQPush;

	public void SendMessage(MessageDTO message,String WebSocketEndPointPath) {
		//verify if user has permision to write into chat
		//if not exception will be thrown and catch by global handler
		this.SecurityVerification.verifyChatWritePermission(message.getSenderID(), this.sessionAttributeInterface.getSessionOwnerID(), message.getChatID());
		MessageEntity entity=new MessageEntity(message);
		//message will be save, and update with order in table
		this.messageRepo.saveAndFlush(entity);
		message=entity.convertEntityToDTO();
		
		this.PushMessageToRabitMQService(message, WebSocketEndPointPath);
	}
	public void ChangeMessage(MessageDTO message,String WebSocketEndPointPath) {
		//if message is not exist EntityWasNotFoundException would be thrown
		MessageEntity entity=this.messageRepo.findByPrimaryKey(message.getMessageID());
		//verify, if user has permision to change message(E.t.c it is owner of message)
		this.SecurityVerification.verifyMessageOwnership(entity.getSenderID(),message.getSenderID(),this.sessionAttributeInterface.getSessionOwnerID());
		
		//optimistic locking can be thrown, if message will be modify from other device
		this.messageRepo.saveAndFlush(entity);
		message=entity.convertEntityToDTO();
			
		this.PushMessageToRabitMQService(message, WebSocketEndPointPath);
	}

	private void PushMessageToRabitMQService(MessageDTO message,String WebSocketEndPointPath) {
		//retrieved all memberIDOfChat
		List<Long> memberID=this.chatManagement.getUserIDofMembers();
		//push message to rabitMQ
		this.rabitMQPush.PushMessage(message, WebSocketEndPointPath, memberID);
	}
}
