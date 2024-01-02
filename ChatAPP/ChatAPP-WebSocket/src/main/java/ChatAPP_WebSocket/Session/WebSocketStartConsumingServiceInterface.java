package ChatAPP_WebSocket.Session;

public interface WebSocketStartConsumingServiceInterface {

	/**Metod start consuming message.
	 * Metod add container to WebSocketSession
	 * @return if attemp was sucesfull */
	public boolean StartConsumingMessage();
	public boolean StopConsumingMessage();
}
