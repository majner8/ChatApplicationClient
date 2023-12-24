package ChatAPP_RabitMQ;

public enum RabitMQMessageType {

	SendChatMessage((Integer)5);
	private Integer priority;
	private RabitMQMessageType(Integer priority){
		this.priority=priority;
	}
	public Integer getPriority() {
		return priority;
	}
	
	
}
