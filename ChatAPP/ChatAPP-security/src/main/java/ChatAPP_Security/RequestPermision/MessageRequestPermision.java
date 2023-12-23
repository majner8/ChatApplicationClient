package ChatAPP_Security.RequestPermision;

public interface MessageRequestPermision {

	/**Metod verify, if id is same, and eventually make security consequences  */
	public void verifyMessageOwnership(long messageOwnerID,long SenderID,long WebSocketSessionOwnerID);
	
	/**Metod verify if sender of message is same as sender from WebSocketSession  
	 * */
	public void verifyChatWritePermission(long SenderID,long WebSocketSessionOwnerID,String chatID);
	
}
