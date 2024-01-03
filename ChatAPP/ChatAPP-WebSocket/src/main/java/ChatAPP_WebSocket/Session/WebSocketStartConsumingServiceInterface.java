package ChatAPP_WebSocket.Session;

public interface WebSocketStartConsumingServiceInterface {

	/**Metod start consuming message.
	 * Metod add container to WebSocketSession
	 * @return return false-if queue had to be created, and server start quick synchronization */
	public boolean StartConsumingMessage();
	public boolean StopConsumingMessage();
}
