package Test;


import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import Authorization.HttpServletRequestInetAdress;
import AuthorizationDTO.AutorizationRequestDTO;
import AuthorizationEntity.DeviceIdEntityRepositoryInterface;
import AuthorizationEntity.LoginActivityEntityInterface;
import AuthorizationEntity.UserRepositoryInterface;
import PathProperties.AuthorizationPath;
import Properties.AuthorizationProperties;
import jwt.JwtTokenInterface;

@RunWith(MockitoJUnitRunner.class)
public class AuthorizationControlerTest {

	//MockBean
	@InjectMocks	
	private BCryptPasswordEncoder BCryptEncoder;
	@InjectMocks	
	private AuthorizationProperties autProperties;
	@InjectMocks	
	private HttpServletRequestInetAdress inetAdress;
	@InjectMocks
	private JwtTokenInterface JWTtoken;
	 
	

	 
	@Mock
	private DeviceIdEntityRepositoryInterface device;
	@Mock
	private LoginActivityEntityInterface activity;
	@MockBean
	private UserRepositoryInterface userRepo;
	

	//simulate request
	 @InjectMocks
	 private MockMvc mockMvc;
	 
	 @InjectMocks
	 private ObjectMapper objectMapper;

	 @Test
	 public void runTest() {
		try {
			AutorizationRequestDTO value=new AutorizationRequestDTO();
			this.mockMvc.perform(MockMvcRequestBuilders
					.post(AuthorizationPath.registerPath)
		            .contentType(MediaType.APPLICATION_JSON)
		            .content(this.objectMapper.writeValueAsString(value)))

			 ;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("saddas");
	 }
}
