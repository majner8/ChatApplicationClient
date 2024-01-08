package ChatAPP_WebSocket_EndPoint.Configuration;

public interface WebSocketEndPointConfigInterface {

	
	public void StartConsumingMessage(int offSetStart,int offSetEnd);
	public void StopConsumingMessage();
}
