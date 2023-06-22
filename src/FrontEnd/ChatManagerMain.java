package FrontEnd;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import Backend.ClientDatabase;
import Backend.TextFieldPlaceHolder;
import CommonPart.SQL.MainSQL;
import CommonPart.SQL.MainSQL.Query;
import CommonPart.SocketComunication.SocketComunication;
import CommonPart.SocketComunication.SocketComunication.OneSocketMessage;
import CommonPart.SocketComunication.SocketComunication.SimpleResultSet;
import CommonPart.ThreadManagement.ThreadPoolingManagement;
import FrontEnd.ComponentLanguageName.TypeLanguage;
import FrontEnd.MainJFrame.ConnectionLanguageChoosePanel;
import Main.ComunicationWithServer;

public class ChatManagerMain extends JPanel {

//	public static String defaultUserName="ahoj";
	
	private static BorderLayout layout;
	private boolean SynchronizationProgress=false;
	public  SynchronizationProcess synClas;
	public static ChatManagerMain MainChat;
	public static DateTimeFormatter formatOfShownDate=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	
	
	public  void StartSynchronization() {
		
		SwingUtilities.invokeLater(()->{

			if(this.synClas!=null) {
				return;
			}
			this.synClas=new SynchronizationProcess();
			super.add(this.synClas,BorderLayout.CENTER);
			
			MainChat.synClas.StartSynchronization();
			MainChat.revalidate();
			MainChat.repaint();
			

			SocketComunication synMes=SocketComunication.createNewSocketComunication(null, null);
			OneSocketMessage mes=new OneSocketMessage(SocketComunication.SocketComunicationEnum.Synchronization, "start");
			synMes.addOneSocketMessage(mes);
			ComunicationWithServer.SendMessage(synMes, null);
		});		
		
	}
	public  void StopSynchronization() {
		if(this.synClas==null) {
			return;
		}
		

		SwingUtilities.invokeLater(()->{
			
			this.SynchronizationProgress=false;
			super.remove(this.synClas);
			this.synClas.interrupt();
			this.synClas=null;
			MainChatPanel x;

			MainQuickMessage xx=new MainQuickMessage(x=new MainChatPanel());

			//super.add(BorderLayout.EAST,xx);
			super.add(xx,BorderLayout.EAST);

			super.add(x,BorderLayout.CENTER);
			super.revalidate();
			super.repaint();
			
		});

	}

	
	/**Metod return chatUUID, depends on alphabetical order
	 * ChatUUID is including with client owner UUID+other user */
	public static String returnChatUUID(String otherUserUUID) {
		int result=ComunicationWithServer.Comunication.comun.getUserUUID().compareTo(otherUserUUID);
		return result<0? ComunicationWithServer.Comunication.comun.getUserUUID()+otherUserUUID:
			otherUserUUID+ComunicationWithServer.Comunication.comun.getUserUUID();
		}
	
	public ChatManagerMain() {
		this.MainChat=this;
		this.layout=new BorderLayout();
		super.setLayout(this.layout);
		super.setOpaque(false);
		this.StartSynchronization();
	}
		
	
	


	
	public class SynchronizationProcess extends JPanel{
		
		private final static int AmountOfDot=5;
		private static Thread Animation;

		private  JLabel AnimationJLabel=new JLabel();

		private  String AnimationRawText;
		private  String []AnimationDotText=new String[AmountOfDot];


		public SynchronizationProcess() {

			super.setOpaque(true);
			super.setVisible(true);
	        super.setLayout(new BorderLayout());
	        this.AnimationRawText=ComponentLanguageName.SynchronizationProgress.getName(MainJFrame.language).toString();
	        String dot=".";
	        //special character for html
        	String gap="&nbsp;";
	        for(int i=0;i<AmountOfDot;i++) {
	        	AnimationDotText[i]=dot.repeat(i)+gap.repeat(AmountOfDot-i);
	        }
	        Animation=new Thread() {
				@Override
				public void run() {
					int i=0;
					while(!this.interrupted()&&SynchronizationProgress) {
						AnimationJLabel.setText(String.format(AnimationRawText, AnimationDotText[i]));
						i = i < AmountOfDot - 1 ? i + 1 : 0;
						try {
							this.sleep(600);
							
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
						}
					}
				}
			};
			this.AnimationJLabel.setFont(this.AnimationJLabel.getFont().deriveFont(25F));
			this.AnimationJLabel.setHorizontalAlignment(JLabel.CENTER);
			super.add(AnimationJLabel,BorderLayout.CENTER);

		}
		public void StartSynchronization() {
		
			if(!SynchronizationProgress) {
				SynchronizationProgress=true;
				this.Animation.start();
			}
		}
		
		
		public void interrupt() {
			this.Animation.interrupt();
		}
		@Override
		protected void paintComponent(Graphics g) {
			if(MainJFrame.ImgBacgtround==null) {
				//try to load
			}

			g.drawImage(MainJFrame.ImgBacgtround, 0, 0, super.getWidth(), super.getHeight(), this);
		}
		
		
		/*
		@Override
		protected void paintComponent(Graphics g) {
			g.drawImage(MainJFrame.ImgBacgtround, 0, 0, super.getWidth(), super.getHeight(), this);
		}*/		
	}
	

	public class MainChatPanel extends JPanel {
		public MainChatPanel() {
			super.setOpaque(true);
			super.setVisible(true);
			
			super.setBackground(Color.red);
			super.setLayout(new BorderLayout());
			super.addMouseListener(MainQuickMessage.CleanListener);
		}
		public ChatPanel init() {
			ChatPanel x=new ChatPanel();
			super.add(x,BorderLayout.PAGE_END);
			return x;

		}
		@Override
		protected void paintComponent(Graphics g) {
			if(MainJFrame.ImgBacgtround==null) {
				//try to load
			}

			g.drawImage(MainJFrame.ImgBacgtround, 0, 0, super.getWidth(), super.getHeight(), this);
		}
	}
	
	
}
