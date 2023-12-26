package ChatAPP_RabitMQ;

public enum RabitMQMessageType {
	
	SendChatMessage((Integer)5,false),
	UserDatabaseRequest((Integer)1,true);
	private Integer priority;
	private boolean ShouldBeMessageRequired;
	private RabitMQMessageType(Integer priority,boolean ShouldBeMessageRequired){
		this.priority=priority;
		this.ShouldBeMessageRequired=ShouldBeMessageRequired;
	}
	
	public boolean isShouldBeMessageRequired() {
		return ShouldBeMessageRequired;
	}

	public Integer getPriority() {
		return priority;
	}
	
	
}
