package ChatAPP_WebSocket_EndPoint;


import chatAPP_CommontPart.EndPoint.WebSocketEndPointAndMessageType;

public class WebSocketEndPointPath  {

	public static final String Config_StartConsumingPath=WebSocketEndPointAndMessageType.config_startConsuming.getPath();
	public static final String Config_StopConsumingPath=WebSocketEndPointAndMessageType.config_stopConsuming.getPath();
	public static final String AcknowledgeEndPoint_ConfirmMessage=WebSocketEndPointAndMessageType.acknowledgeEndPoint_confirmMessage.getPath();
	public static final String Chat_SendMessagePath=WebSocketEndPointAndMessageType.chat_sendMessage.getPath();
	public static final String Chat_sawMessagePath=WebSocketEndPointAndMessageType.chat_sawMessage.getPath();
	public static final String Chat_changeMessagePath=WebSocketEndPointAndMessageType.chat_changeMessage.getPath();



}
