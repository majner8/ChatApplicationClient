package ChatAPP_WebSocket_EndPoint.AOP;

import java.util.concurrent.atomic.AtomicLong;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


import chatAPP_CommontPart.Log4j2.Log4j2;
import chatAPP_CommontPart.Properties.WebSocketProperties;
import chatAPP_CommontPart.ThreadLocal.ThreadLocalSessionSimpMessageHeaderAccessor;


@Component
@Aspect
public class WebSocketSessionAspect  {
	
	private  AtomicLong longCounter; 
	private ThreadLocal<Long> actualLog;
	@Autowired
	private ThreadLocalSessionSimpMessageHeaderAccessor manipulation;
	@Autowired
	private WebSocketProperties properties;
	public WebSocketSessionAspect() {
		if(Log4j2.log.isDebugEnabled()) {
			longCounter=new AtomicLong();
			longCounter.set(0);
			this.actualLog=new ThreadLocal<Long>();
		}
		else {
			longCounter=null;
		}
	}
	
    @Pointcut("execution(void *.*(..)) && @annotation(MessageMapping)")
	public void MessageMappingCalled() {};
	
	@Around("execution(MessageMappingCalled())")
	public void ProcessMessageMapping(ProceedingJoinPoint joinPoint,MessageMapping map)throws Throwable {
		 this.makeItAsync(joinPoint,map);
	}
	 @Async
	 private void makeItAsync(ProceedingJoinPoint joinPoint,MessageMapping map) {
		 if(Log4j2.log.isDebugEnabled()) {
			 this.actualLog.set(this.longCounter.getAndIncrement());
			 String message=String.format("running aspect Metod manageWebSocketSession"+System.lineSeparator()
			 +"Evoked by: %s"+System.lineSeparator()
			 +"LogID: %d"
			 ,joinPoint.getClass().getName()+"."+joinPoint.getSignature().getName());
			 Log4j2.log
			 .debug(Log4j2.MarkerLog.Aspect.getMarker(),message,this.actualLog.get());
		 }
		 
		 SimpMessageHeaderAccessor ses = null;
		 for(Object x:joinPoint.getArgs()) {
		 		if(x instanceof SimpMessageHeaderAccessor) {
		 			ses=(SimpMessageHeaderAccessor)x;
		 		}
		 	}
		 if(ses==null) {

			 if(Log4j2.log.isDebugEnabled()) {
				 String message="Aspect WebSocketSession metod could not be apply,"+System.lineSeparator()
				 +"Evoked Metod do not contain SimpMessageHeaderAccessor parametr type"+System.lineSeparator()
				+ "log ID: "+this.actualLog.get();
				 
				 Log4j2.log
				 .debug(Log4j2.MarkerLog.Aspect.getMarker(),message);
			 }
			  
			
			 
			 return;
		 }
		  this.manipulation.setSimpMessageHeaderAccessor(ses,this.properties.getWebSocketEndPointAndMessageTypeByEndPoint(String.join("/", map.value())));
		 	try {
				try {
					joinPoint.proceed();
				} catch (Throwable e) {
					if(Log4j2.log.isDebugEnabled()) {

						 Log4j2.log.debug(Log4j2.MarkerLog.Aspect.getMarker(),"Error during processing WebSocketEndPoint"+System.lineSeparator()
						 +"LogID: "+this.actualLog.get()+System.lineSeparator()
						 +e);
					}
					else {
						Log4j2.log.error(Log4j2.MarkerLog.Aspect.getMarker(),"Error during processing WebSocketEndPoint"+System.lineSeparator() 
						 +e);		
					}
						
				}
			}
		 	finally {
		 		this.manipulation.clear();
		 	}
		 
	 }
	
}
