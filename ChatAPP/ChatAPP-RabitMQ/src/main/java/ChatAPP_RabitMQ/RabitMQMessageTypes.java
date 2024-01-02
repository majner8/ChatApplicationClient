package ChatAPP_RabitMQ;

import chatAPP_DTO.Message.MessageDTO;

public enum RabitMQMessageTypes {
	
	SendChatMessage((Integer)5,false,MessageDTO.class),
	UserDatabaseRequest((Integer)1,true,null);
	private Integer priority;
	private boolean ShouldBeMessageRequired;
	private Class<?> DtoClass;
	private RabitMQMessageType(Integer priority,boolean ShouldBeMessageRequired,Class<?> typeOfDTO){
		this.priority=priority;
		this.ShouldBeMessageRequired=ShouldBeMessageRequired;
		this.DtoClass=typeOfDTO;
	}
	
	
	public Class<?> getDtoClass() {
		return DtoClass;
	}


	public boolean isShouldBeMessageRequired() {
		return ShouldBeMessageRequired;
	}

	public Integer getPriority() {
		return priority;
	}
	
	
}
