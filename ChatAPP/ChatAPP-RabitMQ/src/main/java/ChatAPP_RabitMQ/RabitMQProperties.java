package ChatAPP_RabitMQ;

public interface RabitMQProperties {

	public long getUnacknowledgedMessageTimeout();
	
	public String getMessagePropertiesWebSocketEndPointName();
	
	public String getHaveToBeMessageRequiredHeaderName();
}
