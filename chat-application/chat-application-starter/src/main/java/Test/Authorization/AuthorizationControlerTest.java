package Test.Authorization;



import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import AuthorizationDTO.AutorizationRequestDTO;
import AuthorizationDTO.TokenDTO;
import chat_application_authorization.jwt.JwtTokenInterface;
import chat_application_commonPart.Authorization.HttpServletRequestInetAdress;
import chat_application_commonPart.Logger.Log4j2;
import chat_application_commonPart.PathProperties.AuthorizationPath;
import chat_application_commonPart.Properties.AuthorizationProperties;
import chat_application_database.AuthorizationEntity.DeviceIdEntity;
import chat_application_database.AuthorizationEntity.DeviceIdEntityRepositoryInterface;
import chat_application_database.AuthorizationEntity.LoginActivityEntityInterface;
import chat_application_database.AuthorizationEntity.UserRepositoryInterface;

@SpringBootTest(classes=Main.Main.class)
@AutoConfigureMockMvc
@Rollback(false)
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
	 private MockMvc mockMvc;
	 
		@Autowired	
	 private ObjectMapper objectMapper;

		@Autowired
		private WebApplicationContext wac;


		@BeforeEach
		public void setup() {
		    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
		    
		}
	
		
	
		
		
		
		@Test
		public void TestValueValidator() {
			this.makeTestValueValidator(AuthorizationPath.authorizationPreflix+AuthorizationPath.loginPath);
			this.makeTestValueValidator(AuthorizationPath.authorizationPreflix+AuthorizationPath.registerPath);

		}
	 
		/**Metod make value validator test/login/register path */
		private void makeTestValueValidator(String testedPath) {
			AutorizationRequestDTO value=new AutorizationRequestDTO();
			//	AutorizationRequestDTO value=null;
				RequestBuilder request;
				try {
					//verify null
					request = MockMvcRequestBuilders
							.post(testedPath)
					        .contentType(MediaType.APPLICATION_JSON)
					        .content(this.objectMapper.writeValueAsString(null));
					MockHttpServletResponse respo=
							this.mockMvc.perform(request)

							.andReturn().getResponse();
					
					assertEquals(HttpStatus.valueOf(respo.getStatus()),HttpStatus.BAD_REQUEST);

					//verify DTO is empty
					request = MockMvcRequestBuilders
							.post(testedPath)
					        .contentType(MediaType.APPLICATION_JSON)
					        .content(this.objectMapper.writeValueAsString(value));
					 respo=
							this.mockMvc.perform(request)

							.andReturn().getResponse();
					
					assertEquals(HttpStatus.valueOf(respo.getStatus()),HttpStatus.BAD_REQUEST);

					
					//verify emailIsWrong, but password is good
					value.setEmail("ycasd");
					value.setPassword("daseas");
					request = MockMvcRequestBuilders
							.post(AuthorizationPath.loginPath)
					        .contentType(MediaType.APPLICATION_JSON)
					        .content(this.objectMapper.writeValueAsString(value));
					assertEquals(HttpStatus.valueOf(respo.getStatus()),HttpStatus.BAD_REQUEST);
					
					//wrong phone
					value.setPhone("ewq");
					value.setCountryPreflix("šě");
					request = MockMvcRequestBuilders
							.post(AuthorizationPath.loginPath)
					        .contentType(MediaType.APPLICATION_JSON)
					        .content(this.objectMapper.writeValueAsString(value));
					assertEquals(HttpStatus.valueOf(respo.getStatus()),HttpStatus.BAD_REQUEST);
					
					//wrong phone to
					value.setPhone("1234");
					value.setCountryPreflix("4323");
					request = MockMvcRequestBuilders
							.post(AuthorizationPath.loginPath)
					        .contentType(MediaType.APPLICATION_JSON)
					        .content(this.objectMapper.writeValueAsString(value));
					assertEquals(HttpStatus.valueOf(respo.getStatus()),HttpStatus.BAD_REQUEST);
					
					//wrong phone to
					value.setPhone("1234");
					value.setCountryPreflix("+4323");
					request = MockMvcRequestBuilders
							.post(AuthorizationPath.loginPath)
					        .contentType(MediaType.APPLICATION_JSON)
					        .content(this.objectMapper.writeValueAsString(value));
					assertEquals(HttpStatus.valueOf(respo.getStatus()),HttpStatus.BAD_REQUEST);
					
					//Phone is ok, but email to
					value.setPhone("1234");
					value.setCountryPreflix("+423");
					request = MockMvcRequestBuilders
							.post(AuthorizationPath.loginPath)
					        .contentType(MediaType.APPLICATION_JSON)
					        .content(this.objectMapper.writeValueAsString(value));
					assertEquals(HttpStatus.valueOf(respo.getStatus()),HttpStatus.BAD_REQUEST);
					
					
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				
				}


		}
		
		
		@Test
		public void EndPointWithCorrectValue() {
			Log4j2.log.info("I am starting registerEndPointTest");
			RequestBuilder request;
			MockHttpServletResponse respo = null;
			AutorizationRequestDTO value=new AutorizationRequestDTO();

			value.setEmail("tonik.120@seznam.cz");
			value.setPassword("dsasa");
			value.setIsDeviceNew(true);
			
			try {
				
			Mockito.when(this.userRepo.existsByEmailOrPhoneAndCountryPreflix(Mockito.any(),Mockito.any(),Mockito.any()))
			.thenReturn(true,false);

			//first time register attemp failt, because in database exist
			request = MockMvcRequestBuilders
					.post(AuthorizationPath.authorizationPreflix+AuthorizationPath.registerPath)
			        .contentType(MediaType.APPLICATION_JSON)
			        .content(this.objectMapper.writeValueAsString(value));
			 respo=
					this.mockMvc.perform(request)

					.andReturn().getResponse();
				assertEquals(HttpStatus.CONFLICT,HttpStatus.valueOf(respo.getStatus()));
			//call it again, but for second time, database return false-email adress has not been register

				//simulate situation, where another device register email/phone after verify if it is not exist, but before this thread persist it
				//just first time,otherwise it do nothing
				Mockito.when(this.userRepo.saveAndFlush(Mockito.any()))
				.thenThrow(new DataIntegrityViolationException(""))
				;

				request = MockMvcRequestBuilders
						.post(AuthorizationPath.authorizationPreflix+AuthorizationPath.registerPath)
				        .contentType(MediaType.APPLICATION_JSON)
				        .content(this.objectMapper.writeValueAsString(value));
				 respo=
						this.mockMvc.perform(request)

						.andReturn().getResponse();
					assertEquals(HttpStatus.CONFLICT,HttpStatus.valueOf(respo.getStatus()));
			
					//Set id generation
					Mockito.when(this.device.DeviceIdGeneration(Mockito.any(), Mockito.any()))
					.then((argument)->{
						DeviceIdEntity device=new DeviceIdEntity();
						device.setDeviceID(1);
						return device;
					});
					Mockito.when(this.activity.savedNewActivity(Mockito.any(), Mockito.any()))
					.thenReturn(1);
							
				
					//just do nothing, simulate that persist operation work properly
					Mockito.reset(this.userRepo);
					respo=
								this.mockMvc.perform(request)
								.andReturn().getResponse();
					
					assertEquals(HttpStatus.CREATED,HttpStatus.valueOf(respo.getStatus()));
					
					TokenDTO actualDto = objectMapper.readValue(respo.getContentAsByteArray(),TokenDTO.class);
					
			}
			catch(Exception e) {
				e.printStackTrace();
			}

						//call it again, but for second time, database return false-email adress has not been register
			
			 //call again, because for second time database return false
			/*
			respo=
					this.mockMvc.perform(request)

					.andReturn().getResponse();
			
			assertEquals(HttpStatus.valueOf(respo.getStatus()),HttpStatus.CONFLICT);
			 */
			
			
			
		}
		
		@Test
		public void testAuthorizatedPath() {
			
			RequestBuilder request;
			try {
				//verify null
				request = MockMvcRequestBuilders
						.post(AuthorizationPath.authorizationPreflix+AuthorizationPath.finishRegistrationPath)
				        .contentType(MediaType.APPLICATION_JSON)
				        .content(this.objectMapper.writeValueAsString(null));
				MockHttpServletResponse respo=
						this.mockMvc.perform(request)

						.andReturn().getResponse();
				
				assertEquals(HttpStatus.UNAUTHORIZED,HttpStatus.valueOf(respo.getStatus()));
			}
			catch(Exception e) {}
			
		}
		
		
		
		@Test
	
		

		public void test() {
		
			try {
		//	AutorizationRequestDTO value=null;
			RequestBuilder request=MockMvcRequestBuilders
					.post(AuthorizationPath.loginPath)
		            .contentType(MediaType.APPLICATION_JSON)
		            .content(this.objectMapper.writeValueAsString(null));
			
			MockHttpServletResponse respo=
					this.mockMvc.perform(request)

					.andReturn().getResponse();

			
					
			 ;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
}
