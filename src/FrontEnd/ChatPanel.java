package FrontEnd;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.BorderFactory;
import javax.swing.BoundedRangeModel;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import Backend.ClientDatabase;
import CommonPart.SQL.MainSQL;
import CommonPart.SQL.MainSQL.Query;
import CommonPart.SocketComunication.SocketComunication;
import CommonPart.SocketComunication.SocketComunication.OneSocketMessage;
import CommonPart.SocketComunication.SocketComunication.SimpleResultSet;
import CommonPart.ThreadManagement.ThreadPoolingManagement;
import CommonPart.ThreadManagement.ThreadPoolingManagement.ProcessSQLTask;
import FrontEnd.MainQuickMessage.JScrollPanel;
import FrontEnd.MainQuickMessage.QuickMessageText;
import Main.ComunicationWithServer;
import Main.Main;

public class ChatPanel extends JPanel{

	public static Dimension sizeOfChatPanel=new Dimension(200,300);
	private int defaultYLocationOfChat=0;
	public static ChatPanel panel;
	public static String UserUUID=ComunicationWithServer.Comunication.comun.getUserUUID();
	
	private static JScrollPanel quickPanel;
	public ChatPanel() {
		panel=this;
		super.setPreferredSize(sizeOfChatPanel);
		super.setVisible(true);
		super.setOpaque(false);
		super.setBackground(Color.blue);
		super.setLayout(new FlowLayout());
		super.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
	
		super.addMouseListener(MainQuickMessage.CleanListener);

	}
	public void SetScrollPane(JScrollPanel panel) {
		this.quickPanel=panel;
	}
	
	/**
	 * @param chatUUID-UUID  of chat
	 * @param OpenInPrior-if Chat Open user, chat is put as first in order in the
	 *                       other hand, chat is put into queue
	 *@param newChat true-if User just open new UserChat without previous chat interaction */
	public synchronized Chat OpenChat(boolean newChat,String chatUUID,boolean OpenInPrior,String chatName) {
		// verify if a component was not added before
		{
	
			synchronized (super.getComponents()) {
				int indexToRemove=-1;
				for (int i = 0; i < super.getComponents().length; i++) {
				    Component x = super.getComponents()[i];
				    if (x.toString().equals(chatUUID)) {
				        if (OpenInPrior) {
				        	indexToRemove=i;
				           break;
				        }
				        return (Chat) x;
				    }
				}
				if(indexToRemove==0) {
					// chat is on top
					return (Chat)super.getComponent(0);
				}
				if(indexToRemove!=-1) {
					Component x=super.getComponent(indexToRemove); 
					super.remove(indexToRemove);
			            super.add(x, 0);
			            return (Chat) x;
				}

			}
			//if newChat is true, default other UserName is same as table name
			if(newChat) {
				HashMap<String,String>map=new HashMap<String,String>();

				map.put(UserUUID, Main.getLogginUserDefaultName());
				map.put(chatUUID.replace(UserUUID, ""), chatName);
				Chat x=new Chat(chatUUID,newChat,this,chatName,map);

				super.add(x, OpenInPrior ? 0 : super.getComponentCount());

				super.revalidate();
				super.repaint();
				return x;
			}
			else {
				//have to load from database
				Query q[]=MainSQL.getQuery(ClientDatabase.databaseTaskType.SelectFromAdministrationTable, new SimpleResultSet(new ArrayList<String>(),null), chatUUID);
				ThreadPoolingManagement.thread.ProcesSQLTask(q,(Stm,Rs,Ex)->{
					if(Ex!=null) {
						Ex.printStackTrace();
						return;
					}
					//metod loadaed all data
					try {
						HashMap<String,String>map=new HashMap<String,String>();
						while(Rs.next()) {
							String userUUID=Rs.getString("userUUID");
							String userChatName=Rs.getString("chatName");
							map.put(userUUID, userChatName);
						
						}
						MainSQL.ClosedStatement(Stm, Rs);
						Chat x=new Chat(chatUUID,newChat,this,chatName,map);
						super.add(x, OpenInPrior ? 0 : super.getComponentCount());
						super.repaint();
						super.revalidate();
					
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						MainSQL.ClosedStatement(Stm, Rs);
						return;
					}
				});
			}
			
	return null;
		}
	}
	
	/**Metod just manage that new user value will be loaded */
	public void UserChatNameUpdate(String chatUUID) {
		synchronized(super.getComponents()) {
			for(Component x:super.getComponents()) {
				if(x instanceof Chat&&x.equals(chatUUID)) {
					//same chat
					((Chat)x).UpdateChatName();
					return;
				}
			}
		}
	}
	
	public void removeChat(Chat chatToRemove) {
		super.remove(chatToRemove);
		super.repaint();
		super.revalidate();
	}	
		

	
	public static class Chat extends JPanel{
		private final String chatUUID;
		private boolean visitible=false;
		private ChatPanel panel;
		private String chatName;
		private boolean newChat; //when a user open chat with new user, but chat is without interaction-
		private	boolean doesSingleChat=false;
		private final Map<String,String> UserNameInChat;
		public FrontEnd.ChatPanel.Chat.ShownMessage.messageValue messages;
		public Chat(String chatUUID,boolean newChat,ChatPanel panel,String chatName,
			HashMap<String,String>MapWithUserName) {
			UserNameInChat=Collections.synchronizedMap(MapWithUserName);

			this.chatUUID=chatUUID;
			this.newChat=newChat;
			this.panel=panel;
			super.setOpaque(false);
			super.setLayout(new BorderLayout());
			super.setPreferredSize(ChatPanel.sizeOfChatPanel);
			super.add(new ShownMessage(),BorderLayout.CENTER);
			super.add(new WritingPart(),BorderLayout.PAGE_END);
			super.add(new UpperOptionClass(chatName),BorderLayout.PAGE_START);
			
			super.addMouseListener(MainQuickMessage.CleanListener);

		}
		
		private void UpdateChatName() {
			ThreadPoolingManagement.thread.ProcesSQLTask(MainSQL.getQuery(ClientDatabase.databaseTaskType.SelectFromAdministrationTable, new SimpleResultSet(new ArrayList<String>(),null), chatName), (Stm,Res,Ex)->{
				if(Ex!=null) {
					Ex.printStackTrace();
					Main.stopServer(null);
					return;
				}
				try {
					while(Res.next()) {
						this.UserNameInChat.put(Res.getString("userUUID"), Res.getString("chatName"));
						//	userUUID
					//	chatName
						
					}
					MainSQL.ClosedStatement(Stm, Res);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Main.stopServer(null);
				}
			});
		}
		
		
		
		private void CloseChat() {
			panel.removeChat(this);

		}
		/**Notice, if a component is visitible, and can be shown and load new chat
		 * chat is loaded in separate thread 
		 * @param makeVisitible-false if chat is hidden
		 *  */
		public  void Visitible(boolean makeVisitible) {	
			this.messages.Visitible(makeVisitible,false);
			
			
		}
		/**Metod manage creating new chat database, and other think around 
		 * @param ValueWithAllUserInChat- AdministrationTable
		 * @param defaultTableName-default name which will be shown to this user
		 * @param callAfterFinish-Runnable which will be run after metod finish task*/
		public void CreateNewChat(SimpleResultSet ValueWithAllUserInChat,String defaultTableName,Runnable callAfterFinish) {
			Query[] CreateTableTask=MainSQL.getQuery(ClientDatabase.databaseTaskType.CreateUserChatAndAdministration, new SimpleResultSet(new ArrayList<String>(),null), chatUUID);
			
			Query[] InsertIntoAdministrationTable=MainSQL.getQuery(ClientDatabase.databaseTaskType.InsertIntoAdministrationTableWhenUserSendNewMessage, ValueWithAllUserInChat, chatUUID);
			Query[] InsertIntoQuickMessageTable;
			{
				SimpleResultSet x=new SimpleResultSet(new ArrayList<String>(),null);
				x.addNewColumn("chatUUID", false);
				x.addNewColumn("ChatEnd", false);
				x.addNewColumn("UserTableName", false);
				ArrayList<String> value=new ArrayList<String>();
				value.add(chatUUID);
				value.add(String.valueOf(false));
				value.add(defaultTableName);
				x.addValue(value, false);
				InsertIntoQuickMessageTable=MainSQL.getQuery(ClientDatabase.databaseTaskType.InsertIntoQuickMessageWhenUserWriteNewMessage, x, UserUUID);
				
				Query[] xx=Query.ConvertListOFqueryToOne(Arrays.asList(CreateTableTask,InsertIntoAdministrationTable,InsertIntoQuickMessageTable));
		
				ThreadPoolingManagement.thread.ProcesSQLTask(xx, (Stm,Rs,Ex)->{
					MainSQL.ClosedStatement(Stm, Rs);

					if(Ex!=null) {
						Ex.printStackTrace();
						return;
					}
					if(callAfterFinish!=null) {
						callAfterFinish.run();
					}

				});
			}

			
			//chatUUID,ChatEnd,UserTableName
			

		}
		
		
		
		// class where will be writen name
		private class UpperOptionClass extends JPanel{
			public UpperOptionClass(String NameChat) {
				
				chatName=NameChat;
				super.setLayout(new BorderLayout());
		        super.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Add a border

				super.add(new ChatName(NameChat),BorderLayout.CENTER);
				super.setVisible(true);
				super.add(new CloseChatButton(),BorderLayout.EAST);
				super.setOpaque(true);
				super.setBackground(Color.white);
				
				super.addMouseListener(MainQuickMessage.CleanListener);

			}
			private class CloseChatButton extends JButton implements ActionListener{
				private static ImageIcon icon;
				static {
					icon=new ImageIcon(Main.getFileWithAbsolutePath("Img\\CloseChatImg.png"));
					icon=new ImageIcon(icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
					
				}
				public CloseChatButton() {
					
					super.setIcon(icon);
					super.setVisible(true);
					super.addActionListener(this);
					super.setBorderPainted(false);
					super.setContentAreaFilled(false);
					super.setPreferredSize(new Dimension(icon.getIconWidth(),icon.getIconHeight()));
					super.addMouseListener(MainQuickMessage.CleanListener);

				}
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					messages.Visitible(false,true);
				}
			}
			private class ChatName extends JLabel
			{
				private static Font font;

				public ChatName(String chatName) {
					super(chatName);
					if(font==null) {
						font=super.getFont().deriveFont(15F);
					}
					super.setFont(font);
					super.setVisible(true);
					super.addMouseListener(MainQuickMessage.CleanListener);

				}
			}
		}
		
		private class WritingPart extends JScrollPane{
			private final static Dimension DimensionOfChat=new Dimension(ChatPanel.sizeOfChatPanel.width,60);

			public WritingPart() {
				super.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				super.setVisible(true);
				super.setOpaque(true);
				super.setPreferredSize(DimensionOfChat);
		        super.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Add a border

				super.setViewportView(new UserWritingPart());
			}
			
			
			private class UserWritingPart extends JTextArea  implements KeyListener{
				
				public UserWritingPart() {
					super(50,1);
					super.setLineWrap(true);
					super.setWrapStyleWord(true);
					super.setPreferredSize(DimensionOfChat);
					super.addKeyListener(this);
					super.addMouseListener(MainQuickMessage.CleanListener);

				}

				/**Metod process request to send message
				 *@param text-raw message
				 *@param nameOfSenderInChat-Nick in this chat, if nick was not set, put null and will be use default name */
				private void ProcessSendMessageUser(String text) {
					ThreadPoolingManagement.thread.Execute(()->{
						if(newChat) {
							
							//this can be call only for User-ToUserChat, group chat have to be created before it is open
							
							SimpleResultSet x=new SimpleResultSet(new ArrayList<String>(),null);
							x.addNewColumn("userUUID", false);
							x.addNewColumn("chatName", false);
							ArrayList<String>value=new ArrayList<String>();
							value.add(UserUUID);
							value.add(UserNameInChat.get(UserUUID));
							x.addValue(value, false);
							
							//add other User
							value=new ArrayList<String>();
							{
								String otherUUID=chatUUID.replace(UserUUID, "");
								value.add(otherUUID);
								value.add(UserNameInChat.get(otherUUID));
							}x.addValue(value, false);
							//userUUID ,chatName
							CreateNewChat(x,chatName,()->{
								this.ProcessSaveMessageToDatabase(text, true);
							});
						}
						else {
							this.ProcessSaveMessageToDatabase(text, false);
						}
					});
					
				}

				/**Metod process request to send message
				 *@param text-raw message
				 *@param nameOfSenderInChat-Nick in this chat, if nick was not set, put null and will be use default name */
				private void ProcessSaveMessageToDatabase(String Message,boolean wasCreated) {
					//put message to send, id made in separate thread
					
						//if true, server have to create new chat
						//it send back 
						OneSocketMessage FirstMessage=new OneSocketMessage(SocketComunication.SocketComunicationEnum.SendMessage,String.valueOf(wasCreated));	
						//does not have to be synchronized-create chatTAble is call as create if not exist...
						SocketComunication mes=SocketComunication.createNewSocketComunication(chatUUID, null);
						mes.addOneSocketMessage(FirstMessage);

						String SenderUUID=UserUUID;
						String MessageUUID=mes.getUUIDTask();
						LocalDateTime DateTime=null;
						
						ShownMessage.Message GUIMessage=ShownMessage.Message.createNewMessage(SenderUUID, MessageUUID, null,DateTime, Message.trim(),false);
						SimpleResultSet rs=GUIMessage.makeSimpleResultSetFromMessage(chatUUID);
						{
							Query [] q=MainSQL.getQuery(ClientDatabase.databaseTaskType.InsertMessageChatUser, rs, chatUUID);
							ThreadPoolingManagement.thread.ProcesSQLTask(q, (Stm,RS,EX)->{
								if(EX!=null) {
									EX.printStackTrace();
									Main.stopServer(null);
									return;
								}
								MainSQL.ClosedStatement(Stm, RS);
								
							});
						}
						
						OneSocketMessage secondMessage=new OneSocketMessage(SocketComunication.SocketComunicationEnum.SendMessage,rs);
						
					
						
				
						//shown message
						messages.newMessageArrive(GUIMessage,false,false);
						
						
						//put messageTo Send to Server
						mes.addOneSocketMessage(secondMessage);	
						//save message into database, specifically put message to queue, to save into database
						//message is do by single thread, so it not necessary wait until database saved message
						//send message
			
						
						ComunicationWithServer.Comunication.SendMessage(mes, (Responce)->{
							//server send responce, first Message is just UUID
							//other message, if wasCreateChat was true, other Message will be sim
							SimpleResultSet res=Responce.getMessage(0).getSimpleResultSet();
						
							String CurrentUUID=res.getValue("LastGenerateUUID", false, 0);
							LocalDateTime timeOfReceived=
							LocalDateTime.parse(res.getValue("TimeOfMessage", false, 0));
							//chech if Message do not contain second message, which represent administration table
							if(Responce.getMessage(1)!=null) {
								//save administration table, than just call chatUpdate
								ThreadPoolingManagement.thread.ProcesSQLTask(MainSQL.getQuery(ClientDatabase.databaseTaskType.InsertIntoAdministrationTableWhenUserSendNewMessage, Responce.getMessage(1).getSimpleResultSet(), Responce.getUUIDRecipient()), (Stat,Ress,Ex)->{
									if(Ex!=null) {
										Ex.printStackTrace();
										Main.stopServer(null);
										return;
									}
									MainSQL.ClosedStatement(Stat, Ress);
									ChatPanel.panel.UserChatNameUpdate(Responce.getUUIDRecipient());
									messages.MessageArriveToServer(MessageUUID, timeOfReceived, CurrentUUID);
									
								});

							}else {
							
								messages.MessageArriveToServer(MessageUUID, timeOfReceived, CurrentUUID);
							}
							});
					
				}
				
				

				
				
				@Override
				public void keyTyped(KeyEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void keyPressed(KeyEvent e) {
					// TODO Auto-generated method stub
					//iff shift down is false, then do nothing
					
					if(e.getKeyCode()==10) {
						if(super.getText().trim().length()>=MainSQL.MaximumAllowLetterInOneMessage) {
							e.consume();
						}
						if(e.isShiftDown()) {
							//shift is down, add newLine
						super.append("\n");	
						}
						else {
						//user press enter, 
						//have to put message to send
						//consume this event and clear textArea
							
							if(super.getText().trim().length()==0) {
								super.setText(null);
								e.consume();
								return;
							}
							
						String text=super.getText();
						super.setText(null);
						e.consume();
						this.ProcessSendMessageUser(text.trim());
						}
						return;
					}
					

				}

				@Override
				public void keyReleased(KeyEvent e) {
					
					// TODO Auto-generated method stub
					if(super.getText().length()>=MainSQL.MaximumAllowLetterInOneMessage-2) {
						e.consume();
					}
				}

				
				
			}
			
		}
		
		public class ShownMessage extends JScrollPane{
			
			public ShownMessage() {
				super.setViewportView(new messageValue());
		        super.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		        super.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		        super.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Add a border
		        super.getVerticalScrollBar().addAdjustmentListener(new ComListener());
				super.setVisible(true);
		        super.setOpaque(true);
		        super.revalidate();
		        super.repaint();
		        
			}
			private BoundedRangeModel ShownMessage=this.getVerticalScrollBar().getModel();
			private class ComListener implements AdjustmentListener{

				
				
				
				int previousMaximumSize=-1;//max value without extend
				int previousValue=-1;
				@Override
				public void adjustmentValueChanged(AdjustmentEvent e) {
					// TODO Auto-generated method stub
					if(this.previousMaximumSize==-1) {
						this.previousMaximumSize=this.Maximum();
					}
					if(this.previousValue==-1) {
						this.previousValue=ShownMessage.getValue();
					}
					
		
					if(this.previousMaximumSize!=this.Maximum()&&this.previousMaximumSize==ShownMessage.getValue()) {
						//maximum size change and also value change
						ShownMessage.setValue(Maximum());
					}
					this.previousMaximumSize=this.Maximum();

					
				}
				private int Maximum() {
				
					return ShownMessage.getMaximum()-ShownMessage.getExtent();
				}
			}
			
		
			public class messageValue extends JPanel{
				
				private Map<String,MessageWithSpaceAround>listOFAddedMessage=Collections.synchronizedMap(new HashMap<String,MessageWithSpaceAround>());
				
				
				public messageValue() {
					super.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
					super.setVisible(true);
					super.setOpaque(true);
					messages=this;
					super.addMouseListener(MainQuickMessage.CleanListener);

				}
				
				/**Metod remove all component in chat, or load new message
				 * @param TemporaryRemoved-true if user closed chat, 
				 * if you put false, message will be just removed and chat will be put into hidden List*/
				private synchronized void Visitible(boolean makeVisitible,boolean TemporaryRemoved) {
					if(makeVisitible==visitible) {
						return;
					}
					visitible=makeVisitible;
					ThreadPoolingManagement.thread.Execute(()->{
						synchronized(this) {
							if(makeVisitible) {
								//load new messages
								this.LoadNewMessage(null);
								
							}
							else {
								SwingUtilities.invokeLater(()->{
									super.removeAll();
									this.listOFAddedMessage.clear();
									if(TemporaryRemoved) {
										CloseChat();
									}
									//CloseChat();
									//just remove all message
								});
			
							}
						}
					});
				}
				
				//when a user ask database for new value, set this value
				//prevent to ask database multipletimes
				private String WaitingForDatabaseResponceMessage;
				/**Metod load new Message from database and put them into panel
				 * @param UUIDLastKnownMessage last known messages, if you want to load new chat
				 * put null */
				private void LoadNewMessage(String UUIDLastKnownMessage) {

						//call task which return 15 message
						
					if(newChat) {
						//new chat true-mean that chat do not have any history
						return;
					}
					Query x[]=MainSQL.getQuery(ClientDatabase.databaseTaskType.SelectMessageFromChat, null, chatUUID);
					ThreadPoolingManagement.thread.ProcesSQLTask(x, (Statement,ResultSet,SQLEx)->{
						if(SQLEx!=null) {
							SQLEx.printStackTrace();
							Main.stopServer(null);
						}
						try {

							while(ResultSet.next()) {
								//    c.numberOFmessage
							    //c.MessageUUID
								String messageUUID=ResultSet.getString("numberOFmessage");
								boolean messageArive=true;
								if(messageUUID==null) {
									messageArive=false;
								}
								Message mes=Message.createNewMessage(ResultSet.getString("senderUUID"), ResultSet.getString("MessageUUID"),ResultSet.getString("numberOFmessage"),ResultSet.getTimestamp("TimeOfMessage").toLocalDateTime(), ResultSet.getString("message"),true);
								this.newMessageArrive(mes, messageArive, true);
							}
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							Main.stopServer(null);
						}
					});
						
				}

				
				private static Dimension gapBetweenMessage=new Dimension(0,15);
				private int messageOnBottom;
				/**Call metod when you want to render new message, or change message which wass added before
				 * Metod show new message, or replace previous one 
				 * @param Message- Message to show
				 * @param WasDeliveredToServer- false- Message will be always shown on the bottom of JPanel
				 * @param addTopOfPanel true if you want to add new message with index 0*/
				public void newMessageArrive(Message message,boolean wasDeliveredToServer,boolean addTopOfPanel) {
			
					SwingUtilities.invokeLater(()->{
						MessageWithSpaceAround previousMessage=null;
						MessageWithSpaceAround newMessage=new MessageWithSpaceAround(message);
					
						newMessage.SetMessageSenderName(UserNameInChat);

						if((previousMessage=this.listOFAddedMessage.get(message.MessageUUID))!=null){
							//change message, previous message will be replaced
							this.listOFAddedMessage.remove(message.MessageUUID);
							int index=super.getComponentZOrder(previousMessage);
							super.remove(index);
							super.add(newMessage,index);
							this.listOFAddedMessage.remove(message.MessageUUID);
							this.listOFAddedMessage.put(message.MessageUUID, newMessage);
							
						}
						else {
							this.listOFAddedMessage.put(message.MessageUUID, newMessage);
							if(addTopOfPanel) {
								super.add(newMessage,0);
							}
							else if(wasDeliveredToServer) {
								//will be put to bottom, but after messageWhich was not delivered
								super.add(newMessage,super.getComponentCount()-this.messageOnBottom);
								this.listOFAddedMessage.put(message.MessageUUID, newMessage);
							}
							else {

								/*
								for(Component x:super.getComponents()) {
									if(x instanceof MessageWithSpaceAround) {
										System.out.println("true");
										continue;
									}
									super.remove(x);
									System.out.println("false");

								}*/
								super.add(newMessage);
								this.messageOnBottom=this.messageOnBottom+1;
							}
						}
					
						if(!message.wasCreatedFromDatabase) {

							//add new quickMessage
							QuickMessageText x=message.makeQuickMessageTextFromMessage(message.timeOfReceived.timeOfMessage,chatName,panel,chatUUID,doesSingleChat);
							//String chatName,ChatPanel chat,String chatUUID,boolean doesSingleChat
							quickPanel.addValue(x);
								
						}
						//add extra panel, to reach gap between each component
						//super.add(Box.createRigidArea(this.gapBetweenMessage));
						super.revalidate();
						super.repaint();
					
					});
					
					
				}

				
				/**Call when client get responce, that sent message successfully arrive to server *
				 * @param previousUUIDMessage- UUID which message get before send
				 * @param time-time of process message in server side
				 * @param CurrentUUIDMessage-Unique UUID-which message got after process on server*/
				public void MessageArriveToServer(String previousUUIDMessage,LocalDateTime time,String CurrentUUIDMessage) {
					SwingUtilities.invokeLater(()->{
						this.messageOnBottom=this.messageOnBottom-1;
						MessageWithSpaceAround x=this.listOFAddedMessage.get(previousUUIDMessage);
						x.messagePanel.MessageArriveToServer(CurrentUUIDMessage, time);
						this.listOFAddedMessage.remove(previousUUIDMessage);
						this.listOFAddedMessage.put(CurrentUUIDMessage, x);
						
					});
				}
			
			}
		
			public static class MessageWithSpaceAround extends JPanel{
				
				private Message messagePanel;
				public MessageWithSpaceAround(Message panel) {
					//set border
					super.setBorder(BorderFactory.createEmptyBorder(40, 0,0,0));
					this.messagePanel=panel;
					super.setLayout(new BorderLayout());
					super.add(panel,BorderLayout.CENTER);
					super.add(new ExtraPanel(),this.SenderIsOwner(panel.SenderUUID)?BorderLayout.LINE_START:BorderLayout.LINE_END);
					super.addMouseListener(MainQuickMessage.CleanListener);

				}
				public void SetMessageSenderName(Map<String,String> mapWithName) {
					this.messagePanel.SetSenderName(mapWithName);
				};
				private class ExtraPanel extends JPanel{
					public ExtraPanel() {
						super.addMouseListener(MainQuickMessage.CleanListener);

						super.setPreferredSize(new Dimension(sizeOfChatPanel.width/3,5));
					}
				}
				public static boolean SenderIsOwner(String sender) {
			
					return sender.equals(ComunicationWithServer.Comunication.comun.getUserUUID());
				}
				
			}
			//one object represent one message
			public static class Message extends JPanel{
				private String SenderUUID;
				private String MessageUUID;
				private String NumberOfMessage;// represent main UUID when user getBackResponce
				private Sender senderPanel;
				private String message;
				private boolean wasCreatedFromDatabase;
				private QuickMessageText QuickMessageFromThisMessage;
				//is align to left side, in other hand it is align to right
				
				/**Metod create new Message object
				 * @param SenderUUID UUID of Sender,name will be get from collection
				 * @param timeOfReceived- Time of received, if message has not been process yet, put null
				 * @param UUIDSender- UUID of sender-if UUID sender is same as UUID User, message will be align to right
				 * in the other hand, will be align left
				 * @param NumberOfMessage-number of message, or put null if message was not send to server
				 * @param message-message to show
				 * @param wasCreatedFromDatabase*/
				public static Message createNewMessage(String SenderUUID,String MessageUUID,String NumberOfMessage,LocalDateTime timeOfReceived,String message,boolean wasCreatedFromDatabase) {
					return new Message(SenderUUID,MessageUUID,NumberOfMessage,timeOfReceived,message,wasCreatedFromDatabase);
					//wasCreatedFromDatabase-if true send message do not trigger quick message
				}
				
				public Message(String SenderUUID,String messageUUID,String NumberOfMessage,LocalDateTime timeOfReceived,String message,boolean wasCreatedFromDatabase) {
					super(new GridBagLayout());  // use GridBagLayout for this JPanel
					this.wasCreatedFromDatabase=wasCreatedFromDatabase;
					this.SenderUUID=SenderUUID;
					this.NumberOfMessage=NumberOfMessage;
					super.setMaximumSize(new Dimension(sizeOfChatPanel.width/3*2,Integer.MAX_VALUE));
				        GridBagConstraints gbc = new GridBagConstraints();
				        gbc.gridwidth = GridBagConstraints.REMAINDER;
				        gbc.weightx = 1;
				        gbc.fill = GridBagConstraints.HORIZONTAL;
				        this.MessageUUID=messageUUID;
				        super.add(this.senderPanel=new Sender(), gbc);  // add Sender to top row
				        gbc.gridy=1;
				        super.add(new textMessage(message), gbc);  // add textMessage to second row
				        this.timeOfReceived = new TimeOfReceivedMessage(timeOfReceived);
				        gbc.gridy=2;
				        this.message=message;
				        super.add(this.timeOfReceived, gbc);  // add TimeOfReceivedMessage to third row
				        super.setVisible(true);			
						super.addMouseListener(MainQuickMessage.CleanListener);

				}
				
				
				private TimeOfReceivedMessage timeOfReceived;
				
				/**Call when message arrive to server */
				private void MessageArriveToServer(String UUID,LocalDateTime timeOfArrive) {
					this.NumberOfMessage=UUID;
					this.timeOfReceived.setNewTimeOfMessage(timeOfArrive);
					String QuickText=this.generateTextToQuickMessage(this.QuickMessageFromThisMessage.ChatName);
					this.QuickMessageFromThisMessage.MessageArrive(timeOfArrive, QuickText);				
				}
				
				public static Message createMessageFromSimpleResultSet(SimpleResultSet result,boolean wasCreatedFromDatabase) {

					String SenderUUID=result.getValue("userUUID", false, 0);
					
					LocalDateTime timeOfReceived=LocalDateTime.parse(result.getValue("TimeOfMessage", false, 0));
					String message=result.getValue("message", false, 0);
					Message x=Message.createNewMessage(SenderUUID, result.getValue("MessageUUID", false, 0),result.getValue("numberOFmessage", false, 0), timeOfReceived, message,wasCreatedFromDatabase);
					return x;
				}
				
				
				
				public SimpleResultSet makeSimpleResultSetFromMessage(String chatUUID) {

					
					//userUUID,message,TimeOfMessage,numberOFmessage,MessageUUID
					ArrayList<String>RowColumnName=new ArrayList<String>();
					RowColumnName.add("userUUID");
					RowColumnName.add("message");
					RowColumnName.add("TimeOfMessage");
					
					//code which is order of message in global ChatTable
					RowColumnName.add("numberOFmessage");
					//messageUUID generate by server/ when a server process Message
					RowColumnName.add("MessageUUID");
					//including have to be chatUUID
					RowColumnName.add(MainSQL.tableName.toString());
					
					
					//add message to simpleResultSet.
					SimpleResultSet rs=new SimpleResultSet(RowColumnName,null);
					ArrayList<String> value=new ArrayList<String>();
					value.add(SenderUUID);
					value.add(message.trim());
					value.add(this.timeOfReceived==null?null:this.timeOfReceived.toString());
					value.add(this.NumberOfMessage);
					value.add(this.MessageUUID);
					value.add(chatUUID);		
					rs.addValue(value, false);
					return rs;
				}
				
				
				private String generateTextToQuickMessage(String chatName) {
					//string chat name textMessage, TimeStamp
					String message=String.format("%s: %s",this.SenderName,this.message);
				String text=	 MainQuickMessage.HistorySearchChatPanel.ResultPanel.getQuickMessageText(
							chatName, message.substring(0, message.length()>30?30:message.length()),this.timeOfReceived.timeOfMessage==null?null:Timestamp.valueOf(this.timeOfReceived.timeOfMessage) );
				return text;
				}
				/**Metod create quickMessageText from this message 
				 * Metod have to be call after add message to chat
				 *@param chatName-default name of this chat */
				public QuickMessageText makeQuickMessageTextFromMessage(LocalDateTime time,String chatName,ChatPanel chat,String chatUUID,boolean doesSingleChat) {
					return this.QuickMessageFromThisMessage=new QuickMessageText(time,chatName,false,chat,this.generateTextToQuickMessage(chatName),chatUUID,doesSingleChat);
					
				}
				
				private class textMessage extends JTextArea{
					private Font font;
					
					public textMessage(String message) {
						if(font==null) {
							font=super.getFont().deriveFont(14F);
						}
						super.setFont(this.font);
						super.setText(message);
						super.setWrapStyleWord(true);
						super.setLineWrap(true);
						super.setEditable(false);
						super.setVisible(true);
						
						super.addMouseListener(MainQuickMessage.CleanListener);

					
					}
				}
				
				private class TimeOfReceivedMessage extends JLabel{
					private String TimeMessage;
					public LocalDateTime timeOfMessage;
					public TimeOfReceivedMessage(LocalDateTime time) {
						super.addMouseListener(MainQuickMessage.CleanListener);
						this.timeOfMessage=time;
						if(time!=null) {
							//super.setText(time.toString());
							super.setText(time.format(ChatManagerMain.formatOfShownDate));
							this.TimeMessage=super.getText();
						}
						else {
							this.TimeMessage=null;
							super.setText("-----");
						}
						//add animation of sending message
						//later
						super.setVisible(true);
						super.setHorizontalAlignment(JLabel.CENTER);
						
					}
					private void setNewTimeOfMessage(LocalDateTime messageTime) {
						this.timeOfMessage=messageTime;
						this.TimeMessage=messageTime.format(ChatManagerMain.formatOfShownDate);
						SwingUtilities.invokeLater(()->{
									super.setText(this.TimeMessage);
								
								
						});
						
					}
					public String toString() {
						return this.TimeMessage;
					}
				}
				
				public void SetSenderName(Map<String,String> mapWithName) {
					this.senderPanel.SetSenderName(mapWithName.get(SenderUUID));
				}
				private String SenderName;
				private class Sender extends JLabel {
					
					public Sender() {
						super("");
						super.setHorizontalAlignment(JLabel.CENTER);
						super.addMouseListener(MainQuickMessage.CleanListener);

					}
					public void SetSenderName(String Name) {
						super.setText(Name);
						SenderName=Name;
						super.repaint();
						super.revalidate();
					}
				}
				@Override
				public String toString() {
					return this.NumberOfMessage==null?this.MessageUUID:this.NumberOfMessage;
				}
			}
			
			
		}
		
		
		@Override
		public String toString() {
			return this.chatUUID;
		
		}
	
		
	
		public static class ChatWasNotCreatedRunnTimeException extends RuntimeException{
			
		}

	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		synchronized(super.getComponents()) {
			for(int i=0;i<super.getComponentCount();i++) {
				Component x=super.getComponent(i);
				if(!(x instanceof Chat)) {
					continue;
					}
				Chat xx=(Chat)x;
				if(this.defaultYLocationOfChat==0) {
					this.defaultYLocationOfChat=xx.getLocation().y;
				}
				boolean isVisitible=this.defaultYLocationOfChat==xx.getLocation().y;
				if(isVisitible) {
					//chat is visitible
					if(!xx.visitible) {
						// chat is technically visitible, but it is not loaded
						xx.Visitible(true);
					}
				}
				else {
					//chat is not visitible
					if(!xx.visitible) {
						//if metod reach the first unVisitible component, 
						//it end
						return;
					}
					xx.Visitible(false);
					
					
				}
				}
			
		}
		
		
	}




	
}
