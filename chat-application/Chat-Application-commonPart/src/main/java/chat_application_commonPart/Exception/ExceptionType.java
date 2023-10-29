package chat_application_commonPart.Exception;

public enum ExceptionType {

	InvalidEmailOrPhone("Email or phone did not meet conditions"),
	InvalidPassword("Password did not meet conditions"),InvalidPhoneOrCountryCoude("Phone/country code did not meet conditions")
	,InvalidEmail("Email did not meet conditions");
	
	private String message;
	 ExceptionType(String mes) {
		this.message=mes;
	}
	 
	 @Override
	 public String toString() {
		 return this.message;
	 }
}
