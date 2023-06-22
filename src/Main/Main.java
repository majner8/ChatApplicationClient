package Main;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
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
	private static String UserDefaultName;
	public static String getLogginUserDefaultName() {
		return UserDefaultName;
		
	}
	
	
	static {
		String x;
		try {
			String fileURl= Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
			
			x = Path=(new File(fileURl)).getParent();

		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Main.stopServer(null);
		}
	}
	private static String Path;
	
	
	public static String getFileWithAbsolutePath(String path) {
		return Path+"/"+path;
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
		public String toString() {
			return this.mes;
		}
		
		
	}
}
