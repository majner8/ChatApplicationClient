package FrontEnd;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import CommonPart.SocketComunication.SocketComunication.OneSocketMessage;
import CommonPart.ThreadManagement.ThreadPoolingManagement;
import FrontEnd.ComponentLanguageName.TypeLanguage;
import Main.ComunicationWithServer;


public class MainJFrame extends JFrame{

	public static final String ImgBacgroundURL="Img\\FirstView.jpeg";
	public static Image ImgBacgtround=MainJFrame.loadImage(ImgBacgroundURL);
	public final static Dimension minimunSize=new Dimension(300,300);
	public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	public static TypeLanguage language;
	public static volatile boolean AutorizationFinish=false;
	public MainJFrame() {
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		super.setMinimumSize(new Dimension(500,400));
        super.setLayout(new BorderLayout());
        this.frame=this;
        frame.setVisible(true);
        super.add(new ConnectionLanguageChoosePanel(),BorderLayout.CENTER);
	}
	
	public static MainJFrame frame;

	

	public static void AutorizateProcess(TypeLanguage typ) {
		
			MainJFrame.language=typ;

			    frame.getContentPane().removeAll();

				AutorizateProcessPanel panel=new AutorizateProcessPanel();
				frame.add(panel,BorderLayout.CENTER);
				frame.revalidate();
				frame.repaint();
			


       
	}
	

	public static Image loadImage(String url) {
		File x=new File(url);
		
		    Image img;
			try {
				img = ImageIO.read(x);
			    if(img==null) {

			    }
			    return img;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}	
	
	public void AutorizationComplete(OneSocketMessage userName) {
	
	SwingUtilities.invokeLater(()->{
		Main.Main.setFrameTitle(userName.getOneValue(), true);
			//super.setTitle(ComponentLanguageName.UserTitleJFrame.getName(language).toString()+userName.getOneValue());
			this.AutorizationFinish=true;
			super.getContentPane().removeAll();
			super.add(new ChatManagerMain(),BorderLayout.CENTER);
			super.revalidate();
			super.repaint();
			
		});
	
	}
	

	

	public static void main(String []args) {
		new MainJFrame();
	}

	public class ConnectionLanguageChoosePanel extends JPanel{
		private static ConnectionLanguageChoosePanel Panel;
		private static boolean stopAnimation=false;

		/**Metod noticifed Jframe if a attemp of connection was succesful */
		public static void Connected(boolean connectedSucessful) {
			stopAnimation=true;
			if(!connectedSucessful) {
				//conection was not Succesfull
				frame.dispose();
				ThreadPoolingManagement.ShutDown();
				String message = "Could not connect to server. Check your internet connection and try again later.";
		        
		        // Display the message in a dialog
		        JOptionPane.showMessageDialog(null, message, "Connection Error", JOptionPane.ERROR_MESSAGE);
		        Main.Main.stopServer(null);
		        return;
			}
			//connection was succesfull ask USer To select language
			 Panel.SetLanguageOption();
		}
		public ConnectionLanguageChoosePanel() {
			
			super.setOpaque(true);
	        super.setLayout(new GridBagLayout());
	        super.add(new WaitingForConnection());
	        this.Panel=this;
		}
		private void SetLanguageOption() {
			GridBagConstraints c=new GridBagConstraints();
		    c.fill = GridBagConstraints.HORIZONTAL; // Make components fill their grid horizontally

			c.gridheight=1;
			c.gridwidth=1;
				super.removeAll();
				JComboBox<String> languageComboBox;

		        languageComboBox = new JComboBox<>();
		        languageComboBox.addItem("Choose language");
		        for(TypeLanguage x:TypeLanguage.values()) {
		        	languageComboBox.addItem(x.name());
		        }
	
		        // Add more languages as needed...

		        // Add a label and the JComboBox to the panel
		        c.gridx=0;
		        Panel.add(languageComboBox,c);
		        JButton Continue=new JButton("Continue");
		        Continue.addActionListener((ActionListener)->{
		        	
		        	String x=(String)languageComboBox.getSelectedItem();
		        	if(x.equals("Choose language")){
		        	return;
		        }
		        	MainJFrame.AutorizateProcess(TypeLanguage.valueOf((String)languageComboBox.getSelectedItem()));
		        	Panel=null;
		        });
		        c.gridx = 0;
		        c.gridy = 1;
		        c.gridheight = 1; // Span one row
		        c.gridwidth = 1; // Span two columns
		        super.add(Continue,c);
		        super.repaint();
		        super.revalidate();
		}
		private class WaitingForConnection extends JLabel{
			private  static String defaul="Trying connect to server";
			public WaitingForConnection() {
				
				ConcurrentLinkedQueue<String> animation=new ConcurrentLinkedQueue<String>();
				//init queue with text
				{
					String character=".";
					String gap=" ";
					int AmountAnimation=6;

					for(int i=0;i<=AmountAnimation;i++) {
						animation.add(character.repeat(i)+gap.repeat(AmountAnimation-i));
					}

				}
				super.setVisible(true);
				super.setOpaque(false);
				super.setHorizontalAlignment(JLabel.CENTER);
				Font font = super.getFont();
				super.setFont(font.deriveFont(26f));  
				Thread Animation=new Thread(()->{
					while(!stopAnimation) {
						try {
							String text=animation.poll();
							animation.add(text);
							super.setText(defaul+text);
							Thread.sleep(300);

						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
					defaul=null;
				});
				Animation.setDaemon(true);
				Animation.start();
			}
			
		
			
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			g.drawImage(ImgBacgtround, 0, 0, super.getWidth(), super.getHeight(), this);
		}
	}
}
