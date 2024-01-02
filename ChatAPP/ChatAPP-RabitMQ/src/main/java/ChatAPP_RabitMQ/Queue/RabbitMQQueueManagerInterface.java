package ChatAPP_RabitMQ.Queue;

import ChatAPP_RabitMQ.Queue.RabbitMQQueueManager.CustomRabitMQQueue;

public interface RabbitMQQueueManagerInterface {

	/**Metod return deviceQueueName
	 * if queue has not been created yet, metod manage creating new one
	 *, bind them to user Key 
	 * And start  async synchronization-send first X newest Chat, with fistMessages
	 *  Metod does not synchronizated
	 *  
	 *  */
	public CustomRabitMQQueue getDeviceQueueName();
}

