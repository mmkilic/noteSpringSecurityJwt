package mmk.security.app.controller;

import java.io.IOException;
import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import mmk.security.app.entity.UserJwt;
import mmk.security.app.entity.UserJwtRequestChangePassword;
import mmk.security.app.service.AuthenticationService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
	
	private final AuthenticationService authService;
	
	
	@GetMapping("/hello")
	public String getHello() {
		return "Hello Authentication";
	}
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody UserJwt request) {
	  return ResponseEntity.ok(authService.register(request));
	}
	@PostMapping("/authenticate")
	public ResponseEntity<String> authenticate(@RequestBody UserJwt request) {
	  return ResponseEntity.ok(authService.authenticate(request));
	}
	@PostMapping("/refresh-token")
	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
		authService.refreshToken(request, response);
	}
	
	@PatchMapping("/change")
    public ResponseEntity<String> changePassword(@RequestBody UserJwtRequestChangePassword request, Principal connectedUser) {
        return ResponseEntity.ok(authService.changePassword(request, connectedUser));
    }
}
