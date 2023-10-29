package chat_application_authorization.Security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import chat_application_authorization.jwt.JwtTokenInterface;
import chat_application_commonPart.Config.SecurityConfiguration;
import chat_application_commonPart.Logger.Log4j2;
import chat_application_commonPart.PathProperties.AuthorizationPath;


/**
 * This filter verifies if a request contains a JWT token and then creates an authentication.
 * The filter does not verify paths meant for authorization, specifically server login and registration requests.
 */
@Component
public class JwtTokenFilter extends OncePerRequestFilter  {

	
	public JwtTokenFilter() {
	}
	@Autowired
	private JwtTokenInterface tokenValidation;
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
        // If the request URI is for authorization (login or registration), skip JWT verification
		if(request.getRequestURI().equals(AuthorizationPath.authorizationPreflix+AuthorizationPath.loginPath)
			||request.getRequestURI().equals(AuthorizationPath.authorizationPreflix+AuthorizationPath.registerPath)	) {
			filterChain.doFilter(request, response);
			return;
		}
		
		
		
		CustomUserDetails user;
		DecodedJWT jwt=	this.tokenValidation.tokenValidation(request);
		if(jwt==null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			 return;
		}
		 long version=Long.parseLong(this.removeQuotes(jwt.getClaim(SecurityConfiguration.VersionClaimName).asString()));			 

		List <GrantedAuthority> autority=new ArrayList<GrantedAuthority>();
		Claim userIsActive=jwt.getClaim(SecurityConfiguration.userIsActiveClaimName);
		if(Boolean.valueOf(userIsActive.asString())==true) {
			//user is active
			autority.add(new SimpleGrantedAuthority(SecurityConfiguration.userIsActiveRole));
			user=CustomUserDetails.createCustomUserDetails(version,autority, jwt.getClaim(SecurityConfiguration.userIdClaimName).asString(),Integer.parseInt(jwt.getSubject())
					,jwt.getClaim(SecurityConfiguration.loginIdClaimName).asLong()
					);
			
		}
		else {
			//user is not active
			autority.add(new SimpleGrantedAuthority(SecurityConfiguration.userIsNotActiveRole));
			user=CustomUserDetails.createCustomUserDetails(version,autority, jwt.getClaim(SecurityConfiguration.userIdClaimName).asString(),Integer.parseInt(jwt.getSubject()),
					jwt.getClaim(SecurityConfiguration.loginIdClaimName).asLong());
			
		}
		
		if(user==null) {
			Log4j2.log.info(Log4j2.LogMarker.Security.getMarker(),"jwt TokenFilter Unautorizate");

		 filterChain.doFilter(request, response);
		return; 
		}
		Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()); 
		SecurityContextHolder.getContext().setAuthentication(auth);
		filterChain.doFilter(request, response);
		
	}
	

	private static String removeQuotes(String claimValue) {
		if(claimValue.startsWith("\"")){
			claimValue=claimValue.substring(1);
		}
		if(claimValue.endsWith("\"")){
			claimValue=claimValue.substring(0,claimValue.length()-1);
		}
		return claimValue;
		}


	
}
