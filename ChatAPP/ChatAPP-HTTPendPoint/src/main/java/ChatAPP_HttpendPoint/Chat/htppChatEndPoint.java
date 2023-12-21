package ChatAPP_HttpendPoint.Chat;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import chatAPP_DTO.Message.ChatInformationDTO;
import chatAPP_DTO.Message.MessageDTO;
import chatAPP_DTO.Message.UserChatOverViewDTO;

public interface htppChatEndPoint {

	@GetMapping("/{chatID}/{offSetStart}/{offSetEnd}")
	public ResponseEntity<List<MessageDTO>> loadChatHistory(
			@PathVariable String chatID,
			@PathVariable long offSetStart,
/*have to be some limit security*/		@PathVariable long offSetEnd);
	
	@GetMapping("/UserID/{offSetStart}/{offSetEnd}")
	public ResponseEntity<UserChatOverViewDTO> loadUserChatOverview(
			@PathVariable long UserID,
			@PathVariable long offSetStart,
			@PathVariable long offSetEnd
			);
	@GetMapping("/chatID")
	public ResponseEntity<ChatInformationDTO> getChatInformation(
			@PathVariable String chatID);
	
	@GetMapping("/{chatID}/{MessageOrder}")
	public ResponseEntity<MessageDTO> getMessage(
			@PathVariable String chatID,
			@PathVariable long MessageOrder);

}
