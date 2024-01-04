package chatAPP_CommontPart.ThreadLocal;

import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

public interface ThreadLocalSessionSimpMessageHeaderAccessor {

	/**Metod clear current ThreadLocalValue */
	public void clear();
	public void setSimpMessageHeaderAccessor(SimpMessageHeaderAccessor par);
	

	public static class WsebSocketSessionThreadLocalMessageAttribute{
		private final int rabitMQPriory;
		private final boolean haveToBeMessageRequired;
		private final String processingWebSocketDestion;
		private final String dtoClassName;
		
		
		public WsebSocketSessionThreadLocalMessageAttribute(int rabitMQPriory, boolean haveToBeMessageRequired,
				String processingWebSocketDestion, String dtoClassName) {
			this.rabitMQPriory = rabitMQPriory;
			this.haveToBeMessageRequired = haveToBeMessageRequired;
			this.processingWebSocketDestion = processingWebSocketDestion;
			this.dtoClassName = dtoClassName;
		}
		public static WsebSocketSessionThreadLocalMessageAttribute createWebSocketSessionThreadLocalMessageAttribute(int rabitMQPriory, boolean haveToBeMessageRequired,
				String processingWebSocketDestion, String dtoClassName) {
			return new WsebSocketSessionThreadLocalMessageAttribute(rabitMQPriory,
					 haveToBeMessageRequired,
					 processingWebSocketDestion, 
					  dtoClassName);
		}
		public int getRabitMQPriory() {
			return rabitMQPriory;
		}
		public boolean isHaveToBeMessageRequired() {
			return haveToBeMessageRequired;
		}
		public String getProcessingWebSocketDestion() {
			return processingWebSocketDestion;
		}
		public String getDtoClassName() {
			return dtoClassName;
		}
		
	}
	

	
}
