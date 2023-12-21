package ChatAPP_HttpendPoint.Chat;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import chatAPP_DTO.Message.ChatInformationDTO;
import chatAPP_DTO.Message.MessageDTO;
import chatAPP_DTO.Message.UserChatOverViewDTO;
public class ChatControler implements htppChatEndPoint {

	
	@Autowired
	private ChatHistoryService service;

	public ResponseEntity<List<MessageDTO>> loadChatHistory(
			 String chatID,
			 long offSetStart,
			 long offSetEnd){
		List<MessageDTO> messages=this.service.loadChatHistory(chatID, offSetStart, offSetEnd);
		
		
		return null;
	}
	public ResponseEntity<UserChatOverViewDTO> loadUserChatOverview(
			 long UserID,
			 long offSetStart,
			 long offSetEnd
			) {
		return null;
	}
	public ResponseEntity<ChatInformationDTO> getChatInformation(
			 String chatID) {
		this.service.getChatInformation(chatID);
		return null;
	}
	public ResponseEntity<MessageDTO> getMessage(
			 String chatID,
			 long MessageOrder)
	{return null;}
}

