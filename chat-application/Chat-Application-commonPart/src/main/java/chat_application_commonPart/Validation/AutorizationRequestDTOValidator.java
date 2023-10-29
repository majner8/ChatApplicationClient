package chat_application_commonPart.Validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Set;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolation;
import javax.validation.Payload;
import javax.validation.Validator;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import AuthorizationDTO.AutorizationRequestDTO;
import chat_application_commonPart.Exception.ExceptionType;
import chat_application_commonPart.Logger.Log4j2;



@Documented
@Constraint(validatedBy = AutorizationRequestDTOValidator.AutorizationRequestProvider.class)
@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface AutorizationRequestDTOValidator {
    String message() default "AutorizationRequest validation failt";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    
    static class AutorizationRequestProvider  implements ConstraintValidator<AutorizationRequestDTOValidator, AutorizationRequestDTO> {
  
    	@Autowired
    	private Validator validator;
    	
    	
    	public boolean isValid(AutorizationRequestDTO value, ConstraintValidatorContext context) {
			//context.disableDefaultConstraintViolation();
    		if(this.makeValidation(value, context)) return true;
    		Log4j2.log.info(Log4j2.LogMarker.Validation.getMarker(),"Authorization validation failed");
    		
    		return false;
    	} 
    	
    	private boolean makeValidation(AutorizationRequestDTO value, ConstraintValidatorContext context) {
    		{
    	    	
        		if(value==null) {
            	        context.buildConstraintViolationWithTemplate("AutorizationRequestDTO cannot be null")
            	               .addConstraintViolation();
            	        

        			return false;}
        		if(Log4j2.log.isTraceEnabled()) {
        			Log4j2.log.trace(Log4j2.LogMarker.Validation.getMarker(),"AuthorizationRequestValue:");
        			Log4j2.log.trace(Log4j2.LogMarker.Validation.getMarker(),"CountryCode: "+value.getCountryPreflix());
        			Log4j2.log.trace(Log4j2.LogMarker.Validation.getMarker(),"Phone: "+value.getPhone());
        			Log4j2.log.trace(Log4j2.LogMarker.Validation.getMarker(),"Device id: "+value.getDeviceID());
        			Log4j2.log.trace(Log4j2.LogMarker.Validation.getMarker(),"Email: "+value.getEmail());
        			Log4j2.log.trace(Log4j2.LogMarker.Validation.getMarker(),"Password: "+value.getPassword());
        			Log4j2.log.trace(Log4j2.LogMarker.Validation.getMarker(),"does device new: "+value.getIsDeviceNew());
        		}
        		
        		}
    			boolean email=this.validateEmail(value.getEmail());
    			boolean phone=this.validatePhone(value.getCountryPreflix(), value.getPhone());
    			
    			if((email&&phone)||(!email&&!phone)) {
    				/*
           	        context.buildConstraintViolationWithTemplate(ExceptionType.InvalidPhoneOrCountryCoude.toString())
           	               .addConstraintViolation();*/
    				return false;
    			}
    	
        		if(!this.validatePassword(value.getPassword())) {
        			/*context.buildConstraintViolationWithTemplate(ExceptionType.InvalidPassword.toString())
           	               .addConstraintViolation();*/
        			return false;
        			}
        		return true;
        	

    	}
    	
    	private boolean validatePhone(String phonePreflix,String phone) {
    		if(phonePreflix==null||phone==null) {
    			if(Log4j2.log.isDebugEnabled()) {
       	        	Log4j2.log.debug(Log4j2.LogMarker.Validation.getMarker(),"Phone validation failed, countryCodeOrPhone is null");
       	        }
    			
    			return false;}
    		phonePreflix=phonePreflix.trim();
    		phone=phone.trim();
    		if(!phonePreflix.startsWith("+")) {
    			if(Log4j2.log.isDebugEnabled()) {
       	        	Log4j2.log.debug(Log4j2.LogMarker.Validation.getMarker(),"Phone validation failed,CountryCode do not start with +");
       	        }

    			return false;}
    		if(phonePreflix.length()>4) {
    			if(Log4j2.log.isDebugEnabled()) {
       	        	Log4j2.log.debug(Log4j2.LogMarker.Validation.getMarker(),"Phone validation failed, CountryCode is longer than 4");
       	        }
    			return false;}
    		try {
    		Integer.parseInt(phonePreflix.substring(1));
    		Integer.parseInt(phone);
    		} catch(NumberFormatException e) {
    			if(Log4j2.log.isDebugEnabled()) {
       	        	Log4j2.log.debug(Log4j2.LogMarker.Validation.getMarker(),"Phone validation failed,CountryCode or phone is not number");
       	        }
    			
    			return false;
    		}
    		
    		return true;
    	}
    	
    	private boolean validatePassword(String password) {
    		if(password==null) return false;
    		return true;
    	}
    	private boolean validateEmail(String rawEmail) {
    		if(rawEmail==null) {
    			if(Log4j2.log.isDebugEnabled()) {
       	        	Log4j2.log.debug(Log4j2.LogMarker.Validation.getMarker(),"Email validation failed, email is null");
       	        }
    			return false;
    		}
    		//validate email
    		EmailWrapper email=new EmailWrapper(rawEmail);
    		Set<ConstraintViolation<EmailWrapper>> em=validator.validate(email);
    		if(!em.isEmpty()) {
    			/** 
    			context.disableDefaultConstraintViolation();
       	        context.buildConstraintViolationWithTemplate(ExceptionType.InvalidEmail.toString())
       	               .addConstraintViolation();
       	        	*/
       	        if(Log4j2.log.isDebugEnabled()) {
       	        	Log4j2.log.debug(Log4j2.LogMarker.Validation.getMarker(),"Email validation failed");
       	        }
    			return false;
    		}
    		return true;
    	}
    	private static class EmailWrapper{
    		@Email
    		private final String email;

    		public EmailWrapper(String email) {
    			this.email=email;
    		}
			public String getEmail() {
				return email;
			}
    		
    	}
    }


}