package Main;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import CommonPart.SQL.MainSQL.Query;
import CommonPart.ThreadManagement.ThreadPoolingManagement;

public class ThreadPoolingManagementClientold extends ThreadPoolingManagement {

	
	private ScheduledExecutorService ScheduledAndWaitService=Executors.newSingleThreadScheduledExecutor();
	
	
	@Override
	public void ProcessReceivedMessage(Runnable command) {
		// TODO Auto-generated method stub
		ScheduledAndWaitService.execute(command);
	}

	@Override
	public void ProcesSQLTask(Query[] SQLQueryTask, ProcessSQLTask callAfter) {
		// TODO Auto-generated method stub
		
	}
//dsdsa@se.cz
	@Override
	public void schedule(Runnable command, long delay, TimeUnit timeUnit) {
		// TODO Auto-generated method stub
		ScheduledAndWaitService.schedule(command, delay, timeUnit);
	}

	@Override
	public void Execute(Runnable Command) {
		// TODO Auto-generated method stub
		ScheduledAndWaitService.execute(Command);
	}


	@Override
	protected void ShutDownService() {
		// TODO Auto-generated method stub
		this.ScheduledAndWaitService.shutdown();

	}


}
