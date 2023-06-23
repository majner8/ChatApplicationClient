package Main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.SwingUtilities;

import Backend.ClientDatabase;
import Backend.ClientDatabase.databaseTaskType;
import CommonPart.SQL.MainSQL;
import CommonPart.SQL.MainSQL.Query;
import CommonPart.SocketComunication.ComunicationPortHandling;
import CommonPart.SocketComunication.SocketComunication;
import CommonPart.SocketComunication.SocketComunication.OneSocketMessage;
import CommonPart.SocketComunication.SocketComunication.SimpleResultSet;
import CommonPart.SocketComunication.SocketComunication.SocketComunicationEnum;
import CommonPart.ThreadManagement.ThreadPoolingManagement;
import FrontEnd.ChatManagerMain;
import FrontEnd.ChatPanel;
import FrontEnd.ChatPanel.Chat;
import FrontEnd.ChatPanel.Chat.ShownMessage.Message;
import FrontEnd.ComponentLanguageName;
import FrontEnd.MainJFrame;

public class ComunicationWithServer implements ComunicationPortHandling.ComunicationPortHandlingInterface{
	//public static final int defaultPort=34897;//server
	public static final int defaultPort=34897;//when a server is same as client
	//public static final int defaultPort=3406;//when a server is same as client

	public static  InetAddress IPAdres;

	public static ComunicationWithServer Comunication;
	private static boolean UnConnected=true;

	public static void InitConnection() throws UnConnected{
		try {
			//89.24.68.96:34897
			//IPAdres=InetAddress.getByName("192.168.1.161");
			IPAdres=InetAddress.getByName("89.24.68.96");
			//IPAdres=InetAddress.getByName("localhost");
			//IPAdres=InetAddress.getByName("192.168.1.79");
			Socket socket=new Socket(IPAdres,defaultPort);
			UnConnected=false;
			Comunication=new ComunicationWithServer(new BufferedReader(new InputStreamReader(socket.getInputStream())),new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),socket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			UnConnected=true;
			throw new UnConnected();
			
		}

	}
	
	public ComunicationPortHandling comun;
	public ComunicationWithServer(BufferedReader read, BufferedWriter write, Socket socket) {
		this.comun=new ComunicationPortHandling(true,this,read, write, socket);
		// TODO Auto-generated constructor stub
		this.comun.writeMessage(this.comun.AutorizationKey, true);
	}
	

	@Override
	public void ConnectionIsEnd() {
		// TODO Auto-generated method stub
		this.UnConnected=true;
		Main.stopServer(null);
	}

	
	@Override
	public void ProcessMessage(SocketComunication message) {
		// TODO Auto-generated method stub
		MakeTaskAfterResponce x=this.CallTaskAfter.get(message.getUUIDTask());
		if(x!=null) {
			this.CallTaskAfter.remove(message.getUUIDTask());
			ThreadPoolingManagement.thread.Execute(()->{
				x.makeTask(message);
				
			});
			return;
		}
		if(!MainJFrame.AutorizationFinish) {
			//if a autorization not finish
			return;
		}

		//process message normally
		if(message.getMessage(0).getTypeOfMessage()==SocketComunicationEnum.Synchronization||message.getMessage(0).getTypeOfMessage()==
				SocketComunication.SocketComunicationEnum.StartSynchronization) {
			//process message which is synchronized
			this.getSynch(true).ProcessSynchronization(message);


		}
		else if(message.getMessage(0).getTypeOfMessage()==SocketComunicationEnum.SendMessage) {
			//new message arrive
			this.ReceivedMessage(message);
		}
		else if(message.getMessage(0).getTypeOfMessage()==SocketComunicationEnum.CreateNewChat) {
			//new message arrive
			this.CreateNewChatMessage(message);
		}

	}

	private void CreateNewChatMessage(SocketComunication message) {

		//create administration and chatTable
		Query [] administration=MainSQL.getQuery(databaseTaskType.CreateUserChatAndAdministration, new SimpleResultSet(new ArrayList<String>(),null), message.getUUIDRecipient());
		//notification quickUserTable
		SimpleResultSet rs=message.getMessage(0).getSimpleResultSet();
		Query[] insertQuickMessage;
		String[] UserTableName = {null};
		{
			ArrayList<String> column=new ArrayList<String>();
			column.add("chatUUID");
			column.add("ChatEnd");
			column.add("UserTableName");
			
			ArrayList<String> value=new ArrayList<String>();
			value.add(message.getUUIDRecipient());
			value.add(String.valueOf(false));
			String otherUserUUID = message.getUUIDRecipient().replace(Comunication.comun.getUserUUID(), "");
			int indexUUID=rs.getColumnName(false).indexOf("userUUID");
			int indexName=rs.getColumnName(false).indexOf("chatName");
			rs.getValues(false).forEach((Value)->{
				if(Value.get(indexUUID).equals(otherUserUUID)) {
					UserTableName[0]=Value.get(indexName);				
			}
			});
			
			
			value.add(UserTableName[0]);//UserTableNameFrom Message
		
			SimpleResultSet res=new SimpleResultSet(column,null);
			res.addValue(value, false);
			//chatUUID,ChatEnd,UserTableName
			
			insertQuickMessage=MainSQL.getQuery(ClientDatabase.databaseTaskType.InsertMessageIntoUserQuickChat, res, comun.getUserUUID());
			
		}
		
		Query[] insertAdministrationTable=MainSQL.getQuery(ClientDatabase.databaseTaskType.InsertAdministration, rs, message.getUUIDRecipient());
		Query[] x=Query.ConvertListOFqueryToOne(Arrays.asList(administration,insertQuickMessage,insertAdministrationTable));
		ThreadPoolingManagement.thread.ProcesSQLTask(x, (Stm,Rs,Ex)->{
			if(Ex!=null) {
				Ex.printStackTrace();
				Main.stopServer(null);
				return;
			}
			MainSQL.ClosedStatement(Stm, Rs);
			this.ReceivedMessage(message);
		});
	
	} 
	/**@param UserTableName-if you put null, name will be loaded from database */
	private void ReceivedMessage(SocketComunication message) {
		
		
		Message mes=Message.createMessageFromSimpleResultSet(message.getMessage(1).getSimpleResultSet(),false);
		
		
		ThreadPoolingManagement.thread.ProcesSQLTask(MainSQL.getQuery(ClientDatabase.databaseTaskType.SavedChatMessage, mes.makeSimpleResultSetFromMessage(message.getUUIDRecipient()), message.getUUIDRecipient()), 
	
				(Stm,Rs,Ex)->{
					if(Ex!=null) {
						Ex.printStackTrace();
						Main.stopServer(null);
						return;
					}
					MainSQL.ClosedStatement(Stm, Rs);
					// as table name putted null,new chat will be loaded from database
					Chat x=ChatPanel.panel.OpenChat(false, message.getUUIDRecipient(), false,null );
					//if return null chat will be loaded from database with saved message
					if(x!=null) {
						x.messages.newMessageArrive(mes, true, false);
					}
				
				});
		}
	
	private HashMap<String,MakeTaskAfterResponce> CallTaskAfter=new HashMap<String,MakeTaskAfterResponce>();
	
	public static void SendMessage(SocketComunication message,MakeTaskAfterResponce callAfter) {

		if(callAfter!=null) {
			//put message to map to make task after a responce arrive
			Comunication.CallTaskAfter.put(message.getUUIDTask(), callAfter);
		}
		
		Comunication.comun.writeMessage(message.toString(),false);

	}
	
	private Synchronization procesSyn;
	/**Metod create/remove Synchronization class, after that return appropriate one */
	public synchronized Synchronization getSynch(boolean start) {
		if(this.procesSyn==null&&start) {
			this.procesSyn=new Synchronization();
		}
		if(this.procesSyn!=null&&start) {
		}
		if(this.procesSyn==null&&!start) {
			this.procesSyn=null;
		}
		return this.procesSyn;
		
	}
	public static class Synchronization {
		
		public Synchronization() {
			
		}
		private void SynchronizationFinish() {
			ChatManagerMain.MainChat.StopSynchronization();

		}
		private void ProcessSynchronization(SocketComunication message) {
			if(message.getMessage(0).getTypeOfMessage()==SocketComunication.SocketComunicationEnum.StartSynchronization) {
				// start proces, server send message with all chat, where have user Interact
				this.ProcessTaskStartSynchronization(message);
				return;
			}
			if(message.getMessage(0).getTypeOfMessage()==SocketComunication.SocketComunicationEnum.Synchronization&&
				message.getMessage(0).getOneValue()==null	) {
				// start proces, server send message with all chat, where have user Interact
				this.ProcessSynchronizationMessage(message);
				return;
			}
			
			
		}
		
		

		/**Metod process start synchronization-create all appropriate user table, and Administration table
		 *also insert new table into userQuickMessage */
		private void ProcessTaskStartSynchronization(SocketComunication message) {

			Query [] createUserQuickMessageTask=MainSQL.getQuery(ClientDatabase.databaseTaskType.StartSynchronization, new SimpleResultSet(new ArrayList<String>(),null), ComunicationWithServer.Comunication.comun.getUserUUID());

			ThreadPoolingManagement.thread.ProcesSQLTask(createUserQuickMessageTask, (Statement,ResultSet,SQLException)->{
				if(SQLException!=null) {
					SQLException.printStackTrace();
					Main.stopServer(null);
					return;
					
				}
				MainSQL.ClosedStatement(Statement, ResultSet);
				
				//continue with task, create appropriate userTable and UserQuckMessage
			
				//metod chech if a simpleResultSet is null, if yes then it mean then user do not have any interact(chat with other user)
				if(message.getMessage(0).getSimpleResultSet().isEmpty(false)) {
					//empty synchronization finish
					this.SynchronizationFinish();
					return;
				}
				ArrayList<Query[]> task=new ArrayList<Query[]>();
				
				
				//query create chat table administration table
				//and add new tableUUID into quickMessage
				SimpleResultSet rs=message.getMessage(0).getSimpleResultSet();
				
				List<List<String>>value=rs.getValues(false);
				value.forEach((List)->{
					//MainSQL get query do not support multiple insert in this time
					//and it nos posible to change, because after that rest of insert code stop work
					SimpleResultSet x=new SimpleResultSet(rs.getColumnName(false),null);
					x.addValue(List, false);
					Query[] tasks=MainSQL.getQuery(ClientDatabase.databaseTaskType.InsertMessageIntoUserQuickChat, x, ComunicationWithServer.Comunication.comun.getUserUUID());
					task.add(tasks);
				});
				
				//here have to be table name userUUID
				int index=rs.getColumnName(false).indexOf("chatUUID");
				List<List<String>> values=rs.getValues(false);
				synchronized(values) {
					values.forEach((list)->{
						task.add(MainSQL.getQuery(ClientDatabase.databaseTaskType.CreateUserChatAndAdministration, new SimpleResultSet(new ArrayList<String>(),null), list.get(index)));	
					});
				}
				
				ArrayList<Query> xx=new ArrayList<Query>();
				task.forEach((List)->{
					for(Query x:List) {
						xx.add(x);
					}
				});
				
				Query[] tasks=new Query[xx.size()];
				AtomicInteger ind=new AtomicInteger(-1);
				xx.forEach((Q)->{
					tasks[ind.incrementAndGet()]=Q;
				});
				//process query, and create userTable,Administration table, and trigger
				ThreadPoolingManagement.thread.ProcesSQLTask(tasks, (stm,res,SQL)->{
					if(SQL!=null) {
						SQL.printStackTrace();
						Main.stopServer(null);
						return;
					}
					//table was succesfully created,
					//now send last known message to Server
					this.SelectLastKnownMessage();
				});

			});
			
			
		}
		
		private void SelectLastKnownMessage() {
			Query [] qurt=MainSQL.getQuery(ClientDatabase.databaseTaskType.SelectChatUUIDFromQuickMessage, 
					new SimpleResultSet(new ArrayList<String>(),null), Comunication.comun.getUserUUID());
					
			ThreadPoolingManagement.thread.ProcesSQLTask(qurt, (Statement,ResultSet,SQLTask)->{
				if(SQLTask!=null) {
					SQLTask.printStackTrace();
					Main.stopServer(null);
					return;
				}
				
				//ResultSetContain all table name.
				//have to convert table new from normal resultSet to extends resultSet
				ArrayList<String>columnName=new ArrayList<String>();
				
				//value have to be saved as table name, even if was received as column chatUUID
				//
				columnName.add(MainSQL.tableName.toString());
				
				SimpleResultSet x=new SimpleResultSet(new ArrayList<String>(),columnName);
				
				try {
					while(ResultSet.next()) {
						ArrayList<String>value=new ArrayList<String>();
						value.add(ResultSet.getString("chatUUID"));
						x.addValue(value, true);
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					Main.stopServer(null);
					return;
				}
				MainSQL.ClosedStatement(Statement, ResultSet);
				
				Query xx[]=MainSQL.getQuery(ClientDatabase.databaseTaskType.LastKnownMessage, x, "");
				//call task which return last known value
				ThreadPoolingManagement.thread.ProcesSQLTask(xx, (STM,RS,SQLEx)->{
					if(SQLEx!=null) {
						SQLEx.printStackTrace();
						Main.stopServer(null);
						return;
					}
					String lastKnownMessageTime = null;
					try {
						if(RS.next()) {
							lastKnownMessageTime = RS.getString("TimeOfMessage");
						}
						
						lastKnownMessageTime=lastKnownMessageTime==null?"null":lastKnownMessageTime;
			

						
						
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						e.printStackTrace();
						Main.stopServer(null);
						return;
					}
					//send last knownMessage time to server, as synchronization
					
					OneSocketMessage mes=new OneSocketMessage(SocketComunication.SocketComunicationEnum.Synchronization,lastKnownMessageTime);
					SocketComunication mess=SocketComunication.createNewSocketComunication(null, null);
					mess.addOneSocketMessage(mes);
					ComunicationWithServer.Comunication.comun.writeMessage(mess.toString(), true);
				
				});
			});
		}
		/**Metod saved send messages into appropriate table, if SimpleResultSetIs null, than end synchronization
		 * Metod also save appropriate value into administration table, if is saved in second message */
		private void ProcessSynchronizationMessage(SocketComunication message) {
			//chech if message contain second message-administration
			if(message.getMessage(1)!=null&&message.getMessage(1).getSimpleResultSet()!=null){
				//saved message
				ArrayList<SimpleResultSet> rs=this.getSimpleResultSetForOneQuery(message.getMessage(1).getSimpleResultSet(), false);
				rs.forEach((SimResultSet)->{
					//make from each SimResultSet one insert query
					
					Query [] qur=MainSQL.getQuery(ClientDatabase.databaseTaskType.SavedUserTableNameAdministrationTable, SimResultSet, null);
					ThreadPoolingManagement.thread.ProcesSQLTask(qur, (Statement,ResultSet,SQLException)->{
						if(SQLException!=null) {
							SQLException.printStackTrace();
							Main.stopServer(null);
							return;
						}
						MainSQL.ClosedStatement(Statement, ResultSet);
					});
					//we can continue
					//without wait for process SQLStatement
				});
			}
			if(message.getMessage(0).getSimpleResultSet().isEmpty(false)) {
				this.SynchronizationFinish();
				return;
			}
			
			this.SelectLastKnownMessage();
			
			//saved normal message
			ArrayList<SimpleResultSet> rs=this.getSimpleResultSetForOneQuery(message.getMessage(0).getSimpleResultSet(), false);
			//verify, if simpleResultSet is null than end synchronization
			
			
			
		
			
			rs.forEach((SimResultSet)->{
				//make from each SimResultSet one insert query
				Query [] qur=MainSQL.getQuery(ClientDatabase.databaseTaskType.SaveChatMessage, SimResultSet, null);
			
			ThreadPoolingManagement.thread.ProcesSQLTask(qur, (Statement,ResultSet,SQLException)->{
					if(SQLException!=null) {
						SQLException.printStackTrace();
						Main.stopServer(null);
						return;
					}
					MainSQL.ClosedStatement(Statement, ResultSet);
				});
				//we can continue
				//without wait for process SQLStatement
			});
			
		}

		/**Metod iterate send SimpleResultSet and return each SimpleResultSet for value which have same table name */
		private  ArrayList<SimpleResultSet> getSimpleResultSetForOneQuery(SimpleResultSet rs,boolean extend){
			ArrayList<SimpleResultSet> List=new ArrayList<SimpleResultSet>();
			
			String tableName=null;
			List<String>rowValue;
			//rs.getColumnName(extend).remove("");
			//rs.getColumnName(extend).forEach((S)->{
			//});
			
			int indexTableName=rs.getColumnName(extend).indexOf(MainSQL.tableName.toString());
			SimpleResultSet resultSet=new SimpleResultSet(rs.getColumnName(extend),rs.getColumnName(extend));
			for(int i=0;(rowValue=rs.getRowValue(extend, i))!=null;i++) {
				

				if(tableName==null) {
					tableName=rowValue.get(indexTableName);
				}
				if(!tableName.equalsIgnoreCase(rowValue.get(indexTableName))) {
					//table name is not same
					List.add(resultSet);
					//create new SimpleResultSet
					 resultSet=new SimpleResultSet(rs.getColumnName(extend),rs.getColumnName(extend));
					 tableName=rowValue.get(indexTableName);
				}
				resultSet.addValue(rowValue, extend);
			}
			List.add(resultSet);
			return List;
		}
		
		
		
	}
	
	public static class UnConnected extends Exception{
		
	}
	
	public static interface MakeTaskAfterResponce{
		public void makeTask(SocketComunication arriveResponce);
	}

	
}
