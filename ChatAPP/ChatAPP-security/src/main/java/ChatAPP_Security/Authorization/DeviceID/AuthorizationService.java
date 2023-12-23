package ChatAPP_Security.Authorization.DeviceID;

import java.util.Optional;

import javax.persistence.OptimisticLockException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import ChatAPP_Security.Authorization.CustomSecurityContextHolder.CustomSecurityContextHolder;
import chatAPP_DTO.User.UserDTO.UserAuthPasswordDTO;
import chatAPP_DTO.User.UserDTO.UserComunicationDTO;
import chatAPP_DTO.User.UserDTO.UserProfileRegistrationDTO;
import chatAPP_database.User.HttpRequestUserEntity;
import chatAPP_database.User.UserAuthEntity;
import chatAPP_database.User.UserAuthEntityRepository;
import chatAPP_database.User.UserEntity;
import chatAPP_database.User.UserEntityRepository;


public class AuthorizationService {

	@Autowired
	private UserEntityRepository userEntityRepo;
	@Autowired
	private UserAuthEntityRepository passwordRepo;
	@Autowired
	private BCryptPasswordEncoder BCryptEncoder;
	@Autowired
	private HttpRequestUserEntity RequestUserEntity;
	/**Metod verify if user exist in database and return appropriate boolen value
	 * @param processLoginRequest- if value is true metod does not call metod existsBy..
	 * Instead, it call metod findBy... and save returned value to threadLocalValue, if returned Optional would be empty
	 * metod return false */
	public boolean doesUserExist(UserComunicationDTO user,boolean processLoginRequest) {
		if(!processLoginRequest)return this.userEntityRepo.existsByEmailOrPhoneAndCountryPreflix(user.getEmail(),user.getPhone(),user.getPhonePreflix());
		Optional<UserEntity> opt=this.userEntityRepo.findByEmailOrPhoneAndCountryPreflix(user.getEmail(), user.getPhone(), user.getPhonePreflix());
		if(opt.isEmpty()) {
			return false;
		}
		this.RequestUserEntity.setUserEntity(opt.get());
		return true;

	
	}
	@Transactional
	public void register(UserComunicationDTO user,UserAuthPasswordDTO password) {
		
		UserEntity userEnt=new UserEntity();
		userEnt.setEmail(user.getEmail());
		userEnt.setPhone(user.getPhone());
		userEnt.setCountryPreflix(user.getPhonePreflix());
		this.userEntityRepo.save(userEnt);	
		this.RequestUserEntity.setUserEntity(userEnt);
		UserAuthEntity autUser=new UserAuthEntity();
		autUser.setUserId(userEnt.getUserId());
		autUser.setPassword(this.BCryptEncoder.encode(password.getPassword()));
		this.passwordRepo.save(autUser);

	}
	
	/**Metod compare sent password and saved password in database */
	public boolean login(UserAuthPasswordDTO password) {
		long userID=this.RequestUserEntity.getUserEntity().getUserId();
		Optional <UserAuthEntity> user=this.passwordRepo.findById(userID);
			if(user.isEmpty()) {
				throw new NullPointerException("user id: "+userID+" was not found in password database");
			}
		return this.BCryptEncoder.matches(password.getPassword(),user.get().getPassword());
	}
	
	public void FinishRegistration(UserProfileRegistrationDTO user,long userID) {
		UserEntity databaseUser=this.userEntityRepo.findByPrimaryKey(userID);
		long previousDatabaseVersion=CustomSecurityContextHolder.getCustomSecurityContext().getCustomUserDetails().getDatabaseVersion();
		if(previousDatabaseVersion!=databaseUser.getVersion()) {
			throw new OptimisticLockException();
		}
		databaseUser.setNick(user.getNickName());
		databaseUser.setSerName(user.getSerName());
		databaseUser.setLastName(user.getLastName());
		databaseUser.setBornDate(user.getUserBorn());
		this.userEntityRepo.saveAndFlush(databaseUser);
		this.RequestUserEntity.setUserEntity(databaseUser);
	}
	
}
