package chat_application_authorization.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import chat_application_commonPart.Logger.Log4j2;
import chat_application_commonPart.PathProperties.AuthorizationPath;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	@Autowired
	private JwtTokenFilter filter;
	 @Override
	 protected void configure(HttpSecurity http)throws Exception {
		 
		 http.csrf().disable()
		 .formLogin().disable()
		 .logout().disable()	
		 .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		 .and() 
		 .addFilterAfter(this.filter,BasicAuthenticationFilter.class)
		 .authorizeRequests()
		 .antMatchers(AuthorizationPath.authorizationPreflix+"/**").permitAll()
		 .antMatchers(new String[] {} ).permitAll()
		 .antMatchers(AuthorizationPath.authorizationPreflix+AuthorizationPath.finishRegistrationPath).authenticated()
		 .anyRequest().authenticated();
		 
	 }

}