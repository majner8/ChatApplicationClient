package chat_application_commonPart.Validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = ChangeUserDetailsRequestValidator.ChangeUserDetailsRequestValidatorProvider.class)
@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ChangeUserDetailsRequestValidator {
    String message() default "Phone and prefix must be both present or both absent.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    
    static class ChangeUserDetailsRequestValidatorProvider  implements ConstraintValidator<ChangeUserDetailsRequestValidator, Object> {

    	
    	public boolean isValid(Object value, ConstraintValidatorContext context) {
    	
    		//custom message
    	     context.disableDefaultConstraintViolation();
    	        context.buildConstraintViolationWithTemplate("Custom message based on condition.")
    	               .addConstraintViolation();
    		return false;
    	}    	
    }


}