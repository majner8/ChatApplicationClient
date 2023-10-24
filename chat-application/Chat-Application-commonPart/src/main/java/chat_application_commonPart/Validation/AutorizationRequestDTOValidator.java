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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import AuthorizationDTO.AutorizationRequestDTO;
import chat_application_commonPart.Logger.Log4j2;



@Documented
@Constraint(validatedBy = AutorizationRequestDTOValidator.AutorizationRequestProvider.class)
@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface AutorizationRequestDTOValidator {
    String message() default "Phone and prefix must be both present or both absent.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    
    @Component
    static class AutorizationRequestProvider  implements ConstraintValidator<AutorizationRequestDTOValidator, AutorizationRequestDTO> {
    	@Autowired
    	private Validator validator;
    	public boolean isValid(AutorizationRequestDTO value, ConstraintValidatorContext context) {
    		{
    	
    		if(value==null)return false;
    		//validate email
    		EmailWrapper email=new EmailWrapper(value.getEmail());
    		if(!validator.validate(email).isEmpty()) {
    			return false;
    		}
    		}
    		if(!this.validatePhone(value.getCountryPreflix(), value.getPhone()))return false;
    		if(!this.validatePassword(value.getPassword()))return false;
    		return true;
    		/*
    	 * 
    		//custom message
    	     context.disableDefaultConstraintViolation();
    	        context.buildConstraintViolationWithTemplate("Custom message based on condition.")
    	               .addConstraintViolation();*/
    		
    		
    	} 
    	
    	private boolean validatePhone(String phonePreflix,String phone) {
    		phonePreflix=phonePreflix.trim();
    		phone=phone.trim();
    		if(!phonePreflix.startsWith("+"))return false;
    		if(phonePreflix.length()>4)return false;
    		try {
    		Integer.parseInt(phonePreflix.substring(1));
    		Integer.parseInt(phone);
    		} catch(NumberFormatException e) {
    			return false;
    		}
    		
    		return true;
    	}
    	
    	private boolean validatePassword(String password) {
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