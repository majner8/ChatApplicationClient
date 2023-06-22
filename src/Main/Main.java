package Main;

import java.util.Arrays;
import java.util.Set;

import Backend.ClientDatabase;
import CommonPart.SQL.MainSQL.FileLoadException;
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
	
	public static void stopServer(String errorMessage) {
		ServerStop=true;
		System.exit(0);
		ThreadGroup rootGroup = Thread.currentThread().getThreadGroup();
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
	
	
	//notification when user make logOut
	public static interface UserLogOut{
		public void LogOut();
	}
}
