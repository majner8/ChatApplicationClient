package ChatAPP_Security.RequestPermision;

public class MessageRequestPermisionImplementation implements MessageRequestPermision{

	@Override
	public void verifyMessageOwnership(long messageOwnerID, long SenderID, long WebSocketSessionOwnerID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void verifyChatWritePermission(long SenderID, long WebSocketSessionOwnerID, String chatID) {
		// TODO Auto-generated method stub
		
	}

	

	@Override
	public void verifyGetQuickUserOverViewPermision(long SenderID, int offSetStart, int offSetEnd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void verifyUserAccestPermisionToChat(long userID, String chatID) {
		// TODO Auto-generated method stub
		
	}

}
