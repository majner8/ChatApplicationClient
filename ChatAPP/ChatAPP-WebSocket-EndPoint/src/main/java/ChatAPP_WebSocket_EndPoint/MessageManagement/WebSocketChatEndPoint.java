package ChatAPP_WebSocket_EndPoint.MessageManagement;

import chatAPP_DTO.Message.MessageDTO;

public interface WebSocketChatEndPoint{
	
	public static final String SendMessagePath="";
	public static final String sawMessagePath="";
	public static final String changeMessagePath="";

	public static String convertPropertiesToSawMessagePath(long userID, String messageID, String chatID) {
		return null;
	}
	public void SendMessage(MessageDTO message);
	
	public void sawMessage();
	
	public void ChangeMessage(MessageDTO message);
	
}
