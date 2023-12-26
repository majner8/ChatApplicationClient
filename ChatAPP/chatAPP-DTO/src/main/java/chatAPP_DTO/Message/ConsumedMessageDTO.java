package chatAPP_DTO.Message;

import java.util.HashMap;
import java.util.Map;

/** */
public class ConsumedMessageDTO {

	private  Map<String,Object>headers=new HashMap<String,Object>();
	
	private byte [] bodyOfMessage;

	public Object getParametr(String headerName) {
		synchronized(this.headers) {
			return this.headers.get(headerName);
		}
	}
	public void addHeader(String headerName,Object headerValue) {
		synchronized(this.headers) {
			this.headers.put(headerName, headerValue);
		}
	}
	
	public Map<String, Object> getHeaders() {
		return headers;
	}

	
	public void setHeaders(Map<String, Object> headers) {
		this.headers = headers;
	}

	public byte[] getBodyOfMessage() {
		return bodyOfMessage;
	}

	public void setBodyOfMessage(byte[] bodyOfMessage) {
		this.bodyOfMessage = bodyOfMessage;
	}
	
	
}
