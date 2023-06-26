package FrontEnd;

import java.awt.Font;

import javax.swing.JComponent;


public enum ComponentLanguageName {
	UnknownException("", ""),
	IntegrityFileProblem("", ""),
	SearchingChatButton("", ""),
	FindNewUserToChat("Najít nové uživatele", "Find new user"),
	SearchingBackToHistoryButton("Zpět k historii", "Back to history"),
	SearchingNewUserButton("Vyhledat", "Start searching"),
	ResultWasNotFound("<html>Nenašel jsem žádné výsledky</html>", "<html>No result was found</html>"),
	UserDoesNotHaveHistory("<html>Zacni chatovat s novými uživateli!</html>", "<html>Start chatting with new users!</html>"),
	AuthorizeProcessLoginButton("Login", "Login"),
	AuthorizeProcessBackButton("Zpět", "Back"),
	InvalidEmail("Nesprávný email", "Invalid email"),
	IncorrectEmailOrPassword("Heslo, nebo email se neshoduji", "Password or email is not correct"),
	PasswordsDoNotMatch("Zadané hesla se neshodují", "Passwords are not the same"),
	EmailIsUsed("Zadaný email je jiz registrovan, použij jiný", "Email address is already registered, use another one"),
	IncorrectPassword("", ""),
	RegisterButton("Registrace", "Register"),
	LoginButton("Prihlaseni", "Login"),
	IncorrectDateFormat("Zadejte datum narození ve zprávném formátu DD-MM-YYYY(10-08-1902)", "Enter the date of birth in the correct format DD-MM-YYYY(10-08-1902)"),
	SynchronizationProgress("<html>Provádím Synchronizaci %s <div style='text-align: center;'><br> Prosím vyčkejte</div></html>", "<html>Synchronizing %s <div style='text-align: center;'><br> Please wait</div></html>"),
	UserTitleJFrame("Přihlášený uživatel: ", "Logged in user: "),
	FindNewUserTextField("Pro vyhledani, napis jmeno", "For searching, type the name"),
	PasswordText("Zadej heslo", "Enter Password"),
	EmailText("Emailová adresa", "Email address"),
	Born("Datum narozeni DD-MM-YYYY", "Birth date DD-MM-YYYY"),
	Name("Krestni jmeno", "First name"),
	LastName("Prijmeni", "Last name"),
	FinishRegistrationButton("Dokonči registraci", "Complete registration");

	private String czech;
	private String English;
	 ComponentLanguageName(String czecz,String english) {
		this.czech=czecz;
		this.English=english;
	}
	
	 public NameWithFont getName(TypeLanguage type) {
		 if(type==TypeLanguage.Czech) {
			 return new NameWithFont(this.czech);

		 }
		 if(type==TypeLanguage.English) {
			 return new NameWithFont(this.English);

		 }
		 return null;
	 }
	 public static enum TypeLanguage{
			Czech,English
		}
	public static class NameWithFont{
		private String name;
		private Font font=null;
		private NameWithFont(String name) {
			this.name = name;
		}
		public Font getFont() {
			return font;
		}
		
		public String toString() {
			return this.name;
		}
	}
}
