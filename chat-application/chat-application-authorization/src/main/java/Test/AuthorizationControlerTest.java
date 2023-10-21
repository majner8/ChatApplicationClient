package Test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import AuthorizationEntity.DeviceIdEntityRepositoryInterface;
import AuthorizationEntity.LoginActivityEntityInterface;
import AuthorizationEntity.UserRepositoryInterface;
import Bean.HttpServletRequestInetAdress;
import Controler.AuthorizationControler;
import Properties.AuthorizationProperties;
import jwt.JwtTokenInterface;

@WebMvcTest(AuthorizationControler.class)
public class AuthorizationControlerTest {

	//MockBean
	@Autowired
	private BCryptPasswordEncoder BCryptEncoder;
	@Autowired
	private AuthorizationProperties autProperties;
	@Autowired
	private HttpServletRequestInetAdress inetAdress;
	@Autowired 
	private JwtTokenInterface JWTtoken;
	 

	 
	@MockBean
	private DeviceIdEntityRepositoryInterface device;
	@MockBean
	private LoginActivityEntityInterface activity;
	@MockBean
	private UserRepositoryInterface userRepo;
	

	//simulate request
	 @Autowired
	 private MockMvc mockMvc;
	 
	 @Test
	 public void runTest() {
	 }
}
