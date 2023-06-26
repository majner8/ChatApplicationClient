package Backend;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import CommonPart.SQL.MainSQL;
import FrontEnd.ComponentLanguageName;
import FrontEnd.MainJFrame;
import Main.Main.ReasonToEndMessage;

public class ClientDatabase extends MainSQL{

	public ClientDatabase() throws FileLoadException {
		super(ClientDatabase.databaseTaskType.values());
		// TODO Auto-generated constructor stub
	}
	public static Connection con;
	static {
	try {
		
		String curentDirectory=Main.Main.MakeInstalationOfDatabase();
		MainSQL.LoadSQLFile=new LoadSQLFile();
		
		Class.forName("org.sqlite.JDBC");
		con=DriverManager.getConnection("jdbc:sqlite:"+curentDirectory);

		con.createStatement().executeQuery("PRAGMA integrity_check;");

	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		Main.Main.stopServer(ReasonToEndMessage.IntegrityProblem, e);
		//Main.Main.stopServer(ComponentLanguageName.IntegrityFileProblem.getName(MainJFrame.language).toString());
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		Main.Main.stopServer(ReasonToEndMessage.IntegrityProblem, e);

		//Main.Main.stopServer(ComponentLanguageName.IntegrityFileProblem.getName(MainJFrame.language).toString());

	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		Main.Main.stopServer(ReasonToEndMessage.IntegrityProblem, e);
	}
	
	}
	

public static final String path="SQL\\";
	
	public static enum databaseTaskType{// insert
		LastKnownMessage("SelectLastKnownTable.sql"),StartSynchronization("StartSynchronization.sql"),

		SelectTableNameToCompare("SelectUserTableNameForHistory.sql"),
		SaveChatMessage("SavedChatMessage.sql"),
		
		InsertMessageIntoUserQuickChat("InsertIntoUserQuickChat.sql"),
		SelectChatUUIDFromQuickMessage("SelectChatUUID.sql"),
		SelectMessageFromChat("SelectMessagesFromChat.sql"),
		SavedUserTableNameAdministrationTable("AdministrationTableSavedUserTableName.sql"),
		CreateUserChatAndAdministration("CreateUserChatAndAdministrationChat.sql"),
		InsertMessageChat("InsertMessageFromUser.sql"),
		InsertMessageChatUser("InsertMessageFromUser.sql"),
		InsertAdministration("InsertOrIgnoreAdministration.sql"),
		InsertIntoQuickMessageWhenUserWriteNewMessage("InsertIntoQuickMessageWhenUserWriteNewMessage.sql")
		,InsertIntoAdministrationTableWhenUserSendNewMessage("InsertIntoAdministrationTableWhenUserSendNewMessage.sql")
		,FindChatInHistory("FindChatInHostory.sql"),
		SelectFromAdministrationTable("SelectFromAdministrationTable.sql"),
		SelectQuickMessageFromDatabase("SelectMessageToQuickMessage.sql"),
		SavedChatMessage("InsertChatMessage.sql"),
		SelectChatName("SelectChatName.sql");		
		
		private String URLQuery;
		databaseTaskType(String UURL){
			this.URLQuery=UURL;
		}
		public static final String path="SQL/";

		@Override
		public String toString() {
			return this.path+this.URLQuery;
			//return Main.Main.getFileWithAbsolutePath(path+this.URLQuery);
		}
	}
	
	
	@Override
	public Connection CreateDatabaseConnection() throws SQLException {
		// TODO Auto-generated method stub
		return con;
	}

	
}
