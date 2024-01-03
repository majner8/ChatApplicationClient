package ChatAPP_HttpendPoint.Authorization;

import javax.persistence.EntityNotFoundException;
import javax.persistence.OptimisticLockException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import ChatAPP_Security.Authorization.CustomSecurityContextHolder.CustomSecurityContextHolder;
import ChatAPP_Security.Authorization.DeviceID.AuthorizationService;
import ChatAPP_Security.Authorization.JwtToken.jwtToken;
import chatAPP_CommontPart.Log4j2.Log4j2;
import chatAPP_DTO.Authorization.TokenDTO;
import chatAPP_DTO.User.UserDTO.UserAuthorizationDTO;
import chatAPP_DTO.User.UserDTO.UserProfileRegistrationDTO;
import chatAPP_database.User.HttpRequestUserEntity;

@RequestMapping()
public class AuthorizationControler implements AuthorizationEndPoint {

	@Autowired
	private AuthorizationService autService;
	@Autowired
	private jwtToken.jwtTokenGenerator jwtTokenGenerator;
	@Autowired
	private HttpRequestUserEntity userEntityScope;
	
	@Override
	public ResponseEntity<TokenDTO> register(@Valid UserAuthorizationDTO userData) {
		// TODO Auto-generated method stub
		String deviceID=CustomSecurityContextHolder.getCustomSecurityContext().getDeviceID();
	
		if(this.autService.doesUserExist(userData.getProfile(), false)) {
			//email/phone has been registred
			Log4j2.log.info(Log4j2.MarkerLog.Authorization.getMarker(),"Email or Phone has been registred yet");
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
			
		}
	
		try {
			this.autService.register(userData.getProfile(),userData.getPassword());
		}
		catch(DataIntegrityViolationException e) {
			//email or password was created after  chech, but before this request was process
			Log4j2.log.warn(Log4j2.MarkerLog.Authorization.getMarker(),"Email or Phone has been registred yet");
			
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
		Log4j2.log.info(Log4j2.MarkerLog.Authorization.getMarker(),"User was registred");
		TokenDTO token=this.jwtTokenGenerator.generateAuthorizationToken(deviceID, this.userEntityScope.getUserEntity());
		
		return ResponseEntity.ok(token);
	
	
	
	}

	@Override
	public ResponseEntity<TokenDTO> login(@Valid UserAuthorizationDTO userData) {
		String deviceID=CustomSecurityContextHolder.getCustomSecurityContext().getDeviceID();
		if(!this.autService.doesUserExist(userData.getProfile(), true)) {
			//email/phone has not been registred
			Log4j2.log.info(Log4j2.MarkerLog.Authorization.getMarker(),"Login was not sucessfull, email/phone were incorecct, user was not found");
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		if(!this.autService.login(userData.getPassword())) {
			//incorect password
			Log4j2.log.info(Log4j2.MarkerLog.Authorization.getMarker(),"Login was not sucessfull, email/phone or password were incorecct");
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		TokenDTO token=this.jwtTokenGenerator.generateAuthorizationToken(deviceID, this.userEntityScope.getUserEntity());
		Log4j2.log.info(Log4j2.MarkerLog.Authorization.getMarker(),
				"Login was sucesfull");
		return ResponseEntity.ok(token);

	
	}

	@Override
	public ResponseEntity<TokenDTO> finishRegistration(@Valid UserProfileRegistrationDTO user) {
	
		HttpStatus status;
		String deviceID=CustomSecurityContextHolder.getCustomSecurityContext().getDeviceID();
		long userID=CustomSecurityContextHolder.getCustomSecurityContext().getUserID();
		try {
			this.autService.FinishRegistration(user,userID);
			status=HttpStatus.OK;
			Log4j2.log.info(Log4j2.MarkerLog.Authorization.getMarker(),"User finish registration");

		}
		catch(OptimisticLockException e) {
			//conflict, user finish registration from diferent device
			//just inform user
			status=HttpStatus.CONFLICT;
			Log4j2.log.info(Log4j2.MarkerLog.Authorization.getMarker(),"FinishRegistratrion task was not sucesfull, registration was finished before");
		} catch (EntityNotFoundException e) {
			Log4j2.log.warn(Log4j2.MarkerLog.Database.getMarker(),"User not found with ID: during finish registeration process");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with ID: "+userID+System.lineSeparator()
            +"Try make register again, if problem persist contact administrator");
		}
		if(Log4j2.log.isDebugEnabled()) {
			Log4j2.log.debug(Log4j2.MarkerLog.Authorization.getMarker(),"I am generating fully authorizated token");
		}

		TokenDTO token=
				this.jwtTokenGenerator.generateAuthorizationToken(deviceID, this.userEntityScope.getUserEntity());
		return ResponseEntity.status(status)
				.body(token);
		
		}

	}
	
	


