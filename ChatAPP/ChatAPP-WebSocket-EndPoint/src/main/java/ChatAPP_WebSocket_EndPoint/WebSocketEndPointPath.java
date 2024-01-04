package ChatAPP_WebSocket_EndPoint;


import ChatAPP_WebSocket_EndPoint.AOP.WebSocketSessionConfig;
import chatAPP_CommontPart.EndPoint.WebSocketEndPointAndMessageType;
import chatAPP_CommontPart.ThreadLocal.ThreadLocalSessionSimpMessageHeaderAccessor.WebSocketSessionThreadLocalMessageAttribute;

public class WebSocketEndPointPath implements WebSocketSessionConfig {

	public static final String Config_StartConsumingPath=WebSocketEndPointAndMessageType.config_startConsuming.getPath();
	public static final String Config_StopConsumingPath=WebSocketEndPointAndMessageType.config_stopConsuming.getPath();
	public static final String AcknowledgeEndPoint_ConfirmMessage=WebSocketEndPointAndMessageType.acknowledgeEndPoint_confirmMessage.getPath();
	public static final String Chat_SendMessagePath=WebSocketEndPointAndMessageType.chat_sendMessage.getPath();
	public static final String Chat_sawMessagePath=WebSocketEndPointAndMessageType.chat_sawMessage.getPath();
	public static final String Chat_changeMessagePath=WebSocketEndPointAndMessageType.chat_changeMessage.getPath();


	@Override
	public WebSocketSessionThreadLocalMessageAttribute getWebSocketSessionThreadLocalMessageAttribute(
			String[] calledEndPoint) {
		// TODO Auto-generated method stub
		return null;
	}

}
