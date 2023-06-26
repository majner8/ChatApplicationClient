package FrontEnd;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

import org.apache.commons.text.similarity.LevenshteinDistance;

import Backend.ClientDatabase;
import Backend.TextFieldPlaceHolder;
import Backend.TextFieldPlaceHolder.TextField;
import CommonPart.SQL.MainSQL;
import CommonPart.SQL.MainSQL.Query;
import CommonPart.SocketComunication.SocketComunication;
import CommonPart.SocketComunication.SocketComunication.OneSocketMessage;
import CommonPart.SocketComunication.SocketComunication.SimpleResultSet;
import CommonPart.ThreadManagement.ThreadPoolingManagement;
import FrontEnd.ChatManagerMain.MainChatPanel;
import Main.ComunicationWithServer;
import Main.Main;

public class MainQuickMessage extends JPanel {

	private static final String HistoryChatName="H";
	private static final String newUserName="W";
	private final CardLayout layout=new CardLayout();
	private ChatPanel chatPanel;
	private final Dimension sizeOFMainQuickPanel=new Dimension(150,100);
	public MainQuickMessage(MainChatPanel mainChat) {
		new CleanQuickMessagePanel();
		super.setLayout(this.layout);
		this.chatPanel=mainChat.init();
		super.setPreferredSize(this.sizeOFMainQuickPanel);
		super.add(new HistorySearchChatPanel(),this.HistoryChatName);
		super.add(new FindNewUserChat(),this.newUserName);
		this.layout.show(this, HistoryChatName);
		
	}
	
	
	
	public void ChangePanel(String nameOfComponent) {
		this.layout.show(this, nameOfComponent);
	}
	
	private  class SearchingPanel extends TextFieldPlaceHolder{

		//
		private TaskForSearch UserWriteSomething;
		private TaskForSearch UserStopWriting;
	//private final Runnable TaskForKeyReleasedEvent;
	protected SearchingPanel(TaskForSearch UserWriteSometingTask,TaskForSearch UserStopWritingTask) {
		super(ComponentLanguageName.FindNewUserTextField.getName(MainJFrame.language).toString());
		this.UserWriteSomething=UserWriteSometingTask;
		this.UserStopWriting=UserStopWritingTask;
		//this.TaskForKeyReleasedEvent=TaskForKeyReleasedEvent;
		//	super(ComponentLanguageName.SearingChatButton.getName(MainJFrame.language).toString(), false);
		// TODO Auto-generated constructor stub
		this.field=new TextField();
	}
	private TextField field;
	private TextField getTextField() {
		return this.field;
	}
	private void init() {
		super.getJTextField().setFont(super.getJTextField().getFont().deriveFont(20F));
	}
	
	
	@Override
	public void keyReleased(KeyEvent e) {
		super.keyReleased(e);
		if(!this.field.PlaceHolderIsEmpty()&&this.UserWriteSomething!=null) {
			//start searing
			this.UserWriteSomething.run(this.getJTextField().getText());
			//ThreadPoolingManagement.thread.Execute(this.UserWriteSomething);
			
			return;
		}
		if(this.UserStopWriting!=null) {
		//ThreadPoolingManagement.thread.Execute(this.UserStopWriting);
			this.UserStopWriting.run(this.getJTextField().getText());

		}
		
		//when user release key, init process of searing
	}

	public static interface TaskForSearch{
		public void run(String searchingTextInput);
	}

}
	
	public JScrollPanel QuickHistoryPanel;
	public class HistorySearchChatPanel extends SearchChatPanel {
		

		private ResultPanel result;
		public HistorySearchChatPanel() {
			super.setVisible(true);
			super.setLayout(new BorderLayout());
			//runnable which is call when user make interact
			this.result=new ResultPanel();
			this.searchPanel= new SearchingPanel(this.result.new StartSearching(),this.result.new StopSearching());
			
			super.add(searchPanel.getTextField(),BorderLayout.PAGE_START);
			searchPanel.init();
			super.add(this.result,BorderLayout.CENTER);
			super.add(new ChatButton(),BorderLayout.PAGE_END);


			
		}
		//middle class which is result panel
		//it is card layout, default here is quick user history
		//when user write something here will be result
		public class ResultPanel extends JPanel{
			private JScrollPanel quickHistory,SearchingUserHistory;
			private CardLayout ResultLayout=new CardLayout();
			private final static String cardNameQuickMessage="Q";
			private final static String cardNameSearching="S";
			public ResultPanel() {
				super.setLayout(ResultLayout);
				QuickHistoryPanel=this.quickHistory=new JScrollPanel(ComponentLanguageName.UserDoesNotHaveHistory);
				this.SearchingUserHistory=new JScrollPanel(ComponentLanguageName.ResultWasNotFound);
				super.add(this.quickHistory,cardNameQuickMessage);
				super.add(this.SearchingUserHistory,cardNameSearching);
				this.quickHistory.addValue(new ArrayList<QuickMessageText>());
				changePanel( cardNameQuickMessage);
				this.LoadNewQuickData();
			}
		
			
			private void LoadNewQuickData() {
				//have to call task which return all table from userTable
				
				Query [] userTableTask=MainSQL.getQuery(ClientDatabase.databaseTaskType.SelectChatUUIDFromQuickMessage, 
						new SimpleResultSet(new ArrayList<String>(),null), ComunicationWithServer.Comunication.comun.getUserUUID());
				
				ThreadPoolingManagement.thread.ProcesSQLTask(userTableTask, (Stm,Res,Ex)->{
					if(Ex!=null) {
						Ex.printStackTrace();
						Main.stopServer(null,Ex);
						return;
					}
				 HashMap<String,String> mapWithTableName=new HashMap<String,String>();
					SimpleResultSet unionTask=new SimpleResultSet(new ArrayList<String>(),new ArrayList<String>());
					unionTask.addNewColumn(MainSQL.tableName.toString(), true);
					try {
						if(!Res.next()) {
							//do not have any history
							return;
						}
						do {
							ArrayList<String> x=new ArrayList<String>();
							x.add(Res.getString("chatUUID"));
							mapWithTableName.put(Res.getString("chatUUID"), Res.getString("UserTableName"));
							unionTask.addValue(x, true);
						}while(Res.next());
						MainSQL.ClosedStatement(Stm, Res);
						//process main union task which return message
						Query [] uninQ=MainSQL.getQuery(ClientDatabase.databaseTaskType.SelectQuickMessageFromDatabase, unionTask, "");
						
						
						ThreadPoolingManagement.thread.ProcesSQLTask(uninQ, (St,Rs,Exe)->{
							if(Exe!=null) {
								Exe.printStackTrace();
								Main.stopServer(null,Exe);
								return;
							}
							try {
								ArrayList<QuickMessageText> mes=new ArrayList<QuickMessageText>();
								
								while(Rs.next()) {
									String message=Rs.getString("QuickMessage");
									Timestamp time=Rs.getTimestamp("TimeOfMessage");
									String ChatUUID=Rs.getString("chatUUID");
									//in future does single chat have to be change
									QuickMessageText x=new QuickMessageText(time.toLocalDateTime(),mapWithTableName.get(ChatUUID),false,chatPanel,this.getQuickMessageText(mapWithTableName.get(ChatUUID),message, time),ChatUUID,true);
									mes.add(x);
									
								}
								MainSQL.ClosedStatement(St, Rs);
								this.quickHistory.addValue(mes);
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								Main.stopServer(null,e);
							}
							
						});
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Main.stopServer(null,e);
					}
					
				});
				
			}
			
			private final static DateTimeFormatter QuickFormater = DateTimeFormatter.ofPattern("HH:mm");
			public static String getQuickMessageText(String chatName,String textMessage, Timestamp timeofreceivedMessage) {
				// Use HTML to align the text
				

				  String time = timeofreceivedMessage==null ?"----":
				  timeofreceivedMessage.toLocalDateTime().format(QuickFormater);
				  /*
				  
				// Use HTML to align the text
				// Use HTML to align the text
				    String labelText = "<html><table width='100%'>" +
				        "<tr><td>" + chatName+"</td>" +
				        "<td style='text-align: left;'>" + textMessage + "</td>" +
				        "<td style='text-align: right;'>" + time + "</td></tr>" +
				        "</table></html>";
					*/
				    String labelText = "<html>" +
				            chatName + "<br>" +
				            "<p style='width: 100px; font-size: 0.6em;'>" + textMessage + "</p>" +
				            "<p style='width: 100px; font-size: 0.4em; text-align: left;'>" + time + "</p>" +
				            "</html>";
					    return labelText;
				    //</tr>

			}
			public void changePanel(String name) {
				ResultLayout.show(this, name);
			}
		
			
			boolean searchingProcess=false;//just notification when user switch panel,
			//which mean new search result would not be add to panel
			private class StartSearching implements SearchingPanel.TaskForSearch{

				@Override
				public void run(String textName) {
					// TODO Auto-generated method stub
					searchingProcess=true;
					
					changePanel(ResultPanel.cardNameSearching);
					ThreadPoolingManagement.thread.Execute(()->{
						SimpleResultSet rs=new SimpleResultSet(new ArrayList<String>(),null);
						rs.addNewColumn("UserTableName", false);
						ArrayList<String> value=new ArrayList<String>();
						value.add(String.format("%%%s%%", textName));
						rs.addValue(value, false);
						Query[] x=MainSQL.getQuery(ClientDatabase.databaseTaskType.FindChatInHistory, rs, ComunicationWithServer.Comunication.comun.getUserUUID());
						ThreadPoolingManagement.thread.ProcesSQLTask(x, (Statement,Result,SQLEx)->{
							if(SQLEx!=null) {
								SQLEx.printStackTrace();
								Main.stopServer(null,SQLEx);
								return;
							}
							ArrayList<QuickMessageText> messageToAdd=new ArrayList<QuickMessageText>();
							try {
								while(Result.next()) {
									QuickMessageText q=new QuickMessageText(null,null,false,chatPanel,Result.getString("UserTableName"),
									Result.getString("chatUUID"),true);
									messageToAdd.add(q);
								}
								MainSQL.ClosedStatement(Statement, Result);
								
								SearchingUserHistory.addValue(messageToAdd);

							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								Main.stopServer(null,e);
								return;
							}
							
							// QuickMessageText(boolean newChat,ChatPanel ChatPanel,
							//String chatName,String ChatUUID,boolean doesSingleChat) {

						});
					});
				}
				}
			
			
			private class StopSearching implements SearchingPanel.TaskForSearch {

				@Override
				public void run(String textName) {
					// TODO Auto-generated method stub
					//swapp panel
					changePanel(ResultPanel.cardNameQuickMessage);
					SearchingUserHistory.value.removeAll();
					searchingProcess=false;
				}
				
			}

		}

		private class ChatButton extends ButtonForScrolPane implements ActionListener{
			public ChatButton() {
				super(ComponentLanguageName.FindNewUserToChat.getName(MainJFrame.language).toString());
				super.addActionListener(this);
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				ChangePanel(newUserName);
			}
		}

		@Override
		protected void clean() {
			// TODO Auto-generated method stub
			this.result.SearchingUserHistory.ClearValue();
			this.result.changePanel(ResultPanel.cardNameQuickMessage);
		}

	}

	
	//class which is responsible for searching new user
	private class FindNewUserChat extends SearchChatPanel implements StopSearching{
		
		private JScrollPanel scrolPanel;
		private String waitingForResultUUID;
		
		
		//is string UUID, when a client send message to get new value
		// set this value and not sending message is not avaiable until value become null again

		public FindNewUserChat() {
			super.setVisible(true);
			super.setLayout(new BorderLayout());
			
			this.scrolPanel=new JScrollPanel(ComponentLanguageName.ResultWasNotFound);
			super.add(new SearingPanelWithButton(),BorderLayout.PAGE_START);
			super.add(this.scrolPanel,BorderLayout.CENTER);
			super.add(new BackButton(),BorderLayout.PAGE_END);

		}
		

		//class which keep main Searching panel+Button for start searching
		private class SearingPanelWithButton extends JPanel{
			public SearingPanelWithButton() {
				GridBagConstraints gridCon=new GridBagConstraints();
				gridCon.fill=GridBagConstraints.BOTH;
				gridCon.weightx=2;

				super.setLayout(new GridBagLayout());
				super.setVisible(true);
				searchPanel=new SearchingPanel(null,null);
				super.add(searchPanel.getTextField(),gridCon);
				gridCon.gridy=1;
				searchPanel.init();
				super.add(new FindNewUserButton(),gridCon);
			}
			
			
			
			private class FindNewUserButton extends ButtonForScrolPane implements ActionListener{

				public FindNewUserButton() {
					super(ComponentLanguageName.SearchingNewUserButton.getName(MainJFrame.language).toString());
					// TODO Auto-generated constructor stub
					super.addActionListener(this);
				}

				@Override
				public void actionPerformed(ActionEvent e) {

					if(searchPanel.field.PlaceHolderIsEmpty()) {
						//placeHolderISempty
						return;
					}
					
					//chech if message wait for result
					if(SetWaitingForResultUUID(null,false)!=null){
						//Program is waiting for response another task is not avaiable
						
						return;
					}
					String searchText=searchPanel.field.getText();	
					
				SocketComunication message=SocketComunication.createNewSocketComunication(null, null);
				SetWaitingForResultUUID(message.getUUIDTask(),false);
				//process of sending message will be process in separate thread
				ThreadPoolingManagement.thread.Execute(()->{
					OneSocketMessage mes=new OneSocketMessage(SocketComunication.SocketComunicationEnum.SearchnewUser,searchText);
					message.addOneSocketMessage(mes);
					//arrive responce
					//just put into panel
					ComunicationWithServer.SendMessage(message, (ArriveResponces)->{
						
						final SocketComunication ArriveResponce=ArriveResponces;
						SimpleResultSet rs=ArriveResponce.getMessage(0).getSimpleResultSet();
						
						if(!ArriveResponce.getUUIDTask().equals(SetWaitingForResultUUID(null,false))) {
							return;
						}
						ArrayList<QuickMessageText>textToAdd=new ArrayList<QuickMessageText>();
						if(rs.isEmpty(false)) {
							//none of user was find with appropriate input
						}
						else {	
							//searching new User-it means that chat was not created
							//for this reason, server send UUID user
							//chatUUID have to make, in SocketComunicationMetod
							
							int chatUUIDindex=rs.getColumnName(false).indexOf("UUIDUser");
							int UserTAbleNameIndex=rs.getColumnName(false).indexOf("UserTableName");
							List<String>rowValue;
		
							if(!waitingForResultUUID.trim().equals(ArriveResponce.getUUIDTask().trim())) {
								return;
							}
							for(int i=0;(rowValue=rs.getRowValue(false, i))!=null&&ArriveResponce.getUUIDTask().equals(SetWaitingForResultUUID(null,false));i++) {
								String ChatUUID=ChatManagerMain.returnChatUUID(rowValue.get(chatUUIDindex));
								QuickMessageText vl=new QuickMessageText(null,null,true,chatPanel,rowValue.get(UserTAbleNameIndex),ChatUUID,true);
								//from history always new chat, have to verify on server side, that return result is not in simpleHistory
								textToAdd.add(vl);
							}
							if(!ArriveResponce.getUUIDTask().equals(SetWaitingForResultUUID(null,false))){
								return;
							}
							//sort by similarity
							sortBySimilarity(searchText,textToAdd);
							}
							//add new value;
							SwingUtilities.invokeLater(()->{
								SetWaitingForResultUUID(null,true);

								scrolPanel.addValue(textToAdd);
								
							});
							
						
					});
				});
				}
				
			}

			
		}
		
		
		private class BackButton extends ButtonForScrolPane implements ActionListener{
			public BackButton() {
				super(ComponentLanguageName.SearchingBackToHistoryButton.getName(MainJFrame.language).toString());
				super.addActionListener(this);
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				ChangePanel(HistoryChatName);
			}
		}


		@Override
		public void StartStopSearching(boolean stop) {
			// TODO Auto-generated method stub
			this.scrolPanel.StartStopSearching(stop);
			//also removed message from waiting for response
			
		}

		/**Metod prove setting String UUID in synchronized way 
		 * @param UUID- UUID of message to set, if is null metod just return appropriate value, or null if has not set yet
		 * @param remove-true if you want to removed this value
		 * @return appropriate value or null if has not created yet,or if a parametr UUID was not putted,*/ 
		public synchronized String SetWaitingForResultUUID(String UUID,boolean remove) {
			if(remove) {
				String valueBefore=this.waitingForResultUUID;
				this.waitingForResultUUID=null;
				return valueBefore;
			}
			
			if(this.waitingForResultUUID==null) {
				this.waitingForResultUUID=UUID;
				return this.waitingForResultUUID;			

			}
			else {
				return this.waitingForResultUUID;
			}
			
		}

		@Override
		protected void clean() {
			// TODO Auto-generated method stub
			this.scrolPanel.ClearValue();
		}
	}
	
	public class JScrollPanel extends JScrollPane implements StopSearching{
		
		//if is true it mean that user cancel searching
		private volatile boolean ScrollPaneEnd=false;
		private ValueForScrolling value;
		
		public JScrollPanel(ComponentLanguageName UsersWasNotFind) {
			this.value=	new ValueForScrolling( UsersWasNotFind);
			super.setViewportView(this.value);
			super.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		}

		public void StartStopSearching(boolean stop) {
			this.ScrollPaneEnd=stop;
		}
		/**Metod removed all previous value and add new one, added in parametr
		 * Do not use if you want to add just one value, or change order */
		public void addValue(List<QuickMessageText> value) {
			this.value.addValue(value);
	

		}

		public void addValue(QuickMessageText value) {
			this.value.addValue(value);
		

		}
		
		private  class ValueForScrolling extends JPanel{
			private ComponentLanguageName textNoUserWasFind;
			public ValueForScrolling(ComponentLanguageName textNoUserWasFind) {
				super.setVisible(true);
				super.setOpaque(true);
				this.textNoUserWasFind=textNoUserWasFind;
				c.gridx = 1;  // Adjust these to center the label
				c.gridy = 1;
				c.fill = GridBagConstraints.NONE;
				super.setLayout(this.boxLayout);

			}
			private GridBagLayout grid=new GridBagLayout();
			private BoxLayout boxLayout=new BoxLayout(this, BoxLayout.PAGE_AXIS);
			private boolean LayoutManagerIsGridBagLayout=false;
			private GridBagConstraints c = new GridBagConstraints();
			public static Font defaultFont;

			
			protected void addValue(List<QuickMessageText> value) {
				super.removeAll();
				if(value.isEmpty()) {
					//have to put text, that none of user was find,
					//also change border layout
					if(!LayoutManagerIsGridBagLayout) {
						//set layout and put GridBagLayout
						this.LayoutManagerIsGridBagLayout=true;
						super.setPreferredSize(sizeOFMainQuickPanel);
						super.setLayout(this.grid);					
					}
					//notification, that none of result
					JLabel notFind=new JLabel(this.textNoUserWasFind.getName(MainJFrame.language).toString());
					if(this.defaultFont==null) {
						this.defaultFont=notFind.getFont().deriveFont(20F);
					}
					notFind.setFont(defaultFont);
					super.add(notFind,c);
				}
				else {
					if(this.LayoutManagerIsGridBagLayout) {
						super.setLayout(this.boxLayout);
						super.setPreferredSize(null);
						this.LayoutManagerIsGridBagLayout=false;
					}
			
					value.forEach((QuickMe)->{
						if(ScrollPaneEnd) {
							return;
						}
						if(this.defaultFont==null) {
							this.defaultFont=QuickMe.getFont().deriveFont(20F);
						}
						QuickMe.setFont(defaultFont);		
						super.add(QuickMe);
					});
					
				}
				super.revalidate();
				super.repaint();
			}
		
			/**Metod add new value to top of panel */
			protected void addValue(QuickMessageText value) {
	
				SwingUtilities.invokeLater(()->{
					//verify if container contain value before
					//then just remove them and add to previous position
					//recognize is manage by override toString
					if(this.LayoutManagerIsGridBagLayout) {
						super.removeAll();
						super.setLayout(this.boxLayout);
						super.setPreferredSize(null);
						this.LayoutManagerIsGridBagLayout=false;
					}
					synchronized(super.getComponents()) {
					
					boolean[] remove= {false};
					Component[] xx = {null};
					synchronized(super.getComponents()) {
						for(Component x:super.getComponents()) {
		
							if(x.toString().equals(value.toString())) {
								remove[0]=true;
								xx[0]=x;
							}
						}
					}
					if(remove[0]) {
						super.remove(xx[0]);
					}
					value.setFont(defaultFont);
					super.add(value,0);
					super.repaint();
					super.revalidate();
			
					}
					});
			
			}
		
		}
		
		/**Metod clear PanelWith value */
		public void ClearValue() {
			this.value.removeAll();
			this.value.repaint();
			this.value.revalidate();
		}
	}


	

	
	public static class QuickMessageText extends JButton implements ActionListener{
		public final String chatUUID;
		public final boolean doesSingleChat;
		private ChatPanel openChat;
		private final boolean newChat;
		public String ChatName;// if wil be null, as table name will be used text Of JButton
		private LocalDateTime time;
		//singleChat mean that chatUUID is also UUID of owner,
		//because singleChatUUID has this patern owner1UUID+owner2UUID, order by alphabet
		public QuickMessageText(LocalDateTime time,String tableName,boolean newChat,ChatPanel ChatPanel,String DisplayMessage,String ChatUUID,boolean doesSingleChat) {
			super(DisplayMessage);
			this.time=time;
			this.ChatName=tableName;
			this.newChat=newChat;
			this.openChat=ChatPanel;
			super.setMargin(new Insets(0,0,0,0));
			super.setVisible(true);
			super.setOpaque(false);
			super.setContentAreaFilled(false);
			super.setBorderPainted(false);
			this.doesSingleChat=doesSingleChat;
			this.chatUUID=ChatUUID;
			super.addActionListener(this);
		}
		/**Call when message arrive to server, and client get time of received Message
		 * Metod display new update text and change time */
		public void MessageArrive(LocalDateTime time,String displayChat) {
			SwingUtilities.invokeLater(()->{

				super.setText(displayChat);
				this.time=time;
				super.revalidate();
				super.repaint();
			});
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			//open new chat
			
			openChat.OpenChat(newChat,chatUUID, true,this.ChatName==null?super.getText():this.ChatName);
			
		}
		@Override
		public String toString() {
			return this.chatUUID;
		}
	
	}
	
	public static interface StopSearching{
		public void StartStopSearching(boolean stop);
	}
	 private  final LevenshteinDistance LEVENSHTEIN = new LevenshteinDistance();
	 
	 /**Metod sort value by similary
	  * @param input-key, to compare
	  * @param candidates value to compare*/
	 public synchronized List<QuickMessageText> sortBySimilarity(String input, List<QuickMessageText> candidates) {
		 if(candidates.isEmpty()) {
			 return candidates;
		 } 
		 Comparator<QuickMessageText> comparator = new Comparator<QuickMessageText>() {
	            @Override
	            public int compare(QuickMessageText s1, QuickMessageText s2) {
	                return Integer.compare(LEVENSHTEIN.apply(s1.getText(), input), LEVENSHTEIN.apply(s2.getText(), input));
	            }
	        };

	        // Sort the candidates list using the comparator
	        Collections.sort(candidates, comparator);

	        return candidates;
	    }
	
	

		public static CleanQuickMessagePanel CleanListener;
		public  class CleanQuickMessagePanel implements MouseListener{

			public CleanQuickMessagePanel() {
				CleanListener=this;
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				//detect when use click on panel
				ChangePanel(MainQuickMessage.HistoryChatName);
				synchronized(searchList) {
				searchList.forEach((X)->{
					X.MakeClean();
				});
				}
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
		private List<SearchChatPanel> searchList=Collections.synchronizedList(new ArrayList<SearchChatPanel>());
		private  abstract class SearchChatPanel extends JPanel {
			protected  SearchingPanel searchPanel;
			protected SearchChatPanel() {
				searchList.add(this);
			}
			//get notification from listener, when user click on chat gui
			//metod manage clear searchText, and after that call abstract metod
			private void MakeClean() {
				this.searchPanel.Clear();
				this.clean();
			}
			protected abstract void clean(); 
		}
}

abstract class ButtonForScrolPane extends JButton{
	public ButtonForScrolPane(String name) {
		super(name);
		super.setVisible(true);
		super.setOpaque(true);
		
	}
}

