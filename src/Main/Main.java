package Main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Set;

import javax.swing.JOptionPane;

import Backend.ClientDatabase;
import CommonPart.SQL.MainSQL.FileLoadException;
import CommonPart.ThreadManagement.ThreadPoolingManagement;
import FrontEnd.ComponentLanguageName;
import FrontEnd.MainJFrame;
import FrontEnd.MainJFrame.ConnectionLanguageChoosePanel;
import Main.ComunicationWithServer.UnConnected;

public class Main {

	
	private volatile static boolean ServerStop=false;
	public static MainJFrame frame;
	public static boolean isServerStopped() {
		return ServerStop;
	}
	
	public static void stopServer(ReasonToEndMessage mes) {
		ServerStop=true;
		if(mes==null) {
			mes=ReasonToEndMessage.UnKnownException;
		}
		
		  // Display the message in a dialog
		ThreadPoolingManagement.ShutDown();

        JOptionPane.showMessageDialog(null, mes.toString(), mes.getTitle(), JOptionPane.ERROR_MESSAGE);
		frame.dispose();

		System.exit(0);
		/* 
		 *ThreadGroup rootGroup = Thread.currentThread().getThreadGroup();
		rootGroup.interrupt();
		ThreadGroup parentGroup;
		while ((parentGroup = rootGroup.getParent())!= null) {
		    rootGroup = parentGroup;
		}
		
		int estimatedSize = rootGroup.activeCount() * 2;
		Thread[] threads = new Thread[estimatedSize];
		int actualSize = rootGroup.enumerate(threads);
		threads = Arrays.copyOf(threads, actualSize);

		// Now you can interrupt all threads
		for (Thread thread : threads) {
		    thread.interrupt();
		}
		 */







	}
	
	public static void stopServer(ReasonToEndMessage mes,Exception e) {
		ServerStop=true;
		if(mes==null) {
			mes=ReasonToEndMessage.UnKnownException;
		}
		  // Display the message in a dialog
		ThreadPoolingManagement.ShutDown();

        JOptionPane.showMessageDialog(null, mes.getString(e), mes.getTitle(), JOptionPane.ERROR_MESSAGE);
		frame.dispose();

		System.exit(0);
		
	}
	
	private static void init() {
					try {
						new ClientDatabase();
						frame=new MainJFrame();
						ComunicationWithServer.InitConnection();
						ConnectionLanguageChoosePanel.Connected(true);
					} catch (UnConnected e) {
						// TODO Auto-generated catch block
						ConnectionLanguageChoosePanel.Connected(false);
					} catch (FileLoadException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		
	}
	
	/**Metod setTittle, and also set userdefaultName if you add 
	 * @param logginUserPreflix as true
	 * -----------------------
	 * @param logginUserPreflix true if you want to add loggin preflix before */
	public static void setFrameTitle(String TittleMessage,boolean logginUserPreflix) {
		if(logginUserPreflix) {
			UserDefaultName=TittleMessage;
			frame.setTitle(ComponentLanguageName.UserTitleJFrame.getName(MainJFrame.language).toString()+TittleMessage);
			return;
		}
		frame.setTitle(TittleMessage);


	}
	
	public static void main(String[] args) {
		ThreadPoolingManagementClient x=new ThreadPoolingManagementClient();
		init();
		x.initDatabase();
		
	}
	
	/**Metod make instalation of database and return Working path of this database */
	public static String MakeInstalationOfDatabase() throws IOException {
		 // Get the current directory.
        String currentDirectory = System.getProperty("user.dir");
        currentDirectory=currentDirectory.replace('\\', '/');
        // Define the path where you want to store the database file.
        Path dbFile = Paths.get(currentDirectory, "my_database.db");

	        // Check if the file already exists.
	        if (!Files.exists(dbFile)) {
	        	Files.createFile(dbFile);

	        	// Get an InputStream to the database file.
	            InputStream dbFileStream = Main.class.getClassLoader().getResourceAsStream("my_database.db");
	            // Copy the database file from the InputStream to the defined path.
	            Files.copy(dbFileStream, dbFile, StandardCopyOption.REPLACE_EXISTING);
	        }
	        return dbFile.toString();
	}
	private static String UserDefaultName;
	public static String getLogginUserDefaultName() {
		return UserDefaultName;
		
	}
	
	
	
	
	
	
	
	//notification when user make logOut
	public static interface UserLogOut{
		public void LogOut();
	}
	
	public static enum ReasonToEndMessage{
		CoouldNotConnectWithServer("Could not connect to server. Check your internet connection and try again later.","Connection error"),
		EndConnection("I have lost connection with server, please try it again,\n App will be closed after you close this alert","Connection error"),
		UnKnownException("Unknown exception","Unknown exception"),
		IntegrityProblem("Cannot load necessary file, please reinstal app.\n"
				+ "IF problem still persist contact developer","Integrity file problem");
		
		String mes;
		String title;
		ReasonToEndMessage(String mes,String title){
			this.mes=mes;
			this.title=title;
		}
		public String getTitle() {
			return this.title;
		}
		/**Metod return String text with exception message */
		public String getString(Exception Ex) {
			return this.mes+"\n"+Ex.toString();
		}
		
		public String toString() {
			return this.mes;
		}
		
		
	}
}
