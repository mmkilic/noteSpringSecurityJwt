package mmk.security.app.service;

import java.io.IOException;
import java.security.Principal;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import mmk.security.app.entity.Token;
import mmk.security.app.entity.UserJwt;
import mmk.security.app.entity.UserJwtRequestChangePassword;
import mmk.security.app.repostory.TokenRepository;
import mmk.security.app.repostory.UserJwtRepository;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
	
	private final UserJwtRepository userRepository;
	private final TokenRepository tokenRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	
	public String register(UserJwt request) {
		var user = UserJwt.builder()
				        .firstName(request.getFirstName())
				        .lastName(request.getLastName())
				        .email(request.getEmail())
				        .password(passwordEncoder.encode(request.getPassword()))
				        .authorities(request.getAuthorities())
				        .accountNonExpired(true)
				        .credentialsNonExpired(true)
				        .enabled(true)
				        .accountNonLocked(true)
				        .build();
	    var savedUser = userRepository.save(user);
	    var jwtToken = jwtService.generateToken(user);
	    System.out.println("JwtToken: " + jwtToken);
	    System.out.println("JwtTokenRefresh: " + jwtService.generateRefreshToken(user));
	    saveUserToken(savedUser, jwtToken);
	    return jwtToken;
	}
	public String authenticate(UserJwt request) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					request.getEmail(),
					request.getPassword()
		));
		var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
		var jwtToken = jwtService.generateToken(user);
		jwtService.generateRefreshToken(user);
		revokeAllUserTokens(user);
		saveUserToken(user, jwtToken);
		return jwtToken;
	}
	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
		final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		final String refreshToken;
		final String userEmail;
		if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
			return;
		}
		refreshToken = authHeader.substring(7);
		userEmail = jwtService.extractUsername(refreshToken);
		if (userEmail != null) {
			var user = this.userRepository.findByEmail(userEmail).orElseThrow();
			if (jwtService.isTokenValid(refreshToken, user)) {
				var accessToken = jwtService.generateToken(user);
				revokeAllUserTokens(user);
				saveUserToken(user, accessToken);
				new ObjectMapper().writeValue(response.getOutputStream(), accessToken);
			}
		}
	}
	public String changePassword(UserJwtRequestChangePassword request, Principal connectedUser) {

        var user = (UserJwt) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        
        return register(user);
    }
	
	
	private void saveUserToken(UserJwt user, String jwtToken) {
	    var token = Token.builder()
	    				.userJwt(user)
	    				.token(jwtToken)
	    				.expired(false)
	    				.revoked(false)
	    				.build();
	    tokenRepository.save(token);
    }
	private void revokeAllUserTokens(UserJwt user) {
	   var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
	   if (validUserTokens.isEmpty())
		   return;
	   validUserTokens.forEach(token -> {
		   token.setExpired(true);
		   token.setRevoked(true);
	   });
	   tokenRepository.saveAll(validUserTokens);
	}

}
