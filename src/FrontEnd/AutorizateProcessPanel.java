package FrontEnd;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Backend.TextFieldPlaceHolder;
import Backend.TextFieldPlaceHolder.PasswordTextField;
import CommonPart.SQL.MainSQL;
import CommonPart.SocketComunication.ComunicationPortHandling;
import CommonPart.SocketComunication.SocketComunication;
import CommonPart.SocketComunication.SocketComunication.OneSocketMessage;
import CommonPart.SocketComunication.SocketComunication.SimpleResultSet;
import FrontEnd.AutorizateProcessPanel.TypeOFCard;
import Main.ComunicationWithServer;



public class AutorizateProcessPanel extends JPanel{
	
	public static enum TypeOFCard{
		EntryPanel,Loggin,Register,FinalRegistration;
	}
	private final CardLayout layout;
	public AutorizateProcessPanel() {
		this.layout=new CardLayout();
		super.setLayout(this.layout);
		super.add(new introductoryPanel(),TypeOFCard.EntryPanel.name());
		super.setVisible(true);
		super.setOpaque(true);
		super.add(new Register(this),TypeOFCard.Register.name());
		super.add(new Loggin(this),TypeOFCard.Loggin.name());

		this.changePanel(TypeOFCard.EntryPanel,null);
	}
	

	private volatile TypeOFCard actualCard;
	public synchronized void changePanel(TypeOFCard newtypeCard,TypeOFCard callFrom) {
		if(actualCard==callFrom||this.actualCard==null) {
			this.actualCard=newtypeCard;
			this.layout.show(this, newtypeCard.name());
			super.revalidate();
			super.repaint();
		}
		
	}
	public synchronized void FinishRegistration(FrontEnd.LogginRegisterPanel.WrongPanel leftCountWrongPanel,FrontEnd.LogginRegisterPanel.WrongPanel wrongPanel) {
		
		super.removeAll();
		super.add(new FinishRegistrationPanel(leftCountWrongPanel, wrongPanel));
		super.revalidate();
		super.repaint();
	}
	
	private class introductoryPanel extends JPanel{

		
		private JButton Register, Loggin;
		
		public introductoryPanel( ) {
			super.setPreferredSize(MainJFrame.minimunSize);
			super.setVisible(true);
			super.setOpaque(false);
			super.setLayout(new GridBagLayout());
			this.initButton();
		}

	
		private void initButton() {
			GridBagConstraints c=new GridBagConstraints();
			c.insets=new Insets(0,0,0,0);
			c.gridheight=1;
			c.gridwidth=1;
			c.gridx=0;
			c.gridy=0;
			this.Loggin = new JButton("Login");
			this.Loggin.setOpaque(false);
			super.add(this.Loggin,c);

			this.Loggin.setVisible(true);
			this.Loggin.addActionListener((ActionEvent)->{
				//processUserLogginClick
				changePanel(TypeOFCard.Loggin,TypeOFCard.EntryPanel);
			});
			
			c.gridx=1;
			this.Register = new JButton("Register");
			super.add(this.Register,c);
			this.Register.setVisible(true);
			this.Register.addActionListener((ActionEvent)->{
				//processUserRegisterClick
				changePanel(TypeOFCard.Register,TypeOFCard.EntryPanel);
			});


		}
		
}



		private static class Register extends LogginRegisterPanel{

			public Register(AutorizateProcessPanel AutPanel) {
				super(AutPanel,TypeOFCard.Register);
				// TODO Auto-generated constructor stub
			}

			@Override
			protected JButton InitLogginRegisterButton() {
				// TODO Auto-generated method stub
				JButton But=new JButton(ComponentLanguageName.RegisterButton.getName(MainJFrame.language).toString());
			
				But.addActionListener((ActionEvent)->{
					super.MakeRegistration();
				});
				return But;
			}

			@Override
			protected PasswordTextField InitPasswordFieldAgain() {
				// TODO Auto-generated method stub
				PasswordTextField pas=new TextFieldPlaceHolder(ComponentLanguageName.PasswordText.getName(MainJFrame.language).toString()).new PasswordTextField();		
				return pas;
			}
			
		}
		private class Loggin extends LogginRegisterPanel{

			public Loggin(AutorizateProcessPanel AutPanel) {
				super(AutPanel,TypeOFCard.Loggin);
				// TODO Auto-generated constructor stub
			}

			@Override
			protected JButton InitLogginRegisterButton() {
				// TODO Auto-generated method stub
				JButton But=new JButton(ComponentLanguageName.LoginButton.getName(MainJFrame.language).toString());
				But.addActionListener((ActionEvent)->{
					super.makeLoggin();
				});
				return But;
			}

			@Override
			protected PasswordTextField InitPasswordFieldAgain() {
				// TODO Auto-generated method stub
				return null;
			}
			
		}
	
		
		@Override
		protected void paintComponent(Graphics g) {
			if(MainJFrame.ImgBacgtround==null) {
				//try to load
			}

			g.drawImage(MainJFrame.ImgBacgtround, 0, 0, super.getWidth(), super.getHeight(), this);
		}
	


}


abstract  class LogginRegisterPanel extends JPanel implements MouseListener{
	//	private JLabelOwn WrongEmailText,WrongPasswordText;
	
		private static boolean LogginRegistrationProcess=false;
		private  AutorizateProcessPanel autPanel;
		private JButton BackButton, LogginRegisterButton;
		private Backend.TextFieldPlaceHolder.TextField EmailTextField;
		private Backend.TextFieldPlaceHolder.PasswordTextField PasswordText, PasswordTextAgain = null;
		private final GridBagConstraints gridCon;
		private final GridBagLayout layout;
		private final WrongPanel wrongPanel,CountWrongPanel;
		

		
		
		public LogginRegisterPanel(AutorizateProcessPanel AutPanel,TypeOFCard actualCard) {
			this.autPanel=AutPanel;
			this.layout=new GridBagLayout();
			super.setLayout(layout);
			this.gridCon=new GridBagConstraints();
			this.gridCon.gridheight=1;
			this.gridCon.gridwidth=2;
			this.gridCon.gridx=1;
			this.gridCon.fill=GridBagConstraints.HORIZONTAL;
			this.gridCon.anchor=GridBagConstraints.CENTER;
			int Y=0;
			gridCon.gridy = Y;			
			super.setOpaque(false);
			this.EmailTextField=(new TextFieldPlaceHolder(ComponentLanguageName.EmailText.getName(MainJFrame.language).toString()).new TextField());
			this.EmailTextField.addMouseListener(this);
			this.addComponent(this.EmailTextField, gridCon);
			this.PasswordText=(new TextFieldPlaceHolder(ComponentLanguageName.PasswordText.getName(MainJFrame.language).toString()).new PasswordTextField());
			this.PasswordText.addMouseListener(this);
			this.gridCon.gridy++;
			this.addComponent(this.PasswordText, this.gridCon);
			if((this.PasswordTextAgain=this.InitPasswordFieldAgain())!=null) {
					this.gridCon.gridy++;
					this.addComponent(this.PasswordTextAgain, this.gridCon);
					this.PasswordTextAgain.addMouseListener(this);
			}
			gridCon.gridy++;
			gridCon.gridwidth = 1;
			this.BackButton=new JButton(ComponentLanguageName.AuthorizeProcessBackButton.getName(MainJFrame.language).toString());
			this.BackButton.setVisible(true);
			super.add(this.BackButton,this.gridCon);
			this.BackButton.addActionListener((ActionEvet)->{
				if(!LogginRegistrationProcess) {
				this.autPanel.changePanel(AutorizateProcessPanel.TypeOFCard.EntryPanel,actualCard);
				}
			});
			
			gridCon.gridx = 2;
			this.LogginRegisterButton=this.InitLogginRegisterButton();
			super.add(this.LogginRegisterButton,this.gridCon);
			this.LogginRegisterButton.setVisible(true);
			//inicializate wrong panel
			this.gridCon.fill=GridBagConstraints.VERTICAL;

			gridCon.gridheight=gridCon.gridy;
			gridCon.gridwidth=1;
			gridCon.gridy=0;
			gridCon.gridx=3;
			this.wrongPanel=new WrongPanel(false);
			super.add(this.wrongPanel,gridCon);
			gridCon.gridx=0;
			this.CountWrongPanel=new WrongPanel(true);
			super.add(this.CountWrongPanel,gridCon);
			this.previousColor=this.EmailTextField.getForeground();
			
		}
		protected abstract JButton InitLogginRegisterButton();
		protected abstract PasswordTextField InitPasswordFieldAgain();
		//Email,PhonePreflix,Phone,passwords,UUID
		protected void MakeRegistration() {
			if(!LogginRegistrationProcess) {
					if(!this.EmailTextField.isValidEmail()) {
						//email is not valid
						//noticife user
						this.HighlightTextField(false);
						this.wrongPanel.ShowWrongText(ComponentLanguageName.InvalidEmail);
						return;
					}
					if(!this.PasswordText.isSame(this.PasswordTextAgain)) {
						this.PasswordText.setForeground(Color.RED);
						if(this.PasswordTextAgain!=null) {
							this.PasswordTextAgain.setForeground(Color.RED);
						}
						this.wrongPanel.ShowWrongText(ComponentLanguageName.PasswordsDoNotMatch);
					}
					
				
				if(this.PasswordText.isSame(this.PasswordTextAgain)&&!this.EmailTextField.PlaceHolderIsEmpty()) {
				
					SimpleResultSet rs=new SimpleResultSet(new ArrayList<String>(Arrays.asList(new String[]{"Email","passwords"})),null);
					rs.addValue(new ArrayList<String>(Arrays.asList(new String[]{this.EmailTextField.getText(),this.PasswordText.getText()})),false);
					OneSocketMessage mes=new OneSocketMessage(SocketComunication.SocketComunicationEnum.Register,rs);
					SocketComunication message=SocketComunication.createNewSocketComunication(null,null);
					message.addOneSocketMessage(mes);
					ComunicationWithServer.SendMessage(message, (ArriveResponce)->{
						
						OneSocketMessage x=ArriveResponce.getMessage(0);
						if(x.getOneValue().equals("null")) {
							//uncorect password/email
							this.HighlightTextField(false);
							this.wrongPanel.ShowWrongText(ComponentLanguageName.EmailIsUsed);
							this.LogginRegistrationProcess=false;
						}
						else {
							//send UserUUID is devided by special character &
							//first character mean UserUUID, other mean deviceUUID
							String[] UUID=x.getOneValue().split(ComunicationPortHandling.DeviceUUIDCharacter);
						
							ComunicationWithServer.Comunication.comun.setUserUUID(UUID[0]);
							ComunicationWithServer.Comunication.comun.setUUIDDevice(UUID[1]);
							//correct move to finish registration
							this.FinishRegistration();
						}
						
					});
				}
			}
		}
		
		private void FinishRegistration() {
			
			autPanel.FinishRegistration(this.CountWrongPanel,this.wrongPanel);
			this.autPanel=null;
		}
		private Color previousColor;
		//	Email=ZZZ and passwords=ZZZ		
		protected void makeLoggin() {
			if(!LogginRegistrationProcess) {
				if(!this.EmailTextField.isValidEmail()) {
					//email is not valid
					//noticife user
					this.HighlightTextField(false);
					this.wrongPanel.ShowWrongText(ComponentLanguageName.InvalidEmail);
					return;
				}
				
				
				if(this.PasswordText.isSame(null)&&!this.EmailTextField.PlaceHolderIsEmpty()) {
					LogginRegistrationProcess=true;
					SimpleResultSet rs=new SimpleResultSet(new ArrayList<String>(Arrays.asList(new String[]{"Email","passwords"})),null);
					rs.addValue(new ArrayList<String>(Arrays.asList(new String[]{this.EmailTextField.getText(),this.PasswordText.getText()})),false);
					OneSocketMessage mes=new OneSocketMessage(SocketComunication.SocketComunicationEnum.Loggin,rs);
					SocketComunication message=SocketComunication.createNewSocketComunication(null,null);
					message.addOneSocketMessage(mes);
					
					ComunicationWithServer.SendMessage(message, (ArrivedResponse)->{
						OneSocketMessage mess=ArrivedResponse.getMessage(0);

						if(mess==null) {
							return;
						}
						if(mess.getOneValue()=="false") {
							//throw runTimeException

							return;
						}
						SimpleResultSet result=mess.getSimpleResultSet();
						String UUID;
						if((UUID=result.getValue("UUIDUser", false, 0)).equals("null")) {
							//uncorect password/orEmail
							this.wrongPanel.ShowWrongText(ComponentLanguageName.IncorrectEmailOrPassword);
							this.HighlightTextField(true);
							this.LogginRegistrationProcess=false;
							return;
						}
						ComunicationWithServer.Comunication.comun.setUserUUID(UUID);
						ComunicationWithServer.Comunication.comun.setUUIDDevice(result.getValue("DeviceUUID", false, 0));
						String value=result.getValue("FinishRegistration", false, 0);
						if(value.equals("1")) {
							//attemp loggin was succesful and also user already complete total registration
							//move to chat part and starting make synchronization
							MainJFrame.frame.AutorizationComplete(ArrivedResponse.getMessage(1));
							return;
						}
						if(value.equals("0")) {
							//attemp loggin was succesful but user do not finish registration
							//move to registrationProcess
							this.FinishRegistration();
							return;
						}
						
					});

				}
			}
		}

		private void HighlightTextField(boolean password) {
			this.previousColor=this.EmailTextField.getForeground();
			this.EmailTextField.setForeground(Color.RED);
			if(password) {
				this.PasswordText.setForeground(Color.RED);
				if(this.PasswordTextAgain!=null) {
					this.PasswordTextAgain.setForeground(Color.RED);
				}
			}
		}
		
		private void addComponent(JComponent com,GridBagConstraints c) {
			super.add(com,c);
		}

		
		public static class WrongPanel extends JPanel {
			private WrongTextJLabel WrongText;

			/**@param balanced- true if component serves as filling a gap and noticife
			 * Layout manager */
			public WrongPanel(boolean balanced) {
				super.setPreferredSize(new Dimension(150,10));
				super.setSize(new Dimension(150,10));
				super.setOpaque(false);
				super.setVisible(true);

				if(balanced) {
					return;
				}							
				BoxLayout x=new BoxLayout(this, BoxLayout.PAGE_AXIS);
				super.setLayout(x);
				this.WrongText=new WrongTextJLabel(ComponentLanguageName.InvalidEmail);
				super.add(WrongText);
				this.previousColor=this.WrongText.getForeground();
				
			}
			private Color previousColor;
			
			public void ResetForeGround() {
				this.WrongText.setForeground(this.previousColor);
				this.WrongText.setText("");
			}
			
			public void ShowWrongText(ComponentLanguageName text) {
				this.previousColor=this.WrongText.getForeground();
				this.WrongText.setForeground(Color.RED);
				String textWithWrap=String.format("<html>%s</html>", text.getName(MainJFrame.language).toString());
				this.WrongText.setText(textWithWrap);
			}
			private static class WrongTextJLabel extends JLabel {
				private ComponentLanguageName text;
				public WrongTextJLabel(ComponentLanguageName text) {
					super.setOpaque(false);
					super.setVisible(true);
					this.text=text;
					
				}
			}
			
		}


		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			this.EmailTextField.setForeground(this.previousColor);
			this.PasswordText.setForeground(this.previousColor);
			if(this.PasswordTextAgain!=null) {
				this.PasswordTextAgain.setForeground(this.previousColor);
			}
			this.wrongPanel.ResetForeGround();
		}
		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			return;
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			return;
		}
		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			return;
		}
		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			return;
		}
		
		


}


final class FinishRegistrationPanel extends JPanel implements MouseListener{
		private Color previousColor;
	
	private JTextField Name,LastName,Born;
	private JButton FinishRegistration;
	public FinishRegistrationPanel(FrontEnd.LogginRegisterPanel.WrongPanel left,FrontEnd.LogginRegisterPanel.WrongPanel Right) {
		super.setVisible(true);
		super.setOpaque(false);
		super.setLayout(new GridBagLayout());
		this.wrongPanel=Right;
		this.initField(left,Right);
	}
	private FrontEnd.LogginRegisterPanel.WrongPanel wrongPanel;
	private void initField(FrontEnd.LogginRegisterPanel.WrongPanel left,FrontEnd.LogginRegisterPanel.WrongPanel Right) {
		GridBagConstraints c=new GridBagConstraints();
		c.fill=GridBagConstraints.HORIZONTAL;

		c.gridheight=1;
		c.gridwidth=1;
		c.gridx=1;
		int y=0;
		c.gridx=1;
		c.gridy=y;
		Name=new TextFieldPlaceHolder(ComponentLanguageName.Name.getName(MainJFrame.language).toString()).new TextField();
		this.previousColor=Name.getForeground();
		this.Name.addMouseListener(this);
		super.add(Name,c);
		y++;
		c.gridy=y;
		LastName=new TextFieldPlaceHolder(ComponentLanguageName.LastName.getName(MainJFrame.language).toString()).new TextField();
		super.add(LastName,c);
		this.LastName.addMouseListener(this);
		y++;
		c.gridy=y;
		Born=new TextFieldPlaceHolder(ComponentLanguageName.Born.getName(MainJFrame.language).toString()).new TextField();
		super.add(Born,c);
		this.Born.addMouseListener(this);
		y++;
		c.gridy=y;
		this.FinishRegistration=new FinishRegistration(ComponentLanguageName.FinishRegistrationButton.getName(MainJFrame.language).toString());
		this.FinishRegistration.setVisible(true);
		super.add(this.FinishRegistration,c);
		
		
		c.fill=GridBagConstraints.VERTICAL;
		c.gridy=0;
		c.gridx=0;
		c.gridheight=y+1;
		super.add(left,c);
		c.gridx=2;
		super.add(Right,c);
		
		
	}
	
 	private class FinishRegistration extends JButton implements ActionListener{
 		public FinishRegistration(String name) {
 			super(name);
			super.addActionListener(this);
			
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			LocalDate date;
			//method chech if a Born is valid by patern DD-MM-YYYY

			try {
			date = LocalDate.parse(Born.getText(), MainSQL.dateFormatter);
			}
			catch(DateTimeParseException ex) {
				//unSuport dataTimeFormater
				Born.setForeground(Color.RED);
				wrongPanel.ShowWrongText(ComponentLanguageName.IncorrectDateFormat);
				return;
			}
			//DateTimeFormater is valid,
			//send value to server
		
			SimpleResultSet rs=new SimpleResultSet(new ArrayList<String>(Arrays.asList(new String[]{"name","LastName","Born"})),null);
			rs.addValue(new ArrayList<String>(Arrays.asList(new String[]{Name.getText(),LastName.getText(),date.toString()})),false);
			OneSocketMessage mes=new OneSocketMessage(SocketComunication.SocketComunicationEnum.FinishRegistration,rs);
			SocketComunication message=SocketComunication.createNewSocketComunication(null,null);
			message.addOneSocketMessage(mes);
			ComunicationWithServer.SendMessage(message, (Responce)->{
				if(Responce.getMessage(0).getOneValue().equals("true")) {
					//Complete
					//start synchronization, also send message to get all 
					//table where is user join, if server 
					MainJFrame.frame.AutorizationComplete(Responce.getMessage(1));
					return;
				}
				//throw exception and end application
			});
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		this.Name.setForeground(this.previousColor);
		this.LastName.setForeground(this.previousColor);
		this.Born.setForeground(this.previousColor);
		this.wrongPanel.ResetForeGround();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}

