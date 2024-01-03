package ChatAPP_HttpendPoint.Chat;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import ChatAPP_Security.Authorization.CustomSecurityContextHolder.SecurityContextDataInterface;
import ChatAPP_Security.RequestPermision.MessageRequestPermision;
import chatAPP_DTO.Message.ChatInformationDTO;
import chatAPP_DTO.Message.MessageDTO;
import chatAPP_database.Chat.chatEntityRepository;
import chatAPP_database.Chat.Messages.MessageEntity;
import chatAPP_database.Chat.Messages.MessageRepositoryEntity;
public class ChatControler implements htppChatEndPoint {
	
	@Autowired
	private MessageRepositoryEntity messageRepo;
	@Autowired
	private chatEntityRepository chatRepo;
	@Autowired
	private MessageRequestPermision messagePermision;
	@Autowired
	private SecurityContextDataInterface securityContextSession;
	
	public ResponseEntity<List<MessageDTO>> loadChatHistory(
			 String chatID,
			 long offSetStart,
			 long offSetEnd){
		this.messagePermision.verifyUserAccestPermisionToChat(this.securityContextSession.getOwnerUserId(), chatID);

		List<MessageDTO> res= this.messageRepo.findByChatID(chatID, offSetStart, offSetEnd)
				.stream().map((En)->{return En.convertEntityToDTO();}).collect(Collectors.toList());
		return ResponseEntity.ok(res);
		
	}
	
	public ResponseEntity<ChatInformationDTO> getChatInformation(
			 String chatID) {
		this.messagePermision.verifyUserAccestPermisionToChat(this.securityContextSession.getOwnerUserId(), chatID);
		ChatInformationDTO dto= this.chatRepo.findByPrimaryKey(chatID).convertEntityToDTO();
		return ResponseEntity.ok(dto);
	}
	public ResponseEntity<MessageDTO> getMessage(
			 String chatID,
			 long MessageOrder)
	{
		Optional<MessageDTO> mes=
			
				this.messageRepo.findByChatIDAndOrder(chatID, MessageOrder)
				.map((E)->{
					return E.convertEntityToDTO();
				});
		if(mes.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.ok(mes.get());
		}

	@Override
	public ResponseEntity<List<MessageDTO>> loadUserChatOverview(int offSetStart, int offSetEnd) {
		this.messagePermision.verifyGetQuickUserOverViewPermision(this.securityContextSession.getOwnerUserId(), offSetStart, offSetEnd);
		List<MessageDTO> message=this.messageRepo.getQuickUserSynchronizationMessage(this.securityContextSession.getOwnerUserId(), offSetStart, offSetEnd)
				.stream().map((MessageEntity-> {
					return MessageEntity.convertEntityToDTO();
				}
				)).collect(Collectors.toList());
		
		return ResponseEntity.ok(message);		
	}
}

