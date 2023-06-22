package FrontEnd;

import java.awt.Font;

import javax.swing.JComponent;


public enum ComponentLanguageName {

	UnKnownException("",""),IntegrityFileProblem("",""),SearingChatButton("",""),FindnewUserTochat("Najít nové uživatele","Find new user"),
	SearchingBackToHistoryButton("Zpět k historii","Back to history"),
	SearchingnewUserButton("Vyhledat","Start searching"),
	ResultWasNotFind("<html>Nenašel jsem žádné výsledky</html>","<html>I was not find result</html>"),
	UserDoNotHaveHistory("<html>Zacni chatovat s uživateli!</html>","XXX"),
	AutorizeProcesLogginButton("Loggin","Loggin"),
	AutorizeProcesBackButton("Zpět","Back"),UnValidEmail("Nesprávný email","Invalid email"),WrongEmailPassword("Heslo, nebo email se neshoduji","Password, or email is not correct"),
	PasswordIsNotEqual("Zadané hesla se neshodují","Passwords are not same"),EmailIsUsed("Zadaný email je jiz registrovan, použij jiný","Email adres has alredy registred, use another one"),WrongEmail("",""),WrongPassword("",""),RegisterButton("Registrace","Register"),LogginButton("Prihlaseni","Loggin"),
	UnCorectDateFormat("Zadejte datum narození ve zprávném formátu","Put born date in correct form"),
	SynchronizationProgress("<html>Provádím Synchronizaci %s <div style='text-align: center;'><br> Prosím vyčkejte</div></html>",
			"<html>I am making synchronization %s <div style='text-align: center;'><br> Please wait</div></html>"),
	UserTitleJFrame("Přihlášený uživatel: ","Loggin user: ");
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
