package chatAPP_database.Chat.Messages;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;

import chatAPP_database.CustomJpaRepository;
import chatAPP_database.Chat.Messages.MessageEntity.MessageEntityProjection;



public interface MessageRepositoryEntity extends CustomJpaRepository<MessageEntity,String> {

    List<MessageEntity> findByChatID(String chatId, Pageable pageable);
    
    default List<MessageEntity> findByChatID(String chatID,
			 long offSetStart,
			 long offSetEnd){
    	long size=offSetEnd-offSetStart;
    	double startPage=Math.floor(offSetStart/offSetEnd);
    	int difference=(int)(offSetStart%size);
    	
    	Pageable page=PageRequest.of((int)startPage,(int)(size+difference),Sort.by(MessageEntity.JPQLorderName));
    	return this.findByChatID(chatID, page);
    }
    
	public Optional<MessageEntity> findByChatIDAndOrder(String chatID,long order);
	
	@Query(value=
			" with userAccest as ( select chat_id from user_chats  "
			+ " where user_id= :userID and member_until is null)"
			+", newest_message as("
			+ " SELECT chat_id, MAX(order_of_message) from"
			+ "messages"
			+ "where chat_id in (select chat_id from userAccest)"
			+ " GROUP BY chat_id "
			+ ""
			+ ")"
			
			+ " select * from messages m"
			+ " where order_of_message and chat_id in(select * from newest_message)"
			+ " order by(select order_of_message from messages)DESC"
			+ " limit :offsetstart,:offsetend;" ,nativeQuery = true
			)	
	public List<MessageEntity> getQuickUserSynchronizationMessage(long userID,int offsetstart,int offsetend);
	
	
}
