package mmk.security.app.controller;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import mmk.security.app.entity.UserJwt;
import mmk.security.app.service.JwtService;
import mmk.security.app.service.UserJwtService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
	
	private final UserJwtService userService;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	
	
	@GetMapping("/hello")
	public String getHello() {
		return "Hello Authentication";
	}
	@PostMapping("/addNewUser")
    public UserJwt addUser(@RequestBody UserJwt request) {
        return userService.createUser(request);
    }
	@PostMapping("/generateToken")
    public String generateToken(@RequestBody UserJwt request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(request);
        }
        throw new UsernameNotFoundException("invalid username {} " + request.getUsername());
    }
	
	/*@PatchMapping
    public ResponseEntity<?> changePassword(@RequestBody UserJwt request, Principal connectedUser) {
		userService.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }*/
	
	
}
