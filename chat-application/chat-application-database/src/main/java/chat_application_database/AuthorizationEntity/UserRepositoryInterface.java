package chat_application_database.AuthorizationEntity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import chat_application_commonPart.Logger.Log4j2;




public interface UserRepositoryInterface extends JpaRepository<UserEntity,Integer> {

	Optional<UserEntity> findByEmail(String email);
    
    Optional<UserEntity> findByCountryPreflixAndPhone(String countryPrefix, String phone);
    
    Optional<UserFinishAuthorization> findById(int id);
    
    default Optional<UserEntity> findByEmailOrPhoneAndCountryPreflix(String email,String countryPrefix, String phone){
    	
    	if(email!=null) {
    		return this.findByEmail(email);
    	}
    	else if(countryPrefix!=null&&phone!=null) {
    		return this.findByCountryPreflixAndPhone(countryPrefix, phone);
    	}
    	Log4j2.log.error("email, phone and phone preflix could not be null at the same time");
    	throw new NullPointerException();
    }
    
    boolean existsByEmail(String email);
	boolean existsByPhoneAndCountryPreflix(String phone,String countryPreflix);

	default boolean existsByEmailOrPhoneAndCountryPreflix(String email, String phone, String countryPreflix) {
    	
    	if(email!=null) {
			return existsByEmail(email);

    	}
    	else if(countryPreflix!=null&&phone!=null) {
   		 return this.existsByPhoneAndCountryPreflix(phone, countryPreflix);

    	}
    	
    	
    	Log4j2.log.error("email, phone and phone preflix could not be null at the same time");
    	throw new NullPointerException();

		
	}

	
	public static interface UserAuthorization
	{
		public String getPassword();
		public int getUserId();
		public boolean isUserActive();
		public long getVersion();
	}
	
	public static interface UserFinishAuthorization{
		public String getUserId();
		public long getVersion();
	}
	
}