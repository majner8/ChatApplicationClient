package ChatAPP_HttpendPoint.Authorization;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import chatAPP_DTO.Authorization.TokenDTO;
import chatAPP_DTO.User.UserDTO.UserAuthorizationDTO;
import chatAPP_DTO.User.UserDTO.UserProfileRegistrationDTO;

public interface AuthorizationEndPoint {

	public final static String registerPath="";
	public static final String loginPath="";
	
	/**General authorizationPreflix, including authenticated and unAuthenticated path*/
	public static final String AuthorizationPreflix="";
	/**Specification of unAuthenticated path */
	public static final String UnAuthenticatedPreflix="";
	
	public static final String deviceIDPath="";
	
	public ResponseEntity<TokenDTO> register(@RequestBody @Valid UserAuthorizationDTO 
			userData);

	public ResponseEntity<TokenDTO> login(@RequestBody @Valid UserAuthorizationDTO 
			userData);
	public ResponseEntity<TokenDTO>finishRegistration(@RequestBody @Valid UserProfileRegistrationDTO user);
	
	
	

}
