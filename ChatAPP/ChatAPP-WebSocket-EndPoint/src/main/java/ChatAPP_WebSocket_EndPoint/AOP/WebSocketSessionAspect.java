package ChatAPP_WebSocket_EndPoint.AOP;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;

import chatAPP_CommontPart.Log4j2.Log4j2;
import chatAPP_CommontPart.ThreadLocal.ThreadLocalSessionSimpMessageHeaderAccessor;


@Component
@Aspect
public class WebSocketSessionAspect  {
	@Autowired
	private WebSocketSessionConfig config;
	@Autowired
	private ThreadLocalSessionSimpMessageHeaderAccessor manipulation;
    @Pointcut("execution(void *.*(..)) && @annotation(MessageMapping)")
	public void MessageMappingCalled() {};
	
	@Around("execution(MessageMappingCalled())")
	public void ProcessMessageMapping(ProceedingJoinPoint joinPoint,MessageMapping map)throws Throwable {
		 this.makeItAsync(joinPoint,map);
	}
	 
	 private void makeItAsync(ProceedingJoinPoint joinPoint,MessageMapping map) {
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
		 
		 this.manipulation.setSimpMessageHeaderAccessor(ses,this.config.getWebSocketSessionThreadLocalMessageAttribute(map.value()));
		 	
		 	try {
				try {
					joinPoint.proceed();
				} catch (Throwable e) {
					 Log4j2.log.error(Log4j2.MarkerLog.Aspect.getMarker(),"Error during processing WebSocketEndPoint"+System.lineSeparator()
					 +e);					
				}
			}
		 	finally {
		 		this.manipulation.clear();
		 	}
		 
	 }
	
}
