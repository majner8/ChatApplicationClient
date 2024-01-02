package ChatAPP_WebSocket_EndPoint.AOP;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;

import chatAPP_CommontPart.Log4j2.Log4j2;
import chatAPP_CommontPart.ThreadLocal.ThreadLocalSessionSimpMessageHeaderAccessor;


@Component
@Aspect
public class WebSocketSessionAspect  {
	@Autowired
	private ThreadLocalSessionSimpMessageHeaderAccessor manipulation;
	 @Around("execution(void ChatAPP_WebSocket_EndPoint.EndPoint.*.*(..))")
	 /**Metod add WebSocket and callWebSocketPath */   
	 public void manageWebSocketSession(ProceedingJoinPoint joinPoint) throws Throwable {
		 if(Log4j2.log.isDebugEnabled()) {
			 String message=String.format("running aspect Metod manageWebSocketSession"+System.lineSeparator()
			 +"Evoked by: %s"
			 ,joinPoint.getClass().getName()+"."+joinPoint.getSignature().getName());
			 Log4j2.log
			 .debug(Log4j2.MarkerLog.Aspect.getMarker(),message);
		 }
		 
		 SimpMessageHeaderAccessor ses = null;
		 for(Object x:joinPoint.getArgs()) {
		 		if(x instanceof SimpMessageHeaderAccessor) {
		 			ses=(SimpMessageHeaderAccessor)x;
		 
		 		}
		 	}
		 if(ses==null)return;
		 	this.manipulation.setSimpMessageHeaderAccessor(ses);
		 	
		 	try {
				joinPoint.proceed();
			}
		 	finally {
		 		this.manipulation.clear();
		 	}
		 
	    }
	
}
